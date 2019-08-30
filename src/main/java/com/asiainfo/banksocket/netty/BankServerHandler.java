package com.asiainfo.banksocket.netty;

import com.asiainfo.banksocket.common.PacketHead;
import com.asiainfo.banksocket.service.IBankService;

import com.asiainfo.banksocket.service.impl.BankServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component
public class BankServerHandler extends ChannelInboundHandlerAdapter{

    @Autowired
    BankServiceImpl bankService;


    private static BankServerHandler bankServerHandler;

    @PostConstruct
    public void init(){
        bankServerHandler=this;
        bankServerHandler.bankService=this.bankService;
    }
    /**
     * ChannelInboundHandlerAdapter：ChannelInboundHandlerAdapter是ChannelInboundHandler的一个简单实现，默认情况下不会做任何处理，
     *   只是简单的将操作通过fire*方法传递到ChannelPipeline中的下一个ChannelHandler中让链中的下一个ChannelHandler去处理。
     *
     * SimpleChannelInboundHandler：SimpleChannelInboundHandler支持泛型的消息处理，默认情况下消息处理完将会被自动释放，无法提供
     *   fire*方法传递给ChannelPipeline中的下一个ChannelHandler,如果想要传递给下一个ChannelHandler需要调用ReferenceCountUtil#retain方法。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object message){
        //System.out.println("ServerHandler receive msg:"+ message.toString());
        //写消息:先得到channel，再写入通道，然后flush刷新通道把消息发出去
        //ctx.channel().writeAndFlush("this is ServerHandler repyl message happend at" + System.currentTimeMillis());
        //把消息往下一个Hanlder传
        //ctx.fireChannelRead(message);
        String request=message.toString();
        //HashMap<String,Object> result=new HashMap<String,Object>();
        String result=null;
        if(request.length()<32){
            //向客户端回复请求包数据异常（包数据不完整，包头长度不足）
            //result="no";
        }else{
            //包头实例
           // PacketHead packetHead=new PacketHead(request);
            String head=request.substring(0,30);
            //获取交易码
            String busiCode=request.substring(30,33);
            //根据交易码调取相应接口
            switch(busiCode){
                case "110":
                    //交易码110，缴费查询（余额查询）
                    result=bankServerHandler.bankService.queryBalance(head,request);
                    //result="6510000012377310000030603001缴费号码|boss|用户应缴款|当前余额实时话费|费率区代码|";
                    break;
                case "120":
                    //交易码120，销账(余额充值)
                    result=bankServerHandler.bankService.rechargeBalance(head,request);
                    break;
                case "160":
                    //交易码160，反销（余额充值回退[非充值卡]）
                    result=bankServerHandler.bankService.rollRechargeBalance(head,request);
                    break;
                default:
                    break;
            }
        }
        ctx.channel().writeAndFlush(result);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event=(IdleStateEvent) evt;
            String type="";
            if(event.state()	==IdleState.READER_IDLE){
                type="read idle";
            }else if(event.state()==IdleState.WRITER_IDLE){
                type="write idle";
            }else if(event.state()==IdleState.ALL_IDLE){
                type="all idle";
            }
            ctx.channel().writeAndFlush(type);
        }else{

        }
    }


}

