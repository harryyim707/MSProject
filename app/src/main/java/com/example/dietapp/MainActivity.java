package com.example.dietapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    Home home;
    CalendarFragment calendar;
    AnalysisFragment analysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = new Home();
        calendar = new CalendarFragment();
        analysis = new AnalysisFragment();
        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this,"카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_IMAGE_CAPTURE);
                Toast.makeText(this,"카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, home).commit();
        NavigationBarView navigationBarView = findViewById(R.id.menu_bottom_navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, home).commit();
                    return true;
                case R.id.menu_analysis:
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, analysis).commit();
                    return true;
                case R.id.menu_calendar:
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, calendar).commit();
                    return true;
            }
            return false;
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}