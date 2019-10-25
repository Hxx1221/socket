package com.imooc.biochatroom;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    private final String DEFAULT_SERVER_HOST = "127.0.0.1";
    private final int DEFAULT_SERVER_PORT = 8888;

    private final String QUIT = "quit";


    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public void send(String msg) {
        if (!socket.isOutputShutdown()) {
            try {
                bufferedWriter.write(msg + "\n");
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


    public String receive() throws IOException {
        String msg = null;
        if (!socket.isInputShutdown()) {
            msg = bufferedReader.readLine();
        }
        return msg;
    }

    public boolean readyToQuit(String msg) {
        return msg.equals("quit");
    }

    public void close() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
                System.out.println("关闭客户端");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {

        try {
            socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new Thread(new UserInputHandler(this)).start();
            String msg = null;
            while ((msg = receive()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }
}