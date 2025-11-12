package com.arcane.tetris.engine.spells;

import com.arcane.tetris.engine.Board;
import com.arcane.tetris.engine.GameEngine;
import com.arcane.tetris.engine.Spell;

/**
 * Feitiço Shield: protege o jogador por 8 segundos
 * Durante esse tempo, feitiços adversários não têm efeito
 */
public class ShieldSpell extends Spell {
    private static final double DURATION = 8.0; // 8 segundos
    
    public ShieldSpell() {
        super("Shield", "Escudo", 35, 20);
    }
    
    @Override
    public void cast(GameEngine targetEngine, GameEngine casterEngine) {
        // Shield afeta o próprio jogador (caster), não o alvo
        if (casterEngine == null) {
            return;
        }
        
        // Se já está ativo, não pode ser lançado novamente
        if (isActive) {
            return;
        }
        
        // Remove a última linha completa (mais próxima da base) quando ativado
        Board board = casterEngine.getBoard();
        board.removeBottomCompleteLine();
        
        isActive = true;
        startCooldown();
        
        // Remove o efeito após a duração
        new Thread(() -> {
            try {
                Thread.sleep((long)(DURATION * 1000));
                isActive = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isActive = false;
            }
        }).start();
    }
    
    /**
     * Verifica se o shield está ativo e bloqueia feitiços
     */
    public boolean blocksSpell() {
        return isActive;
    }
}

