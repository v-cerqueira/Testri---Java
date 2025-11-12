package com.arcane.tetris.app;

import com.arcane.tetris.ui.MainMenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.arcane.tetris.util.Logger;

/**
 * Classe principal do ArcaneTetris
 * Inicializa a aplicação JavaFX e exibe o menu principal
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Logger.info("Iniciando ArcaneTetris...");
        
        try {
            MainMenuController mainMenu = new MainMenuController(primaryStage);
            Scene scene = new Scene(mainMenu.getRoot(), 800, 600);
            
            // Aplicar tema arcano (roxo, azul, dourado)
            try {
                String cssPath = getClass().getResource("/styles/arcane.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (Exception e) {
                // CSS opcional, continua sem ele se não encontrar
                Logger.warn("CSS não encontrado, continuando sem estilo");
            }
            
            primaryStage.setTitle("ArcaneTetris - Magical Tetris Battle");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
            
            Logger.info("Menu principal exibido com sucesso");
        } catch (Exception e) {
            Logger.error("Erro ao iniciar aplicação", e);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

