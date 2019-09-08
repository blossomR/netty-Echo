package com.redwall;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 */
public class serverBootstrap {

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Receive Data");
                    }
                });
        //通过配置好的severBootStrap的实例绑定该channel
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("server bound");
                } else {
                    System.out.println("bound fail");
                    channelFuture.cause().printStackTrace();
                }
            }
        });

    }
}
