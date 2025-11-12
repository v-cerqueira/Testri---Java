package com.arcane.tetris.engine.spells;

import com.arcane.tetris.engine.GameEngine;
import com.arcane.tetris.engine.Piece;
import com.arcane.tetris.engine.Spell;

/**
 * Feitiço PieceSwap: troca a peça atual com a próxima
 */
public class PieceSwapSpell extends Spell {
    
    public PieceSwapSpell() {
        super("PieceSwap", "Trocar Peça", 25, 10);
    }
    
    @Override
    public void cast(GameEngine targetEngine, GameEngine casterEngine) {
        // PieceSwap afeta o próprio jogador (caster), não o alvo
        if (casterEngine == null) {
            return;
        }
        
        Piece current = casterEngine.getCurrentPiece();
        Piece next = casterEngine.getNextPiece();
        
        if (current != null && next != null) {
            // Troca as peças usando método do GameEngine
            casterEngine.swapPieces();
        }
        startCooldown();
    }
}

