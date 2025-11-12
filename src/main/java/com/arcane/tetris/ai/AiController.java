package com.arcane.tetris.ai;

import com.arcane.tetris.engine.*;
import com.arcane.tetris.util.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de IA heurística
 * Avalia jogadas possíveis e escolhe a melhor
 */
public class AiController {
    private final Board board;
    private final Config config;
    private double weightHeight;
    private double weightHoles;
    private double weightBumpiness;
    private double weightLines;
    
    public AiController(Board board) {
        this.board = board;
        this.config = Config.getInstance();
        loadWeights();
    }
    
    /**
     * Carrega pesos da configuração
     */
    @SuppressWarnings("unchecked")
    private void loadWeights() {
        Object weightsObj = config.get("aiWeights", new HashMap<String, Double>());
        Map<String, Object> weightsMap = (Map<String, Object>) weightsObj;
        Map<String, Double> aiWeights = new HashMap<>();
        for (Map.Entry<String, Object> entry : weightsMap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Number) {
                aiWeights.put(entry.getKey(), ((Number) value).doubleValue());
            }
        }
        weightHeight = aiWeights.getOrDefault("height", 0.5);
        weightHoles = aiWeights.getOrDefault("holes", 0.7);
        weightBumpiness = aiWeights.getOrDefault("bumpiness", 0.3);
        weightLines = aiWeights.getOrDefault("lines", -1.0);
    }
    
    /**
     * Representa uma jogada possível
     */
    public static class Move {
        public final int x;
        public final int rotation;
        public final double score;
        
        public Move(int x, int rotation, double score) {
            this.x = x;
            this.rotation = rotation;
            this.score = score;
        }
    }
    
    /**
     * Decide a melhor jogada para uma peça
     */
    public Move decideBestMove(Piece piece) {
        List<Move> possibleMoves = generatePossibleMoves(piece);
        
        Move best = null;
        double bestScore = Double.MAX_VALUE;
        
        for (Move move : possibleMoves) {
            if (move.score < bestScore) {
                bestScore = move.score;
                best = move;
            }
        }
        
        return best != null ? best : new Move(Board.WIDTH / 2, 0, 0);
    }
    
    /**
     * Gera todas as jogadas possíveis para uma peça
     */
    private List<Move> generatePossibleMoves(Piece piece) {
        List<Move> moves = new ArrayList<>();
        
        // Testa todas as rotações
        for (int rotation = 0; rotation < 4; rotation++) {
            Piece testPiece = piece.copy();
            for (int r = 0; r < rotation; r++) {
                testPiece.rotate();
            }
            
            // Testa todas as posições horizontais
            for (int x = -2; x < Board.WIDTH + 2; x++) {
                // Simula queda da peça
                int y = findDropY(testPiece, x);
                
                if (y >= 0) {
                    // Cria um board temporário para avaliar
                    Board tempBoard = createTempBoard();
                    tempBoard.placePiece(testPiece, x, y);
                    
                    // Avalia a jogada
                    double score = evaluatePosition(tempBoard);
                    moves.add(new Move(x, rotation, score));
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Encontra a posição Y onde a peça cairia
     */
    private int findDropY(Piece piece, int x) {
        int y = 0;
        while (board.canPlacePiece(piece, x, y + 1)) {
            y++;
        }
        return board.canPlacePiece(piece, x, y) ? y : -1;
    }
    
    /**
     * Cria uma cópia temporária do board para avaliação
     */
    private Board createTempBoard() {
        return board.copy();
    }
    
    /**
     * Avalia uma posição usando heurística
     */
    private double evaluatePosition(Board tempBoard) {
        double height = tempBoard.getAggregateHeight();
        double holes = tempBoard.getHoles();
        double bumpiness = tempBoard.getBumpiness();
        int linesCleared = tempBoard.getLinesCleared();
        
        return (weightHeight * height) +
               (weightHoles * holes) +
               (weightBumpiness * bumpiness) +
               (weightLines * linesCleared);
    }
    
    /**
     * Decide qual feitiço usar baseado no estado do jogo
     */
    public String decideSpell(Player aiPlayer, Player opponent) {
        if (opponent == null) {
            return null;
        }
        
        Board oppBoard = opponent.getEngine().getBoard();
        Board aiBoard = aiPlayer.getEngine().getBoard();
        
        // Se o oponente está quase perdendo, envia linha
        if (oppBoard.getAggregateHeight() > Board.HEIGHT * 0.7) {
            if (aiPlayer.castSpell("PushLine", opponent)) {
                return "PushLine";
            }
        }
        
        // Se a IA está em perigo, usa shield
        if (aiBoard.getAggregateHeight() > Board.HEIGHT * 0.6) {
            if (aiPlayer.castSpell("Shield", null)) {
                return "Shield";
            }
        }
        
        // Se o oponente está indo bem, reduz velocidade
        if (oppBoard.getLinesCleared() > aiBoard.getLinesCleared() + 5) {
            if (aiPlayer.castSpell("TimeSlow", opponent)) {
                return "TimeSlow";
            }
        }
        
        // Se há muitos buracos, usa bomb
        if (aiBoard.getHoles() > 5) {
            if (aiPlayer.castSpell("Bomb", null)) {
                return "Bomb";
            }
        }
        
        return null;
    }
}

