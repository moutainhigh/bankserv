package com.asiainfo.banksocket.common;

//服务对象
public class SvcObjectStruct {
    public String objType;//对象类型 1-帐户标识 2-用户标识 3-用户号码 4-客户标识 5-销售品实例
    public String objValue;//对象值 如果是用户号码且用户号码属性为固话、宽带时，此值要求带区号，含0
    public String objAttr; //用户号码属性 0-固话 1-小灵通 2-移动 3-宽带 4-智能公话
                            //5-互联星空 6-天翼高清 99-未知   (可为空)
    public String dataArea;//数据范围 1-按帐户范围 2-按用户范围 3-按客户范围 4-按销售品范围 (可为空)

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjValue() {
        return objValue;
    }

    public void setObjValue(String objValue) {
        this.objValue = objValue;
    }

    public String getObjAttr() {
        return objAttr;
    }

    public void setObjAttr(String objAttr) {
        this.objAttr = objAttr;
    }

    public String getDataArea() {
        return dataArea;
    }

    public void setDataArea(String dataArea) {
        this.dataArea = dataArea;
    }
}
