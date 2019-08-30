package com.asiainfo.banksocket.common.utils;

public class UUID {
	    protected static int count = 0;   
	  
	    public static synchronized String getUUID() {   
	        count++;   
	       long time = System.currentTimeMillis();   
	 
	        String timePattern = Long.toHexString(time); // long转换为16进制的字符串 
	        int leftBit = 14 - timePattern.length();   
	        if (leftBit > 0) {   
	            timePattern = "0000000000".substring(0, leftBit) + timePattern;   
	        }   
	  
	        String uuid = timePattern   
	                + Long.toHexString(Double.doubleToLongBits(Math.random()))   
	                + Long.toHexString(Double.doubleToLongBits(Math.random()))   
	                + "000000000000000000";   
	  
	        uuid = uuid.substring(0, 32).toUpperCase();   
	  
	        return uuid;   
	    }   
	}