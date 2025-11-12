package com.arcane.tetris.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sistema de bag 7 aleatório para distribuição justa de peças
 * Garante que todas as 7 peças apareçam antes de repetir
 */
public class PieceBag {
    private List<PieceType> bag;
    private int index;
    
    public PieceBag() {
        this.bag = new ArrayList<>();
        this.index = 0;
        refillBag();
    }
    
    /**
     * Preenche o bag com todas as 7 peças em ordem aleatória
     */
    private void refillBag() {
        bag.clear();
        for (PieceType type : PieceType.values()) {
            bag.add(type);
        }
        Collections.shuffle(bag);
        index = 0;
    }
    
    /**
     * Retorna a próxima peça do bag
     */
    public Piece next() {
        if (index >= bag.size()) {
            refillBag();
        }
        PieceType type = bag.get(index++);
        return new Piece(type);
    }
    
    /**
     * Retorna a próxima peça sem consumi-la (preview)
     */
    public Piece peek() {
        if (index >= bag.size()) {
            refillBag();
        }
        PieceType type = bag.get(index);
        return new Piece(type);
    }
    
    /**
     * Reseta o bag
     */
    public void reset() {
        refillBag();
    }
}

