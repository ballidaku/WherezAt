package ameba.com.wherezat.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ameba.com.wherezat.R;
import ameba.com.wherezat.fragments.Invite_Logs;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;

public
class GetInvites extends AsyncTask<Void, Void, Void>
{

    Context con;
    String customerid;


    HttpResponse response;
    String ResponseString;
    SharedPreferences sharedpreferencesObj;
    SharedPreferences.Editor editorObj;
//    ProgressDialog pd;

    ArrayList<HashMap<String, String>> list = new ArrayList<>();

    String status = "";

    LayoutInflater inf;
    Fragment fragment;

    public
    GetInvites(Context context, String customerid, Fragment fragment)
    {

        this.customerid = customerid;
        this.con = context;
        this.fragment = fragment;
        inf = LayoutInflater.from(context);

        sharedpreferencesObj = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //editorObj=sharedpreferencesObj.edit();

    }

    protected
    void onPreExecute()
    {

      /*  pd = new ProgressDialog(con);
        pd.setCancelable(false);
        pd.setMessage("Processing...");
        pd.show();*/

        super.onPreExecute();
    }

    @Override
    protected
    Void doInBackground(Void... params)
    {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.getinvites);
        try
        {

            List<NameValuePair> paramsL = new LinkedList<NameValuePair>();

            paramsL.add(new BasicNameValuePair("CustomerId", customerid));


            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            ResponseString = EntityUtils.toString(response.getEntity());


           // Log.e("Getinvites ResponseString", "..." + ResponseString);

            try
            {


                JSONObject jsonObject = new JSONObject(ResponseString);

                if (jsonObject.getString("Status").equals("success"))
                {
                    status = "success";

                    JSONArray MessageArray = jsonObject.getJSONArray("Message");


                    for (int i = 0; i < MessageArray.length(); i++)
                    {

                        HashMap<String, String> map = new HashMap<>();
                        JSONObject data = MessageArray.getJSONObject(i);


                        map.put("CustomerId", data.getString("CustomerId"));
                        map.put("InvitationId", data.getString("InvitationId"));
                        map.put("Name", data.getString("Name"));
                        map.put("RequestType", data.getString("RequestType"));

                        list.add(map);

                    }
                }
                else if (jsonObject.optString("Status").equals("error"))
                {
                    status = "error";
                    ResponseString = jsonObject.optString("Message");
                }


            }
            catch (Exception e)
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

        if (status.equals("success"))
        {
            ((Invite_Logs) fragment).set_data(list);
        }
        else
        {
            Utill_G_S.showToast(ResponseString, con);
        }


        super.onPostExecute(result);
    }




}
