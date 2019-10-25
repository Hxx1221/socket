package com.imooc.biochatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatHandler implements Runnable {


    private ChatServer server;
    private Socket socket;

    public ChatHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }


    @Override
    public void run() {

        try {
            server.addClient(socket);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = null;
            while ((msg = bufferedReader.readLine()) != null) {
                String message = "客户端【" + socket.getPort() + "】:" + msg;
                System.out.println(message);
                //将消息转发给聊天室
                server.forwardMessage(socket, message + "\n");
                //检查用户是否准备退出
                boolean b = server.readyToQuit(msg);
                if (b) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}