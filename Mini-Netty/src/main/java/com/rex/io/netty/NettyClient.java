package com.rex.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.TimeUnit;

public class NettyClient {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap().group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 给客户端出战提供一个编码
                        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(12024))
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                });
                    }
                });

        ChannelFuture connect = bootstrap.connect("localhost", 8080);
        connect.addListener(f-> {
            if(f.isSuccess()){
                System.out.println("成功连接到8080服务器");
                EventLoop eventLoop = connect.channel().eventLoop();
                eventLoop.scheduleAtFixedRate(() -> {
                    connect.channel().writeAndFlush("hello" + System.currentTimeMillis() + "\n");
                }, 0,1, TimeUnit.SECONDS);

            }
        });
    }
}
