package com.asiainfo.banksocket.mapper;

import com.asiainfo.banksocket.common.BankChargeRecord;
import com.asiainfo.banksocket.common.PacketHead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface BankMapper {

    String queryProdInstId(@Param("obj") String obj);


    HashMap<String,Object> queryProdInstInfo(@Param("prodInstId")String prodInstId, @Param("objValue")String objValue);

    String checkRowNum(@Param("flowId") String flowId,
                       @Param("areaId") String areaId,
                       @Param("bankId") String bankId);

    void insertBankChargeRecord(@Param("bankChargeRecord") BankChargeRecord bankChargeRecord);

    void updateBankChargeRecord(@Param("bankChargeRecord") BankChargeRecord bankChargeRecord);
}
