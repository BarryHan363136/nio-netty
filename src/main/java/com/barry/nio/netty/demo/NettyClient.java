package com.barry.nio.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建客户端启动对象
            //注意客户端使用的不是ServerBootStrap而是Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group) //设置线程组
            .channel(NioSocketChannel.class) //使用NioSocketChannel作为客户端的通道实现，注意nettyserver使用的NioServerSocketChannel
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //加入处理器
                    ch.pipeline().addLast(new NettyClientHandler());
                }
            });
            log.info("netty client start");
            //启动客户端去连接服务器端
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8099)).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Netty CLient启动失败{} ", e);
        } finally {
            group.shutdownGracefully();
        }

    }




}
