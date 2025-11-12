package com.arcane.tetris.engine;

import com.arcane.tetris.engine.spells.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa um jogador com seu engine, mana e feitiços
 */
public class Player {
    private final String id;
    private final String name;
    private GameEngine engine;
    private int mana;
    private int maxMana;
    private int manaPerLine;
    private Map<String, Spell> spells;
    private boolean isShielded;
    
    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.engine = new GameEngine();
        this.maxMana = 100;
        this.mana = 0;
        this.manaPerLine = 10;
        this.spells = new HashMap<>();
        this.isShielded = false;
        
        // Inicializa feitiços
        initializeSpells();
        
        // Callback para ganhar mana ao completar linhas
        engine.onLineClear(() -> {
            addMana(manaPerLine);
        });
    }
    
    /**
     * Inicializa todos os feitiços disponíveis
     */
    private void initializeSpells() {
        spells.put("PushLine", new PushLineSpell());
        spells.put("TimeSlow", new TimeSlowSpell());
        spells.put("PieceSwap", new PieceSwapSpell());
        spells.put("Bomb", new BombSpell());
        spells.put("Shield", new ShieldSpell());
    }
    
    /**
     * Adiciona mana ao jogador
     */
    public void addMana(int amount) {
        mana = Math.min(maxMana, mana + amount);
    }
    
    /**
     * Tenta lançar um feitiço
     * @param spellId ID do feitiço
     * @param target Jogador alvo (null no single player)
     * @return true se o feitiço foi lançado com sucesso, false caso contrário
     */
    public boolean castSpell(String spellId, Player target) {
        Spell spell = spells.get(spellId);
        if (spell == null) {
            return false;
        }
        
        // Feitiços que requerem alvo (não funcionam no single player)
        boolean requiresTarget = "PushLine".equals(spellId) || 
                                 "TimeSlow".equals(spellId) || 
                                 "Bomb".equals(spellId);
        
        // Se o feitiço requer alvo e não há alvo, não gasta mana
        if (requiresTarget && target == null) {
            return false; // Não gasta mana no single player
        }
        
        // Verifica se o alvo está protegido
        if (target != null && target.isShielded) {
            Spell targetShield = target.spells.get("Shield");
            if (targetShield != null && targetShield.isActive()) {
                return false; // Feitiço bloqueado (não gasta mana)
            }
        }
        
        // Verifica se pode lançar (mana, cooldown, etc)
        if (spell.canCast(mana)) {
            spell.cast(target != null ? target.engine : null, this.engine);
            mana -= spell.getManaCost();
            return true;
        }
        return false; // Não gasta mana se não pode lançar
    }
    
    /**
     * Atualiza o estado do jogador (cooldowns, etc)
     */
    public void update(double deltaTime) {
        engine.update(deltaTime);
        for (Spell spell : spells.values()) {
            spell.update(deltaTime);
        }
        
        // Atualiza estado do shield
        Spell shield = spells.get("Shield");
        isShielded = shield != null && shield.isActive();
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public GameEngine getEngine() {
        return engine;
    }
    
    public int getMana() {
        return mana;
    }
    
    public int getMaxMana() {
        return maxMana;
    }
    
    public Map<String, Spell> getSpells() {
        return spells;
    }
    
    public boolean isShielded() {
        return isShielded;
    }
    
    public void reset() {
        engine.reset();
        mana = 0;
        isShielded = false;
        for (Spell spell : spells.values()) {
            spell.reset();
        }
    }
}

