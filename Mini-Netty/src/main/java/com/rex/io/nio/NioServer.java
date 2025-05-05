package com.rex.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();

        server.configureBlocking(false);
        server.bind(new InetSocketAddress("localhost",8080));
        server.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            //This method performs a blocking selection operation
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel client = channel.accept();
                    client.configureBlocking(false);
                    client.register(selector,SelectionKey.OP_READ);
                    System.out.println("client connect: " + client.getRemoteAddress());
                }

                if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int length = channel.read(buffer);
                    if(length == -1){
                        // client close connection
                        System.out.println("client close connection: " + channel.getRemoteAddress());

                        channel.close();
                    }else{
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String s = new String(bytes);
                        System.out.println(s);
                    }
                }
            }

        }


    }
}
