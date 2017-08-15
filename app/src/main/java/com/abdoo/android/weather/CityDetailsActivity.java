package com.abdoo.android.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CityDetailsActivity extends AppCompatActivity {
    TextView humidityTV, windTV, cloudsTV, PresTV, cityName, temp, weatherDescTV,
            maxTempTV1, maxTempTV2, maxTempTV3, maxTempTV4, maxTempTV5,
            minTempTV1, minTempTV2, minTempTV3, minTempTV4, minTempTV5,
            day1, day2, day3, day4, day5;
    LinearLayout infoLayout, tempLayout;
    ImageView mainIcoIV ,icoIV1, icoIV2,icoIV3, icoIV4, icoIV5;
    ArrayList<Location> locationsList;
    String[][] forecast;
    String ico_url[];
    int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citydetailslayout);

        Bundle bundleObj = getIntent().getExtras();

        locationsList = (ArrayList<Location>) bundleObj.getSerializable("locationsList");
        index = getIntent().getIntExtra("index", 0);

        humidityTV = (TextView) findViewById(R.id.humidity);
        windTV = (TextView) findViewById(R.id.wind);
        cloudsTV = (TextView) findViewById(R.id.clouds);
        PresTV = (TextView) findViewById(R.id.pressure);
        cityName = (TextView) findViewById(R.id.city_name);
        tempLayout = (LinearLayout) findViewById(R.id.temp_layout);
        infoLayout = (LinearLayout) findViewById(R.id.info_layout);

        day1 = (TextView) findViewById(R.id.day1);
        day2 = (TextView) findViewById(R.id.day2);
        day3 = (TextView) findViewById(R.id.day3);
        day4 = (TextView) findViewById(R.id.day4);
        day5 = (TextView) findViewById(R.id.day5);


        temp = (TextView) findViewById(R.id.temp);
        weatherDescTV = (TextView) findViewById(R.id.weather_desc);

        maxTempTV1 = (TextView) findViewById(R.id.max_temp1);
        maxTempTV2 = (TextView) findViewById(R.id.max_temp2);
        maxTempTV3 = (TextView) findViewById(R.id.max_temp3);
        maxTempTV4 = (TextView) findViewById(R.id.max_temp4);
        maxTempTV5 = (TextView) findViewById(R.id.max_temp5);

        minTempTV1 = (TextView) findViewById(R.id.min_temp1);
        minTempTV2 = (TextView) findViewById(R.id.min_temp2);
        minTempTV3 = (TextView) findViewById(R.id.min_temp3);
        minTempTV4 = (TextView) findViewById(R.id.min_temp4);
        minTempTV5 = (TextView) findViewById(R.id.min_temp5);

        mainIcoIV = (ImageView) findViewById(R.id.ico_main);
        icoIV1 = (ImageView) findViewById(R.id.image1);
        icoIV2 = (ImageView) findViewById(R.id.image2);
        icoIV3 = (ImageView) findViewById(R.id.image3);
        icoIV4 = (ImageView) findViewById(R.id.image4);
        icoIV5 = (ImageView) findViewById(R.id.image5);

        tempLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                infoLayout.setVisibility(View.VISIBLE);
            }
        });

        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                tempLayout.setVisibility(View.VISIBLE);
            }
        });


        Location loc = locationsList.get(index);

        cityName.setText(loc.getName());
        humidityTV.setText(loc.getHumidity());
        windTV.setText(loc.getWind());
        cloudsTV.setText(loc.getCloudiness());
        PresTV.setText(loc.getPressure());

        forecast = loc.getForecast();

        temp.setText(loc.getTemp());
        weatherDescTV.setText(loc.getWeatherMain());
        minTempTV1.setText(forecast[0][1]);
        minTempTV2.setText(forecast[1][1]);
        minTempTV3.setText(forecast[2][1]);
        minTempTV4.setText(forecast[3][1]);
        minTempTV5.setText(forecast[4][1]);

        maxTempTV1.setText(forecast[0][2]);
        maxTempTV2.setText(forecast[1][2]);
        maxTempTV3.setText(forecast[2][2]);
        maxTempTV4.setText(forecast[3][2]);
        maxTempTV5.setText(forecast[4][2]);

        Picasso.with(this).load(loc.getIcoUrl()).resize(110,110).into(mainIcoIV);
        Picasso.with(this).load(forecast[0][3]).resize(80, 80).into(icoIV1);
        Picasso.with(this).load(forecast[1][3]).resize(80, 80).into(icoIV2);
        Picasso.with(this).load(forecast[2][3]).resize(80, 80).into(icoIV3);
        Picasso.with(this).load(forecast[3][3]).resize(80, 80).into(icoIV4);
        Picasso.with(this).load(forecast[4][3]).resize(80, 80).into(icoIV5);


        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        // full name form of the day
        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());




    }
}
