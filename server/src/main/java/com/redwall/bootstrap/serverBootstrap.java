package com.redwall.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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


    /**
     * 从channel引导客户端
     */
    public void strapFromChannel() {
        //1 创建一个新的ServerBootstrap来创建新的SocketChannel管道并且绑定他们
        ServerBootstrap bootstrap = new ServerBootstrap();
        //2 指定EventLoopGroups从ServerChannel和接收到的管道来注册并获取EventLoops
        bootstrap.group(new NioEventLoopGroup(),
                //3 指定Channel类来使用
                new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                //4 设置处理器用于处理接收到的管道的I / O和数据
                .childHandler(
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            ChannelFuture connectFuture;

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                //5 创建一个新的Bootstrap来连接到远程主机
                                Bootstrap bootstrap = new Bootstrap();
                                //6 设置管道类
                                bootstrap.channel(NioSocketChannel.class)
                                        //7 置处理器来处理I / O.
                                        .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                            @Override
                                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
                                                System.out.println("Reveived data");
                                            }
                                        });
                                //8 使用相同的EventLoop作为分配到接收的管道
                                bootstrap.group(ctx.channel().eventLoop());
                                //9 连接到远端
                                connectFuture = bootstrap.connect(new InetSocketAddress("www.xxxxx.com", 80));
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                if (connectFuture.isDone()) {
                                    // 10 连接完成处理业务逻辑（比如，代理）

                                    // do something with the data
                                    // ...
                                }
                            }
                        });
        //11  通过配置了的Bootstrap来绑定到管道
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bound attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }

}
