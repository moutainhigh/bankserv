package com.asiainfo.banksocket.common;


import com.asiainfo.banksocket.common.utils.BaseDomain;

/**
 * 
 * ClassName: QryBalanceRecordDetailReq <br/>
 * date: 2019年4月29日 上午11:33:04 <br/>
 * @author yinyanzhen
 */
public class GetUnitedBalanceReq extends BaseDomain{
	private static final long serialVersionUID = -431721907367951291L;
	private String systemId;
	private StdCcrQueryBalanceBalance stdCcrQueryBalanceBalance;
	public static class StdCcrQueryBalanceBalance{
		private BalanceQueryInformation balanceQueryInformation;
		public static class BalanceQueryInformation{
			private String queryFlag;
			private String queryItemType;
			private String areaCode;
			private String destinationIdType;
			private String destinationAttr;
			private String destinationId;

			public String getQueryFlag() {
				return queryFlag;
			}

			public void setQueryFlag(String queryFlag) {
				this.queryFlag = queryFlag;
			}

			public String getQueryItemType() {
				return queryItemType;
			}

			public void setQueryItemType(String queryItemType) {
				this.queryItemType = queryItemType;
			}

			public String getAreaCode() {
				return areaCode;
			}

			public void setAreaCode(String areaCode) {
				this.areaCode = areaCode;
			}

			public String getDestinationIdType() {
				return destinationIdType;
			}

			public void setDestinationIdType(String destinationIdType) {
				this.destinationIdType = destinationIdType;
			}

			public String getDestinationAttr() {
				return destinationAttr;
			}

			public void setDestinationAttr(String destinationAttr) {
				this.destinationAttr = destinationAttr;
			}

			public String getDestinationId() {
				return destinationId;
			}

			public void setDestinationId(String destinationId) {
				this.destinationId = destinationId;
			}
		}

		public BalanceQueryInformation getBalanceQueryInformation() {
			return balanceQueryInformation;
		}

		public void setBalanceQueryInformation(BalanceQueryInformation balanceQueryInformation) {
			this.balanceQueryInformation = balanceQueryInformation;
		}
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public StdCcrQueryBalanceBalance getStdCcrQueryBalanceBalance() {
		return stdCcrQueryBalanceBalance;
	}

	public void setStdCcrQueryBalanceBalance(StdCcrQueryBalanceBalance stdCcrQueryBalanceBalance) {
		this.stdCcrQueryBalanceBalance = stdCcrQueryBalanceBalance;
	}
}
