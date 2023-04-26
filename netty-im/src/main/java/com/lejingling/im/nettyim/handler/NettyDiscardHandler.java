package com.lejingling.im.nettyim.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = Logger.getAnonymousLogger();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        try {
            logger.info("收到消息,丢弃如下: ");
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
            }
            System.out.println();
        } finally {
            // 丢弃收到的数据
            in.release();
        }
    }
}
