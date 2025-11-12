package com.arcane.tetris.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Gerencia configurações do jogo (carrega e salva config.json)
 */
public class Config {
    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.arcane_tetris";
    private static final String CONFIG_FILE = CONFIG_DIR + "/config.json";
    
    private static Config instance;
    private Map<String, Object> config;
    private ObjectMapper mapper;
    
    private Config() {
        this.mapper = new ObjectMapper();
        this.config = new HashMap<>();
        loadConfig();
    }
    
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
    
    /**
     * Carrega configurações do arquivo ou cria padrão
     */
    private void loadConfig() {
        try {
            Path configPath = Paths.get(CONFIG_FILE);
            if (Files.exists(configPath)) {
                config = mapper.readValue(configPath.toFile(), Map.class);
                Logger.info("Configurações carregadas de " + CONFIG_FILE);
            } else {
                createDefaultConfig();
                saveConfig();
            }
        } catch (IOException e) {
            Logger.error("Erro ao carregar configurações", e);
            createDefaultConfig();
        }
    }
    
    /**
     * Cria configuração padrão
     */
    private void createDefaultConfig() {
        config.put("manaPerLine", 10);
        config.put("maxMana", 100);
        
        Map<String, Map<String, Integer>> spells = new HashMap<>();
        spells.put("PushLine", Map.of("cost", 20, "cooldown", 6));
        spells.put("TimeSlow", Map.of("cost", 30, "cooldown", 12));
        spells.put("PieceSwap", Map.of("cost", 25, "cooldown", 10));
        spells.put("Bomb", Map.of("cost", 40, "cooldown", 18));
        spells.put("Shield", Map.of("cost", 35, "cooldown", 20));
        config.put("spells", spells);
        
        Map<String, Double> aiWeights = new HashMap<>();
        aiWeights.put("height", 0.5);
        aiWeights.put("holes", 0.7);
        aiWeights.put("bumpiness", 0.3);
        aiWeights.put("lines", -1.0);
        config.put("aiWeights", aiWeights);
    }
    
    /**
     * Salva configurações no arquivo
     */
    public void saveConfig() {
        try {
            Path configDir = Paths.get(CONFIG_DIR);
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File(CONFIG_FILE), config);
            Logger.info("Configurações salvas em " + CONFIG_FILE);
        } catch (IOException e) {
            Logger.error("Erro ao salvar configurações", e);
        }
    }
    
    /**
     * Obtém um valor de configuração
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Object value = config.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return (T) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }
    
    /**
     * Define um valor de configuração
     */
    public void set(String key, Object value) {
        config.put(key, value);
    }
}

