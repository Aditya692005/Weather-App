package com.example.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView viewCity;
    private TextView temperature;
    private EditText cityName;
    private ImageView icon;
    private TextView viewDescription;
    private TextView humidity;
    private TextView wind;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final String API_KEY = "0e8e242de880a887cd2d9a161fcf5771";
    String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        viewCity = findViewById(R.id.viewCity);
        temperature = findViewById(R.id.temperature);
        cityName = findViewById(R.id.cityName);
        icon = findViewById(R.id.viewImage);
        viewDescription = findViewById(R.id.viewDescription);
        humidity = findViewById(R.id.humidityText);
        wind = findViewById(R.id.windText);
        checkLocationPermission();

    }
    public void onWeatherButtonClick(View v) {
        String cityInput = cityName.getText().toString();
        String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityInput +  "&units=metric&appid=0e8e242de880a887cd2d9a161fcf5771";
        @SuppressLint("SetTextI18n") StringRequest request = new StringRequest(Request.Method.GET, weatherUrl,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject main = jsonResponse.getJSONObject("main");
                        String temp = main.getString("temp");
                        String city = jsonResponse.getString("name");
                        int humidityValue = main.getInt("humidity");
                        JSONObject windValue = jsonResponse.getJSONObject("wind");
                        double windSpeed = windValue.getDouble("speed");
                        windSpeed = windSpeed * 3.6;
                        String formattedWindSpeed = String.format(Locale.US, "%.2f", windSpeed);
                        JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                        if(weatherArray.length() > 0) {
                            JSONObject weather = weatherArray.getJSONObject(0);
                            String iconCode = weather.getString("icon");
                            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
                            String description = weather.getString("description");
                            viewDescription.setText(description);
                            Glide.with(this).load(iconUrl).into(icon);
                        }
                        viewCity.setText(city);
                        temperature.setText(temp + " °C");
                        humidity.setText(humidityValue + "%");
                        wind.setText(formattedWindSpeed + "km/hr");
                    }
                    catch(Exception e) {
                        viewCity.setText("Error parsing data!");
                        viewCity.setText("Error fetching weather!");
                        temperature.setText(null);
                        humidity.setText(null);
                        wind.setText(null);
                        icon.setImageDrawable(null);
                        viewDescription.setText(null);
                        Log.e("ParsingData", "Error parsing data");
                    }
                },
                error -> {
                    viewCity.setText("Error fetching weather!");
                    temperature.setText(null);
                    humidity.setText(null);
                    wind.setText(null);
                    icon.setImageDrawable(null);
                    viewDescription.setText(null);
                    Log.e("FetchingWeather", "Error fetching weather");
                });
        Volley.newRequestQueue(this).add(request);
    }
    public void checkLocationPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
        else {
            fetchLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            }
            else {
                Toast.makeText(this, "Location permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }
    @SuppressLint({"MissingPermission", "SetTextI18n"})
    public void fetchLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e("LocationCheck", "GPS check failed");
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.e("LocationCheck", "Network check failed");
        }

        if (!gpsEnabled && !networkEnabled) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return;
        }
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String weatherURL = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=" + API_KEY;
                    Log.d("Error", weatherURL);
                    fetchWeatherData(weatherURL);
                    fusedLocationClient.removeLocationUpdates(this);
                }
                else {
                    viewCity.setText("Could not get location.");
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    public void fetchWeatherData(String url) {
        @SuppressLint("SetTextI18n") StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject main = jsonResponse.getJSONObject("main");
                        String temp = main.getString("temp");
                        String city = jsonResponse.getString("name");
                        int humidityValue = main.getInt("humidity");
                        JSONObject windValue = jsonResponse.getJSONObject("wind");
                        double windSpeed = windValue.getDouble("speed");
                        windSpeed = windSpeed * 3.6;
                        String formattedWindSpeed = String.format(Locale.US, "%.2f", windSpeed);
                        JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                        if(weatherArray.length() > 0) {
                            JSONObject weather = weatherArray.getJSONObject(0);
                            String iconCode = weather.getString("icon");
                            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
                            String description = weather.getString("description");
                            viewDescription.setText(description);
                            Glide.with(this).load(iconUrl).into(icon);
                        }
                        viewCity.setText(city);
                        temperature.setText(temp + " °C");
                        humidity.setText(humidityValue + "%");
                        wind.setText(formattedWindSpeed + "km/hr");
                    }
                    catch(Exception e) {
                        viewCity.setText("Error parsing data!");
                        viewCity.setText("Error fetching weather!");
                        temperature.setText(null);
                        humidity.setText(null);
                        wind.setText(null);
                        icon.setImageDrawable(null);
                        viewDescription.setText(null);
                        Log.e("ParsingData", "Error parsing data");
                    }
                },
                error -> {
                    viewCity.setText("Error fetching weather!");
                    viewCity.setText("Error fetching weather!");
                    temperature.setText(null);
                    humidity.setText(null);
                    wind.setText(null);
                    icon.setImageDrawable(null);
                    viewDescription.setText(null);
                    Log.e("FetchingWeather", "Error fetching weather");
                });
        Volley.newRequestQueue(this).add(request);
    }
}