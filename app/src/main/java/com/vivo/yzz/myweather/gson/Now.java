package com.vivo.yzz.myweather.gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/16.
 */

public class Now implements Serializable{

    public String tmp;

    public More cond;

    public class More implements Serializable{
        public String txt;
    }
}
