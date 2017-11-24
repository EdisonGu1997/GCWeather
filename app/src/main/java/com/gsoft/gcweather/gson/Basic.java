package com.gsoft.gcweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edison on 2017/11/24 0024.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;


    public class Update{

        @SerializedName("loc")
        public String updateTime;
    }
}
