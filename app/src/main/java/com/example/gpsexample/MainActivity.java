package com.example.gpsexample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 10;

    private LocationManager myLocationManager;
    private LocationListener myLocationListener;

    private MapsFragment fragMap;

    private EditText etLat, etLong, etZoom;
    private Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragMap = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.fragMap);

        etLat = findViewById(R.id.etLat);
        etLong = findViewById(R.id.etLong);
        etZoom = findViewById(R.id.etZoom);

        btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(op);

        myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new lokasiListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "No permission to access location, please allow it to use this app.",  Toast.LENGTH_LONG).show();
            String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            };
            ActivityCompat.requestPermissions(this, permission, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is not granted, this app won't work.",  Toast.LENGTH_LONG).show();
            }
            else {
                myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 25, myLocationListener);
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("NonConstantResourceId")
    View.OnClickListener op = view -> {
        switch (view.getId()) {
            case R.id.btnGo:
                hideKeyboard(view);
                goToLokasi();
                break;
        }
    };

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void goToLokasi() {
        Double lat = Double.parseDouble(etLat.getText().toString());
        Double lng = Double.parseDouble(etLong.getText().toString());
        float zoom = Float.parseFloat(etZoom.getText().toString());

        Toast.makeText(this, "Move to Lat: " + lat + " Long: " + lng, Toast.LENGTH_LONG).show();
        fragMap.gotoPeta(lat, lng, zoom);
    }
}