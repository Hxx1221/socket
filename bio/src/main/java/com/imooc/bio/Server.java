package com.imooc.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int DEFAULT_PORT = 8088;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);

            while (true) {
                Socket accept = serverSocket.accept();
                System.out.println("客户端来了[" + accept.getPort() + "]");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
                String s = null;
                while((s =bufferedReader.readLine())!= null) {
                    System.out.println("客户端[" + accept.getPort() + "]:" + s);
                    bufferedWriter.write("服务器：" + s + "\n");
                    bufferedWriter.flush();
                    if (s.equals("quit")){
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}