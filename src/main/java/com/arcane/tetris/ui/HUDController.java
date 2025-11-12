package com.arcane.tetris.ui;

import com.arcane.tetris.engine.Player;
import com.arcane.tetris.engine.Spell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Controlador do HUD (Heads-Up Display)
 * Exibe pontuação, mana, cooldowns e próxima peça
 */
public class HUDController {
    private VBox root;
    private Player player;
    private int playerNumber; // 1 ou 2
    private Text scoreText;
    private Text linesText;
    private Text levelText;
    private ProgressBar manaBar;
    private VBox spellsBox;
    
    public HUDController(Player player) {
        this(player, 1);
    }
    
    public HUDController(Player player, int playerNumber) {
        this.player = player;
        this.playerNumber = playerNumber;
        createHUD();
    }
    
    private void createHUD() {
        root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(
            Color.web("#0d1b2a"), null, null)));
        
        // Informações do jogo
        scoreText = createInfoText("Pontuação: 0");
        linesText = createInfoText("Linhas: 0");
        levelText = createInfoText("Nível: 1");
        
        // Barra de mana
        manaBar = new ProgressBar(0);
        manaBar.setPrefWidth(200);
        manaBar.setPrefHeight(20);
        manaBar.setStyle(
            "-fx-accent: #9d4edd; " +
            "-fx-background-color: #1a0d2e;"
        );
        
        Text manaLabel = createInfoText("Mana");
        
        // Feitiços - apenas os que funcionam no modo atual
        spellsBox = new VBox(5);
        spellsBox.setAlignment(Pos.CENTER);
        
        // Ordem específica dos feitiços
        String[] spellOrder = {"PieceSwap", "Shield", "PushLine", "TimeSlow", "Bomb"};
        for (String spellId : spellOrder) {
            Spell spell = player.getSpells().get(spellId);
            if (spell != null) {
                HBox spellRow = createSpellRow(spell);
                spellsBox.getChildren().add(spellRow);
            }
        }
        
        root.getChildren().addAll(
            scoreText,
            linesText,
            levelText,
            manaLabel,
            manaBar,
            spellsBox
        );
    }
    
    private Text createInfoText(String text) {
        Text t = new Text(text);
        t.setFont(Font.font("Arial", 14));
        t.setFill(Color.web("#e0aaff"));
        return t;
    }
    
    private HBox createSpellRow(Spell spell) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER);
        
        // Traduz nomes dos feitiços para português
        String spellName = translateSpellName(spell.getName());
        
        // Obtém a tecla correspondente ao feitiço
        String key = getSpellKey(spell.getId());
        
        // Texto com nome e tecla
        Text nameAndKey = new Text(spellName + " [" + key + "]");
        nameAndKey.setFont(Font.font("Arial", 11));
        nameAndKey.setFill(Color.web("#c77dff"));
        
        ProgressBar cooldownBar = new ProgressBar(0);
        cooldownBar.setPrefWidth(100);
        cooldownBar.setPrefHeight(10);
        cooldownBar.setStyle("-fx-accent: #ffd60a;");
        
        row.getChildren().addAll(nameAndKey, cooldownBar);
        
        // Armazena a referência para atualização
        cooldownBar.setUserData(spell);
        
        return row;
    }
    
    /**
     * Retorna a tecla correspondente ao feitiço baseado no player
     */
    private String getSpellKey(String spellId) {
        if (playerNumber == 1) {
            switch (spellId) {
                case "PushLine":
                    return "Q";
                case "TimeSlow":
                    return "E";
                case "Bomb":
                    return "R";
                case "PieceSwap":
                    return "T";
                case "Shield":
                    return "F";
                default:
                    return "?";
            }
        } else {
            // Player 2
            switch (spellId) {
                case "PushLine":
                    return "U";
                case "TimeSlow":
                    return "O";
                case "Bomb":
                    return "P";
                case "PieceSwap":
                    return "Y";
                case "Shield":
                    return "H";
                default:
                    return "?";
            }
        }
    }
    
    private String translateSpellName(String englishName) {
        switch (englishName) {
            case "Shield":
                return "Escudo";
            case "Time Slow":
                return "Lentidão";
            case "Piece Swap":
                return "Trocar Peça";
            case "Push Line":
                return "Enviar Linha";
            case "Bomb":
                return "Bomba";
            default:
                return englishName;
        }
    }
    
    public void update() {
        // Atualiza informações do jogo
        scoreText.setText("Pontuação: " + player.getEngine().getScore());
        linesText.setText("Linhas: " + player.getEngine().getLines());
        levelText.setText("Nível: " + player.getEngine().getLevel());
        
        // Atualiza barra de mana
        double manaPercent = (double) player.getMana() / player.getMaxMana();
        manaBar.setProgress(manaPercent);
        
        // Atualiza cooldowns dos feitiços (agora em segundos, não ticks)
        for (var node : spellsBox.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                for (var child : row.getChildren()) {
                    if (child instanceof ProgressBar) {
                        ProgressBar bar = (ProgressBar) child;
                        Spell spell = (Spell) bar.getUserData();
                        if (spell != null) {
                            double cooldownPercent = 1.0 - (spell.getRemainingCooldown() / 
                                spell.getCooldownSeconds());
                            bar.setProgress(Math.max(0, Math.min(1, cooldownPercent)));
                        }
                    }
                }
            }
        }
    }
    
    public VBox getRoot() {
        return root;
    }
}

