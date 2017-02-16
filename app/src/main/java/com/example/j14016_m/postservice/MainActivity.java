package com.example.j14016_m.postservice;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //取得した位置をDBに登録する
    private Button btnAdd;

    //位置情報を取得する
    private LocationManager locationManager;

    private double lat; //緯度
    private double lng; //経度

    private long minTime = 1000;                                //位置情報を取得する最小時間
    private float minDistance = 1;                             //位置情報を取得する最小距離
    private String provider = LocationManager.GPS_PROVIDER;   //GPSで位置情報を取得

    //Locationのリクエストコード
    private final int REQUEST_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ボタンの設定
        btnAdd = (Button) findViewById(R.id.button1);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AsyncHttp post = new AsyncHttp("Android", 10.11);
                //AsyncHttp post = new AsyncHttp("Location", lat, lng);
                String value = lat + "," + lng;
                AsyncHttp post = new AsyncHttp("緯度,経度", value);
                post.execute();

            }
        });

        //LocationManagerを取得
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        //GPSが有効か
        if(locationManager.isProviderEnabled(provider)) {
            //Locationの権限を要求
            requestLocationPermission();
        }

    }


    //Locationの権限を要求
    private void requestLocationPermission() {
        //権限が無い場合
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //権限を要求
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if(requestCode == REQUEST_LOCATION_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(this)
                            .setTitle("パーミッション取得エラー")
                            .setMessage("再試行する場合は、再度Requestボタンを押してください")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestLocationPermission();
                                }
                            })
                            .create()
                            .show();
                }
                else {
                    new AlertDialog.Builder(this)
                            .setTitle("パーミッション取得エラー")
                            .setMessage("今後は許可しないが選択されました。アプリ設定＞権限をチェックしてください（権限をON/OFFすることで状態はリセットされます）")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    openSettings();
                                }
                            })
                            .create()
                            .show();
                }
            }
        }
    }


    //アプリの設定画面を開く
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager.requestLocationUpdates(provider, minTime, minDistance, this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager.removeUpdates(this);
        }
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
