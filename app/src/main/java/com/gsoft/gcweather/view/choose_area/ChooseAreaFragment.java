package com.gsoft.gcweather.view.choose_area;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gsoft.gcweather.MainActivity;
import com.gsoft.gcweather.R;
import com.gsoft.gcweather.util.LogUtil;
import com.gsoft.gcweather.view.weather.WeatherActivity;
import com.gsoft.gcweather.db.City;
import com.gsoft.gcweather.db.County;
import com.gsoft.gcweather.db.Province;
import com.gsoft.gcweather.util.HttpUtil;
import com.gsoft.gcweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by edison on 2017/11/23 0023.
 */

public class ChooseAreaFragment extends Fragment {

    private static final String TAG = new ChooseAreaFragment().getClass().getName();

    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;

    private Button backButton;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);

        backButton = (Button) view.findViewById(R.id.back_button);
        titleText = (TextView) view.findViewById(R.id.title_text);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.area_item, dataList){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                ViewHolder viewHolder = new ViewHolder();
                if (convertView == null){
                    view = LayoutInflater.from(getContext())
                            .inflate(R.layout.area_item, parent, false);
                    viewHolder.areaNameText = (TextView) view.findViewById(R.id.area_name_text);
                    view.setTag(viewHolder);
                }else {
                    view = convertView;
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.areaNameText.setText(dataList.get(position));
                return view;
            }

            class ViewHolder{
                TextView areaNameText;
            }
        };

        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (LEVEL_PROVINCE == currentLevel){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (LEVEL_CITY == currentLevel){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if (LEVEL_COUNTY == currentLevel){
//                    County county = countyList.get(position);
//                    Snackbar.make(listView
//                            , county.getCountyName()+" 的气象编码是："+county.getWeatherId()
//                            ,Snackbar.LENGTH_SHORT).show();
                    LogUtil.d(TAG, getActivity() != null ? getActivity().getClass().getName() : "null");
                    if (getActivity() instanceof MainActivity) {
                        String weatherId = countyList.get(position).getWeatherId();
                        Intent intent = new Intent(getContext(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity weatherA = (WeatherActivity) getActivity();
                        weatherA.drawerLayout.closeDrawers();
                        weatherA.requestWeather(countyList.get(position).getWeatherId());

                    }

                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LEVEL_COUNTY == currentLevel){
                    queryCities();
                }else if (LEVEL_CITY == currentLevel){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * query all the provinces of our china, query from local database first,
     * if no data, then query from service ;
     */
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://www.guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * query all cities from database first, if there is not data, then query from server
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        if (backButton.getVisibility() == View.GONE){
            backButton.setVisibility(View.VISIBLE);
        }
        cityList = DataSupport
                .where("provinceid = ?", String.valueOf(selectedProvince.getId()))
                .find(City.class);
        if (cityList.size() > 0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://www.guolin.tech/api/china/"+provinceCode;
            queryFromServer(address, "city");
        }
    }
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        if (backButton.getVisibility() == View.GONE){
            backButton.setVisibility(View.VISIBLE);
        }
        countyList = DataSupport
                .where("cityid = ?", String.valueOf(selectedCity.getId()))
                .find(County.class);
        if (countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://www.guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address, "county");
        }

    }

    private void queryFromServer(final String address, final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Snackbar.make(backButton, "fail to load", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountiesResponse(responseText, selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
