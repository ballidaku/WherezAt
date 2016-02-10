package ameba.com.wherezat.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
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
import ameba.com.wherezat.fragments.Invite_Logs;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;

public class RespondToInvite  extends AsyncTask<Void, Void, Void> {

    Context con;
    String StatusId,InvitationId;
    HttpResponse response;
    String ResponseString;
    SharedPreferences sharedpreferencesObj;
    SharedPreferences.Editor editorObj;
    ProgressDialog pd;
    SharedPreferences sp;
    String value="";
    Fragment fragment;


    public RespondToInvite(Context context,String StatusId,String InvitationId ,String value, Fragment fragment) {

        this.con = context;
        this.InvitationId = InvitationId;
        this.StatusId = StatusId;
        this.value=value;
        sharedpreferencesObj =context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editorObj=sharedpreferencesObj.edit();

        this.fragment=fragment;
    }

    protected void onPreExecute()
    {
        ((MainActivity) con).right.closeMenu();
//
        pd = new ProgressDialog(con);
        pd.setCancelable(false);
        pd.setMessage("Processing...");
        pd.show();

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.RespondToInvite);
        try {

            List<NameValuePair> paramsL = new LinkedList<NameValuePair>();

            //value is for accept or decline
            paramsL.add(new BasicNameValuePair("StatusId",value));
            paramsL.add(new BasicNameValuePair("InvitationId",InvitationId));


            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            ResponseString = EntityUtils.toString(response.getEntity());

            Log.i("Response of yes Button", "=====" + ResponseString);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseString = "ERROR";
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void result)
    {

        pd.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(ResponseString);
            if (jsonObject.getString("Status").equals("success"))
            {

                String success=value.equals("2")?"Request accepted successfully.":"Request Declined";
                Utill_G_S.showToast(success, con);

                /*Intent i = new Intent(Util_Constants.BROADCAST_UPDATERIGHTSIDE);
                con.sendBroadcast(i);*/
                ((Invite_Logs) fragment).refresh();
                ((MainActivity)con).update_map();



            }
            else
            {
                Utill_G_S.showToast(jsonObject.optString("Message"), con);

            }


        } catch (JSONException e)  {
            e.printStackTrace();
        }

        super.onPostExecute(result);
    }
}


