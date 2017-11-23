package com.gsoft.gcweather.util;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.gsoft.gcweather.db.City;
import com.gsoft.gcweather.db.County;
import com.gsoft.gcweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by edison on 2017/11/23 0023.
 */

public class Utility {
    /**
     * get information of provinces ;
     * @param responce the JSON data about information of province returned by service ;
     * @return
     */
    public static boolean handleProvinceResponce(String responce){
        if (!TextUtils.isEmpty(responce)){
            try {
                JSONArray allProvinces = new JSONArray(responce);
                for (int i = 0; i < allProvinces.length(); i ++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * information of cities ;
     * @param responce
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponce(String responce, int provinceId){
        if (!TextUtils.isEmpty(responce)){
            try {
                JSONArray allCities = new JSONArray(responce);
                for (int i = 0; i < allCities.length(); i ++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * resolve the dates of the counties ;
     * and save the datas to database ;
     * @param responce
     * @param cityId
     * @return
     */
    public static boolean handleCountiesResponce(String responce, int cityId){

        if (!TextUtils.isEmpty(responce)){
            try {
                JSONArray allCounties = new JSONArray(responce);
                for (int i = 0; i < allCounties.length(); i ++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setCityId(cityId);
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
