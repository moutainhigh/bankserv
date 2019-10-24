package com.asiainfo.banksocket.dao;


import com.asiainfo.banksocket.common.BankChargeRecord;
import com.asiainfo.banksocket.mapper.BankMapper;
import com.asiainfo.banksocket.mapper.mysqlmapper.BankMysqlMapper;
import com.asiainfo.banksocket.mapper.orclmapper.BankOrclMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


@Repository
public class BankDao {

    @Autowired
    BankMapper bankMapper;

    @Autowired
    BankMysqlMapper bankMysqlMapper;

    @Autowired
    BankOrclMapper bankOrclMapper;




    public String queryProdInstId(String obj){
        return bankMysqlMapper.queryProdInstId(obj);
    }
    public HashMap<String,Object> queryProdInstInfo(String prodInstId, String objValue){
        return bankMysqlMapper.queryProdInstInfo(prodInstId,objValue);
    }

    public String checkRowNum(String flowId,String areaId,String bankId){
        return bankOrclMapper.checkRowNum(flowId,areaId,bankId);
    }

    public void insertBankChargeRecord(BankChargeRecord bankChargeRecord){
        bankOrclMapper.insertBankChargeRecord(bankChargeRecord);
    }

    public void updateBankChargeRecord(BankChargeRecord bankChargeRecord){
        bankOrclMapper.updateBankChargeRecord(bankChargeRecord);
    }
}
