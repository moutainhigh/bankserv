package com.asiainfo.banksocket.mapper.mysqlmapper;

import com.asiainfo.banksocket.common.BankChargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
@Repository
public interface BankMysqlMapper {

    String queryProdInstId(@Param("obj") String obj);


    HashMap<String,Object> queryProdInstInfo(@Param("prodInstId") String prodInstId, @Param("objValue") String objValue);

}
