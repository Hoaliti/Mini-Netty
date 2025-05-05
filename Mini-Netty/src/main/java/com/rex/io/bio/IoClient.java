package com.rex.io.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class IoClient {
    public static void main(String[] args) throws IOException, InterruptedException {

        Thread tom = new Thread(() -> {
            try {
                sendMessage();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"Tom");

        Thread jerry = new Thread(() -> {
            try {
                sendMessage();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"Jerry");

        tom.start();
        jerry.start();

        tom.join();
        jerry.join();

        System.out.println("消息发送完毕");
    }

    private static void sendMessage() throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost",8080));
        OutputStream outputStream = socket.getOutputStream();

        for(int i = 0; i < 10; i ++){
            outputStream.write((Thread.currentThread().getName() + " hello " + i).getBytes());
            outputStream.flush();
        }

        System.out.println("client close");
        Thread.sleep(10000);
        socket.close();
    }
}
