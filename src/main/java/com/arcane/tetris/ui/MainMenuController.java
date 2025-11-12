package com.arcane.tetris.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.arcane.tetris.util.Logger;

/**
 * Controlador do menu principal
 */
public class MainMenuController {
    private VBox root;
    private Stage stage;
    
    public MainMenuController(Stage stage) {
        this.stage = stage;
        createMenu();
    }
    
    private void createMenu() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setBackground(new Background(new BackgroundFill(
            Color.web("#1a0d2e"), null, null)));
        
        // Título
        Text title = new Text("ArcaneTetris");
        title.setFont(Font.font("Arial", 48));
        title.setFill(Color.web("#9d4edd"));
        
        Text subtitle = new Text("Magical Tetris Battle");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setFill(Color.web("#c77dff"));
        
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().addAll(title, subtitle);
        
        // Botões
        Button singlePlayerBtn = createMenuButton("Single Player");
        Button localMultiplayerBtn = createMenuButton("Local Multiplayer");
        Button onlineMultiplayerBtn = createMenuButton("Online Multiplayer");
        Button optionsBtn = createMenuButton("Options");
        Button exitBtn = createMenuButton("Exit");
        
        singlePlayerBtn.setOnAction(e -> {
            Logger.info("Iniciando Single Player...");
            GameController gameController = new GameController(stage, GameMode.SINGLE_PLAYER);
            stage.getScene().setRoot(gameController.getRoot());
        });
        
        localMultiplayerBtn.setOnAction(e -> {
            Logger.info("Iniciando Local Multiplayer...");
            GameController gameController = new GameController(stage, GameMode.LOCAL_MULTIPLAYER);
            stage.getScene().setRoot(gameController.getRoot());
        });
        
        onlineMultiplayerBtn.setOnAction(e -> {
            Logger.info("Iniciando Online Multiplayer...");
            // TODO: Implementar menu de conexão
        });
        
        optionsBtn.setOnAction(e -> {
            Logger.info("Abrindo opções...");
            // TODO: Implementar tela de opções
        });
        
        exitBtn.setOnAction(e -> stage.close());
        
        root.getChildren().addAll(
            titleBox,
            singlePlayerBtn,
            localMultiplayerBtn,
            onlineMultiplayerBtn,
            optionsBtn,
            exitBtn
        );
    }
    
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(250);
        btn.setPrefHeight(50);
        btn.setStyle(
            "-fx-background-color: #5a189a; " +
            "-fx-text-fill: #e0aaff; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-color: #9d4edd; " +
            "-fx-border-width: 2px;"
        );
        
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #7b2cbf; " +
            "-fx-text-fill: #f7d794; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-color: #c77dff; " +
            "-fx-border-width: 2px;"
        ));
        
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: #5a189a; " +
            "-fx-text-fill: #e0aaff; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-color: #9d4edd; " +
            "-fx-border-width: 2px;"
        ));
        
        return btn;
    }
    
    public VBox getRoot() {
        return root;
    }
    
    public enum GameMode {
        SINGLE_PLAYER,
        LOCAL_MULTIPLAYER,
        ONLINE_MULTIPLAYER,
        AI_VS_AI
    }
}

