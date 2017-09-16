package com.vivo.yzz.myweather.gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/16.
 */

public class Basic implements Serializable{


    public String city;

    public String id;

    public Update update;

    public class Update implements Serializable{
        public String updateTime;
    }
}
