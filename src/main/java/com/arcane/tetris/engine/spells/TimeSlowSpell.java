package com.arcane.tetris.engine.spells;

import com.arcane.tetris.engine.GameEngine;
import com.arcane.tetris.engine.Spell;

/**
 * Feitiço TimeSlow: reduz a velocidade do adversário
 */
public class TimeSlowSpell extends Spell {
    private static final double SLOW_MULTIPLIER = 0.5; // 50% da velocidade
    private static final double DURATION = 8.0; // 8 segundos
    
    public TimeSlowSpell() {
        super("TimeSlow", "Lentidão", 30, 12);
    }
    
    @Override
    public void cast(GameEngine targetEngine, GameEngine casterEngine) {
        // TimeSlow só funciona se houver um alvo (multiplayer)
        if (targetEngine == null) {
            return; // Não faz sentido no single player
        }
        targetEngine.applyTimeSlow(SLOW_MULTIPLIER, DURATION);
        isActive = true;
        startCooldown();
        
        // Remove o efeito após a duração
        new Thread(() -> {
            try {
                Thread.sleep((long)(DURATION * 1000));
                if (targetEngine != null) {
                    targetEngine.removeTimeSlow();
                }
                isActive = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

