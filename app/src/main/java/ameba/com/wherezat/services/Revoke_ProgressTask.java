package ameba.com.wherezat.services;

import android.app.ProgressDialog;
import android.content.Context;
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

/**
 * Created by Sharanpal on 8/20/2015.
 */
public
class Revoke_ProgressTask extends AsyncTask<Void, Void, Void>
{

    Context con;
    String ResponseString = "",Message="";
    String status = "";
    String InvitationId;
    ProgressDialog pd;
    Fragment fragment;

    public
    Revoke_ProgressTask(Context context, String InvitationId, Fragment fragment)
    {

        this.con = context;
        this.InvitationId = InvitationId;
        this.fragment = fragment;
    }

    protected
    void onPreExecute()
    {
        super.onPreExecute();
        ((MainActivity)con).right.closeMenu();
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
        HttpPost httpPost = new HttpPost(Util_Constants.revoke);
        try
        {

            List<NameValuePair> paramsL = new LinkedList<>();

            paramsL.add(new BasicNameValuePair("InvitationId", InvitationId));

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);

            ResponseString = EntityUtils.toString(response.getEntity());

            Log.e("Revoke ResponseString", ResponseString);

            try
            {
                JSONObject jsonObject = new JSONObject(ResponseString);
                if (jsonObject.getString("Status").equals("success"))
                {
                    status = "success";
                    Message = jsonObject.optString("Message");

                }
                else if (jsonObject.optString("Status").equals("error"))
                {
                    status = "error";
                    Message = jsonObject.optString("Message");
                }


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
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
        if (pd.isShowing())
        {
            pd.dismiss();
        }

        if (status.equals("success"))
        {
            ((MainActivity)con).update_map();
            ((Invite_Logs) fragment).refresh();

            Utill_G_S.showToast(Message, con);
        }
        else
        {
            Utill_G_S.showToast(Message, con);
        }

        super.onPostExecute(result);
    }

}
