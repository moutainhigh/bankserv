package com.asiainfo.banksocket.common;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 模拟客户端
 */
public class SocketClient {
    public static String sendSocktMsg(String ipAddr, byte[] datas) throws Exception{
        //解析服务器地址和端口号
        //int dotPos = ipAddr.indexOf(':');
        // String ip = ipAddr.substring(0, dotPos).trim();
        //int port = Integer.parseInt(ipAddr.substring(dotPos+1).trim());
        InetSocketAddress endpoint = new InetSocketAddress("127.0.0.1", 12345);

        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            socket = new Socket();
            //设置发送逗留时间2秒
            socket.setSoLinger(true, 2);
            //设置InputStream上调用 read()阻塞超时时间
            //socket.setSoTimeout(10000);
            //设置socket发包缓冲为32k；
            socket.setSendBufferSize(32*1024);
            //设置socket底层接收缓冲为32k
            socket.setReceiveBufferSize(32*1024);
            //关闭Nagle算法.立即发包
            socket.setTcpNoDelay(true);
            //连接服务器
            socket.connect(endpoint);
            //获取输出输入流
            out = socket.getOutputStream();
            in = socket.getInputStream();
            //输出请求
            out.write(datas);
            out.flush();
            //接收应答
            BufferedReader br = new BufferedReader( new InputStreamReader(in,"UTF-8") , 4096);
            StringWriter received = new StringWriter(4096);
            char[] charBuf = new char[4096];
            int size = 0;
            char lastChar = 0;
/*	        do {
	            size = br.read(charBuf , 0 , 4096);
	            lastChar = charBuf[size-1];
	            if(lastChar == 0){
	                received.write(charBuf, 0, size - 1);
	            }
	            received.write(charBuf,0,size);
	            //System.out.println(received.toString());
	        }while(lastChar != 0);*/

            size = br.read(charBuf , 0 , 4096);
            lastChar = charBuf[size-1];
            if(lastChar == 0){
                received.write(charBuf, 0, size - 1);
            }
            received.write(charBuf,0,size);

            return received.toString();

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
