package com.example.j14016_m.postservice;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.value;

/**
 * Created by J14016_M on 2017/02/13.
 */

public class AsyncHttp extends AsyncTask<String, Integer, Boolean> {
    private HttpURLConnection urlConnection = null;
    private Boolean flg = false;

    private String name;
    private String value;
    //private double lat;
    //private double lng;


    public AsyncHttp(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /*
    public AsyncHttp(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
    */

    @Override
    protected Boolean doInBackground(String... contents) {
        //String urlinput = "http://10.250.0.40/upload/post.php";
        String urlinput = "http://10.0.2.2/upload/post.php";
        try {
            URL url = new URL(urlinput);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            //String postDataSample = "name=" + name + "&text=" + lat + ", " + lng;
            String postDataSample = "name=" + name + "&text=" + value;
            OutputStream out = urlConnection.getOutputStream();
            out.write(postDataSample.getBytes());
            out.flush();
            out.close();

            urlConnection.getInputStream();

            flg = true;
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return flg;
    }
}
