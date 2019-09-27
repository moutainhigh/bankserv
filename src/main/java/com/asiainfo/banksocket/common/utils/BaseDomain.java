package com.asiainfo.banksocket.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * 调用类父级定义,实现序列化和json格式化
 * ClassName: BaseDomain <br/>
 * date: 2019年4月20日 下午2:27:26 <br/>
 * @author yinyanzhen
 */
public class BaseDomain implements Serializable{
	private static final long serialVersionUID = 6304143481733529148L;
	
	@Override
	public String toString(){
		return JSON.toJSONString(this,SerializerFeature.WriteMapNullValue);
	}
}