package com.asiainfo.banksocket.common;


import com.asiainfo.banksocket.common.utils.BaseBonResDomain;

import java.util.List;

/**
 * @author WangBaoQiang
 * @ClassName: StdCcrQueryServRes
 * @description: TODO
 * @date 2019/9/2315:21
 * @Version 1.0
 */
public class StdCcrQueryServRes extends BaseBonResDomain {

    /**
     * stdCcaQueryServRes : {"stdCcaQueryServList":[{"custName":"王宝强","destinationAttr":"2","groupUser":"1","homeAreaCode":"0431","paymentFlag":"1","productId":"379","productName":"CDMA","servId":"313040326592","servState":"0","stateDate":"20190328082627"}]}
     */

    private StdCcaQueryServResBean stdCcaQueryServRes;

    public StdCcaQueryServResBean getStdCcaQueryServRes() {
        return stdCcaQueryServRes;
    }

    public void setStdCcaQueryServRes(StdCcaQueryServResBean stdCcaQueryServRes) {
        this.stdCcaQueryServRes = stdCcaQueryServRes;
    }

    public static class StdCcaQueryServResBean {
        private List<StdCcaQueryServListBean> stdCcaQueryServList;

        public List<StdCcaQueryServListBean> getStdCcaQueryServList() {
            return stdCcaQueryServList;
        }

        public void setStdCcaQueryServList(List<StdCcaQueryServListBean> stdCcaQueryServList) {
            this.stdCcaQueryServList = stdCcaQueryServList;
        }

        public static class StdCcaQueryServListBean {
            /**
             * custName : 王宝强
             * destinationAttr : 2
             * groupUser : 1
             * homeAreaCode : 0431
             * paymentFlag : 1
             * productId : 379
             * productName : CDMA
             * servId : 313040326592
             * servState : 0
             * stateDate : 20190328082627
             */

            private String custName;
            private String destinationAttr;
            private String groupUser;
            private String homeAreaCode;
            private String paymentFlag;
            private String productId;
            private String productName;
            private String servId;
            private String servState;
            private String stateDate;

            public String getCustName() {
                return custName;
            }

            public void setCustName(String custName) {
                this.custName = custName;
            }

            public String getDestinationAttr() {
                return destinationAttr;
            }

            public void setDestinationAttr(String destinationAttr) {
                this.destinationAttr = destinationAttr;
            }

            public String getGroupUser() {
                return groupUser;
            }

            public void setGroupUser(String groupUser) {
                this.groupUser = groupUser;
            }

            public String getHomeAreaCode() {
                return homeAreaCode;
            }

            public void setHomeAreaCode(String homeAreaCode) {
                this.homeAreaCode = homeAreaCode;
            }

            public String getPaymentFlag() {
                return paymentFlag;
            }

            public void setPaymentFlag(String paymentFlag) {
                this.paymentFlag = paymentFlag;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getServId() {
                return servId;
            }

            public void setServId(String servId) {
                this.servId = servId;
            }

            public String getServState() {
                return servState;
            }

            public void setServState(String servState) {
                this.servState = servState;
            }

            public String getStateDate() {
                return stateDate;
            }

            public void setStateDate(String stateDate) {
                this.stateDate = stateDate;
            }
        }
    }
}
