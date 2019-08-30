package com.asiainfo.banksocket.dao;


import com.asiainfo.banksocket.common.BankChargeRecord;
import com.asiainfo.banksocket.mapper.BankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


@Repository
public class BankDao {

    @Autowired
    BankMapper bankMapper;


    public String queryProdInstId(String obj){
        return bankMapper.queryProdInstId(obj);
    }
    public HashMap<String,Object> queryProdInstInfo(String prodInstId, String objValue){
        return bankMapper.queryProdInstInfo(prodInstId,objValue);
    }

    public String checkRowNum(String flowId,String areaId,String bankId){
        return bankMapper.checkRowNum(flowId,areaId,bankId);
    }

    public void insertBankChargeRecord(BankChargeRecord bankChargeRecord){
        bankMapper.insertBankChargeRecord(bankChargeRecord);
    }

    public void updateBankChargeRecord(BankChargeRecord bankChargeRecord){
        bankMapper.updateBankChargeRecord(bankChargeRecord);
    }
}
