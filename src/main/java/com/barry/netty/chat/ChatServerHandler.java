package com.barry.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义handler需要继承netty规定好的某个HandlerAdapter(规范)
 * */
@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    //GlobalEventExecuter.INSTANCE是全局的事件执行器
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 表示channel处于就绪状态,提示上线
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户端加入聊天的消息推送给其他在线的客户端
        //该方法会将channleGroup中的所有channel遍历，并发送消息
        channelGroup.writeAndFlush(
                "[ 客户端 ]" + channel.remoteAddress()
                        + " 上线了" + dateFormat.format(new Date())
                        + "\n");
        channelGroup.add(channel);
        log.info(ctx.channel().remoteAddress()+" 上线了"+"\n");
    }

    /**
     * 表示channel处于不活动状态，提示离线了
     * */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将客户端离开的信息推送给当前在线的客户
        channelGroup.writeAndFlush(
                "[ 客户端 ]" + channel.remoteAddress()
        + " 下线了" + "\n");
        log.info(ctx.channel().remoteAddress()+" 下线了"+"\n");
        log.info("channelGroup size=" + channelGroup.size());
    }

    /**
     * 读取数据
     * */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //读取到当前channel
        Channel channel = ctx.channel();
        //这时我们遍历channelGroup, 根据不同的情况，回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch) { //不是当前的 channel,转发消息
                ch.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 发送了消息：" + msg + "\n");
            } else {//回显自己发送的消息给自己
                ch.writeAndFlush("[ 自己 ]发送了消息：" + msg + "\n");
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }




}
