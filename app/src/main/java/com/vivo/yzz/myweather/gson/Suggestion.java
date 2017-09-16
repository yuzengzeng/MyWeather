package com.vivo.yzz.myweather.gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/16.
 */

public class Suggestion implements Serializable{

    public Comfort comf;

    public CarWash cw;

    public Sport sport;

    public class Comfort implements Serializable{
        public String txt;
    }

    public class CarWash implements  Serializable{

        public String txt;

    }

    public class Sport implements  Serializable{
        public String txt;
    }
}
