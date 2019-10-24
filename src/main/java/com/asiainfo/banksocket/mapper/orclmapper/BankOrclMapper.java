package com.asiainfo.banksocket.mapper.orclmapper;

import com.asiainfo.banksocket.common.BankChargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
@Repository
public interface BankOrclMapper {

    String checkRowNum(@Param("flowId") String flowId,
                       @Param("areaId") String areaId,
                       @Param("bankId") String bankId);

    void insertBankChargeRecord(@Param("bankChargeRecord") BankChargeRecord bankChargeRecord);

    void updateBankChargeRecord(@Param("bankChargeRecord") BankChargeRecord bankChargeRecord);
}
