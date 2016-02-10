package ameba.com.wherezat.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ameba.com.wherezat.activity.CodeVerification;
import ameba.com.wherezat.activity.MainActivity;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;

/**
 * Created by Sharanpal on 8/24/2015.
 */
public
class Reverification_ProgressTask extends AsyncTask<Void, Void, Void>
{

    ProgressDialog pd;
    HttpResponse response;
    String ResponseString;
    SharedPreferences sharedpreferencesObj;
    Context con;
    HashMap<String,String> map;

    public
    Reverification_ProgressTask(Context context, HashMap<String,String> map)
    {
        this.con = context;
        this.map=map;
        sharedpreferencesObj = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }


    @Override
    protected
    void onPreExecute()
    {
        super.onPreExecute();
        pd = new ProgressDialog(con);
        pd.setCancelable(false);
        pd.setMessage("Processing...");
        pd.show();

    }

    @Override
    protected
    Void doInBackground(Void... params)
    {


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.reverify);
        try
        {
            List<NameValuePair> paramsL = new LinkedList<NameValuePair>();

            paramsL.add(new BasicNameValuePair("CustomerId",sharedpreferencesObj.getString("CustomerId", "")));

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");

            HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            ResponseString = EntityUtils.toString(response.getEntity());


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
    void onPostExecute(Void aVoid)
    {


        if (pd.isShowing())
        {
            pd.dismiss();
        }

        try
        {

            JSONObject jsonObject = new JSONObject(ResponseString);

           // Log.e("Reverification Response", "" + jsonObject);


            if (jsonObject.getString("Status").equals("success"))
            {
                Utill_G_S.showToast(jsonObject.getString("Message"), con);

                Intent inn = new Intent(con, CodeVerification.class);
                inn.putExtra("ApplicationID", map.get("ApplicationID"));
                inn.putExtra("DeviceSerialNo", map.get("mac"));
                con.startActivity(inn);
                ((Activity) con).finish();

            }
            else
            {
                Utill_G_S.showToast(jsonObject.getString("Message"), con);
            }


        }
        catch (JSONException e)
        {
            Utill_G_S.showToast("Error", con);
            e.printStackTrace();
        }

        super.onPostExecute(aVoid);
    }


}