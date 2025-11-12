package com.arcane.tetris.engine;

/**
 * Representa uma peça do Tetris com sua forma, rotação e posição
 */
public class Piece {
    private final PieceType type;
    private int[][] shape;
    private int rotation; // 0, 1, 2, 3 (0°, 90°, 180°, 270°)
    
    // Formas base das peças (estado inicial, rotação 0)
    private static final int[][][] SHAPES = {
        // I
        {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        },
        // O
        {
            {1, 1},
            {1, 1}
        },
        // T
        {
            {0, 1, 0},
            {1, 1, 1},
            {0, 0, 0}
        },
        // L
        {
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
        },
        // J
        {
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
        },
        // S
        {
            {0, 1, 1},
            {1, 1, 0},
            {0, 0, 0}
        },
        // Z
        {
            {1, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
        }
    };
    
    public Piece(PieceType type) {
        this.type = type;
        this.rotation = 0;
        this.shape = getBaseShape();
    }
    
    /**
     * Retorna a forma base da peça
     */
    private int[][] getBaseShape() {
        int[][] base = SHAPES[type.ordinal()];
        int[][] copy = new int[base.length][base[0].length];
        for (int i = 0; i < base.length; i++) {
            System.arraycopy(base[i], 0, copy[i], 0, base[i].length);
        }
        return copy;
    }
    
    /**
     * Rotaciona a peça no sentido horário
     */
    public void rotate() {
        rotation = (rotation + 1) % 4;
        shape = rotateMatrix(shape);
    }
    
    /**
     * Rotaciona a peça no sentido anti-horário
     */
    public void rotateCounterClockwise() {
        rotation = (rotation + 3) % 4; // +3 = -1 mod 4
        shape = rotateMatrixCounterClockwise(shape);
    }
    
    /**
     * Rotaciona uma matriz 90° no sentido horário
     */
    private int[][] rotateMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotated = new int[cols][rows];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = matrix[i][j];
            }
        }
        return rotated;
    }
    
    /**
     * Rotaciona uma matriz 90° no sentido anti-horário
     */
    private int[][] rotateMatrixCounterClockwise(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotated = new int[cols][rows];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[cols - 1 - j][i] = matrix[i][j];
            }
        }
        return rotated;
    }
    
    /**
     * Retorna uma cópia da peça rotacionada (para teste de colisão)
     */
    public Piece rotatedCopy() {
        Piece copy = new Piece(this.type);
        copy.rotation = this.rotation;
        copy.shape = new int[this.shape.length][this.shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            System.arraycopy(this.shape[i], 0, copy.shape[i], 0, this.shape[i].length);
        }
        copy.rotate();
        return copy;
    }
    
    public PieceType getType() {
        return type;
    }
    
    public int[][] getShape() {
        return shape;
    }
    
    public int getRotation() {
        return rotation;
    }
    
    /**
     * Cria uma cópia da peça
     */
    public Piece copy() {
        Piece copy = new Piece(this.type);
        copy.rotation = this.rotation;
        copy.shape = new int[this.shape.length][this.shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            System.arraycopy(this.shape[i], 0, copy.shape[i], 0, this.shape[i].length);
        }
        return copy;
    }
}

