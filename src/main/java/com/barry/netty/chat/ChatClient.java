package com.barry.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class ChatClient {

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
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new StringDecoder());
                    //加入处理器
                    ch.pipeline().addLast(new ChatClientHandler());
                }
            });

            //启动客户端去连接服务器端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8098).sync();
            //得到channel
            Channel channel = channelFuture.channel();
            log.info("<==========" + channel.remoteAddress() + "=============>");
            //客户端需要输入信息,创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                //通过 channel 发送到服务器端
                channel.writeAndFlush(msg);
            }
        } catch (InterruptedException e) {
            log.error("Netty CLient启动失败{} ", e);
        } finally {
            group.shutdownGracefully();
        }

    }




}
