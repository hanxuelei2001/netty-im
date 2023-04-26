package com.lejingling.im.nettyim.server;

import com.lejingling.im.nettyim.handler.NettyDiscardHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.logging.Logger;

public class NettyDiscardServer {
    private final int serverPort;
    private Logger logger = Logger.getAnonymousLogger();
    ServerBootstrap boss = new ServerBootstrap();

    public NettyDiscardServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        // 创建反应器轮询组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workLoopGroup = new NioEventLoopGroup();

        try {
            // 设置反应器轮训组
            boss.group(bossLoopGroup, workLoopGroup);
            // 设置 nio 类型的通道
            boss.channel(NioServerSocketChannel.class);
            // 设置监听端口
            boss.localAddress(serverPort);
            // 设置通道参数
            boss.option(ChannelOption.SO_KEEPALIVE, true);
            // 装配子通道流水线
            boss.childHandler(new ChannelInitializer<SocketChannel>() {
                // 有链接到达的时候创建一个通道
                protected void initChannel(SocketChannel ch) {
                    // 流水线的职责: 负责管理通道中的 Handler 处理器
                    // 配置 handler 处理器
                    ch.pipeline().addLast(new NettyDiscardHandler());
                }
            });

            // 通过调用 sync 同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = boss.bind().sync();
            logger.info(" 服务器启动成功，监听端口: " +
                    channelFuture.channel().localAddress());
            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8 优雅关闭 EventLoopGroup
            // 释放掉所有资源包括创建的线程
            workLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }
}