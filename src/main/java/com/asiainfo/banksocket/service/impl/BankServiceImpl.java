package com.asiainfo.banksocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.banksocket.common.*;
import com.asiainfo.banksocket.common.QueryBalanceRes.BalanceQuery;
import com.asiainfo.banksocket.common.StdCcrQueryServRes.StdCcaQueryServResBean;
import com.asiainfo.banksocket.common.StdCcrQueryServRes.StdCcaQueryServResBean.StdCcaQueryServListBean;
import com.asiainfo.banksocket.common.utils.HttpUtil;
import com.asiainfo.banksocket.common.utils.HttpUtil.HttpResult;
import com.asiainfo.banksocket.common.utils.LogUtil;
import com.asiainfo.banksocket.dao.BankDao;
import com.asiainfo.banksocket.service.IBankService;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.asiainfo.banksocket.common.GetUnitedBalanceReq.StdCcrQueryBalanceBalance;
import com.asiainfo.banksocket.common.GetUnitedBalanceReq.StdCcrQueryBalanceBalance.BalanceQueryInformation;
import com.asiainfo.banksocket.common.GetUnitedBalanceRes.StdCcaQueryBalanceBalance;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
public class BankServiceImpl implements IBankService {



    @Autowired
    BankDao bankDao;

    @Autowired
    BankHttpUrl bankHttpUrl;

    @Autowired
    ErrorCode errorCode;








    /**
     * 缴费查询（余额查询）
     *  @param packetHead	请求数据包包头
     *  @param request	请求数据包内容
     *  @return
     * */
    @Override
    public String queryBalance(String packetHead, String request) throws IOException,ClientProtocolException {
        String result = "";
        try {
            LogUtil.info("socket接收的串为["+request+"]",null, this.getClass());
            String content = request.substring(33, request.length() - 1);//内容
            String phoneNum=content.substring(6, 26);
            phoneNum=phoneNum.replace(" ","");
            StdCcrQueryServRes accountInfo = searchAcctInfo(phoneNum);//查询用户信息
            StdCcaQueryServResBean stdCcaQueryServResBean=accountInfo.getStdCcaQueryServRes();
            if(stdCcaQueryServResBean==null){//用户信息不存在
                LogUtil.error("用户信息不存在", null, this.getClass());
                result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                result += "110"+errorCode.getUserInfoNoData()+"#";
                LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                return result;
            }
            List<StdCcaQueryServListBean> StdCcaQueryServListBean=stdCcaQueryServResBean.getStdCcaQueryServList();
            String custName = StdCcaQueryServListBean.get(0).getCustName();
            String destinationAttr=StdCcaQueryServListBean.get(0).getDestinationAttr();
            LogUtil.info("用户名称为："+custName,null, this.getClass());
           /* String custName = "王军";
            String destinationAttr="2";*/
            //String content="CDMA  13324382737         20190812065724";
            LogUtil.info("获取staffId、bankHead、bankId···",null, this.getClass());
            HashMap<String, String> map = getBankInfo(packetHead);
            String bankHead = map.get("bankHead").toString();
            String bankId = map.get("bankId").toString();
            String staffId = map.get("staffId").toString();
            LogUtil.info("获取到的值为[bankHead]="+bankHead,null, this.getClass());
            LogUtil.info("获取到的值为[bankId]="+bankId,null, this.getClass());
            LogUtil.info("获取到的值为[staffId]="+staffId,null, this.getClass());
            if (bankId.equals("") || staffId.equals("") || bankHead.equals("false")) {
                LogUtil.error("bankId或staffId或bankHead值为空",null,this.getClass());
                result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                result += "110"+errorCode.getBankidStaffidBankheadNoData()+"#";
                LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                return result;
            } else {
                HashMap<String, Object> operAttrStructMap = new HashMap<String, Object>();//操作人属性
                operAttrStructMap.put("staffId", staffId);
                operAttrStructMap.put("operOrgId", bankId);
                operAttrStructMap.put("operTime", content.substring(26));
                operAttrStructMap.put("operPost", 0);
                operAttrStructMap.put("operServiceId", 0);
                operAttrStructMap.put("lanId", 0);


                phoneNum=phoneNum.replace(" ","");//手机号码
                LogUtil.info("号码为："+phoneNum,null, this.getClass());
                LogUtil.info("destinationAttr="+destinationAttr,null, this.getClass());
                HashMap<String, Object> svcObjectStructMap = new HashMap<String, Object>();//服务对象条件
                svcObjectStructMap.put("objType", "3");
                svcObjectStructMap.put("objValue", phoneNum);
                svcObjectStructMap.put("objAttr", destinationAttr);//0 固话、2移动
                svcObjectStructMap.put("dataArea", "");


                String rechargeSource = "1003";

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

                Map<String, String> object = new HashMap<String, String>();
                object.put("appID", "1111111");
                LogUtil.info("[开始调用远程服务 余额查询]"+ bankHttpUrl.getQueryBalanceUrl(),null, this.getClass());
                LogUtil.info("输入参数[QueryBalanceReq]="+query,null, this.getClass());
                HttpResult balanceResult = null;
                try {
                    balanceResult = HttpUtil.doPostJson(bankHttpUrl.getQueryBalanceUrl(), query, object);
                } catch (ClientProtocolException e) {
                    LogUtil.error("连接错误", e, this.getClass());
                    result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    result += "110"+errorCode.getConnectError()+"#";
                    LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                    return result;
                } catch (IOException e) {
                    LogUtil.error("IO流错误", e, this.getClass());
                    result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    result += "110"+errorCode.getIoConnectError()+"#";
                    LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                    return result;
                }
                LogUtil.info("调用远程服务返回的报文："+balanceResult.getData().toString(),null, this.getClass());
                System.out.println(balanceResult.getCode());
                if (balanceResult.getCode() == HttpStatus.SC_OK) {
                    object.clear();
                    object.putAll(balanceResult.getHeaders());
                    result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    result += "1100  1";//交易码 +正确返回标识
                    QueryBalanceRes queryBalance= JSON.parseObject(balanceResult.getData(), QueryBalanceRes.class) ;
                    List<BalanceQuery> balanceQuery=queryBalance.getBalanceQuery();
                    BalanceQuery queryMap=balanceQuery.get(0);
                    Long acctId=queryMap.getAcctId();
                    result += String.format("%1$-15s", acctId) + String.format("%1$-60s", custName);
                   //String s = "0";
                    GetUnitedBalanceReq getUnitedBalanceReq=new GetUnitedBalanceReq();
                    StdCcrQueryBalanceBalance stdCcrQueryBalanceBalance=new StdCcrQueryBalanceBalance();
                    BalanceQueryInformation balanceQueryInformation= new BalanceQueryInformation();
                    balanceQueryInformation.setAreaCode("0431");
                    balanceQueryInformation.setDestinationAttr(destinationAttr);
                    balanceQueryInformation.setDestinationId(phoneNum);
                    balanceQueryInformation.setDestinationIdType("1");
                    balanceQueryInformation.setQueryFlag("1");
                    balanceQueryInformation.setQueryItemType("0");
                    stdCcrQueryBalanceBalance.setBalanceQueryInformation(balanceQueryInformation);
                    getUnitedBalanceReq.setStdCcrQueryBalanceBalance(stdCcrQueryBalanceBalance);

                    LogUtil.info("[开始调用远程服务 余额查询]"+ bankHttpUrl.getGetUnitedBalance(),null, this.getClass());
                    LogUtil.info("输入参数[getUnitedBalanceReq]="+getUnitedBalanceReq.toString(),null, this.getClass());
                    HttpResult unitedBalanceResult=null;
                    try {
                        unitedBalanceResult = HttpUtil.doPostJson(bankHttpUrl.getGetUnitedBalance(), getUnitedBalanceReq.toString(), object);
                        LogUtil.info("调用远程服务返回的报文："+unitedBalanceResult.getData().toString(),null, this.getClass());
                    } catch (ClientProtocolException e) {
                        LogUtil.error("连接错误", e, this.getClass());
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                        result += "110"+errorCode.getConnectError()+"#";
                        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                        return result;
                    } catch (IOException e) {
                        LogUtil.error("IO流错误", e, this.getClass());
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                        result += "110"+errorCode.getIoConnectError()+"#";
                        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                        return result;
                    }
                    LogUtil.info("调用远程服务返回的报文："+unitedBalanceResult.getData().toString(),null, this.getClass());
                    System.out.println(unitedBalanceResult.getCode());
                    if (unitedBalanceResult.getCode() == HttpStatus.SC_OK) {
                        object.clear();
                        object.putAll(unitedBalanceResult.getHeaders());
                        GetUnitedBalanceRes getUnitedBalanceRes = JSON.parseObject(unitedBalanceResult.getData(), GetUnitedBalanceRes.class);
                        StdCcaQueryBalanceBalance stdCcaQueryBalanceBalance=getUnitedBalanceRes.getStdCcaQueryBalanceBalance();
                        String totalBalanceAvailable=stdCcaQueryBalanceBalance.getTotalBalanceAvailable();
                        String num="0";
                        if (!totalBalanceAvailable.equals("null")) {
                            float realBalance=Float.parseFloat(totalBalanceAvailable);
                            if (realBalance != 0){
                                DecimalFormat df = new DecimalFormat("0.00");
                                num = df.format( realBalance / 100.0);
                                if (num.indexOf("-") > -1) {
                                    num = num.replace("-", "");
                                } else {
                                    num = "-" + num;
                                }
                            }else{
                                num="0.00";
                            }
                        }
                        result += String.format("%1$-12s", num) + "#";//金额
                    }else{
                        LogUtil.error("远程服务["+bankHttpUrl.getGetUnitedBalance()+"]服务调用失败", null, this.getClass());
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                        result += "110"+errorCode.getQuerybalanceRemoteServiceError()+"#";
                        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                        return result;
                    }
                   /* Integer realBalance=queryBalance.getRealBalance();
                    String num="0";
                    if (!realBalance.equals("null")) {
                        if (realBalance != 0){
                            DecimalFormat df = new DecimalFormat("0.00");
                            num = df.format((float) realBalance / 100.0);
                            if (num.indexOf("-") > -1) {
                                num = num.replace("-", "");
                            } else {
                                num = "-" + num;
                            }
                        }else{
                            num="0.00";
                        }
                    }
                    result += String.format("%1$-12s", num) + "#";//金额*/
                } else {
                    LogUtil.error("远程服务["+bankHttpUrl.getQueryBalanceUrl()+"]服务调用失败", null, this.getClass());
                    result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    result += "110"+errorCode.getQuerybalanceRemoteServiceError()+"#";
                    LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                    return result;
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            LogUtil.error("queryBalance服务调用失败", e, this.getClass());
            //result="查询失败，请联系管理员";
            result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
            result += "110"+errorCode.getServiceError()+"#";
            LogUtil.info("返回的串为["+result+"]",null, this.getClass());
            return result;

        }
        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
        return result;
    }




    /**
     * 销账(余额充值)
     *
     * */
    @Override
    public String rechargeBalance(String packetHead, String param) throws IOException {

        String result="";
        try{
            LogUtil.info("socket接收的串为："+param,null, this.getClass());
            String content=param.substring(33,param.length()-1);//内容
            LogUtil.info("获取staffId、bankHead、bankId···",null, this.getClass());
            HashMap<String,String> map= getBankInfo(packetHead);
            String bankHead=map.get("bankHead").toString();
            String bankId=map.get("bankId").toString();
            String staffId=map.get("staffId").toString();
            LogUtil.info("获取到的值为[bankHead]="+bankHead,null, this.getClass());
            LogUtil.info("获取到的值为[bankId]="+bankId,null, this.getClass());
            LogUtil.info("获取到的值为[staffId]="+staffId,null, this.getClass());
            if(bankId.equals("")||staffId.equals("")||bankHead.equals("false")){
                LogUtil.error("bankId或staffId或bankHead值为空",null,this.getClass());
                result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                result+="120"+errorCode.getBankidStaffidBankheadNoData()+"#";
                LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                return result;
            }else{
                String flowId=content.substring(0,12);
                String areaId=content.substring(2,6);;
                LogUtil.info("[调用数据库 查询余额充值流水号是否重复]",null, this.getClass());
                LogUtil.info("输入参数[flowId]="+flowId+"[areaId=]"+areaId+"[bankId=]"+bankId,null, this.getClass());
                String row=bankDao.checkRowNum(flowId,packetHead.substring(2,6),bankId);
                if(Integer.parseInt(row)==0){
                    //String content="000220000574313014344470          50.00201908090655121";
                    HashMap<String,Object> operAttrStructMap=new HashMap<String,Object>();//操作人属性
                    operAttrStructMap.put("staffId",staffId);
                    operAttrStructMap.put("operOrgId",bankId);
                    operAttrStructMap.put("operTime",content.substring(39,53));
                    operAttrStructMap.put("operPost",0);
                    operAttrStructMap.put("operServiceId","1");
                    operAttrStructMap.put("lanId",0);
                    String objValue=content.substring(12,27).replace(" ","");
                    LogUtil.info("号码为："+objValue,null, this.getClass());
                  /*  StdCcrQueryServRes accountInfo = searchAcctInfo(objValue);//查询用户信息
                    StdCcaQueryServResBean stdCcaQueryServResBean=accountInfo.getStdCcaQueryServRes();
                    if(stdCcaQueryServResBean==null){//用户信息不存在
                        LogUtil.error("用户信息不存在", null, this.getClass());
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                        result += "120"+errorCode.getUserInfoNoData()+"#";
                        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                        return result;
                    }
                    List<StdCcaQueryServListBean> StdCcaQueryServListBean=stdCcaQueryServResBean.getStdCcaQueryServList();
                    String destinationAttr=StdCcaQueryServListBean.get(0).getDestinationAttr();*/
                    HashMap<String,Object> svcObjectStructMap=new HashMap<String,Object>();//服务对象条件
                    svcObjectStructMap.put("objType","1");
                    svcObjectStructMap.put("objValue",objValue);
                    svcObjectStructMap.put("objAttr","1");
                    svcObjectStructMap.put("dataArea","1");


                   // String flowId=content.substring(0,12);
                    String rechargeSource="10003";
                    String num=content.substring(27,39).replaceAll(" ","");
                    float scale = Float.valueOf(num);
                    DecimalFormat fnum = new DecimalFormat("##0.00");
                    scale = Float.valueOf(fnum.format(scale));
                    Integer rechargeAmount= (int)(scale*100);
                    LogUtil.info("充值金额为（分）："+rechargeAmount,null, this.getClass());
                    String resp= null;
                    JSONObject obj = new JSONObject();
                    obj.put("operAttrStruct", operAttrStructMap);
                    obj.put("flowId", flowId);
                    obj.put("rechargeSource",rechargeSource);
                    obj.put("svcObjectStruct", svcObjectStructMap);
                    obj.put("destinationIdType","3");
                    obj.put("balanceItemTypeId","0");
                    obj.put("rechargeUnit","0");
                    obj.put("rechargeAmount",rechargeAmount);
                    obj.put("systemId","1");
                    String query = obj.toString();
                    //log.info("发送到URL的报文为：")log.info(query);
                    //ApplicationContext ac=new ClassPathXmlApplicationContext("httpUrl_jl.xml");
                    //bankHttpUrl=(BankHttpUrl)ac.getBean("BankHttpUrl");
                    Map<String,String> object=new HashMap<String, String>();
                    object.put("appID","1111111");
                    LogUtil.info("[开始调用远程服务 余额充值]"+ bankHttpUrl.getRechargeBalanceUrl(),null, this.getClass());
                    LogUtil.info("输入参数[RechargeBalanceReq]="+query,null, this.getClass());
                    HttpResult rechargeResult=null;
                    try {
                        rechargeResult = HttpUtil.doPostJson(bankHttpUrl.getRechargeBalanceUrl(), query, object);
                        LogUtil.info("调用远程服务返回的报文："+rechargeResult.getData().toString(),null, this.getClass());
                    } catch (ClientProtocolException e) {
                        LogUtil.error("连接错误", e, this.getClass());
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                        result += "120"+errorCode.getConnectError()+"#";
                        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                        return result;
                    } catch (IOException e) {
                        LogUtil.error("IO流错误", e, this.getClass());
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                        result += "120"+errorCode.getIoConnectError()+"#";
                        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                        return result;
                    }
                    //HttpResult rechargeResult = HttpUtil.doPostJson(bankHttpUrl.getRechargeBalanceUrl(), query, object);
                    JSONObject json =JSON.parseObject(rechargeResult.getData());
                    //状态码为请求成功
                    if(rechargeResult.getCode() == HttpStatus.SC_OK){
                        object.clear();
                        object.putAll(rechargeResult.getHeaders());
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                        result += "1200  ";
                        String paymentId = "3769036028";//暂时写死，后面补充 （12位）  计费流水
                        result += paymentId+"  #";
                        BankChargeRecord bankChargeRecord=new BankChargeRecord();
                        bankChargeRecord.setPayment_id(paymentId);
                        bankChargeRecord.setOther_payment_id(flowId);
                        //bankChargeRecord.setCreated_date(content.substring(39,content.length()-1));
                        //bankChargeRecord.setPayment_date(json.get("effDate").toString());
                        bankChargeRecord.setAmount(String.valueOf(rechargeAmount));
                        bankChargeRecord.setState("0");
                        bankChargeRecord.setSo_region_code(packetHead.substring(2,6));
                        bankChargeRecord.setBankid(bankId);
                        bankChargeRecord.setBusi_code("4103");
                        bankChargeRecord.setStaff_id(staffId);
                        bankDao.insertBankChargeRecord(bankChargeRecord);
                    }else{
                        LogUtil.error("调用远程服务["+bankHttpUrl.getRechargeBalanceUrl()+"]失败！",null,this.getClass());
                        result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                        result+="120"+errorCode.getRechargeBalanceRemoteServiceError()+"#";
                        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                        return result;
                    }
                }else if(Integer.parseInt(row)>0){
                    LogUtil.error("余额充值流水号重复，充值失败！",null,this.getClass());
                    result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                    result+="120"+errorCode.getRechargeBalanceFlowidSecond()+"#";
                    LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                    return result;
                }else{
                    LogUtil.error("判断余额充值流水号是否重复失败！",null,this.getClass());
                    result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                    result+="120"+errorCode.getRechargeBalanceFlowidError()+"#";
                    LogUtil.info("返回的串为["+result+"]",null, this.getClass());
                    return result;
                }
            }
        }catch(Exception e){
            LogUtil.error("rechargeBalance服务调用失败！",e,this.getClass());
            result = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
            result+="120"+errorCode.getServiceError()+"#";
        }



        LogUtil.info("返回的串为["+result+"]",null, this.getClass());
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
        String destinationAttr = "";
        try {
            LogUtil.info("socket接收的串为："+request,null, this.getClass());
            String content = request.substring(33, request.length() - 1);//内容
            //String content="000220030463363019952002   3769099055         50.0020190809141357";
            HashMap<String, String> map = getBankInfo(packetHead);
            LogUtil.info("获取staffId、bankHead、bankId···",null, this.getClass());
            String bankHead = map.get("bankHead").toString();
            String bankId = map.get("bankId").toString();
            String staffId = map.get("staffId").toString();
            LogUtil.info("获取到的值为[bankHead]="+bankHead,null, this.getClass());
            LogUtil.info("获取到的值为[bankId]="+bankId,null, this.getClass());
            LogUtil.info("获取到的值为[staffId]="+staffId,null, this.getClass());
            if (bankId.equals("") || staffId.equals("") || bankHead.equals("false")) {
                LogUtil.error("bankId或staffId或bankHead值为空",null,this.getClass());
                returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                returnResult += "160"+errorCode.getBankidStaffidBankheadNoData()+"#";
                LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
                return returnResult;
            } else {
                HashMap<String, Object> operAttrStructMap = new HashMap<String, Object>();//操作人属性
                operAttrStructMap.put("staffId", staffId);
                operAttrStructMap.put("operOrgId", bankId);
                operAttrStructMap.put("operTime", content.substring(51,65));
                operAttrStructMap.put("operPost", 0);
                operAttrStructMap.put("operServiceId", "1");
                operAttrStructMap.put("lanId", 0);


                String objValue = content.substring(12, 27);
                objValue=objValue.replace(" ","");
                LogUtil.info("号码为："+objValue,null, this.getClass());
                String prodInstId = bankDao.queryProdInstId(objValue);
                HashMap<String, Object> result = bankDao.queryProdInstInfo(prodInstId, objValue);
                String acc_Num = result.get("ACC_NUM").toString();
                String prod_id = result.get("PROD_ID").toString();

                if("379".equals(prod_id)) {
                    destinationAttr = "2";
                }else if("280000002".equals(prod_id)||"900037".equals(prod_id)) {
                    destinationAttr = "3";
                }


                System.out.println(destinationAttr);
                String resp = null;
                JSONObject obj = new JSONObject();
                String srcServiceId=content.substring(27, 39).replace(" ","");
                obj.put("operAttrStruct", operAttrStructMap);
                obj.put("srcServiceId", srcServiceId);
                obj.put("reqServiceId", content.substring(0, 12));
                obj.put("destinationAccount", acc_Num);
                obj.put("destinationAttr",destinationAttr);
                obj.put("systemId", "1");
                String query = obj.toString();
                // log.info("发送到URL的报文为：");log.info(query);
                /*ApplicationContext ac = new ClassPathXmlApplicationContext("httpUrl_jl.xml");
                bankHttpUrl = (BankHttpUrl) ac.getBean("BankHttpUrl");*/
                Map<String, String> object = new HashMap<String, String>();
                object.put("appID", "1111111");
                LogUtil.info("[开始调用远程服务 余额回退]"+ bankHttpUrl.getRollRechargeBalanceUrl(),null, this.getClass());
                LogUtil.info("输入参数[RollRechargeBalanceReq]="+query,null, this.getClass());
                HttpResult RollRechargeResult=null;
                try {
                    RollRechargeResult = HttpUtil.doPostJson(bankHttpUrl.getRollRechargeBalanceUrl(), query, object);
                    LogUtil.info("远程服务返回的报文："+RollRechargeResult.getData().toString(),null, this.getClass());
                } catch (ClientProtocolException e) {
                    LogUtil.error("连接错误", e, this.getClass());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    returnResult += "160"+errorCode.getConnectError()+"#";
                    LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
                    return returnResult;
                } catch (IOException e) {
                    LogUtil.error("IO流错误", e, this.getClass());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    returnResult += "160"+errorCode.getIoConnectError()+"#";
                    LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
                    return returnResult;
                }
               // HttpResult RollRechargeResult = HttpUtil.doPostJson(bankHttpUrl.getRollRechargeBalanceUrl(), query, object);
                JSONObject json = JSON.parseObject(RollRechargeResult.getData());
                //状态码为请求成功
                if (RollRechargeResult.getCode() == HttpStatus.SC_OK) {
                    object.clear();
                    object.putAll(RollRechargeResult.getHeaders());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                    returnResult += "1600  ";
                    returnResult += "3769036028  #";//暂时写死，后面补充 （12位） 计费流水
                    BankChargeRecord bankChargeRecord = new BankChargeRecord();
                    bankChargeRecord.setUnpay_other_payment_id(content.substring(0, 12));//返销流水
                    bankChargeRecord.setUnpay_payment_id(json.get("reqServiceId").toString());//计费流水
                    bankChargeRecord.setPayment_id(content.substring(27, 39));//缴费流水
                    bankDao.updateBankChargeRecord(bankChargeRecord);
                } else {
                    LogUtil.error("调用远程服务["+bankHttpUrl.getRollRechargeBalanceUrl()+"]失败！",null,this.getClass());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
                    returnResult += "160"+errorCode.getRollRechargeBalanceRemoteServiceError()+"#";
                    LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
                    return returnResult;
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            LogUtil.error("调用rollRechargeBalance服务失败！",e,this.getClass());
            returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
            returnResult += "160"+errorCode.getServiceError()+"#";
            LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
            return returnResult;
        }

        LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
        return returnResult;
    }

    /**
     *
     *
     * */
    public String getUnitedBalance(String  packetHead, String request) throws IOException {
        String returnResult="";
        try{
                LogUtil.info("socket接收的串为["+request+"]",null, this.getClass());
                String content = request.substring(33, request.length() - 1);//内容
                String phoneNum=content.substring(6, 26);
                phoneNum=phoneNum.replace(" ","");
                StdCcrQueryServRes accountInfo = searchAcctInfo(phoneNum);//查询用户信息
                StdCcaQueryServResBean stdCcaQueryServResBean=accountInfo.getStdCcaQueryServRes();
                if(stdCcaQueryServResBean==null){//用户信息不存在
                    LogUtil.error("用户信息不存在", null, this.getClass());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    returnResult += "110"+errorCode.getUserInfoNoData()+"#";
                    LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
                    return returnResult;
                }
                List<StdCcaQueryServListBean> StdCcaQueryServListBean=stdCcaQueryServResBean.getStdCcaQueryServList();
                String custName = StdCcaQueryServListBean.get(0).getCustName();
                String destinationAttr=StdCcaQueryServListBean.get(0).getDestinationAttr();
                LogUtil.info("用户名称为："+custName,null, this.getClass());
                GetUnitedBalanceReq getUnitedBalanceReq=new GetUnitedBalanceReq();
                StdCcrQueryBalanceBalance stdCcrQueryBalanceBalance=new StdCcrQueryBalanceBalance();
                BalanceQueryInformation balanceQueryInformation= new BalanceQueryInformation();
                balanceQueryInformation.setAreaCode("0431");
                balanceQueryInformation.setDestinationAttr(destinationAttr);
                balanceQueryInformation.setDestinationId(phoneNum);
                balanceQueryInformation.setDestinationIdType("1");
                balanceQueryInformation.setQueryFlag("1");
                balanceQueryInformation.setQueryItemType("0");
                stdCcrQueryBalanceBalance.setBalanceQueryInformation(balanceQueryInformation);
                getUnitedBalanceReq.setStdCcrQueryBalanceBalance(stdCcrQueryBalanceBalance);

                Map<String,String> object=new HashMap<String, String>();
                object.put("appID","1111111");
                LogUtil.info("[开始调用远程服务 余额查询]"+ bankHttpUrl.getGetUnitedBalance(),null, this.getClass());
                LogUtil.info("输入参数[getUnitedBalanceReq]="+getUnitedBalanceReq.toString(),null, this.getClass());
                HttpResult unitedBalanceResult=null;
                try {
                    unitedBalanceResult = HttpUtil.doPostJson(bankHttpUrl.getGetUnitedBalance(), getUnitedBalanceReq.toString(), object);
                    LogUtil.info("调用远程服务返回的报文："+unitedBalanceResult.getData().toString(),null, this.getClass());
                } catch (ClientProtocolException e) {
                    LogUtil.error("连接错误", e, this.getClass());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    returnResult += "110"+errorCode.getConnectError()+"#";
                    LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
                    return returnResult;
                } catch (IOException e) {
                    LogUtil.error("IO流错误", e, this.getClass());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    returnResult += "110"+errorCode.getIoConnectError()+"#";
                    LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
                    return returnResult;
                }
                LogUtil.info("调用远程服务返回的报文："+unitedBalanceResult.getData().toString(),null, this.getClass());
                System.out.println(unitedBalanceResult.getCode());
                if (unitedBalanceResult.getCode() == HttpStatus.SC_OK) {
                    object.clear();
                    object.putAll(unitedBalanceResult.getHeaders());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    returnResult += "1100  1";//交易码 +正确返回标识
                    GetUnitedBalanceRes queryBalance= JSON.parseObject(unitedBalanceResult.getData(), GetUnitedBalanceRes.class) ;
                   /* List<BalanceQuery> balanceQuery=queryBalance.getBalanceQuery();
                    BalanceQuery queryMap=balanceQuery.get(0);
                    Long acctId=queryMap.getAcctId();
                    returnResult += String.format("%1$-15s", acctId) + String.format("%1$-60s", custName);
                    String s = "0";
                    Integer realBalance=queryBalance.getRealBalance();
                    String num="0";
                    if (!realBalance.equals("null")) {
                        if (realBalance != 0){
                            DecimalFormat df = new DecimalFormat("0.00");
                            num = df.format((float) realBalance / 100.0);
                            if (num.indexOf("-") > -1) {
                                num = num.replace("-", "");
                            } else {
                                num = "-" + num;
                            }
                        }else{
                            num="0.00";
                        }
                    }
                    returnResult += String.format("%1$-12s", num) + "#";//金额*/
                } else {
                    LogUtil.error("远程服务["+bankHttpUrl.getGetUnitedBalance()+"]服务调用失败", null, this.getClass());
                    returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "125");//包头+包长度
                    returnResult += "110"+errorCode.getQuerybalanceRemoteServiceError()+"#";
                    LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
                    return returnResult;
                }
        }catch(Exception e){
            System.out.println(e.getMessage());
            LogUtil.error("调用rollRechargeBalance服务失败！",e,this.getClass());
            returnResult = packetHead.substring(0, 20) + String.format("%1$-10s", "49");//包头+包长度
            returnResult += "160"+errorCode.getServiceError()+"#";
            LogUtil.info("返回的串为["+returnResult+"]",null, this.getClass());
            return returnResult;
        }


        return returnResult;
    }



    /**
     * 查询账户信息
     *
     * */
    public StdCcrQueryServRes searchAcctInfo(String phone) throws IOException {
        HashMap<String,Object> stdCcrQueryAcctMap=new HashMap<String,Object>();//服务对象条件
        stdCcrQueryAcctMap.put("areaCode","0431");
        stdCcrQueryAcctMap.put("value",phone);
        stdCcrQueryAcctMap.put("valueType","1");
        stdCcrQueryAcctMap.put("queryType","2");

        JSONObject obj = new JSONObject();
        obj.put("stdCcrQueryServ", stdCcrQueryAcctMap);
        Map<String, String> object = new HashMap<String, String>();
        object.put("appID", "1111111");

        String query = obj.toString();
        LogUtil.info("[开始调用远程服务 用户查询]"+ bankHttpUrl.getSearchServInfo(),null, this.getClass());
        LogUtil.info("输入参数[StdCcrQueryServReq]="+query,null, this.getClass());
        HttpResult result = HttpUtil.doPostJson(bankHttpUrl.getSearchServInfo(),
                query, object);
        //状态码为请求成功
        LogUtil.info("调用远程服务返回的报文："+result.getData().toString(),null, this.getClass());
        if(result.getCode() == HttpStatus.SC_OK){
            return JSON.parseObject(result.getData(), StdCcrQueryServRes.class) ;
        }else{
            LogUtil.error("远程服务/bon3/searchServInfo服务调用失败", null, this.getClass());
            return JSON.parseObject(result.getData(), StdCcrQueryServRes.class) ;
        }
        //return JSON.parseObject(result.getData(), StdCcrQueryServRes.class) ;
    }

    /**
     *DOM4J 解析xml文件
     * */
    public String dom4jXml(String url,String head){
        String result="";
        // 解析books.xml文件
        // 创建SAXReader的对象reader
        //SAXReader reader = new SAXReader();
        InputStream is=this.getClass().getResourceAsStream(url);
        try {
            // 通过reader对象的read方法加载xml文件,获取docuemnt对象。
            //Document document = reader.read(new File(url));
            Document document = new SAXReader().read(is);
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
        /*String xmlUrl=directory.getAbsolutePath()+"/src/main/resources/bankByHead.xml";
        String bankIdXml=directory.getAbsolutePath()+"/src/main/resources/bankId.xml";
        String staffIdXml=directory.getAbsolutePath()+"/src/main/resources/staffId.xml";*/
        //String path =  this.getClass().getResource("/init.properties").getPath();
 /*       String xmlUrl=this.getClass().getResource("/bankByHead.xml").getPath();
        String bankIdXml=this.getClass().getResource("/bankId.xml").getPath();
        String staffIdXml=this.getClass().getResource("/staffId.xml").getPath();*/
        String xmlUrl="/bankByHead.xml";
        String bankIdXml="/bankId.xml";
        String staffIdXml="/staffId.xml";
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
