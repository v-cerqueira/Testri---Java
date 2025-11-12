package com.arcane.tetris.engine.spells;

import com.arcane.tetris.engine.GameEngine;
import com.arcane.tetris.engine.Spell;

/**
 * Feitiço PushLine: envia uma linha para o adversário
 */
public class PushLineSpell extends Spell {
    
    public PushLineSpell() {
        super("PushLine", "Enviar Linha", 20, 6);
    }
    
    @Override
    public void cast(GameEngine targetEngine, GameEngine casterEngine) {
        // PushLine só funciona se houver um alvo (multiplayer)
        if (targetEngine == null) {
            return; // Não faz sentido no single player
        }
        targetEngine.getBoard().pushLine();
        startCooldown();
    }
}

