package com.revolve44.firebird_test025;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String CITY = "Moscow";

    public static String AppId = "4e78fa71de97f97aa376e42f2f1c99cf";
    public static String MC = "&units=metric&appid=";



    public static String lat = "53.3";
    public static String lon = "43.2";
    public static String metric = "metric";


    //Variables
    public float NominalPower;//????????????????????????????????
    public float CurrentPower;
    public int CurrentPowerInt;
    public float cloud;
    public float windF;
    public int windI;
    public float temp;

    public String desription;
    public float pressure;
    public float humidity;
    public boolean tempScale;

    public long unixSunrise;
    public long unixSunset;

    public String city;
    public String country;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startcall(){
        getCurrentData();
    }

    public float getTemp() {
        return temp;
    }

    public float getWindF() {
        return windF;
    }

    public int getWindI() {
        return windI;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void getCurrentData() {
        Log.d("Lifecycle -> method ", " getCurrentdata ");

        Log.d("Lifecycle -> method ", " latitude " + lat + lon);
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();//for create a LOGs
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); //for create a LOGs
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); //for create a LOGs
        okhttpClientBuilder.addInterceptor(logging); //for create a LOGs

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build()) //for create a LOGs
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        //Call<WeatherResponse> call = service.getCurrentWeatherData(CITY, metric, AppId);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, metric, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    //main variables
                    cloud = weatherResponse.clouds.all;
                    temp = weatherResponse.main.temp;
                    windF = weatherResponse.wind.speed;
                    country = weatherResponse.sys.country;


                    Log.d("From retrofit         ", "Temp and press " + temp + " " + pressure);

                    if (cloud > -1) {
                        CurrentPower = NominalPower - NominalPower * (cloud / 100) * 0.8f;
                    } else {
                        CurrentPower = 404;
                    }
                }
            }


            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.e("ERROR", " On Retrofit");
//                Context context = getApplicationContext();
//                CharSequence text = "Check Internet connection. Fail in Response" + t.getMessage();
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
            }
        });


    }

}
