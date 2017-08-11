package com.abdoo.android.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    String baseUrl, jsonString;
    EditText cityField;
    Button submitBtn;
    TextView resultView;
    ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> locationsList;
    ListView locationsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseUrl = "http://api.openweathermap.org/data/2.5/weather?appid=72a308d2afb23e5ae5f69ad98a9c7f4b&units=metric&q=";
        cityField = (EditText) findViewById(R.id.cityField);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        locationsLV = (ListView) findViewById(R.id.locationsLV);


        locationsList = new ArrayList<>();



    }

    public void submitCity(View view) {
        String url = baseUrl + cityField.getText().toString();
        new GettingJSON().execute(url);
    }

    private String HTTPHandling(String... params){
        //////  Handling with HTTP - Getting the JSON weather data
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
        ///////////
        return null;
    }

    private class GettingJSON extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonString = HTTPHandling(params[0]);

            if(jsonString != null){
                try {

                    // Deserialize JSON

                    JSONObject jObject = new JSONObject(jsonString);
                    JSONArray jArr = jObject.getJSONArray("weather");
                    JSONObject jWeather = jArr.getJSONObject(0);
                    JSONObject mainObj = jObject.getJSONObject("main");

                    HashMap<String, String> single_loc = new HashMap<>();

                    single_loc.put("name", jObject.getString("name"));
                    single_loc.put("temp", mainObj.getString("temp")+" °");
                    single_loc.put("weatherMain", jWeather.getString("main"));
                    locationsList.add(single_loc);

                } catch (final JSONException e) {
                    Log.e("ffff", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, locationsList,
                    R.layout.list_item, new String[]{"name", "temp"},
                    new int[]{R.id.name, R.id.temp}
                    );
            locationsLV.setAdapter(adapter);

        }
    }
}
