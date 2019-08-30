package com.asiainfo.banksocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class StartSocketServer {
    public void startServer(){
        //1.定义server启动类
        ServerBootstrap serverBootstarp=new ServerBootstrap();

        //2.定义工作组:boss分发请求给各个worker(boss负责监听端口请求，worker负责处理请求(读写))
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        //3.定义工作组
        serverBootstarp.group(boss,worker);

        //4.设置通道channel
        serverBootstarp.channel(NioServerSocketChannel .class);

        //5.添加handler，管道中的处理器，通过ChannelInitializer来构造
        serverBootstarp.childHandler(new ChannelInitializer<Channel>(){

            @Override
            protected void initChannel(Channel channel){
                ChannelPipeline pipeline=channel.pipeline();
                pipeline.addLast(new StringDecoder());
                //pipeline.addLast(new IdleStateHandler(0,5,0));
                pipeline.addLast("bankServerHandler",new BankServerHandler());
                pipeline.addLast(new StringEncoder());
            }
        });

        //6.设置参数
        //连接缓冲池大小
        serverBootstarp.option(ChannelOption.SO_BACKLOG, 50);
        //维持链接的活跃，清除死链接()
        //serverBootstarp.childOption(ChannelOption.SO_KEEPALIVE, true);
        //关闭延迟发送
        serverBootstarp.childOption(ChannelOption.TCP_NODELAY, true);

        //7.绑定ip和prot
        try {
            ChannelFuture channelFuture=serverBootstarp.bind("127.0.0.1",12345);
            //监听关闭,等待服务关闭，关闭后应该释放资源
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.out.println("server start got exception:"+e);
            e.printStackTrace();
        } finally{
            //8.	优雅的关闭资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
