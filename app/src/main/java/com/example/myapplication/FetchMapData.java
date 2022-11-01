package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchMapData extends AsyncTask<LatLng, String, String> {
    /*String url;*/
    private static final String API_KEY = "AIzaSyC0bF1q80VY5W1vD73VCb45NMEU4mUvHsg";
    MapsActivity ctxt;
    LatLng destination;

    FetchMapData(MapsActivity context)
    {
        ctxt = context;
        destination = null;
    }

    @Override
    protected String doInBackground(LatLng ... latLngs) {
        String url = getPathUrl(latLngs[0], latLngs[1]);
        destination = latLngs[1];
        String data = "";
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.connect();
            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while((line = br.readLine()) != null)
                sb.append(line);
            data = sb.toString();
            //Log.i("Map", String.format("Data is %s", data));
            br.close();
        } catch (Exception e)
        {
            Log.e("Map", "Exception while reading route data!");
        } finally {
            try {
                is.close();
            } catch (Exception e)
            {
            }
            conn.disconnect();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String res)
    {
        super.onPostExecute(res);
        //Log.i("Data", res);
        new ParseMapData(ctxt, destination).execute(res);
    }

    public String getPathUrl(LatLng a, LatLng b)
    {
        String res = String.format("https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&sensor=false&mode=driving&key=%s", a.latitude, a.longitude, b.latitude, b.longitude, API_KEY);
        Log.i("URL", res);
        return res;
    }
}
