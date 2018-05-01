package com.hing.simplelauncher.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HingTang on 5/1/18.
 */
public class Country {
    @SerializedName("name")
    private String countryName;
    @SerializedName("capital")
    private String capital;
    @SerializedName("currencies")
    private List<Currency> currencyList;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }
}
