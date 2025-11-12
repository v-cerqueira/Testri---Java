package com.arcane.tetris.engine.spells;

import com.arcane.tetris.engine.Board;
import com.arcane.tetris.engine.GameEngine;
import com.arcane.tetris.engine.Spell;

/**
 * Feitiço Bomb: remove um bloco 3x3 do tabuleiro
 */
public class BombSpell extends Spell {
    
    public BombSpell() {
        super("Bomb", "Bomba", 40, 18);
    }
    
    @Override
    public void cast(GameEngine targetEngine, GameEngine casterEngine) {
        // Bomba afeta o adversário (target), não o próprio jogador
        // Só funciona no multiplayer
        if (targetEngine == null) {
            return; // Não faz sentido no single player
        }
        
        Board board = targetEngine.getBoard();
        
        // Remove as 2 linhas completas mais altas (mais próximas do topo)
        board.removeTopTwoCompleteLines();
        
        startCooldown();
    }
}

