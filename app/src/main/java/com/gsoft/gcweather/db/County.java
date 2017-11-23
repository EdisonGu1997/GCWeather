package com.gsoft.gcweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by edison on 2017/11/23 0023.
 */

public class County extends DataSupport {

    private int cityId;
    private int id;
    private String countyName;
    private String weatherId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
