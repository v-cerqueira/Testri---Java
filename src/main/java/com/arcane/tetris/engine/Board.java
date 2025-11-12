package com.arcane.tetris.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o tabuleiro do Tetris (10x20)
 * Gerencia células ocupadas, colisões e limpeza de linhas
 */
public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    
    private final int[][] grid; // 0 = vazio, 1-7 = tipo de peça
    private int linesCleared;
    
    public Board() {
        this.grid = new int[HEIGHT][WIDTH];
        this.linesCleared = 0;
    }
    
    /**
     * Verifica se uma posição está dentro dos limites do tabuleiro
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }
    
    /**
     * Verifica se uma célula está ocupada
     */
    public boolean isOccupied(int x, int y) {
        if (!isValidPosition(x, y)) {
            return true; // Fora dos limites = ocupado (colisão)
        }
        return grid[y][x] != 0;
    }
    
    /**
     * Verifica se uma peça pode ser colocada em uma posição
     */
    public boolean canPlacePiece(Piece piece, int x, int y) {
        int[][] shape = piece.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int boardX = x + col;
                    int boardY = y + row;
                    if (isOccupied(boardX, boardY)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Coloca uma peça no tabuleiro (quando ela "gruda")
     */
    public void placePiece(Piece piece, int x, int y) {
        int[][] shape = piece.getShape();
        int color = piece.getType().ordinal() + 1;
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int boardX = x + col;
                    int boardY = y + row;
                    if (isValidPosition(boardX, boardY)) {
                        grid[boardY][boardX] = color;
                    }
                }
            }
        }
    }
    
    /**
     * Remove linhas completas e retorna quantas foram removidas
     */
    public int clearLines() {
        List<Integer> fullLines = new ArrayList<>();
        
        // Identifica linhas completas
        for (int y = 0; y < HEIGHT; y++) {
            boolean isFull = true;
            for (int x = 0; x < WIDTH; x++) {
                if (grid[y][x] == 0) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                fullLines.add(y);
            }
        }
        
        // Remove linhas completas (de baixo para cima)
        for (int line : fullLines) {
            for (int y = line; y > 0; y--) {
                System.arraycopy(grid[y - 1], 0, grid[y], 0, WIDTH);
            }
            // Limpa a linha superior
            for (int x = 0; x < WIDTH; x++) {
                grid[0][x] = 0;
            }
        }
        
        linesCleared += fullLines.size();
        return fullLines.size();
    }
    
    /**
     * Adiciona uma linha na parte inferior (efeito de feitiço PushLine)
     */
    public void pushLine() {
        // Move todas as linhas para cima
        for (int y = 0; y < HEIGHT - 1; y++) {
            System.arraycopy(grid[y + 1], 0, grid[y], 0, WIDTH);
        }
        
        // Cria nova linha com um buraco aleatório
        int hole = (int) (Math.random() * WIDTH);
        for (int x = 0; x < WIDTH; x++) {
            grid[HEIGHT - 1][x] = (x == hole) ? 0 : 8; // 8 = cor de linha enviada
        }
    }
    
    /**
     * Remove um bloco 3x3 (efeito de feitiço Bomb) - DEPRECADO
     * Use removeTopTwoCompleteLines() ao invés disso
     */
    public void bomb(int centerX, int centerY) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int x = centerX + dx;
                int y = centerY + dy;
                if (isValidPosition(x, y)) {
                    grid[y][x] = 0;
                }
            }
        }
    }
    
    /**
     * Remove as 2 linhas completas mais altas (mais próximas do topo)
     * Se não houver linhas completas suficientes, remove as linhas mais altas que tenham blocos
     * Usado pelo feitiço Bomba
     */
    public void removeTopTwoCompleteLines() {
        int removed = 0;
        
        // Remove até 2 linhas, priorizando linhas completas
        while (removed < 2) {
            int lineToRemove = -1;
            boolean foundComplete = false;
            
            // Primeiro procura por linhas completas (de cima para baixo)
            for (int y = 0; y < HEIGHT; y++) {
                boolean isFull = true;
                for (int x = 0; x < WIDTH; x++) {
                    if (grid[y][x] == 0) {
                        isFull = false;
                        break;
                    }
                }
                if (isFull) {
                    lineToRemove = y;
                    foundComplete = true;
                    break;
                }
            }
            
            // Se não encontrou linha completa, procura qualquer linha com blocos
            if (!foundComplete) {
                for (int y = 0; y < HEIGHT; y++) {
                    boolean hasBlocks = false;
                    for (int x = 0; x < WIDTH; x++) {
                        if (grid[y][x] != 0) {
                            hasBlocks = true;
                            break;
                        }
                    }
                    if (hasBlocks) {
                        lineToRemove = y;
                        break;
                    }
                }
            }
            
            // Se não encontrou nenhuma linha para remover, para
            if (lineToRemove == -1) {
                break;
            }
            
            // Remove a linha encontrada: move todas as linhas acima para baixo
            for (int y = lineToRemove; y > 0; y--) {
                System.arraycopy(grid[y - 1], 0, grid[y], 0, WIDTH);
            }
            // Limpa a linha superior
            for (int x = 0; x < WIDTH; x++) {
                grid[0][x] = 0;
            }
            removed++;
        }
    }
    
    /**
     * Remove a última linha completa (mais próxima da base)
     * Se não houver linha completa, remove a última linha que tenha blocos
     * Usado pelo feitiço Shield
     */
    public void removeBottomCompleteLine() {
        // Primeiro tenta encontrar uma linha completa
        for (int y = HEIGHT - 1; y >= 0; y--) {
            boolean isFull = true;
            for (int x = 0; x < WIDTH; x++) {
                if (grid[y][x] == 0) {
                    isFull = false;
                    break;
                }
            }
            
            if (isFull) {
                // Remove esta linha completa: move todas as linhas acima para baixo
                for (int yy = y; yy < HEIGHT - 1; yy++) {
                    System.arraycopy(grid[yy + 1], 0, grid[yy], 0, WIDTH);
                }
                // Limpa a última linha (base)
                for (int x = 0; x < WIDTH; x++) {
                    grid[HEIGHT - 1][x] = 0;
                }
                return; // Remove apenas uma linha
            }
        }
        
        // Se não encontrou linha completa, remove a última linha que tenha pelo menos um bloco
        for (int y = HEIGHT - 1; y >= 0; y--) {
            boolean hasBlocks = false;
            for (int x = 0; x < WIDTH; x++) {
                if (grid[y][x] != 0) {
                    hasBlocks = true;
                    break;
                }
            }
            
            if (hasBlocks) {
                // Remove esta linha: move todas as linhas acima para baixo
                for (int yy = y; yy < HEIGHT - 1; yy++) {
                    System.arraycopy(grid[yy + 1], 0, grid[yy], 0, WIDTH);
                }
                // Limpa a última linha (base)
                for (int x = 0; x < WIDTH; x++) {
                    grid[HEIGHT - 1][x] = 0;
                }
                return; // Remove apenas uma linha
            }
        }
    }
    
    /**
     * Calcula a altura agregada (heurística para IA)
     */
    public double getAggregateHeight() {
        double sum = 0;
        for (int x = 0; x < WIDTH; x++) {
            sum += getColumnHeight(x);
        }
        return sum;
    }
    
    /**
     * Retorna a altura de uma coluna
     */
    public int getColumnHeight(int x) {
        for (int y = 0; y < HEIGHT; y++) {
            if (grid[y][x] != 0) {
                return HEIGHT - y;
            }
        }
        return 0;
    }
    
    /**
     * Conta o número de buracos (células vazias com blocos acima)
     */
    public int getHoles() {
        int holes = 0;
        for (int x = 0; x < WIDTH; x++) {
            boolean foundBlock = false;
            for (int y = 0; y < HEIGHT; y++) {
                if (grid[y][x] != 0) {
                    foundBlock = true;
                } else if (foundBlock) {
                    holes++;
                }
            }
        }
        return holes;
    }
    
    /**
     * Calcula a "bumpiness" (variação de altura entre colunas)
     */
    public double getBumpiness() {
        double sum = 0;
        for (int x = 0; x < WIDTH - 1; x++) {
            sum += Math.abs(getColumnHeight(x) - getColumnHeight(x + 1));
        }
        return sum;
    }
    
    /**
     * Retorna uma cópia do grid (para IA e renderização)
     */
    public int[][] getGrid() {
        int[][] copy = new int[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            System.arraycopy(grid[y], 0, copy[y], 0, WIDTH);
        }
        return copy;
    }
    
    public int getLinesCleared() {
        return linesCleared;
    }
    
    /**
     * Verifica se o jogo acabou (linha superior ocupada)
     */
    public boolean isGameOver() {
        for (int x = 0; x < WIDTH; x++) {
            if (grid[0][x] != 0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Limpa o tabuleiro
     */
    public void clear() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = 0;
            }
        }
        linesCleared = 0;
    }
    
    /**
     * Cria uma cópia profunda do board (útil para IA)
     */
    public Board copy() {
        Board copy = new Board();
        for (int y = 0; y < HEIGHT; y++) {
            System.arraycopy(grid[y], 0, copy.grid[y], 0, WIDTH);
        }
        copy.linesCleared = this.linesCleared;
        return copy;
    }
}

