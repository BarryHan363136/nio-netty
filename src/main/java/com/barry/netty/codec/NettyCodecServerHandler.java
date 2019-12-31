package com.barry.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyCodecServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("从客户端读取到Object：" + ((User)msg).toString());
        User user = new User();
        user.setUserId("0002");
        user.setUserName("李四");
        user.setAge(50);
        user.setMobile("15814712345");
        user.setBirthDay(DateUtils.getDate2());
        ctx.writeAndFlush(user);
        //System.out.println("从客户端读取到String：" + msg.toString());
        //System.out.println("从客户端读取到Long：" + (Long)msg);
        //给客户端发回一个long数据
        //ctx.writeAndFlush(2000L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}