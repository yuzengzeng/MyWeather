package com.vivo.yzz.myweather.gson;

/**
 * Created by Administrator on 2017/9/16.
 */

public class Forecast {

    public String date;

    public Temperature tmp;

    public More cond;

    public class Temperature{
        public String max;

        public String min;
    }

    public class More{
        public String txt_d;
    }
}
