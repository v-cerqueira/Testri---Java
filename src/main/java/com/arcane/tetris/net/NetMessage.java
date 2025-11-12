package com.arcane.tetris.net;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mensagem de rede genérica (JSON)
 */
public class NetMessage {
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("playerId")
    private String playerId;
    
    @JsonProperty("data")
    private Object data;
    
    public NetMessage() {}
    
    public NetMessage(String type, String playerId, Object data) {
        this.type = type;
        this.playerId = playerId;
        this.data = data;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}

