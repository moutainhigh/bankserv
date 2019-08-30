package com.asiainfo.banksocket.service.impl;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


public class test {
    public static void main(String[] arg){
        BankServiceImpl bankService=new BankServiceImpl();
        String request="";
        //bankService.queryBalance(null,"");
        //bankService.rechargeBalance(null,request);
        bankService.rollRechargeBalance(null,request);
    }
}
