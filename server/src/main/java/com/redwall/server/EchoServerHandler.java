package com.redwall.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * 1. @Sharable 标识这类的实例之间可以在渠道里面共享
 *
 * 2.日志消息输出到控制台
 *
 * 3.将所接收的消息返回给发送者。注意，这还没有冲刷数据
 *
 * 4.冲刷所有待审消息到远程节点。关闭通道后，操作完成
 *
 * 5.打印异常堆栈跟踪
 *
 * 6.关闭通道
 */


@ChannelHandler.Sharable
//该注解表示该ChannelHandler可以被多个Channel安全地共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        //将消息记录到控制台
        System.out.println("Server received " + in.toString(CharsetUtil.UTF_8));
        //将接收到的消息写给发送者，而不冲刷出站消息
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将目前暂存于ChannelOutboundBuffer中的消息（在下一次调用flush（）或者writeAnflush（）方法时将尝试写出到套接字），冲刷到远程节点，并且关闭该channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常
        cause.printStackTrace();
        //关闭该channel
        ctx.close();


    }

}
