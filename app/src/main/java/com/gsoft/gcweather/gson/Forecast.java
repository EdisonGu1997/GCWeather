package com.gsoft.gcweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edison on 2017/11/24 0024.
 */

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{

        public String max;

        public String min;
    }

    public class More{

        @SerializedName("txt_d")
        public String info;
    }
}
