package com.vivo.yzz.myweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.vivo.yzz.myweather.db.County;
import com.vivo.yzz.myweather.util.HttpUtil;

import org.w3c.dom.Text;

import java.io.IOException;

public class WeatherActivity extends AppCompatActivity {

    private County county;
    private String ss;
    private TextView textView;
    private static final String TAG = "WeatherActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent=getIntent();

        county = (County) intent.getSerializableExtra("county");

        textView = (TextView)findViewById(R.id.weatherText);

        ss = new String();

        String adddress="http://guolin.tech/api/weather?cityid="+county.getWeatherId()+"&key=7fe80cb0baf348198f248350abf945a3 ";

        Log.d(TAG, "onCreate: "+"   "+adddress);
        HttpUtil.sendOkHttpRequest(adddress, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                ss =response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(ss);
                    }
                });

            }
        });


    }
}
