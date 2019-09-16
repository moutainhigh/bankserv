package com.asiainfo.banksocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.banksocket.common.BankChargeRecord;
import com.asiainfo.banksocket.common.BankHttpUrl;
import com.asiainfo.banksocket.common.utils.HttpUtil;
import com.asiainfo.banksocket.common.utils.HttpUtil.HttpResult;
import com.asiainfo.banksocket.dao.BankDao;
import com.asiainfo.banksocket.service.IBankService;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



@Service
public class BankServiceImpl implements IBankService {



    @Autowired
    BankDao bankDao;

    @Autowired
    BankHttpUrl bankHttpUrl;








    /**
     * 缴费查询（余额查询）
     *  @param packetHead	请求数据包包头
     *  @param request	请求数据包内容
     *  @return
     * */
    @Override
    public String queryBalance(String packetHead, String request) throws IOException {
        String result="";
        String content=request.substring(33,request.length()-1);//内容
        //String content="CDMA  13324382737         20190812065724";
        HashMap<String,String> map= getBankInfo(packetHead);
        String bankHead=map.get("bankHead").toString();
        String bankId=map.get("bankId").toString();
        String staffId=map.get("staffId").toString();
        if(bankId.equals("")||staffId.equals("")||bankHead.equals("false")){
            result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
            result+="110110#";
        }else {
            HashMap<String, Object> operAttrStructMap = new HashMap<String, Object>();//操作人属性
            operAttrStructMap.put("staffId", staffId);
            operAttrStructMap.put("operOrgId", bankId);
            operAttrStructMap.put("operTime", content.substring(26));
            operAttrStructMap.put("operPost", 0);
            operAttrStructMap.put("operServiceId", 0);
            operAttrStructMap.put("lanId", 0);

            HashMap<String, Object> svcObjectStructMap = new HashMap<String, Object>();//服务对象条件
            svcObjectStructMap.put("objType", "3");
            svcObjectStructMap.put("objValue", content.substring(6, 26));
            svcObjectStructMap.put("objAttr", "");
            svcObjectStructMap.put("dataArea", "");


            String rechargeSource = "手机";

            String resp = null;
            JSONObject obj = new JSONObject();
            obj.put("operAttrStruct", operAttrStructMap);
            obj.put("svcObjectStruct", svcObjectStructMap);
            obj.put("queryFlag", "1");
            obj.put("queryItemType", "0");
            obj.put("areacode", "");
            obj.put("systemId", "1");
            String query = obj.toString();
       /* log.info("发送到URL的报文为：");
        log.info(query);*/
            ApplicationContext ac = new ClassPathXmlApplicationContext("httpUrl_jl.xml");
            Map<String, String> object = new HashMap<String, String>();
            object.put("appID", "1111111");
            bankHttpUrl = (BankHttpUrl) ac.getBean("BankHttpUrl");
            HttpResult result2 = HttpUtil.doPostJson(bankHttpUrl.getQueryBalanceUrl(), query, object);
            JSONObject json = JSON.parseObject(result2.getData());
            if(result2.getCode() == HttpStatus.SC_OK){
                object.clear();
                object.putAll(result2.getHeaders());
                result += "1100  1";//交易码 +正确返回标识
                JSONObject  balanceQuery=(JSONObject) JSON.parse(json.get("balanceQuery").toString());
                HashMap<String, Object> accountInfo=searchAcctInfo(content.substring(6,26));
                String custName=accountInfo.get("custName").toString();//账户名
                String telAcctName=accountInfo.get("telAcctName").toString();//账户
                result+=String.format("%1$-15s", telAcctName)+String.format("%1$-60s", custName);
                //String acctId=balanceQuery.get("accId").toString();
                String s = "";
                if (!json.get("realBalance").toString().equals("null")) {
                    int balance = Integer.parseInt(json.get("realBalance").toString());
                    double num;
                    if (balance != 0) {
                        num = (double) (balance / 100.0);
                        if(num<0) {
                            s = Double.toString(num).replace("-", "");
                        }else{
                            s ="-"+Double.toString(num);
                        }

                    }
                }
                result+=String.format("%1$-12s", s)+"#";//金额
            }else{
                result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                result+="110110#";
            }
        }
        /*try {
            URL url = new URL(bankHttpUrl.getQueryBalanceUrl()); //url地址
            //URL url = new URL("http://136.160.153.42:8026/billsrv/openApi/QueryBalance");


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                os.write(query.getBytes("UTF-8"));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                //log.info("返回来的报文："+sbf.toString());
                resp = sbf.toString();
                System.out.println(resp);

            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JSONObject json = (JSONObject) JSON.parse(resp);
            String resultCode=json.get("resultCode").toString();
            result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
            if(resultCode.equals("0")) {
                result += "1100  1";//交易码 +正确返回标识
                JSONObject  balanceQuery=(JSONObject) JSON.parse(json.get("balanceQuery").toString());
                HashMap<String, Object> accountInfo=searchAcctInfo(content.substring(6,26));
                String custName=accountInfo.get("custName").toString();//账户名
                String telAcctName=accountInfo.get("telAcctName").toString();//账户
                result+=String.format("%1$-15s", telAcctName)+String.format("%1$-60s", custName);
                //String acctId=balanceQuery.get("accId").toString();
                String s = "";
                if (!json.get("realBalance").toString().equals("null")) {
                    int balance = Integer.parseInt(json.get("realBalance").toString());
                    double num;
                    if (balance != 0) {
                        num = (double) (balance / 100.0);
                        if(num<0) {
                            s = Double.toString(num).replace("-", "");
                        }else{
                            s ="-"+Double.toString(num);
                        }

                    }
                }
                result+=String.format("%1$-12s", s)+"#";//金额
            }else{
                result+="110110#";
            }
        }*/


        return result;
    }




    /**
     * 销账(余额充值)
     *
     * */
    @Override
    public String rechargeBalance(String packetHead, String param) throws IOException {

        String result="";
        String content=param.substring(33,param.length()-1);//内容
        HashMap<String,String> map= getBankInfo(packetHead);
        String bankHead=map.get("bankHead").toString();
        String bankId=map.get("bankId").toString();
        String staffId=map.get("staffId").toString();
        if(bankId.equals("")||staffId.equals("")||bankHead.equals("false")){
            result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
            result+="120666#";
        }else{
            String row=bankDao.checkRowNum(content.substring(0,12),packetHead.substring(2,6),bankId);
            if(Integer.parseInt(row)<=0){
                //String content="000220000574313014344470          50.00201908090655121";
                HashMap<String,Object> operAttrStructMap=new HashMap<String,Object>();//操作人属性
                operAttrStructMap.put("staffId",staffId);
                operAttrStructMap.put("operOrgId",bankId);
                operAttrStructMap.put("operTime",content.substring(39));
                operAttrStructMap.put("operPost",0);
                operAttrStructMap.put("operServiceId","");
                operAttrStructMap.put("lanId",0);

                HashMap<String,Object> svcObjectStructMap=new HashMap<String,Object>();//服务对象条件
                svcObjectStructMap.put("objType","1");
                svcObjectStructMap.put("objValue",content.substring(12,27));
                svcObjectStructMap.put("objAttr","");
                svcObjectStructMap.put("dataArea","");


                String flowId=content.substring(0,12);
                String rechargeSource="手机";
                String num=content.substring(27,39).replaceAll(" ","");
                float scale = Float.valueOf(num);
                DecimalFormat fnum = new DecimalFormat("##0.00");
                scale = Float.valueOf(fnum.format(scale));
                Integer rechargeAmount= (int)(scale*100);

                String resp= null;
                JSONObject obj = new JSONObject();
                obj.put("operAttrStruct", operAttrStructMap);
                obj.put("flowId", flowId);
                obj.put("rechargeSource",rechargeSource);
                obj.put("svcObjectStruct", svcObjectStructMap);
                obj.put("destinationIdType","1");
                obj.put("balanceItemTypeId","");
                obj.put("rechargeUnit","0");
                obj.put("rechargeAmount",rechargeAmount);
                obj.put("systemId","1");
                String query = obj.toString();
                //log.info("发送到URL的报文为：")log.info(query);
                ApplicationContext ac=new ClassPathXmlApplicationContext("httpUrl_jl.xml");
                bankHttpUrl=(BankHttpUrl)ac.getBean("BankHttpUrl");
                Map<String,String> object=new HashMap<String, String>();
                object.put("appID","1111111");

                HttpResult result2 = HttpUtil.doPostJson(bankHttpUrl.getRechargeBalanceUrl(), query, object);
                JSONObject json =JSON.parseObject(result2.getData());
                //状态码为请求成功
                if(result2.getCode() == HttpStatus.SC_OK){
                    object.clear();
                    object.putAll(result2.getHeaders());
                    result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                    result += "1200  ";
                    String paymentId = "3769036028  #";//暂时写死，后面补充 （12位）
                    BankChargeRecord bankChargeRecord=new BankChargeRecord();
                    bankChargeRecord.setPayment_id("3769036028");
                    bankChargeRecord.setOther_payment_id(flowId);
                    bankChargeRecord.setCreated_date(content.substring(39,content.length()-1));
                    bankChargeRecord.setPayment_date(json.get("effDate").toString());
                    bankChargeRecord.setAmount(String.valueOf(rechargeAmount));
                    bankChargeRecord.setState("0");
                    bankChargeRecord.setSo_region_code(packetHead.substring(2,6));
                    bankChargeRecord.setBankid(bankId);
                    bankChargeRecord.setBusi_code("4103");
                    bankChargeRecord.setStaff_id(staffId);
                    bankDao.insertBankChargeRecord(bankChargeRecord);
                }else{
                    result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                    result+="120666#";
                }
                /*try {
                    URL url = new URL(bankHttpUrl.getRechargeBalanceUrl()); //url地址
                    //URL url = new URL("http://136.160.153.42:8026/billsrv/openApi/RechargeBalance"); //url地址
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.connect();

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(query.getBytes("UTF-8"));
                    }

                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {
                        String lines;
                        StringBuffer sbf = new StringBuffer();
                        while ((lines = reader.readLine()) != null) {
                            lines = new String(lines.getBytes(), "utf-8");
                            sbf.append(lines);
                        }
                        //log.info("返回来的报文："+sbf.toString());
                        resp = sbf.toString();
                        System.out.println(resp);

                    }
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    JSONObject json = (JSONObject) JSON.parse(resp);

                    if(json.get("resultCode").equals("0")) {
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                        result += "1200  ";
                        String paymentId = "3769036028  #";//暂时写死，后面补充 （12位）
                        BankChargeRecord bankChargeRecord=new BankChargeRecord();
                        bankChargeRecord.setPayment_id("3769036028");
                        bankChargeRecord.setOther_payment_id(flowId);
                        bankChargeRecord.setCreated_date(content.substring(39,content.length()-1));
                        bankChargeRecord.setPayment_date(json.get("effDate").toString());
                        bankChargeRecord.setAmount(String.valueOf(rechargeAmount));
                        bankChargeRecord.setState("0");
                        bankChargeRecord.setSo_region_code(packetHead.substring(2,6));
                        bankChargeRecord.setBankid(bankId);
                        bankChargeRecord.setBusi_code("4103");
                        bankChargeRecord.setStaff_id(staffId);
                        bankDao.insertBankChargeRecord(bankChargeRecord);
                    }else{
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                        result+="120666#";
                    }
                }*/
            }
        }




        return result;
    }

    /**
     * 反销（余额充值回退[非充值卡]）
     *
     * */
    @Override
    public String rollRechargeBalance(String  packetHead, String request)
            throws ClientProtocolException, IOException{
        String returnResult="";

        String content=request.substring(33,request.length()-1);//内容
        //String content="000220030463363019952002   3769099055         50.0020190809141357";
        HashMap<String,String> map= getBankInfo(packetHead);
        String bankHead=map.get("bankHead").toString();
        String bankId=map.get("bankId").toString();
        String staffId=map.get("staffId").toString();
        if(bankId.equals("")||staffId.equals("")||bankHead.equals("false")){
            returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
            returnResult += "16030 #";
        }else {
            HashMap<String, Object> operAttrStructMap = new HashMap<String, Object>();//操作人属性
            operAttrStructMap.put("staffId", staffId);
            operAttrStructMap.put("operOrgId", bankId);
            operAttrStructMap.put("operTime", content.substring(51));
            operAttrStructMap.put("operPost", 0);
            operAttrStructMap.put("operServiceId", "");
            operAttrStructMap.put("lanId", 0);
            HashMap<String, Object> svcObjectStructMap = new HashMap<String, Object>();//服务对象条件
            svcObjectStructMap.put("objType", "3");
            //svcObjectStructMap.put("objValue",content.substring(12,27));
            svcObjectStructMap.put("objValue", "18143661019");
            svcObjectStructMap.put("objAttr", "");
            svcObjectStructMap.put("dataArea", "");

            String objValue = content.substring(12, 27);
            String prodInstId = bankDao.queryProdInstId(objValue);
            HashMap<String, Object> result = bankDao.queryProdInstInfo(prodInstId, objValue);
            String acc_Num = result.get("ACC_NUM").toString();
            String prod_id = result.get("PROD_ID").toString();
            if (prod_id.equals("379")) {
                prod_id = "2";
            }

            //379 -- 移动
            //String acc_Num="18143661019";String prod_id="2";


            String resp = null;
            JSONObject obj = new JSONObject();
            obj.put("operAttrStruct", operAttrStructMap);
            obj.put("srcServiceId", content.substring(27, 39));
            obj.put("reqServiceId", content.substring(0, 12));
            obj.put("destinationAccount", acc_Num);
            obj.put("destinationAttr", prod_id);
            obj.put("systemId", "1");
            String query = obj.toString();
            // log.info("发送到URL的报文为：");log.info(query);
            ApplicationContext ac = new ClassPathXmlApplicationContext("httpUrl_jl.xml");
            bankHttpUrl = (BankHttpUrl) ac.getBean("BankHttpUrl");
            Map<String, String> object = new HashMap<String, String>();
            object.put("appID", "1111111");

            HttpResult result2 = HttpUtil.doPostJson(bankHttpUrl.getRollRechargeBalanceUrl(), query, object);
            JSONObject json = JSON.parseObject(result2.getData());
            //状态码为请求成功
            if (result2.getCode() == HttpStatus.SC_OK) {
                object.clear();
                object.putAll(result2.getHeaders());
                returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                returnResult += "1600  ";
                returnResult += "3769036028  #";//暂时写死，后面补充 （12位）
                BankChargeRecord bankChargeRecord = new BankChargeRecord();
                bankChargeRecord.setUnpay_other_payment_id(content.substring(0, 12));//返销流水
                bankChargeRecord.setUnpay_payment_id(json.get("reqServiceId").toString());//计费流水
                bankChargeRecord.setPayment_id(content.substring(27, 39));//缴费流水
                bankDao.updateBankChargeRecord(bankChargeRecord);
            } else {
                returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                returnResult += "16030 #";
            }
        }

       /* try {
            URL url = new URL(bankHttpUrl.getRollRechargeBalanceUrl()); //url地址
            //URL url = new URL("http://136.160.153.42:8026/billsrv/openApi/RollRechargeBalance"); //url地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                os.write(query.getBytes("UTF-8"));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                //log.info("返回来的报文："+sbf.toString());
                resp = sbf.toString();
                System.out.println(resp);

            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JSONObject json = (JSONObject) JSON.parse(resp);
            if(json.get("resultCode").equals("0")) {
                returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                returnResult += "1600  ";
                returnResult += "3769036028  #";//暂时写死，后面补充 （12位）
                BankChargeRecord bankChargeRecord=new BankChargeRecord();
                bankChargeRecord.setUnpay_other_payment_id(content.substring(0,12));//返销流水
                bankChargeRecord.setUnpay_payment_id(json.get("reqServiceId").toString());//计费流水
                bankChargeRecord.setPayment_id(content.substring(27,39));//缴费流水
                bankDao.updateBankChargeRecord(bankChargeRecord);

            }else{
                returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                returnResult+="16030 #";
            }
        }*/


        return returnResult;
    }



    /**
     * 查询账户信息
     *
     * */
    public HashMap<String, Object> searchAcctInfo(String phone){
        String result="";
        HashMap<String,Object> stdCcrQueryAcctMap=new HashMap<String,Object>();//服务对象条件
        stdCcrQueryAcctMap.put("areaCode","");
        stdCcrQueryAcctMap.put("value",phone);
        stdCcrQueryAcctMap.put("valueType","1");
        stdCcrQueryAcctMap.put("queryType","1");

        JSONObject obj = new JSONObject();
        obj.put("stdCcrQueryAcct", stdCcrQueryAcctMap);

        String query = obj.toString();
        try {
            URL url = new URL("http://136.160.153.42:8026//billing/acct/std/searchAcctInfo"); //url地址
            //URL url = new URL("http://136.160.153.42:8026/billsrv/openApi/QueryBalance");


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                os.write(query.getBytes("UTF-8"));
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                //log.info("返回来的报文："+sbf.toString());
                result = sbf.toString();
                System.out.println(result);

            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> hashMap=JSON.parseObject(result, HashMap.class);
        HashMap<String, Object> hashMap2=(HashMap<String, Object>)hashMap.get("stdCcaQueryAcct");
        ArrayList<Map<String, Object>> list=(ArrayList) hashMap2.get("queryAcctInfo");
        String custName=list.get(0).get("custName").toString();
        String telAcctName=list.get(0).get("telAcctName").toString();
        HashMap<String, Object> map=new HashMap<String, Object>();
        map.put("custName",custName);
        map.put("telAcctName",telAcctName);

        return  map;

    }

    /**
     *DOM4J 解析xml文件
     * */
    public String dom4jXml(String url,String head){
        String result="";
        // 解析books.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载xml文件,获取docuemnt对象。
            Document document = reader.read(new File(url));
            // 通过document对象获取根节点bookstore
            Element bookStore = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = bookStore.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                System.out.println("=====开始遍历=====");
                Element book = (Element) it.next();
                // 获取book的属性名以及 属性值
               /* List<Attribute> bookAttrs = book.attributes();
                for (Attribute attr : bookAttrs) {
                    System.out.println("属性名：" + attr.getName() + "--属性值："
                            + attr.getValue());
                }*/
                Iterator itt = book.elementIterator();
                while (itt.hasNext()) {
                    Element bookChild = (Element) itt.next();
                    System.out.println("节点名：" + bookChild.getName() + "--节点值：" + bookChild.getStringValue());
                    if(bookChild.getName().equals(head)){
                        result=bookChild.getStringValue();
                    }
                }
                System.out.println("=====结束遍历=====");
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return result;
    }

    public HashMap<String,String> getBankInfo(String packetHead){
        HashMap<String,String> map=new HashMap<>();
        String bankHeadId=packetHead.substring(0,2);
        String areaId=packetHead.substring(2,6);
        File directory = new File("");//设定为当前文件夹
        String xmlUrl=directory.getAbsolutePath()+"/src/main/resources/bankByHead.xml";
        String bankIdXml=directory.getAbsolutePath()+"/src/main/resources/bankId.xml";
        String staffIdXml=directory.getAbsolutePath()+"/src/main/resources/staffId.xml";

        if(bankHeadId.equals("06")){
            bankHeadId="YZ06";
        }else if(bankHeadId.equals("31")){
            bankHeadId="TM31";
        }

        String bankHead=dom4jXml(xmlUrl,bankHeadId);
        if(bankHead.equals("")){
            bankHead="false";
        }
        String bankId=dom4jXml(bankIdXml,bankHead);
        String staffId=dom4jXml(staffIdXml,bankHead+areaId);
        map.put("bankHead",bankHead);
        map.put("bankId",bankId);
        map.put("staffId",staffId);
        return  map;
    }


}
