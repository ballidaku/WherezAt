package ameba.com.wherezat.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.LinkedList;
import java.util.List;

import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;

public
class UpdateLocation_ProgressTask extends AsyncTask<Void, Void, Void>
{

    Context con;
    HttpResponse response;
    String ResponseString;
    SharedPreferences sharedpreferencesObj;
    SharedPreferences.Editor editorObj;
    ProgressDialog pd;

    static double latitude;
    static double longitude;

    Location location;
    Utill_G_S util;

    public
    UpdateLocation_ProgressTask(Context context, Location loc)
    {
        this.con = context;
        this.location = loc;

        util = new Utill_G_S();

       /* if(location != null)
        {*/
        Log.e("loc", "..." + location);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
      /*  }
        else
        {
            Log.e("loc","NULL LOcation");


            location=util.get_location(con);

            latitude=location.getLatitude();
            longitude=location.getLongitude();
            Log.e("loc","..."+location);

        }*/
        sharedpreferencesObj = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    protected
    void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected
    Void doInBackground(Void... params)
    {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.UpdateLocation);
        try
        {

            List<NameValuePair> paramsL = new LinkedList<NameValuePair>();


            Log.e("gps.getLatitude()", "..." + latitude + "..." + longitude);
            paramsL.add(new BasicNameValuePair("CustomerId", sharedpreferencesObj.getString("CustomerId", "")));
            paramsL.add(new BasicNameValuePair("Latitude", latitude + ""));
            paramsL.add(new BasicNameValuePair("Longitude", longitude + ""));
            paramsL.add(new BasicNameValuePair("SpeedLastRecorded", "" + location.getSpeed()));


            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            ResponseString = EntityUtils.toString(response.getEntity());

            //Log.e("=====Response====HIT", "=====" + ResponseString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ResponseString = "ERROR";
        }

        return null;

    }

    @Override
    protected
    void onPostExecute(Void result)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(ResponseString);


//            Utill_G_S.showToast("G check", con);



            if (jsonObject.getString("Status").equals("success"))
            {
                JSONObject obj = new JSONObject(jsonObject.getString("Message"));
                editorObj = sharedpreferencesObj.edit();

                String loc = util.get_location_name(con, Double.parseDouble(obj.getString("Latitude")), Double.parseDouble(obj.getString("Longitude")));
                String last_updated = util.millisToLongDHMS(Long.parseLong(obj.getString("MiliSeconds")));

                editorObj.putString("name", obj.getString("Name"));
                editorObj.putString("number", obj.getString("MobilePrefix") + "-" + obj.getString("MobileNo"));
                editorObj.putString("acc_verified", obj.getString("IsMobileVerified"));
                editorObj.putString("last_location", loc);
                editorObj.putString("last_location_updated", last_updated);
                editorObj.putString("speed_last_recorded", obj.getString("SpeedLastRecorded") + "kmph");

                editorObj.commit();

              //  Utill_G_S.showToast("Location updated \n Lat : " + location.getLatitude() + "\n Long" + location.getLongitude(), con);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        super.onPostExecute(result);
    }


}