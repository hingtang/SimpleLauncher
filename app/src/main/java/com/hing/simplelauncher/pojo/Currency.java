package com.hing.simplelauncher.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HingTang on 5/1/18.
 */
public class Currency {
    @SerializedName("code")
    private String currencyCode;
    @SerializedName("name")
    private String currencyName;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
