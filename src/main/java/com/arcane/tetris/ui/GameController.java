package com.arcane.tetris.ui;

import com.arcane.tetris.engine.*;
import com.arcane.tetris.ui.MainMenuController.GameMode;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.arcane.tetris.util.Logger;

/**
 * Controlador principal do jogo
 * Gerencia renderização, input e loop do jogo
 */
public class GameController {
    private BorderPane root;
    private Stage stage;
    private GameMode mode;
    private Player player1;
    private Player player2;
    private Canvas canvas1, canvas2;
    private AnimationTimer gameLoop;
    private long lastUpdate;
    private HUDController hud1, hud2;
    private boolean isPaused;
    
    public GameController(Stage stage, GameMode mode) {
        this.stage = stage;
        this.mode = mode;
        this.isPaused = false;
        this.lastUpdate = System.nanoTime();
        
        initializePlayers();
        createUI();
        setupInput();
        startGameLoop();
    }
    
    private void initializePlayers() {
        player1 = new Player("p1", "Player 1");
        
        if (mode == GameMode.LOCAL_MULTIPLAYER) {
            player2 = new Player("p2", "Player 2");
        } else {
            // TODO: Criar bot ou IA
            player2 = null;
        }
    }
    
    private void createUI() {
        root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
            Color.web("#0d1b2a"), null, null)));
        
        if (mode == GameMode.LOCAL_MULTIPLAYER && player2 != null) {
            // Split screen horizontal - CENTRALIZADO
            HBox gameArea = new HBox(20);
            gameArea.setAlignment(Pos.CENTER);
            gameArea.setPadding(new Insets(10));
            
            VBox leftPane = createPlayerPane(player1, 1);
            VBox rightPane = createPlayerPane(player2, 2);
            
            gameArea.getChildren().addAll(leftPane, rightPane);
            root.setCenter(gameArea);
            BorderPane.setAlignment(gameArea, Pos.CENTER);
        } else {
            // Single player
            VBox singlePane = createPlayerPane(player1, 1);
            root.setCenter(singlePane);
        }
        
        // Botão de pause/voltar
        Button pauseBtn = new Button("Pause");
        pauseBtn.setOnAction(e -> togglePause());
        
        Button backBtn = new Button("Back to Menu");
        backBtn.setOnAction(e -> returnToMenu());
        
        HBox topBar = new HBox(10, pauseBtn, backBtn);
        topBar.setPadding(new Insets(10));
        root.setTop(topBar);
    }
    
    private VBox createPlayerPane(Player player, int playerNum) {
        VBox pane = new VBox(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10));
        pane.setPrefWidth(350); // Largura fixa para centralização
        
        // Label do jogador
        javafx.scene.control.Label playerLabel = new javafx.scene.control.Label(
            playerNum == 1 ? "Jogador 1" : "Jogador 2"
        );
        playerLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 16));
        playerLabel.setTextFill(Color.web(playerNum == 1 ? "#4a90e2" : "#e24a4a"));
        
        // Canvas do jogo
        Canvas canvas = new Canvas(300, 600);
        
        if (playerNum == 1) {
            canvas1 = canvas;
            hud1 = new HUDController(player, 1);
        } else {
            canvas2 = canvas;
            hud2 = new HUDController(player, 2);
        }
        
        // HUD
        VBox hud = playerNum == 1 ? hud1.getRoot() : hud2.getRoot();
        
        pane.getChildren().addAll(playerLabel, canvas, hud);
        return pane;
    }
    
    private void setupInput() {
        root.setOnKeyPressed(this::handleKeyPress);
        root.setOnKeyReleased(this::handleKeyRelease);
        root.setFocusTraversable(true);
        root.requestFocus();
    }
    
    private void handleKeyPress(KeyEvent event) {
        if (isPaused && event.getCode() != KeyCode.ESCAPE) {
            return;
        }
        
        KeyCode key = event.getCode();
        
        // Player 1 controls
        if (key == KeyCode.A || key == KeyCode.LEFT) {
            player1.getEngine().movePiece(-1, 0);
        } else if (key == KeyCode.D || key == KeyCode.RIGHT) {
            player1.getEngine().movePiece(1, 0);
        } else if (key == KeyCode.S || key == KeyCode.DOWN) {
            player1.getEngine().movePiece(0, 1);
        } else if (key == KeyCode.W || key == KeyCode.UP) {
            player1.getEngine().rotatePiece();
        } else if (key == KeyCode.SPACE) {
            player1.getEngine().hardDrop();
        } else if (key == KeyCode.Q) {
            boolean cast = player1.castSpell("PushLine", player2);
            if (!cast && player2 == null) {
                Logger.info("PushLine: Este feitiço só funciona no modo multiplayer");
            } else if (!cast) {
                Logger.info("PushLine: Mana insuficiente ou em recarga");
            }
        } else if (key == KeyCode.E) {
            boolean cast = player1.castSpell("TimeSlow", player2);
            if (!cast && player2 == null) {
                Logger.info("TimeSlow: Este feitiço só funciona no modo multiplayer");
            } else if (!cast) {
                Logger.info("TimeSlow: Mana insuficiente ou em recarga");
            }
        } else if (key == KeyCode.R) {
            boolean cast = player1.castSpell("Bomb", player2);
            if (!cast && player2 == null) {
                Logger.info("Bomba: Este feitiço só funciona no modo multiplayer");
            } else if (!cast) {
                Logger.info("Bomba: Mana insuficiente ou em recarga");
            }
        } else if (key == KeyCode.T) {
            boolean cast = player1.castSpell("PieceSwap", player2);
            if (!cast) {
                Logger.info("Trocar Peça: Mana insuficiente ou em recarga");
            }
        } else if (key == KeyCode.F) {
            boolean cast = player1.castSpell("Shield", player2);
            if (!cast) {
                Logger.info("Escudo: Mana insuficiente ou em recarga");
            }
        }
        
        // Player 2 controls (se multiplayer local)
        if (player2 != null) {
            if (key == KeyCode.J || key == KeyCode.NUMPAD4) {
                player2.getEngine().movePiece(-1, 0);
            } else if (key == KeyCode.L || key == KeyCode.NUMPAD6) {
                player2.getEngine().movePiece(1, 0);
            } else if (key == KeyCode.K || key == KeyCode.NUMPAD5) {
                player2.getEngine().movePiece(0, 1);
            } else if (key == KeyCode.I || key == KeyCode.NUMPAD8) {
                player2.getEngine().rotatePiece();
            } else if (key == KeyCode.NUMPAD0) {
                player2.getEngine().hardDrop();
            } else if (key == KeyCode.U) {
                player2.castSpell("PushLine", player1);
            } else if (key == KeyCode.O) {
                player2.castSpell("TimeSlow", player1);
            } else if (key == KeyCode.P) {
                player2.castSpell("Bomb", player1);
            } else if (key == KeyCode.Y) {
                player2.castSpell("PieceSwap", player1);
            } else if (key == KeyCode.H) {
                player2.castSpell("Shield", player1);
            }
        }
        
        if (key == KeyCode.ESCAPE) {
            togglePause();
        }
    }
    
    @SuppressWarnings("unused")
    private void handleKeyRelease(KeyEvent event) {
        // Para soft drop contínuo, se necessário
    }
    
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused) {
                    return;
                }
                
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // Converter para segundos
                lastUpdate = now;
                
                // Atualiza jogadores
                player1.update(deltaTime);
                if (player2 != null) {
                    player2.update(deltaTime);
                }
                
                // Renderiza
                render();
                
                // Atualiza HUD
                hud1.update();
                if (hud2 != null) {
                    hud2.update();
                }
                
                // Verifica game over
                if (player1.getEngine().isGameOver()) {
                    gameOver("Player 2 Wins!");
                } else if (player2 != null && player2.getEngine().isGameOver()) {
                    gameOver("Player 1 Wins!");
                }
            }
        };
        gameLoop.start();
    }
    
    private void render() {
        renderBoard(canvas1, player1);
        if (canvas2 != null && player2 != null) {
            renderBoard(canvas2, player2);
        }
    }
    
    private void renderBoard(Canvas canvas, Player player) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        GameEngine engine = player.getEngine();
        Board board = engine.getBoard();
        int[][] grid = board.getGrid();
        
        double cellSize = canvas.getWidth() / Board.WIDTH;
        double offsetY = (canvas.getHeight() - (Board.HEIGHT * cellSize)) / 2;
        
        // Desenha grid
        gc.setStroke(Color.web("#1e3a5f"));
        gc.setLineWidth(1);
        for (int y = 0; y <= Board.HEIGHT; y++) {
            double yPos = offsetY + y * cellSize;
            gc.strokeLine(0, yPos, canvas.getWidth(), yPos);
        }
        for (int x = 0; x <= Board.WIDTH; x++) {
            double xPos = x * cellSize;
            gc.strokeLine(xPos, offsetY, xPos, offsetY + Board.HEIGHT * cellSize);
        }
        
        // Desenha blocos
        Color[] colors = {
            Color.TRANSPARENT,
            Color.CYAN,      // I
            Color.YELLOW,    // O
            Color.PURPLE,    // T
            Color.ORANGE,    // L
            Color.BLUE,      // J
            Color.GREEN,     // S
            Color.RED,       // Z
            Color.GRAY       // Linha enviada
        };
        
        for (int y = 0; y < Board.HEIGHT; y++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                int cell = grid[y][x];
                if (cell != 0) {
                    gc.setFill(colors[cell]);
                    gc.fillRect(x * cellSize + 1, offsetY + y * cellSize + 1,
                               cellSize - 2, cellSize - 2);
                }
            }
        }
        
        // Desenha peça atual
        Piece current = engine.getCurrentPiece();
        if (current != null) {
            int[][] shape = current.getShape();
            int px = engine.getCurrentX();
            int py = engine.getCurrentY();
            
            Color pieceColor = colors[current.getType().ordinal() + 1];
            gc.setFill(pieceColor);
            
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0) {
                        double x = (px + col) * cellSize + 1;
                        double y = offsetY + (py + row) * cellSize + 1;
                        gc.fillRect(x, y, cellSize - 2, cellSize - 2);
                    }
                }
            }
        }
    }
    
    private void togglePause() {
        isPaused = !isPaused;
        player1.getEngine().setPaused(isPaused);
        if (player2 != null) {
            player2.getEngine().setPaused(isPaused);
        }
        Logger.info(isPaused ? "Jogo pausado" : "Jogo retomado");
    }
    
    private void gameOver(String message) {
        gameLoop.stop();
        Logger.info("Fim de Jogo: " + message);
        // TODO: Mostrar tela de game over
    }
    
    private void returnToMenu() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        MainMenuController menu = new MainMenuController(stage);
        stage.getScene().setRoot(menu.getRoot());
    }
    
    public BorderPane getRoot() {
        return root;
    }
}

