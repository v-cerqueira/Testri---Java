package com.arcane.tetris.net;

import com.arcane.tetris.util.Logger;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servidor TCP para multiplayer online
 */
public class Server {
    private ServerSocket serverSocket;
    private int port;
    private boolean running;
    private ExecutorService executor;
    private List<ClientHandler> clients;
    private ConcurrentHashMap<String, ClientHandler> playerMap;
    
    public Server(int port) {
        this.port = port;
        this.running = false;
        this.executor = Executors.newCachedThreadPool();
        this.clients = new ArrayList<>();
        this.playerMap = new ConcurrentHashMap<>();
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            Logger.info("Servidor iniciado na porta " + port);
            
            while (running) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);
                executor.execute(handler);
            }
        } catch (IOException e) {
            Logger.error("Erro no servidor", e);
        }
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            executor.shutdown();
            Logger.info("Servidor parado");
        } catch (IOException e) {
            Logger.error("Erro ao parar servidor", e);
        }
    }
    
    /**
     * Broadcasta mensagem para todos os clientes
     */
    private void broadcast(NetMessage message) {
        String json = NetProtocol.serialize(message);
        for (ClientHandler client : clients) {
            if (client.isConnected()) {
                client.send(json);
            }
        }
    }
    
    /**
     * Handler de cliente individual
     */
    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String playerId;
        private boolean connected;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.connected = true;
            try {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                Logger.error("Erro ao criar handler", e);
            }
        }
        
        @Override
        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null && connected) {
                    NetMessage message = NetProtocol.deserialize(line);
                    handleMessage(message);
                }
            } catch (IOException e) {
                Logger.error("Erro ao ler mensagem", e);
            } finally {
                disconnect();
            }
        }
        
        private void handleMessage(NetMessage message) {
            switch (message.getType()) {
                case NetProtocol.TYPE_CONNECT:
                    @SuppressWarnings("unchecked")
                    Map<String, Object> data = (Map<String, Object>) message.getData();
                    playerId = message.getPlayerId();
                    playerMap.put(playerId, this);
                    Logger.info("Cliente conectado: " + playerId);
                    broadcast(message); // Notifica outros clientes
                    break;
                    
                case NetProtocol.TYPE_STATE_UPDATE:
                case NetProtocol.TYPE_SPELL_CAST:
                    // Repassa para outros clientes
                    broadcast(message);
                    break;
                    
                case NetProtocol.TYPE_PING:
                    // Responde pong
                    send(NetProtocol.serialize(new NetMessage("PONG", playerId, null)));
                    break;
                    
                case NetProtocol.TYPE_DISCONNECT:
                    disconnect();
                    break;
            }
        }
        
        public void send(String message) {
            if (out != null && connected) {
                out.println(message);
            }
        }
        
        public void disconnect() {
            connected = false;
            try {
                if (socket != null) {
                    socket.close();
                }
                clients.remove(this);
                if (playerId != null) {
                    playerMap.remove(playerId);
                }
                Logger.info("Cliente desconectado: " + playerId);
            } catch (IOException e) {
                Logger.error("Erro ao desconectar", e);
            }
        }
        
        public boolean isConnected() {
            return connected;
        }
    }
}

