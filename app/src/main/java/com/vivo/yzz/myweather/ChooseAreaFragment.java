package com.vivo.yzz.myweather;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.vivo.yzz.myweather.db.City;
import com.vivo.yzz.myweather.db.County;
import com.vivo.yzz.myweather.db.Province;
import com.vivo.yzz.myweather.util.HttpUtil;
import com.vivo.yzz.myweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    private TextView titleText;
    private Button buttonBack;
    private ListView listView;

    private  int cutrrentLevel;
    private List<String> dataList=new ArrayList<>();

    private Province selectedProvince;


    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;

    private ArrayAdapter adapter;

    private City selectedCity;

    private ProgressDialog progressDialog;

    private static final String TAG = "ChooseAreaFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView)view.findViewById(R.id.title_text);
        buttonBack = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);

        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (cutrrentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(i);
                    queuryCities();
                }else if(cutrrentLevel==LEVEL_CITY){
                    selectedCity = cityList.get(i);
                    queuryCounties();
                }else if(cutrrentLevel==LEVEL_COUNTY){
                   County county= countyList.get(i);

                    Intent intent=new Intent(getActivity(),WeatherActivity.class);

                    intent.putExtra("county",county);

                    getActivity().startActivity(intent);

                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cutrrentLevel==LEVEL_COUNTY){
                    queuryCities();
                }else if(cutrrentLevel==LEVEL_CITY){
                    queuryProvinces();
                }
            }
        });

        queuryProvinces();


    }


    //查询省

    private void queuryProvinces(){
        titleText.setText("中国");

        buttonBack.setVisibility(View.GONE);

        provinceList= DataSupport.findAll(Province.class);

        if (provinceList.size()>0){
            dataList.clear();

            for (Province province:provinceList) {
                dataList.add(province.getProvinceName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            cutrrentLevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";

              queryFeomServer(address,"province");

            //Log.d(TAG, "queuryProvinces: +++++++++++++++++++++++++++++++++");
        }
    }

    //查询市
    private void queuryCities(){
        titleText.setText(selectedProvince.getProvinceName());

        buttonBack.setVisibility(View.VISIBLE);

        cityList= DataSupport
                .where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);


        if (cityList.size()>0){
            dataList.clear();

            for (City city:cityList) {
                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            cutrrentLevel=LEVEL_CITY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFeomServer(address,"city");

        }
    }

    //查询县

    private void queuryCounties(){

        titleText.setText(selectedProvince.getProvinceName());

        buttonBack.setVisibility(View.VISIBLE);

       countyList = DataSupport
                .where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);


        if (countyList.size()>0){

            dataList.clear();

            for (County county:countyList) {

                dataList.add(county.getCountyName());
            }

            adapter.notifyDataSetChanged();

            listView.setSelection(0);

            cutrrentLevel=LEVEL_COUNTY;

        }else{

            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFeomServer(address,"county");

        }
    }



    //从服务器查数据
    private void queryFeomServer(final String address, final String type){
        
        showProgressDialog();
        Log.d(TAG, "queryFeomServer: +++++++++++++++++++++++++QQQQQ");
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            
            @Override
            public void onFailure(Request request, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;

                //Log.d(TAG, "onResponse: ++++++++++++++++++OK"+responseText);

                if ("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId());
                }

               // Log.d(TAG, "onResponse: ++++++++++++++++++++++++++++"+result);
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();

                            if ("province".equals(type)){
                                queuryProvinces();
                            }else if("city".equals(type)){
                                queuryCities();
                            }else if("county".equals(type)){
                                queuryCounties();
                            }
                        }
                    });
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "县解析错误", Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                        }
                    });
                }

            }
        });



    }


    //进度框

    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());

            progressDialog.setMessage("正在加载");

            progressDialog.setCanceledOnTouchOutside(false);
        }

        progressDialog.show();

    }


    //关闭进度

    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}
