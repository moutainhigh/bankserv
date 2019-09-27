package com.asiainfo.banksocket.common;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorCode {
    private static String USER_INFO_NO_DATA="10001";//用户信息不存在
    private static String BANKID_STAFFID_BANKHEAD_NO_DATA="10002";//bankId或staffId或bankHead值为空
    private static String CONNECT_ERROR="10003";//连接错误
    private static String IO_CONNECT_ERROR="10004";//IO流错误
    private static String QUERYBALANCE_REMOTE_SERVICE_ERROR="10005";//查询余额远程服务调用失败
    private static String SERVICE_ERROR="10006";//服务调用失败
    private static String RECHARGE_BALANCE_REMOTE_SERVICE_ERROR="10007";//余额缴费远程服务调用失败
    private static String RECHARGE_BALANCE_FLOWID_SECOND="10008";//余额缴费流水号重复失败
    private static String RECHARGE_BALANCE_FLOWID_ERROR="10009";//判断余额缴费流水号重复失败
    private static String ROLL_RECHARGE_BALANCE_REMOTE_SERVICE_ERROR="10010";//余额缴费远程服务调用失败

    public static String getUserInfoNoData() {
        return USER_INFO_NO_DATA;
    }

    public static void setUserInfoNoData(String userInfoNoData) {
        USER_INFO_NO_DATA = userInfoNoData;
    }

    public static String getBankidStaffidBankheadNoData() {
        return BANKID_STAFFID_BANKHEAD_NO_DATA;
    }

    public static void setBankidStaffidBankheadNoData(String bankidStaffidBankheadNoData) {
        BANKID_STAFFID_BANKHEAD_NO_DATA = bankidStaffidBankheadNoData;
    }

    public static String getConnectError() {
        return CONNECT_ERROR;
    }

    public static void setConnectError(String connectError) {
        CONNECT_ERROR = connectError;
    }

    public static String getIoConnectError() {
        return IO_CONNECT_ERROR;
    }

    public static void setIoConnectError(String ioConnectError) {
        IO_CONNECT_ERROR = ioConnectError;
    }

    public static String getQuerybalanceRemoteServiceError() {
        return QUERYBALANCE_REMOTE_SERVICE_ERROR;
    }

    public static void setQuerybalanceRemoteServiceError(String querybalanceRemoteServiceError) {
        QUERYBALANCE_REMOTE_SERVICE_ERROR = querybalanceRemoteServiceError;
    }

    public static String getServiceError() {
        return SERVICE_ERROR;
    }

    public static void setServiceError(String serviceError) {
        SERVICE_ERROR = serviceError;
    }

    public static String getRechargeBalanceRemoteServiceError() {
        return RECHARGE_BALANCE_REMOTE_SERVICE_ERROR;
    }

    public static void setRechargeBalanceRemoteServiceError(String rechargeBalanceRemoteServiceError) {
        RECHARGE_BALANCE_REMOTE_SERVICE_ERROR = rechargeBalanceRemoteServiceError;
    }

    public static String getRechargeBalanceFlowidSecond() {
        return RECHARGE_BALANCE_FLOWID_SECOND;
    }

    public static void setRechargeBalanceFlowidSecond(String rechargeBalanceFlowidSecond) {
        RECHARGE_BALANCE_FLOWID_SECOND = rechargeBalanceFlowidSecond;
    }

    public static String getRechargeBalanceFlowidError() {
        return RECHARGE_BALANCE_FLOWID_ERROR;
    }

    public static void setRechargeBalanceFlowidError(String rechargeBalanceFlowidError) {
        RECHARGE_BALANCE_FLOWID_ERROR = rechargeBalanceFlowidError;
    }

    public static String getRollRechargeBalanceRemoteServiceError() {
        return ROLL_RECHARGE_BALANCE_REMOTE_SERVICE_ERROR;
    }

    public static void setRollRechargeBalanceRemoteServiceError(String rollRechargeBalanceRemoteServiceError) {
        ROLL_RECHARGE_BALANCE_REMOTE_SERVICE_ERROR = rollRechargeBalanceRemoteServiceError;
    }
}
