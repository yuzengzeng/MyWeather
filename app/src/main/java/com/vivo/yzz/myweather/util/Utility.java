package com.vivo.yzz.myweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.vivo.yzz.myweather.db.City;
import com.vivo.yzz.myweather.db.County;
import com.vivo.yzz.myweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/16.
 */

public class Utility {

    //解析存储省级数据

    private static final String TAG = "Utility";

    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){

            try{
                JSONArray jsonArray=new JSONArray(response);

                Log.d(TAG, "handleProvinceResponse: "+jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject provinceObject=jsonArray.getJSONObject(i);

                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();


                }

                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }

        }

        return  false;
    }

    //解析市级数据

    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){

            try{
                JSONArray jsonArray=new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject cityObject=jsonArray.getJSONObject(i);

                    City city=new City();

                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);

                    city.save();


                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }

        }

        return  false;
    }



    public static boolean handleCountyResponse(String response,int cityId){


        if(!TextUtils.isEmpty(response)){

            try{

                Log.d(TAG, "handleCountyResponse: +"+response);

                JSONArray jsonArray=new JSONArray(response);



                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject countyObject=jsonArray.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();


                }

                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }

        }

        return  false;
    }



}
