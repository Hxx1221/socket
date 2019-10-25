package com.imooc.bio;

import java.io.*;
import java.net.Socket;

public class Client {

    private static String DEFAULT_SERVER_HOST = "127.0.0.1";
    private static int DEFAULT_SERVER_PORT = 8088;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String s1 = bufferedReader1.readLine();
                bufferedWriter.write(s1 + "\n");
                bufferedWriter.flush();
                String s = bufferedReader.readLine();
                System.out.println(s);
                if ("quit".equals(s1)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}