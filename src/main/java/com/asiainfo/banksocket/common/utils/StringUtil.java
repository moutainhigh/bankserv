// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringUtil.java

package com.asiainfo.banksocket.common.utils;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StringUtil
{

	public StringUtil()
	{
	}

	public static Map<String, Object> beanToMap(Object obj){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(obj==null){
			
		}else{
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for(Field field:declaredFields){
				field.setAccessible(true);
			    try {
					resultMap.put(field.getName(), field.get(obj));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 字段转换成对象属性 例如：user_name to userName
	 * 
	 * @param field
	 * @return
	 */
	public static String tableToModelName(String tableName) {
		if (null == tableName) {
			return "";
		}
		tableName = tableName.toLowerCase();
		char[] chars = tableName.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));
					i++;
				}
			} else {
				if(i==0){
					sb.append(StringUtils.upperCase(CharUtils.toString(c)));
				}else{
					sb.append(c);
				}
				
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 字段转换成对象属性 例如：user_name to userName
	 * 
	 * @param field
	 * @return
	 */
	public static String fieldToProperty(String field) {
		if (null == field) {
			return "";
		}
		field = field.toLowerCase();
		char[] chars = field.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));
					i++;
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static boolean isEmpty(String str)
	{
		return str == null || "".equals(str)  || "null".equals(str) || "NULL".equals(str);
	}
	
	public static boolean isEmpty(Object str)
	{
		if(str instanceof JSONNull){
			return true;
		}
		return str == null || "".equals(str) || "null".equals(str);
	}

	public static String replace(String text, String repl, String with)
	{
		if (text == null || repl == null || with == null || repl.length() == 0)
			return text;
		StringBuffer buf = new StringBuffer(text.length());
		int searchFrom = 0;
		do
		{
			int foundAt = text.indexOf(repl, searchFrom);
			if (foundAt != -1)
			{
				buf.append(text.substring(searchFrom, foundAt)).append(with);
				searchFrom = foundAt + repl.length();
			} else
			{
				buf.append(text.substring(searchFrom));
				return buf.toString();
			}
		} while (true);
	}

	public static String getSubStr(String src, int length)
	{
		if (isEmpty(src))
			return null;
		byte b[] = src.getBytes();
		if (b.length > length)
		{
			byte s[] = new byte[length];
			for (int i = 0; i < length; i++)
				s[i] = b[i];

			return new String(s);
		} else
		{
			return src;
		}
	}
	
	// List<String>转用逗号分隔的字符串
	public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
	
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date strToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	     date = new Date();
	   }
	   return date;
	}
	
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date strToDate(String str, String formatStr) {
	  if(isEmpty(formatStr)){
		  formatStr ="yyyy-MM-dd HH:mm:ss";
	  }
	   SimpleDateFormat format = new SimpleDateFormat(formatStr);
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	     date = new Date();
	   }
	   return date;
	}
	
	/**  
     * 将一个 Map 对象转化为一个 JavaBean  
     * @param type 要转化的类型  
     * @param map 包含属性值的 map  
     * @return 转化出来的 JavaBean 对象  
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InstantiationException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */    
    @SuppressWarnings("rawtypes")
    public static Object convertMap(Class type, JSONObject map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
      
    	BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象
    
        // 给 JavaBean 对象的属性赋值    
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {    
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            Class clazz = descriptor.getPropertyType();
            System.out.println(propertyName+"=="+clazz.getName());
            if (map.containsKey(propertyName)) { 
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。    
                Object value = map.get(propertyName);
                if(value==null){
                	//如果是时间类型则为当前时间
                	if(clazz.equals(Date.class)){
                		value = new Date();
                	}
                }
                if(clazz.equals(String.class)){
                	//value+="";  20180321 
                }
                descriptor.getWriteMethod().invoke(obj, value);    
            }else{
            	//如果是时间类型则为当前时间
            	if(clazz.equals(Date.class)){
            		Object value = new Date();
            		 descriptor.getWriteMethod().invoke(obj, value); 
            	}
            }
        }    
        return obj;    
    }  
}
