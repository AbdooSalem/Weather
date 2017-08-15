package com.abdoo.android.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText cityField;
    Button submitBtn;
    FloatingActionButton addCityBtn;
    ProgressDialog progressDialog;
    ArrayList<Location> locationsList;
    ListView locationsLV;
    LinearLayout addCityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityField = (EditText) findViewById(R.id.city_field);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        locationsLV = (ListView) findViewById(R.id.locations_lv);
        addCityLayout = (LinearLayout) findViewById(R.id.add_city_layout);

        locationsList = new ArrayList<>();

        locationsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, CityDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("locationsList", locationsList);
                intent.putExtras(bundle);
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });

    }

    public void submitCity(View view) {
        String city = cityField.getText().toString();
        cityField.setText("");
        new GettingJSON().execute(city);
    }

    public void addCity(View view) {
        Intent intent = new Intent(MainActivity.this, addLocationActivity.class);
        startActivity(intent);
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
            Location loc = new Location(params[0]);

            if(loc.getName()!=null){
                locationsList.add(loc);
                return "true";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(result == null){
                Toast.makeText(getApplicationContext(), "Enter a correct location", Toast.LENGTH_SHORT).show();
                return;
            }

            LocationsAdapter adapter = new LocationsAdapter(MainActivity.this, locationsList);
            locationsLV.setAdapter(adapter);

        }

        public class LocationsAdapter extends ArrayAdapter<Location> {
            public LocationsAdapter(Context context, ArrayList<Location> users) {
                super(context, 0, users);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the data item for this position
                Location loc = getItem(position);
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                }
                // Lookup view for data population
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView temp = (TextView) convertView.findViewById(R.id.temp);
                ImageView img = (ImageView) convertView.findViewById(R.id.weather_ico);

                // Populate the data into the template view using the data object
                name.setText(loc.getName());
                temp.setText(loc.getTemp());
                Picasso.with(MainActivity.this).load(loc.getIcoUrl()).resize(90,90).into(img);
                // Return the completed view to render on screen
                return convertView;
            }
        }
    }





}
