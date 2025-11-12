package com.arcane.tetris.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.arcane.tetris.engine.Board;
import com.arcane.tetris.engine.GameEngine;
import java.util.HashMap;
import java.util.Map;

/**
 * Protocolo de rede para sincronização de estado
 */
public class NetProtocol {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static final String TYPE_CONNECT = "CONNECT";
    public static final String TYPE_STATE_UPDATE = "STATE_UPDATE";
    public static final String TYPE_SPELL_CAST = "SPELL_CAST";
    public static final String TYPE_PING = "PING";
    public static final String TYPE_DISCONNECT = "DISCONNECT";
    
    /**
     * Cria mensagem de conexão
     */
    public static NetMessage createConnectMessage(String playerId, String playerName) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", playerName);
        return new NetMessage(TYPE_CONNECT, playerId, data);
    }
    
    /**
     * Cria mensagem de atualização de estado
     */
    public static NetMessage createStateUpdate(String playerId, GameEngine engine) {
        Map<String, Object> data = new HashMap<>();
        data.put("score", engine.getScore());
        data.put("lines", engine.getLines());
        data.put("level", engine.getLevel());
        
        Board board = engine.getBoard();
        Map<String, Object> boardData = new HashMap<>();
        boardData.put("grid", board.getGrid());
        boardData.put("height", board.getAggregateHeight());
        boardData.put("holes", board.getHoles());
        data.put("board", boardData);
        
        if (engine.getCurrentPiece() != null) {
            Map<String, Object> pieceData = new HashMap<>();
            pieceData.put("type", engine.getCurrentPiece().getType().name());
            pieceData.put("x", engine.getCurrentX());
            pieceData.put("y", engine.getCurrentY());
            pieceData.put("rotation", engine.getCurrentPiece().getRotation());
            data.put("currentPiece", pieceData);
        }
        
        return new NetMessage(TYPE_STATE_UPDATE, playerId, data);
    }
    
    /**
     * Cria mensagem de lançamento de feitiço
     */
    public static NetMessage createSpellCast(String playerId, String spellId, String targetId, int manaCost) {
        Map<String, Object> data = new HashMap<>();
        data.put("spellId", spellId);
        data.put("target", targetId);
        data.put("manaCost", manaCost);
        return new NetMessage(TYPE_SPELL_CAST, playerId, data);
    }
    
    /**
     * Serializa mensagem para JSON
     */
    public static String serialize(NetMessage message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao serializar mensagem", e);
        }
    }
    
    /**
     * Deserializa JSON para mensagem
     */
    public static NetMessage deserialize(String json) {
        try {
            return mapper.readValue(json, NetMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deserializar mensagem", e);
        }
    }
}

