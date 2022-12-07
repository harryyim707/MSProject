package com.example.dietapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private Geocoder geocoder;

    private Button cancel, save, search;
    private EditText place;

    String name, latitude, longitude;
    String resLat, resLng, resname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        String input = intent.getStringExtra("input");
        if(!input.equals("")){
            setPosition(input);
        }

        cancel = (Button) findViewById(R.id.Cancel);
        save = (Button) findViewById(R.id.Save);
        search = (Button) findViewById(R.id.placeSearch);
        place = (EditText)findViewById(R.id.placeInput);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = place.getText().toString();
                place.setText(name);
                setPosition(name);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        geocoder = new Geocoder(Map.this);


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("마커 좌표");
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                markerOptions.snippet(Double.toString(lat) +", "+ Double.toString(lng));
                markerOptions.position(new LatLng(lat, lng));
                map.addMarker(markerOptions);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String res = "("+resLat+", "+resLng+")";
                System.out.println(res);
                intent.putExtra("result", res);
                intent.putExtra("place", resname);
                System.out.println(name);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LatLng seoul = new LatLng(37.5576, 127.000);
        map.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    private void setPosition(String str){
        List<Future<Address>> futures = new ArrayList<>();
        List<Address> addressList = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        Callable<Address> callable = new Callable<Address>() {
            @Override
            public Address call() throws Exception {
                List<Address> addressList = null;
                Address address = null;
                try {
                    addressList = geocoder.getFromLocationName(str, 10);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addressList != null){
                    address = addressList.get(0);
                }
                return address;
            }
        };
        futures.add(executorService.submit(callable));
        executorService.shutdown();

        for(Future<Address> future: futures){
            try {
                addressList.add(future.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!addressList.isEmpty()){
            System.out.println(addressList.get(0).toString());
            String[] splitStr = addressList.get(0).toString().split(",");
            String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2);
            System.out.println(address);
            latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
            longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
            System.out.println(latitude);
            System.out.println(longitude);
            LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            // 마커 생성
            MarkerOptions mOptions2 = new MarkerOptions();
            mOptions2.title(str);
            mOptions2.snippet(address);
            mOptions2.position(point);
            // 마커 추가
            map.addMarker(mOptions2);
            // 해당 좌표로 화면 줌
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    resLat = latitude;
                    resLng = longitude;
                    resname = str;
                    return false;
                }
            });
        }
    }
}