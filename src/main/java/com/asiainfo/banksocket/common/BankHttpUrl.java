package com.asiainfo.banksocket.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/bankConfig.properties")
public class BankHttpUrl {
/*    public String queryBalanceUrl="http://136.160.153.42:8026/billsrv/openApi/QueryBalance";
    public String rechargeBalanceUrl="http://136.160.153.42:8026/billsrv/openApi/RechargeBalance";
    public String rollRechargeBalanceUrl="http://136.160.153.42:8026/billsrv/openApi/RollRechargeBalance";*/

    @Value("${banksocket.queryBalanceUrl}")
    public String queryBalanceUrl;
    @Value("${banksocket.rechargeBalanceUrl}")
    public String rechargeBalanceUrl;
    @Value("${banksocket.rollRechargeBalanceUrl}")
    public String rollRechargeBalanceUrl;
    @Value("${openApi.bon3.searchServInfo}")
    public String searchServInfo;

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

    public String getSearchServInfo() {
        return searchServInfo;
    }

    public void setSearchServInfo(String searchServInfo) {
        this.searchServInfo = searchServInfo;
    }
}
