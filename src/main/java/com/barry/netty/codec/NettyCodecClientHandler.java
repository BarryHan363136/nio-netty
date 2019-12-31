package com.barry.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyCodecClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("从服务端读取到Object：" + ((User)msg).toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler发送数据");
        //ctx.writeAndFlush("测试String编解码");
        //测试自定义Long数据编解码器
        //ctx.writeAndFlush(1000L);
        //测试对象编解码
        User user = new User();
        user.setUserId("0001");
        user.setUserName("张三");
        user.setAge(30);
        user.setMobile("15936954321");
        user.setBirthDay(DateUtils.getDate());
        ctx.writeAndFlush(user);
    }


}