package com.domain;

import java.io.Serializable;

public class Info implements Serializable {
    private boolean flag;
    private String errorMsg;

    public boolean getFlag() {
        return flag;
    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "Info{" +
                "flag=" + flag +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
