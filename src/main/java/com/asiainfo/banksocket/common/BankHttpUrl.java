package com.asiainfo.banksocket.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

@Repository
public class BankHttpUrl {
/*    public String queryBalanceUrl="http://136.160.153.42:8026/billsrv/openApi/QueryBalance";
    public String rechargeBalanceUrl="http://136.160.153.42:8026/billsrv/openApi/RechargeBalance";
    public String rollRechargeBalanceUrl="http://136.160.153.42:8026/billsrv/openApi/RollRechargeBalance";*/


    public String queryBalanceUrl;
    public String rechargeBalanceUrl;
    public String rollRechargeBalanceUrl;


    public String getQueryBalanceUrl() {
        return queryBalanceUrl;
    }

    public void setQueryBalanceUrl(String queryBalanceUrl) {
        this.queryBalanceUrl = queryBalanceUrl;
    }

    public String getRechargeBalanceUrl() {
        return rechargeBalanceUrl;
    }

    public void setRechargeBalanceUrl(String rechargeBalanceUrl) {
        this.rechargeBalanceUrl = rechargeBalanceUrl;
    }

    public String getRollRechargeBalanceUrl() {
        return rollRechargeBalanceUrl;
    }

    public void setRollRechargeBalanceUrl(String rollRechargeBalanceUrl) {
        this.rollRechargeBalanceUrl = rollRechargeBalanceUrl;
    }
}
