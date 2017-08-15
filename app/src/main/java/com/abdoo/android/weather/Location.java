package com.abdoo.android.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class Location implements Serializable {
    String name;
    String temp;
    String tempMin;
    String tempMax;
    String weatherMain;
    String weatherDesc;
    String humidity;
    String country;
    String cloudiness;
    String pressure;
    String wind;
    String icoUrl;
    String day;

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {

        return day;
    }

    public String getIcoUrl() {
        return icoUrl;
    }

    String[][] forecast;

    public String[][] getForecast() {
        return forecast;
    }

    public String getWind() {
        return wind+" m/s";
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getWeatherDesc() {

        return weatherDesc;
    }

    public String getTempMax() {
        return tempMax;
    }

    public String getHumidity() {
        return humidity+" %";
    }

    public String getCountry() {
        return country;
    }

    public String getCloudiness() {
        return cloudiness+" %";
    }

    public String getPressure() {
        return pressure+" hpa";
    }

    public String getName() {
        if(name!=null)
            return name;
        return null;
    }

    public String getTemp() {
        if(temp!=null)
            return temp+"°";
        return null;
    }

    public String getWeatherMain() {
        if(weatherMain!=null)
            return weatherMain;
        return null;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setIcoUrl(String icoId) {
        this.icoUrl = icoId;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setCloudiness(String cloudiness) {
        this.cloudiness = cloudiness;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    private void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public static String gettingRAWJson(String... params){
        // Handling with HTTP - Getting the JSON
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Location(String city){
        String urlWeather = "http://api.openweathermap.org/data/2.5/weather?appid=72a308d2afb23e5ae5f69ad98a9c7f4b&units=metric&q=";
        String urlForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?appid=72a308d2afb23e5ae5f69ad98a9c7f4b&units=metric&q=";
        forecast = new String[5][5];
        String rawJson = gettingRAWJson(urlWeather+city);

        if(rawJson!=null){
            try {

                JSONObject jObject = new JSONObject(rawJson);
                JSONArray jArr = jObject.getJSONArray("weather");
                JSONObject mainObj = jObject.getJSONObject("main");
                JSONObject windObj = jObject.getJSONObject("wind");
                JSONObject cloudsObj = jObject.getJSONObject("clouds");
                JSONObject weatherObj = jArr.getJSONObject(0);
                JSONObject sysObj = jObject.getJSONObject("sys");

                String weatherMain = weatherObj.getString("main");
                String country = sysObj.getString("country");
                int tmp = Float.valueOf(mainObj.getString("temp")).intValue();
                int press = Float.valueOf(mainObj.getString("pressure")).intValue();
                int tmp_min = Float.valueOf(mainObj.getString("temp_min")).intValue();
                int tmp_max = Float.valueOf(mainObj.getString("temp_max")).intValue();
                int hum = Float.valueOf(mainObj.getString("humidity")).intValue();
                int wind = Float.valueOf(windObj.getString("speed")).intValue();
                int clouds = Float.valueOf(cloudsObj.getString("all")).intValue();


                setName(jObject.getString("name"));
                setIcoUrl(getIcoUrl(weatherObj.getString("icon")));
                setWeatherMain(weatherMain);
                setTemp(tmp+"");
                setTempMin(tmp_min+"");
                setTempMax(tmp_max+"");
                setPressure(press+"");
                setHumidity(hum+"");
                setWind(wind+"");
                setCloudiness(clouds+"");
                setCountry(country);

                // forecast

                rawJson = gettingRAWJson(urlForecast+city);

                jObject = new JSONObject(rawJson);
                JSONArray listArr = jObject.getJSONArray("list");
                for(int i=0; i<5; i++){
                    weatherObj = listArr.getJSONObject(i);
                    mainObj =  weatherObj.getJSONObject("temp");
                    Log.d("ffff", mainObj.toString());


                    forecast[i][0] = mainObj.getString("day");
                    forecast[i][1] = mainObj.getString("min");
                    forecast[i][2] = mainObj.getString("max");

                    JSONArray arr =  weatherObj.getJSONArray("weather");
                    JSONObject obj =  arr.getJSONObject(0);

                    forecast[i][3] = getIcoUrl(obj.getString("icon"));

                    forecast[i][0] = Double.valueOf(forecast[i][0]).intValue()+" °";
                    forecast[i][1] = Double.valueOf(forecast[i][1]).intValue()+" °";
                    forecast[i][2] = Double.valueOf(forecast[i][2]).intValue()+" °";

                }

            } catch (final JSONException e) {
                Log.e("ffff", "Json parsing error: " + e.getMessage());
            }
        }

    }

    private String  getIcoUrl(String icoId){
        String baseUrl = "http://openweathermap.org/img/w/";
        return baseUrl+icoId+".png";
    }
}
