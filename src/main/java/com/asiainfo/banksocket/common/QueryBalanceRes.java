package com.asiainfo.banksocket.common;

import com.asiainfo.banksocket.common.utils.BaseApiResDomain;
import com.asiainfo.banksocket.common.utils.BaseDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: QueryBalanceRes <br/>
 * date: 2019年4月29日 下午3:03:57 <br/>
 * @author yinyanzhen
 */
public class QueryBalanceRes extends BaseApiResDomain{
	private static final long serialVersionUID = 1421248928444898637L;
	//实时可用总余额 
	private Integer realBalance ;
	//电话区号
	private String areaCode ;
	//查询业务类型
	private Integer queryFlag ;
	//查询余额类型
	private Integer queryItemType ;
	//付费方式
	private Integer PaymentFlag ;
	//余额类信息组 
	private List<BalanceQuery> balanceQuery = new ArrayList<>();
	
	public static class BalanceQuery extends BaseDomain {
		private static final long serialVersionUID = -6426229905909963576L;
		//帐户标识 
		private long acctId;
		//用户标识 
		private Integer prodInstId;
		//用户号码 
		private String accNbr;
		//余额帐本标识 
		private String acctBalanceId;
		//余额帐本描述 
		private String desc;
		//帐本余额 
		private String balance;
		//余额帐本当月可使用额 
		private String available;
		//限额类型 
		private String cycleType;
		//是否共享帐本 
		private String shareRuleFlag;
		//当月已消费值 
		private String used;
		//帐本使用上限 
		private String cycleUpper;
		//帐本状态 
		private String state;
		//预留余额 
		private String reserveBalance;
		//生效时间 
		private String effDate;
		//失效时间 
		private String expDate;
		//场景三余额类型标识
		private String balanceTypeFlag;
		
		
		public long getAcctId() {
			return acctId;
		}
		public void setAcctId(long acctId) {
			this.acctId = acctId;
		}
		public Integer getProdInstId() {
			return prodInstId;
		}
		public void setProdInstId(Integer prodInstId) {
			this.prodInstId = prodInstId;
		}
		public String getAccNbr() {
			return accNbr;
		}
		public void setAccNbr(String accNbr) {
			this.accNbr = accNbr;
		}
		public String getAcctBalanceId() {
			return acctBalanceId;
		}
		public void setAcctBalanceId(String acctBalanceId) {
			this.acctBalanceId = acctBalanceId;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}
		public String getAvailable() {
			return available;
		}
		public void setAvailable(String available) {
			this.available = available;
		}
		public String getCycleType() {
			return cycleType;
		}
		public void setCycleType(String cycleType) {
			this.cycleType = cycleType;
		}
		public String getShareRuleFlag() {
			return shareRuleFlag;
		}
		public void setShareRuleFlag(String shareRuleFlag) {
			this.shareRuleFlag = shareRuleFlag;
		}
		public String getUsed() {
			return used;
		}
		public void setUsed(String used) {
			this.used = used;
		}
		public String getCycleUpper() {
			return cycleUpper;
		}
		public void setCycleUpper(String cycleUpper) {
			this.cycleUpper = cycleUpper;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getReserveBalance() {
			return reserveBalance;
		}
		public void setReserveBalance(String reserveBalance) {
			this.reserveBalance = reserveBalance;
		}
		public String getEffDate() {
			return effDate;
		}
		public void setEffDate(String effDate) {
			this.effDate = effDate;
		}
		public String getExpDate() {
			return expDate;
		}
		public void setExpDate(String expDate) {
			this.expDate = expDate;
		}
		public String getBalanceTypeFlag() {
			return balanceTypeFlag;
		}
		public void setBalanceTypeFlag(String balanceTypeFlag) {
			this.balanceTypeFlag = balanceTypeFlag;
		}
		
	}

	public Integer getRealBalance() {
		return realBalance;
	}

	public void setRealBalance(Integer realBalance) {
		this.realBalance = realBalance;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(Integer queryFlag) {
		this.queryFlag = queryFlag;
	}

	public Integer getQueryItemType() {
		return queryItemType;
	}

	public void setQueryItemType(Integer queryItemType) {
		this.queryItemType = queryItemType;
	}

	public Integer getPaymentFlag() {
		return PaymentFlag;
	}

	public void setPaymentFlag(Integer paymentFlag) {
		PaymentFlag = paymentFlag;
	}

	public List<BalanceQuery> getBalanceQuery() {
		return balanceQuery;
	}

	public void setBalanceQuery(List<BalanceQuery> balanceQuery) {
		this.balanceQuery = balanceQuery;
	}
}
