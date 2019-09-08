package com.redwall.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导客户端
 */
public class clientBootstrap {

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                //指定实例 - NioSocketChannel
                .channel(NioSocketChannel.class)
                //SimpleChannelInboundHandler，用于设置channel事件和数据
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                System.out.println("Receive Data");
            }
        });
        //连接到远程主机
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.rw.com",80));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    System.out.println("success connect");
                }else{
                    System.out.println("fail");
                    channelFuture.cause().printStackTrace();
                }
            }
        });

    }
}
