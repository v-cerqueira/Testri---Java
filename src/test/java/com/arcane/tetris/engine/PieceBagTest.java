package com.arcane.tetris.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para PieceBag
 */
public class PieceBagTest {
    
    @Test
    void testNext() {
        PieceBag bag = new PieceBag();
        Piece piece = bag.next();
        
        assertNotNull(piece);
        assertNotNull(piece.getType());
    }
    
    @Test
    void testBagDistribution() {
        PieceBag bag = new PieceBag();
        boolean[] found = new boolean[7];
        
        // Deve encontrar todas as 7 peças em um ciclo
        for (int i = 0; i < 7; i++) {
            Piece piece = bag.next();
            found[piece.getType().ordinal()] = true;
        }
        
        // Todas as peças devem aparecer
        for (boolean b : found) {
            assertTrue(b);
        }
    }
}

