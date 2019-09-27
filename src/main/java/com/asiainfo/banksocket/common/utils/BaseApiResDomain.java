package com.asiainfo.banksocket.common.utils;

/**
 * ClassName: BaseApiResDomain <br/>
 * date: 2019年4月26日 下午3:10:57 <br/>
 * @author yinyanzhen
 */
public class BaseApiResDomain extends BaseDomain{
	private static final long serialVersionUID = -2908540914482556437L;
	//返回码
	private String resultCode;
	//返回信息
	private String resultMsg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
}