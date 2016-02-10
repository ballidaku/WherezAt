package ameba.com.wherezat.services;

import android.app.ProgressDialog;
import android.content.Context;
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


import java.util.LinkedList;
import java.util.List;

import ameba.com.wherezat.global_classes.Util_Constants;

public class UpdateLocation extends AsyncTask<Void, Void, Void> {

    Context context;
    String latitude, longitude;

    int customerId;
    HttpResponse response;
    String ResponseString;
    SharedPreferences sharedpreferencesObj;
    SharedPreferences.Editor editorObj;
    ProgressDialog pd;

    public UpdateLocation(Context context, String latitude ,String longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;

        sharedpreferencesObj = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


    }

    protected void onPreExecute() {

       /* pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Processing...");
        pd.show();
*/
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.UpdateLocation);
        try {

            List<NameValuePair> paramsL = new LinkedList<NameValuePair>();

            paramsL.add(new BasicNameValuePair("CustomerId",sharedpreferencesObj.getString("CustomerId","")));
            paramsL.add(new BasicNameValuePair("Latitude", latitude));
            paramsL.add(new BasicNameValuePair("Longitude",longitude));


            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            ResponseString = EntityUtils.toString(response.getEntity());

            Log.i("=====Response====", "=====" + ResponseString);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseString = "ERROR";
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void result) {

        //pd.dismiss();
        try {


            JSONObject jsonObject = new JSONObject(ResponseString);
//
            if (jsonObject.getString("Status").equals("success")) {




                JSONObject jsoninner = new JSONObject(jsonObject.getString("Message"));



            }


        } catch (JSONException e)  {
            e.printStackTrace();
        }

        super.onPostExecute(result);
    }

}