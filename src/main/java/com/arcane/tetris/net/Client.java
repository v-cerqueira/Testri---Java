package com.arcane.tetris.net;

import com.arcane.tetris.util.Logger;
import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Cliente TCP para conexão com servidor
 */
public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String serverHost;
    private int serverPort;
    private String playerId;
    private boolean connected;
    private Consumer<NetMessage> messageHandler;
    private Thread receiveThread;
    
    public Client(String serverHost, int serverPort, String playerId) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.playerId = playerId;
        this.connected = false;
    }
    
    /**
     * Conecta ao servidor
     */
    public boolean connect() {
        try {
            socket = new Socket(serverHost, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
            
            // Envia mensagem de conexão
            NetMessage connectMsg = NetProtocol.createConnectMessage(playerId, "Player");
            send(connectMsg);
            
            // Inicia thread de recebimento
            receiveThread = new Thread(this::receiveLoop);
            receiveThread.start();
            
            Logger.info("Conectado ao servidor " + serverHost + ":" + serverPort);
            return true;
        } catch (IOException e) {
            Logger.error("Erro ao conectar ao servidor", e);
            return false;
        }
    }
    
    /**
     * Loop de recebimento de mensagens
     */
    private void receiveLoop() {
        try {
            String line;
            while ((line = in.readLine()) != null && connected) {
                NetMessage message = NetProtocol.deserialize(line);
                if (messageHandler != null) {
                    messageHandler.accept(message);
                }
            }
        } catch (IOException e) {
            if (connected) {
                Logger.error("Erro ao receber mensagem", e);
            }
        } finally {
            disconnect();
        }
    }
    
    /**
     * Envia mensagem ao servidor
     */
    public void send(NetMessage message) {
        if (out != null && connected) {
            String json = NetProtocol.serialize(message);
            out.println(json);
        }
    }
    
    /**
     * Desconecta do servidor
     */
    public void disconnect() {
        connected = false;
        try {
            if (socket != null) {
                socket.close();
            }
            if (receiveThread != null) {
                receiveThread.interrupt();
            }
            Logger.info("Desconectado do servidor");
        } catch (IOException e) {
            Logger.error("Erro ao desconectar", e);
        }
    }
    
    public void setMessageHandler(Consumer<NetMessage> handler) {
        this.messageHandler = handler;
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public String getPlayerId() {
        return playerId;
    }
}

