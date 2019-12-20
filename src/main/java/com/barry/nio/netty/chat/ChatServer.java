package com.barry.nio.netty.chat;

import com.barry.nio.netty.demo.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {

    public static void main(String[] args) throws InterruptedException {
        //创建两个线程组bossGroup和workGroup,含有的子线程NioEventLoop的个数默认为cpu核数的两倍
        //bossGroup只是处理连接请求, 真正的和客户端业务处理，会交给workGroup完成
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来配置参数
            bootstrap
                    .group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器端的通道实现
                    //初始化服务器连接队列大小,服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接
                    //多个客户端同时来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建通道初始化对象，设置初始化参数
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //对workGroup的SocketChannel设置处理器
                            ch.pipeline().addLast(new ChatServerHandler());
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringDecoder());
                        }
                    });
            log.info("netty server start。。。");
            //绑定一个端口并且同步,生成一个ChannelFuture异步对象, 通过isDone()等方法可以判断异步事件的执行情况
            //启动服务器(并绑定端口)，bind是异步操作， sync方法是等待异步操作执行完毕
            ChannelFuture future = bootstrap.bind("127.0.0.1", 8098).sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()){
                        log.info("监听端口8098成功");
                    }else {
                        log.info("监听端口8098失败");
                    }
                }
            });
            //对通道关闭进行监听, closeFuture是异步操作, 监听通道关闭
            //通过sync方法同步等到通道关闭处理完毕，这里会阻塞等待通道关闭完成
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Netty Server启动失败 {} ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


















}
