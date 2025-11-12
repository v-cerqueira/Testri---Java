package com.arcane.tetris.engine;

import com.arcane.tetris.util.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Motor principal do jogo Tetris
 * Gerencia o loop do jogo, queda de peças, pontuação e eventos
 */
public class GameEngine {
    private Board board;
    private PieceBag pieceBag;
    private Piece currentPiece;
    private Piece nextPiece;
    private int currentX, currentY;
    private int score;
    private int level;
    private int lines;
    private double fallSpeed; // linhas por segundo
    private double fallTimer;
    private boolean isPaused;
    private boolean isGameOver;
    private double timeSlowMultiplier; // multiplicador de velocidade (efeito de feitiço)
    
    // Callbacks para UI
    private List<Runnable> onLineClearCallbacks;
    private List<Runnable> onGameOverCallbacks;
    private List<Runnable> onScoreUpdateCallbacks;
    
    public GameEngine() {
        this.board = new Board();
        this.pieceBag = new PieceBag();
        this.score = 0;
        this.level = 1;
        this.lines = 0;
        this.fallSpeed = 1.0; // 1 linha por segundo inicialmente
        this.fallTimer = 0.0;
        this.isPaused = false;
        this.isGameOver = false;
        this.timeSlowMultiplier = 1.0;
        this.onLineClearCallbacks = new ArrayList<>();
        this.onGameOverCallbacks = new ArrayList<>();
        this.onScoreUpdateCallbacks = new ArrayList<>();
        
        spawnNextPiece();
    }
    
    /**
     * Atualiza o estado do jogo (chamado a cada frame)
     */
    public void update(double deltaTime) {
        if (isPaused || isGameOver) {
            return;
        }
        
        // Aplica efeito de TimeSlow
        double effectiveDelta = deltaTime * timeSlowMultiplier;
        
        fallTimer += effectiveDelta;
        double timePerLine = 1.0 / fallSpeed;
        
        if (fallTimer >= timePerLine) {
            fallTimer = 0.0;
            if (!movePiece(0, 1)) {
                // Peça não pode cair mais, gruda no tabuleiro
                lockPiece();
            }
        }
    }
    
    /**
     * Move a peça atual (retorna false se não conseguir)
     */
    public boolean movePiece(int dx, int dy) {
        if (currentPiece == null) {
            return false;
        }
        
        int newX = currentX + dx;
        int newY = currentY + dy;
        
        if (board.canPlacePiece(currentPiece, newX, newY)) {
            currentX = newX;
            currentY = newY;
            return true;
        }
        return false;
    }
    
    /**
     * Rotaciona a peça atual
     */
    public boolean rotatePiece() {
        if (currentPiece == null) {
            return false;
        }
        
        Piece rotated = currentPiece.rotatedCopy();
        if (board.canPlacePiece(rotated, currentX, currentY)) {
            currentPiece.rotate();
            return true;
        }
        
        // Tenta wall kick (desliza 1 posição para os lados)
        if (board.canPlacePiece(rotated, currentX - 1, currentY)) {
            currentPiece.rotate();
            currentX--;
            return true;
        }
        if (board.canPlacePiece(rotated, currentX + 1, currentY)) {
            currentPiece.rotate();
            currentX++;
            return true;
        }
        
        return false;
    }
    
    /**
     * Faz a peça cair instantaneamente (hard drop)
     */
    public void hardDrop() {
        if (currentPiece == null) {
            return;
        }
        
        while (movePiece(0, 1)) {
            score += 2; // Pontos extras por hard drop
        }
        lockPiece();
    }
    
    /**
     * Fixa a peça no tabuleiro e processa linhas
     */
    private void lockPiece() {
        if (currentPiece == null) {
            return;
        }
        
        board.placePiece(currentPiece, currentX, currentY);
        
        // Verifica linhas completas
        int linesCleared = board.clearLines();
        if (linesCleared > 0) {
            lines += linesCleared;
            updateScore(linesCleared);
            updateLevel();
            
            // Notifica callbacks
            for (Runnable callback : onLineClearCallbacks) {
                callback.run();
            }
        }
        
        // Verifica game over
        if (board.isGameOver()) {
            isGameOver = true;
            for (Runnable callback : onGameOverCallbacks) {
                callback.run();
            }
            Logger.info("Fim de Jogo! Pontuação: " + score);
        } else {
            spawnNextPiece();
        }
    }
    
    /**
     * Spawna a próxima peça
     */
    private void spawnNextPiece() {
        currentPiece = nextPiece != null ? nextPiece : pieceBag.next();
        nextPiece = pieceBag.next();
        
        // Posição inicial (centro superior)
        currentX = Board.WIDTH / 2 - 1;
        currentY = 0;
        
        // Verifica se já está em colisão (game over)
        if (!board.canPlacePiece(currentPiece, currentX, currentY)) {
            isGameOver = true;
            for (Runnable callback : onGameOverCallbacks) {
                callback.run();
            }
        }
    }
    
    /**
     * Atualiza a pontuação baseada em linhas completadas
     */
    private void updateScore(int linesCleared) {
        int[] points = {0, 100, 300, 500, 800}; // 0, 1, 2, 3, 4 linhas
        int pointsEarned = linesCleared < points.length ? points[linesCleared] : 800;
        score += pointsEarned * level;
        
        for (Runnable callback : onScoreUpdateCallbacks) {
            callback.run();
        }
    }
    
    /**
     * Atualiza o nível baseado nas linhas completadas
     */
    private void updateLevel() {
        int newLevel = (lines / 10) + 1;
        if (newLevel > level) {
            level = newLevel;
            // Acelera mais significativamente: velocidade aumenta exponencialmente
            fallSpeed = 0.8 + (level - 1) * 0.15; // Começa em 0.8 e aumenta 0.15 por nível
            Logger.info("Subiu de nível! Novo nível: " + level + " - Velocidade: " + String.format("%.2f", fallSpeed));
        }
    }
    
    /**
     * Aplica efeito de TimeSlow (reduz velocidade)
     */
    public void applyTimeSlow(double multiplier, double duration) {
        timeSlowMultiplier = multiplier;
        // TODO: Implementar timer para reverter após duration
    }
    
    /**
     * Remove efeito de TimeSlow
     */
    public void removeTimeSlow() {
        timeSlowMultiplier = 1.0;
    }
    
    /**
     * Força a próxima peça (remove a atual e spawna a próxima)
     */
    public void forceNextPiece() {
        spawnNextPiece();
    }
    
    /**
     * Troca a peça atual com a próxima (efeito de feitiço PieceSwap)
     */
    public void swapPieces() {
        if (currentPiece == null || nextPiece == null) {
            return;
        }
        
        Piece temp = currentPiece;
        currentPiece = nextPiece;
        nextPiece = temp;
        
        // Reseta a posição da peça para o topo
        currentX = Board.WIDTH / 2 - 1;
        currentY = 0;
        
        // Se a nova peça não pode ser colocada, não faz a troca
        if (!board.canPlacePiece(currentPiece, currentX, currentY)) {
            // Reverte a troca
            temp = currentPiece;
            currentPiece = nextPiece;
            nextPiece = temp;
            currentX = Board.WIDTH / 2 - 1;
            currentY = 0;
        }
    }
    
    // Getters e Setters
    public Board getBoard() {
        return board;
    }
    
    public Piece getCurrentPiece() {
        return currentPiece;
    }
    
    public Piece getNextPiece() {
        return nextPiece;
    }
    
    public int getCurrentX() {
        return currentX;
    }
    
    public int getCurrentY() {
        return currentY;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getLines() {
        return lines;
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
    
    public void reset() {
        board.clear();
        pieceBag.reset();
        score = 0;
        level = 1;
        lines = 0;
        fallSpeed = 1.0;
        fallTimer = 0.0;
        isPaused = false;
        isGameOver = false;
        timeSlowMultiplier = 1.0;
        spawnNextPiece();
    }
    
    // Callbacks
    public void onLineClear(Runnable callback) {
        onLineClearCallbacks.add(callback);
    }
    
    public void onGameOver(Runnable callback) {
        onGameOverCallbacks.add(callback);
    }
    
    public void onScoreUpdate(Runnable callback) {
        onScoreUpdateCallbacks.add(callback);
    }
}

