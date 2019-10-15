package com.asiainfo.banksocket.common;

import com.asiainfo.banksocket.common.utils.BaseApiResDomain;
import com.asiainfo.banksocket.common.utils.BaseDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * QryBalanceRecordDetail 响应体
 * ClassName: QryBalanceRecordDetailRes <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: 2019年4月29日 下午1:52:13 <br/>
 * @author yinyanzhen
 */
public class GetUnitedBalanceRes extends BaseApiResDomain{
	private static final long serialVersionUID = -3629374448104503827L;
	private String errorCode;
	private String errorMsg;
	private String businessCode;
	private StdCcaQueryBalanceBalance stdCcaQueryBalanceBalance;
	public static class StdCcaQueryBalanceBalance{
		private List<BalanceInformation> balanceInformation = new ArrayList<>();
		public static class BalanceInformation{
			private String balanceAvailable;
			private String balanceTypeFlag;

			public String getBalanceAvailable() {
				return balanceAvailable;
			}

			public void setBalanceAvailable(String balanceAvailable) {
				this.balanceAvailable = balanceAvailable;
			}

			public String getBalanceTypeFlag() {
				return balanceTypeFlag;
			}

			public void setBalanceTypeFlag(String balanceTypeFlag) {
				this.balanceTypeFlag = balanceTypeFlag;
			}
		}
		private String paymentFlag;
		private String totalBalanceAvailable;

		public List<BalanceInformation> getBalanceInformation() {
			return balanceInformation;
		}

		public void setBalanceInformation(List<BalanceInformation> balanceInformation) {
			this.balanceInformation = balanceInformation;
		}

		public String getPaymentFlag() {
			return paymentFlag;
		}

		public void setPaymentFlag(String paymentFlag) {
			this.paymentFlag = paymentFlag;
		}

		public String getTotalBalanceAvailable() {
			return totalBalanceAvailable;
		}

		public void setTotalBalanceAvailable(String totalBalanceAvailable) {
			this.totalBalanceAvailable = totalBalanceAvailable;
		}
	}

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

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public StdCcaQueryBalanceBalance getStdCcaQueryBalanceBalance() {
		return stdCcaQueryBalanceBalance;
	}

	public void setStdCcaQueryBalanceBalance(StdCcaQueryBalanceBalance stdCcaQueryBalanceBalance) {
		this.stdCcaQueryBalanceBalance = stdCcaQueryBalanceBalance;
	}
}
