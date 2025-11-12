package com.arcane.tetris.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para Piece
 */
public class PieceTest {
    
    @Test
    void testPieceCreation() {
        Piece piece = new Piece(PieceType.I);
        assertNotNull(piece);
        assertEquals(PieceType.I, piece.getType());
        assertEquals(0, piece.getRotation());
    }
    
    @Test
    void testRotate() {
        Piece piece = new Piece(PieceType.I);
        int[][] originalShape = piece.getShape();
        
        piece.rotate();
        assertEquals(1, piece.getRotation());
        
        // Rotaciona 4 vezes, deve voltar ao original (aproximadamente)
        piece.rotate();
        piece.rotate();
        piece.rotate();
        assertEquals(0, piece.getRotation());
    }
    
    @Test
    void testCopy() {
        Piece original = new Piece(PieceType.T);
        original.rotate();
        
        Piece copy = original.copy();
        assertEquals(original.getType(), copy.getType());
        assertEquals(original.getRotation(), copy.getRotation());
        
        // Modificar cópia não deve afetar original
        copy.rotate();
        assertNotEquals(original.getRotation(), copy.getRotation());
    }
}

