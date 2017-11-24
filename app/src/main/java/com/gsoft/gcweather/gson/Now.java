package com.gsoft.gcweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edison on 2017/11/24 0024.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }
}
