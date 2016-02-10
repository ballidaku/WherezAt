package ameba.com.wherezat.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ameba.com.wherezat.activity.CodeVerification;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;


public
class User_registeration_service extends AsyncTask<Void, Void, Void>
{
    HttpResponse response;
    String ResponseString;
    ProgressDialog pd;
    Context con;
    SharedPreferences sp;

    Utill_G_S util;


    HashMap<String, String> objHashMap = new HashMap<String, String>();

    public
    User_registeration_service(Context context, HashMap objHashMap)
    {

        this.con = context;
        this.objHashMap = objHashMap;
        sp = context.getSharedPreferences("MyPrefs", context.MODE_PRIVATE);
        util = new Utill_G_S();
    }


    @Override
    protected
    void onPreExecute()
    {


        pd = new ProgressDialog(con);
        pd.setCancelable(false);
        pd.setMessage("Processing...");

        pd.show();
        super.onPreExecute();

    }

    @Override
    protected
    Void doInBackground(Void... voids)
    {


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.registeration_url);


        try
        {


            List<NameValuePair> paramsL = new LinkedList<>();

            paramsL.add(new BasicNameValuePair("FirstName", objHashMap.get("FirstName")));
            paramsL.add(new BasicNameValuePair("MobileNo", objHashMap.get("MobileNo")));
            paramsL.add(new BasicNameValuePair("ApplicationID", objHashMap.get("ApplicationID")));
            paramsL.add(new BasicNameValuePair("MobilePrefix", objHashMap.get("MobilePrefix")));

            paramsL.add(new BasicNameValuePair("DeviceType", "android"));
            paramsL.add(new BasicNameValuePair("DeviceSerialNo", objHashMap.get("mac")));


            Log.e("paramL", "" + paramsL);

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

        pd.dismiss();
        super.onPostExecute(aVoid);

        try
        {

            SharedPreferences.Editor editor = sp.edit();


            JSONObject jsonObject = new JSONObject(ResponseString);

            if (jsonObject.optString("Status").equals("success"))
            {
                JSONObject jsoninner = jsonObject.getJSONObject("Message");


                String CustomerId = jsoninner.getString("CustomerId");
                editor.putString("CustomerId", CustomerId);
                editor.putString("is_logged_in", "true");
                editor.putString("is_number_verified", "false");
                editor.commit();

                Intent inn = new Intent(con, CodeVerification.class);
                inn.putExtra("ApplicationID", objHashMap.get("ApplicationID"));
                inn.putExtra("DeviceSerialNo", objHashMap.get("mac"));
                con.startActivity(inn);
                ((Activity) con).finish();

            }
            else if (jsonObject.optString("Status").equals("alreadyexist"))
            {
                Log.e("Registration message", "" + jsonObject);

                JSONObject jsoninner = jsonObject.getJSONObject("Message");

                String CustomerId = jsoninner.getString("CustomerId");
                editor.putString("CustomerId", CustomerId);
                editor.commit();

                HashMap<String,String>map=new HashMap<>();
                map.put("ApplicationID", objHashMap.get("ApplicationID"));
                map.put("DeviceSerialNo", objHashMap.get("mac"));

                util.already_registered_dialog(con,map);

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
