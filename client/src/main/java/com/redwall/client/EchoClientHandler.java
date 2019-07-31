package com.redwall.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 客户端的工作内容：
 * 连接服务器
 * 发送信息
 * 发送的每个信息，等待和接收从服务器返回的同样的信息
 * 关闭连接
 * 用ChannelHandler实现客户端逻辑
 * 跟写服务器一样，我们提供ChannelInboundHandler来处理数据。下面例子，我们用SimpleChannelInboundHandler来处理所有的任务，需要覆盖三个方法：
 *
 * channelActive（） - 服务器的连接被建立后调用
 * channelRead0（） - 数据后从服务器接收到调用
 * exceptionCaught（） - 捕获一个异常时调用
 */


@ChannelHandler.Sharable
//@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        System.out.println("client received : " + byteBuf.toString(CharsetUtil.UTF_8));


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知channel是活跃的时候，发送一条信息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
