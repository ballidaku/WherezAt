package ameba.com.wherezat.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
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

import ameba.com.wherezat.activity.MainActivity;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;


public
class InviteContacts extends AsyncTask<Void, Void, Void>
{

    Context con;
    HttpResponse response;
    String ResponseString;
    SharedPreferences.Editor editorObj;
    ProgressDialog pd;
    SharedPreferences sp;


    String CustomerIdBy, MobileNo, Duration;


    public
    InviteContacts(Context con, String CustomerIdBy, String MobileNo, String Duration)
    {


        this.con = con;
        this.CustomerIdBy = CustomerIdBy;

        this.MobileNo = MobileNo;
        this.Duration = Duration;

        ((MainActivity) con).left.closeMenu();
    }

    protected
    void onPreExecute()
    {
        ((MainActivity) con).left.closeMenu();
        pd = new ProgressDialog(con);
        pd.setCancelable(false);
        pd.setMessage("Processing...");
        pd.show();

        super.onPreExecute();
    }

    @Override
    protected
    Void doInBackground(Void... params)
    {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.inviteContactsURL);
        try
        {

            List<NameValuePair> paramsL = new LinkedList<NameValuePair>();

            paramsL.add(new BasicNameValuePair("CustomerIdBy", CustomerIdBy));
            paramsL.add(new BasicNameValuePair("MobileNo", MobileNo));
            paramsL.add(new BasicNameValuePair("DurationToast", Duration));


            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            ResponseString = EntityUtils.toString(response.getEntity());

             Log.i("=====Invite Contacts Response====", "=====" + ResponseString);
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

        pd.dismiss();

        try
        {
            JSONObject jsonObject = new JSONObject(ResponseString);


            if (jsonObject.getString("Status").equals("success"))
            {

                Utill_G_S.showToast("Request sent successfully", con);
//                /((MainActivity) con).set_Right_Menu_Drawer();

                Intent i=new Intent(Util_Constants.BROADCAST_UPDATERIGHTSIDE);
                con.sendBroadcast(i);

            }
            else
            {
                // Utill_G_S.showToast(jsonObject.optString("Message"), con);
            }


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        super.onPostExecute(result);
    }

}
