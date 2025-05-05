package com.rex.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IoSever {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8080);
        while(true){
            // accept这一步就是blocking的
            Socket accept = socket.accept();
            InputStream inputStream = accept.getInputStream();
            byte[] buffer = new byte[1024];
            int length;

            while((length = inputStream.read(buffer)) != -1){
                String str = new String(buffer,0,length);
                System.out.println(str);
            }
        }
    }
}
