package com.imooc.biochatroom;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {

    private int DEFAULT_PORT = 8888;
    private final String QUIT = "qiut";

    private ServerSocket serverSocket;

    private Map<Integer, Writer> connectedClients;


    public ChatServer() {
        connectedClients = new HashMap<>();
    }

    public synchronized void addClient(Socket socket) throws IOException {
        if (socket != null) {

            int port = socket.getPort();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            connectedClients.put(port, bufferedWriter);
            System.out.println("客户端来了[" + port + "] 连接到服务器");
        }
    }

    public synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            if (connectedClients.containsKey(port)) {
                connectedClients.get(port).close();
                connectedClients.remove(port);
            }
            System.out.println("客户端下线[" + port + "]");
        }
    }


    public synchronized void forwardMessage(Socket socket, String fwdMsg) throws IOException {
        for (Integer port : connectedClients.keySet()) {
            if (!port.equals(socket.getPort())) {
                Writer writer = connectedClients.get(port);
                writer.write(fwdMsg);
                writer.flush();
            }
        }
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器，监听端口：" + DEFAULT_PORT + "....");
            while (true) {
                Socket accept = serverSocket.accept();
               // addClient(accept);
                new Thread(new ChatHandler(this,accept)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public synchronized void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                System.out.println("关闭serverSocket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }


    public boolean readyToQuit(String msg) {
        return msg.equals("quit");
    }



}