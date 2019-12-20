package com.barry.nio.netty.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自定义handler需要继承netty规定好的某个HandlerAdapter(规范)
 * */
@Slf4j
public class ChatServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //private static Map clientChannelMap = new ConcurrentHashMap();

    /**
     *  读取客户端发送的数据
     * @param ctx 上下文对象, 含有通道channel,管道pipeline
     * @param msg 就是客户端发送的数据
     * @throws Exception
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //clientChannelMap.put(ctx.channel().id().toString(), ctx.channel());
//        log.info("服务器读取线程:"+Thread.currentThread().getName());
//        //Channel channel = ctx.channel();
//        //ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接, 出站入站
//        //将msg转化为ByteBuffer, 类似与nio中的ByteBuffer
//        ByteBuf buffer = (ByteBuf) msg;
//        log.info("ServerSide 客户端发送的消息是:" + buffer.toString(CharsetUtil.UTF_8));
        Channel incoming = ctx.channel();
        for (Channel channel : channels){
            if (channel != incoming){
                channel.writeAndFlush("[他人] "+msg);
            }else {
                channel.writeAndFlush("[自己] "+msg);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer("HelloClient", CharsetUtil.UTF_8);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
