package com.asiainfo.banksocket.service;

import com.asiainfo.banksocket.common.PacketHead;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.HashMap;

public interface IBankService {
   /* *//**
     * 欠费查询
     * @param packetHead	请求数据包包头
     * @param request	请求数据包内容
     * @return
     *//*
    String oweQuery(PacketHead packetHead, String request);

    *//**
     * 缴费
     * @param packetHead	请求数据包包头
     * @param request	请求数据包内容
     * @return
     *//*
    String charge(PacketHead packetHead, String request);

    String doReverse(PacketHead packetHead, String request);

    String doBalance(PacketHead packetHead, String request);

    String doBalanceResult(PacketHead packetHead, String request);

    String queryTest();*/

    /**
     * 缴费查询（余额查询）
     *
     * */
    String queryBalance(String packetHead, String request) throws IOException;

    /**
     * 销账(余额充值)
     *
     * */
    String rechargeBalance(String packetHead, String request) throws IOException;

    /**
     * 反销（余额充值回退[非充值卡]）
     *
     * */
    String rollRechargeBalance(String packetHead, String request) throws ClientProtocolException, IOException;
}
