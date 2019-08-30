package com.asiainfo.banksocket.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 因测试环境是日志输出为logback,生产输出为消息队列统一管理,
 * 所以本处定义日志管理类,用于统一管理日志
 * ClassName: LogUtil <br/>
 * date: 2019年4月20日 下午2:55:04 <br/>
 * @author yinyanzhen
 */
public class LogUtil{
	//私有化构造,不允许外部new
	private LogUtil(){};
	
	/**
	 * opeLog:(记录业务日志,推送查询中心). <br/>
	 * @author yinyanzhen
	 * @param opeType
	 * @param msg
	 * @param clazz
	 * @since V1.0.0
	 */
	public static void opeLog(String opeType,String msg,Class<?> clazz){
		//记录操作日志,推送查询中心
	}
	
	/**
	 * debug:(记录Debug日志). <br/>
	 * @author yinyanzhen
	 * @since V1.0.0
	 */
	public static void debug(String str,Exception e,Class<?> clazz){
		//logback进行日志输出
		Logger logger =LoggerFactory.getLogger(clazz);
		if(e==null){
			logger.debug(str);
		}else{
			logger.debug(str, e);
		}
		//消息队列进行日志输出
	}
	/**
	 * info:(记录info日志). <br/>
	 * @author yinyanzhen
	 * @since V1.0.0
	 */
	public static void info(String str,Exception e,Class<?> clazz){
		//logback进行日志输出
		Logger logger =LoggerFactory.getLogger(clazz);
		if(e==null){
			logger.info(str);
		}else{
			logger.info(str, e);
		}
		//消息队列进行日志输出
	}
	/**
	 * warn:(记录warn日志). <br/>
	 * @author yinyanzhen
	 * @since V1.0.0
	 */
	public static void warn(String str,Exception e,Class<?> clazz){
		//logback进行日志输出
		Logger logger =LoggerFactory.getLogger(clazz);
		if(e==null){
			logger.warn(str);
		}else{
			logger.warn(str, e);
		}
		//消息队列进行日志输出
	}
	/**
	 * error:(记录error日志). <br/>
	 * @author yinyanzhen
	 * @since V1.0.0
	 */
	public static void error(String str,Exception e,Class<?> clazz){
		//logback进行日志输出
		Logger logger =LoggerFactory.getLogger(clazz);
		if(e==null){
			logger.error(str);
		}else{
			logger.error(str, e);
		}
		//消息队列进行日志输出
	}
}