package com.arcane.tetris.engine;

/**
 * Classe abstrata base para feitiços
 * Cada feitiço tem custo de mana, cooldown e efeito único
 */
public abstract class Spell {
    protected final String id;
    protected final String name;
    protected final int manaCost;
    protected final int cooldownSeconds;
    protected double remainingCooldown; // em segundos
    protected boolean isActive;
    
    public Spell(String id, String name, int manaCost, int cooldownSeconds) {
        this.id = id;
        this.name = name;
        this.manaCost = manaCost;
        this.cooldownSeconds = cooldownSeconds;
        this.remainingCooldown = 0;
        this.isActive = false;
    }
    
    /**
     * Verifica se o feitiço pode ser lançado
     */
    public boolean canCast(int currentMana) {
        return remainingCooldown <= 0 && currentMana >= manaCost && !isActive;
    }
    
    /**
     * Lança o feitiço (deve ser implementado pelas subclasses)
     */
    public abstract void cast(GameEngine targetEngine, GameEngine casterEngine);
    
    /**
     * Atualiza o cooldown (chamado a cada frame)
     */
    public void update(double deltaTime) {
        if (remainingCooldown > 0) {
            remainingCooldown -= deltaTime; // deltaTime já está em segundos
            if (remainingCooldown < 0) {
                remainingCooldown = 0;
            }
        }
    }
    
    /**
     * Ativa o cooldown após lançar o feitiço
     */
    protected void startCooldown() {
        remainingCooldown = cooldownSeconds; // Cooldown em segundos
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getManaCost() {
        return manaCost;
    }
    
    public int getCooldownSeconds() {
        return cooldownSeconds;
    }
    
    public double getRemainingCooldown() {
        return remainingCooldown; // Já está em segundos
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Reseta o estado do feitiço (cooldown e estado ativo)
     */
    public void reset() {
        remainingCooldown = 0;
        isActive = false;
    }
}

