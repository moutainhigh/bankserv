package com.asiainfo.banksocket.common.utils;

/**
 * ClassName: BaseBonResDomain <br/>
 * date: 2019年4月20日 下午3:50:01 <br/>
 * @author yinyanzhen
 */
public class BaseBonResDomain extends BaseDomain{
	private static final long serialVersionUID = -5491767935867736666L;
	
	private String errorCode;
	
	private String errorMsg;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}