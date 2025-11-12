package com.arcane.tetris.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para Board
 */
public class BoardTest {
    private Board board;
    
    @BeforeEach
    void setUp() {
        board = new Board();
    }
    
    @Test
    void testIsValidPosition() {
        assertTrue(board.isValidPosition(0, 0));
        assertTrue(board.isValidPosition(9, 19));
        assertFalse(board.isValidPosition(-1, 0));
        assertFalse(board.isValidPosition(10, 0));
        assertFalse(board.isValidPosition(0, 20));
    }
    
    @Test
    void testCanPlacePiece() {
        Piece piece = new Piece(PieceType.I);
        assertTrue(board.canPlacePiece(piece, 3, 0));
        assertFalse(board.canPlacePiece(piece, -1, 0));
    }
    
    @Test
    void testPlacePiece() {
        Piece piece = new Piece(PieceType.O);
        board.placePiece(piece, 4, 18);
        
        // Verifica se a peça foi colocada
        assertTrue(board.isOccupied(4, 18));
        assertTrue(board.isOccupied(5, 18));
        assertTrue(board.isOccupied(4, 19));
        assertTrue(board.isOccupied(5, 19));
    }
    
    @Test
    void testClearLines() {
        // Preenche uma linha completa
        for (int x = 0; x < Board.WIDTH; x++) {
            Piece piece = new Piece(PieceType.I);
            board.placePiece(piece, x, 19);
        }
        
        int linesCleared = board.clearLines();
        assertEquals(1, linesCleared);
        assertEquals(1, board.getLinesCleared());
    }
    
    @Test
    void testPushLine() {
        board.pushLine();
        
        // Verifica se uma linha foi adicionada na parte inferior
        int holes = 0;
        for (int x = 0; x < Board.WIDTH; x++) {
            if (!board.isOccupied(x, Board.HEIGHT - 1)) {
                holes++;
            }
        }
        assertEquals(1, holes); // Deve ter exatamente 1 buraco
    }
    
    @Test
    void testGetHoles() {
        // Cria um buraco
        Piece piece = new Piece(PieceType.I);
        board.placePiece(piece, 0, 18);
        board.placePiece(piece, 2, 18);
        
        int holes = board.getHoles();
        assertTrue(holes > 0);
    }
    
    @Test
    void testIsGameOver() {
        assertFalse(board.isGameOver());
        
        // Preenche a linha superior
        Piece piece = new Piece(PieceType.I);
        board.placePiece(piece, 0, 0);
        
        assertTrue(board.isGameOver());
    }
}

