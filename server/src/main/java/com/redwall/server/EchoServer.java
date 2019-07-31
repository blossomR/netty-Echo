package com.redwall.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 1.设置端口值（抛出一个NumberFormatException如果该端口参数的格式不正确）
 *
 * 2.呼叫服务器的start（）方法
 *
 * 3.创建EventLoopGroup
 *
 * 4.创建ServerBootstrap
 *
 * 5.指定使用NIO的传输通道
 *
 * 6.设置socket地址使用所选的端口
 *
 * 7.添加EchoServerHandler到Channel的ChannelPipeline
 *
 * 8.绑定的服务器; sync等待服务器关闭
 *
 * 9.关闭渠道和块，直到它被关闭
 *
 * 10.关机的EventLoopGroup，释放所有资源。
 */

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage：" + EchoServer.class.getSimpleName() + "<port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        //创建 EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建Server - bootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    //指定所使用的NIO传输channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    //添加一个EchoServerHandler到子channel的channelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //EchoServerHandler 被注解为@Sharable，所以可以一直使用同样的实例
                            channel.pipeline().addLast(echoServerHandler);
                        }
                    });
            //  异步绑定服务器
            //调用sync（）方法阻塞，直到绑定完成
            ChannelFuture future = bootstrap.bind().sync();
            //获取channel的closeFuture，并且阻塞，直到当前线程完成该操作
            future.channel().closeFuture().sync();
        } finally {
            //关闭EventLoopGroup，释放资源
            group.shutdownGracefully().sync();
        }
    }


}
