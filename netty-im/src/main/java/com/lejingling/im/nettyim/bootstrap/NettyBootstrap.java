package com.lejingling.im.nettyim.bootstrap;

import com.lejingling.im.nettyim.server.NettyDiscardServer;

public class NettyBootstrap {
    public static void main(String[] args) {
        new NettyDiscardServer(8080).runServer();
    }
}
