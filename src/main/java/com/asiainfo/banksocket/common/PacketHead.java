package com.asiainfo.banksocket.common;

/**
 * 数据包包头实体
 */
public class PacketHead {
    private String packetNo;		//包序号，每个交易从001开始
    private String type;			//包类型，为0时表示该交易包结束，无后续包为1表示还有后续包
    private String size;			//包长度，只包括包内容的长度，不包括包头的长度
    private String rateCode;		//费率区，是指银行所在地的费率区，见：归属费率区
    private String bankId;		//银行代码，见：银行代码
    private String bankSerial;	//银行流水号
    private String busiCode;		//交易码
    private String resultCode;	//返回码(交易结果代码)

    public String getPacketNo() {
        return packetNo;
    }
    public void setPacketNo(String packetNo) {
        this.packetNo = packetNo;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getRateCode() {
        return rateCode;
    }
    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public String getBankSerial() {
        return bankSerial;
    }
    public void setBankSerial(String bankSerial) {
        this.bankSerial = bankSerial;
    }
    public String getBusiCode() {
        return busiCode;
    }
    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getResultCode() {
        return resultCode;
    }
    /**
     *
     * @param resultCode
     * 0000:交易成功；
     * 0010:无用户资料；
     * 0020:交易错误,不允许跨费率区交易；
     * 0030:反销帐错误,无此工单,或该工单已反销；
     * 0031:反销帐错误,缴费号码和工单号不符；
     * 0033:反销帐错误,不允许隔日撤单；
     * 0040:电信后台服务异常；
     * 0060:对帐文件不存在,或文件格式错误；
     * 0061:正在对帐；
     * 0062:文件中的参数和请求包中参数错误；
     * 0063:不平帐总金额差额超过系统设定上限,转手工处理；
     * 0070:发票已打印；
     * 0071:无发票记录或者无收据信；
     * 0072:此流水没有找到对应的充值记录；
     * 0073:此流水已经充值；
     * 0011:预付费用户,请到营业厅缴费；
     * 0099:其他错误
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public PacketHead() {
        super();
    }

    public PacketHead(String packetNo, String type, String size, String rateCode, String bankId, String bankSerial,
                      String busiCode, String resultCode) {
        super();
        this.packetNo = packetNo;
        this.type = type;
        this.size = size;
        this.rateCode = rateCode;
        this.bankId = bankId;
        this.bankSerial = bankSerial;
        this.busiCode = busiCode;
        this.resultCode = resultCode;
    }

    //处理客户端请求包头,构造包头对象
    public PacketHead(String request) {
        super();
        this.packetNo = request.substring(0,3);
        this.type = request.substring(3,4);
        this.size = request.substring(4,10);
        this.rateCode = request.substring(10,13);
        this.bankId = request.substring(13,15);
        this.bankSerial = request.substring(15,25);
        this.busiCode = request.substring(25,28);
        this.resultCode = request.substring(28,32);
    }

    @Override
    public String toString() {
        return "PacketHead [packetNo=" + packetNo + ", type=" + type + ", size=" + size + ", rateCode=" + rateCode
                + ", bankId=" + bankId + ", bankSerial=" + bankSerial + ", busiCode=" + busiCode + ", resultCode="
                + resultCode + "]";
    }

    /**
     * 包头实体转字符串
     * @return
     */
    public String getPacketHeadStr() {
        return packetNo + type + size + rateCode + bankId
                + bankSerial + busiCode + resultCode;
    }
}
