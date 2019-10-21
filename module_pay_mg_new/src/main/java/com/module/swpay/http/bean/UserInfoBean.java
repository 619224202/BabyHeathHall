package com.module.swpay.http.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoBean {
    private String result;
    private String resultDesc;
    private String userId;
    private String messageJID;
    private String messagePassword;
    private String messageAddress;
    private String token;
    private BussinessInfoBean bussinessInfoBean;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageJID() {
        return messageJID;
    }

    public void setMessageJID(String messageJID) {
        this.messageJID = messageJID;
    }

    public String getMessagePassword() {
        return messagePassword;
    }

    public void setMessagePassword(String messagePassword) {
        this.messagePassword = messagePassword;
    }

    public String getMessageAddress() {
        return messageAddress;
    }

    public void setMessageAddress(String messageAddress) {
        this.messageAddress = messageAddress;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BussinessInfoBean getBussinessInfoBean() {
        return bussinessInfoBean;
    }

    public void setBussinessInfoBean(BussinessInfoBean bussinessInfoBean) {
        this.bussinessInfoBean = bussinessInfoBean;
    }

    public BussinessInfoBean createBusinessInfoBean(){
        return new BussinessInfoBean();
    }

    public String toString(){
        JSONObject jobj = new JSONObject();
        String returnString = "";
        try {
            jobj.put("result",result);
            jobj.put("resultDesc",resultDesc);
            jobj.put("userId",userId);
            jobj.put("token",token);
            jobj.put("userLocal",bussinessInfoBean.getUserLocal());
            jobj.put("copyrighteId",bussinessInfoBean.getCopyrightId());
            jobj.put("city",bussinessInfoBean.getCity());
            jobj.put("copyrightId",bussinessInfoBean.getCopyrightId());
            jobj.put("accountIdentity",bussinessInfoBean.getAccountIdentity());
            jobj.put("cpCode",bussinessInfoBean.getCpCode());
            returnString = jobj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public class BussinessInfoBean{
        private String userLocal;
        private String city;
        private String rightChange;
        private String changeTerminalModel;
        private String copyrightId;
        private String ECCode;
        private String authCode;
        private String ECCoporationCode;
        private String cpCode;
        private String accountIdentity;
        private String userGroup;

        public String getUserLocal() {
            return userLocal;
        }

        public void setUserLocal(String userLocal) {
            this.userLocal = userLocal;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRightChange() {
            return rightChange;
        }

        public void setRightChange(String rightChange) {
            this.rightChange = rightChange;
        }

        public String getChangeTerminalModel() {
            return changeTerminalModel;
        }

        public void setChangeTerminalModel(String changeTerminalModel) {
            this.changeTerminalModel = changeTerminalModel;
        }

        public String getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(String copyrightId) {
            this.copyrightId = copyrightId;
        }

        public String getECCode() {
            return ECCode;
        }

        public void setECCode(String ECCode) {
            this.ECCode = ECCode;
        }

        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public String getECCoporationCode() {
            return ECCoporationCode;
        }

        public void setECCoporationCode(String ECCoporationCode) {
            this.ECCoporationCode = ECCoporationCode;
        }

        public String getCpCode() {
            return cpCode;
        }

        public void setCpCode(String cpCode) {
            this.cpCode = cpCode;
        }

        public String getAccountIdentity() {
            return accountIdentity;
        }

        public void setAccountIdentity(String accountIdentity) {
            this.accountIdentity = accountIdentity;
        }

        public String getUserGroup() {
            return userGroup;
        }

        public void setUserGroup(String userGroup) {
            this.userGroup = userGroup;
        }
    }
}
