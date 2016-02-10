package ameba.com.wherezat.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.R;
import ameba.com.wherezat.global_classes.Utill_G_S;


public class GetActiveLocations extends AsyncTask<Void, Void, Void> {

    Context context;
    String CustomerId;
    HttpResponse response;
    String ResponseString;
    SharedPreferences sharedpreferencesObj;
    SharedPreferences.Editor editorObj;
    ProgressDialog pd;
    SharedPreferences sp;
    HashMap<String,String> innerHashmap;
    ArrayList<HashMap<String,String>> jsonResult = new ArrayList<>();
    LinearLayout active_tasks;

    public GetActiveLocations(Context context,String CustomerId,LinearLayout active_tasks)
    {
        this.context = context;
        this.CustomerId = CustomerId;
        this.active_tasks = active_tasks;

        sharedpreferencesObj =context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editorObj=sharedpreferencesObj.edit();

    }

    protected void onPreExecute() {

        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Processing...");
        pd.show();

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Util_Constants.GetActiveLocation);
        try {

            List<NameValuePair> paramsL = new LinkedList<NameValuePair>();

            paramsL.add(new BasicNameValuePair("CustomerId", CustomerId));

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

        pd.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(ResponseString);
            if (jsonObject.getString("Status").equals("success")) {

                JSONArray MessageArray = jsonObject.getJSONArray("Message");
                for(int i = 0 ; i<MessageArray.length(); i++)
                {
                    innerHashmap = new HashMap<>();
                    JSONObject data = MessageArray.getJSONObject(i);

                    String CustomerId = data.getString("CustomerId");
                    String MobileVerifyCode = data.getString("MobileVerifyCode");
                    String IsMobileVerified = data.getString("IsMobileVerified");
                    String MobilePrefix = data.getString("MobilePrefix");
                    String MobileNo = data.getString("MobileNo");
                    String Latitude = data.getString("Latitude");
                    String Longitude = data.getString("Longit1ude");

                    innerHashmap.put("CustomerId",CustomerId);
                    innerHashmap.put("MobileVerifyCode",MobileVerifyCode);
                    innerHashmap.put("IsMobileVerified",IsMobileVerified);
                    innerHashmap.put("MobilePrefix",MobilePrefix);
                    innerHashmap.put("MobileNo",MobileNo);
                    innerHashmap.put("Latitude",Latitude);
                    innerHashmap.put("Longitude",Longitude);

                    jsonResult.add(innerHashmap);
                }

                LayoutInflater inf = LayoutInflater.from(context);
                for (int g = 0; g < jsonResult.size() ; g++) {
                    View convertView = inf.inflate(R.layout.right_adapter_contents, null);

                    TextView text = (TextView) convertView.findViewById(R.id.text);
                    ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
                    icon.setImageResource(R.drawable.timericon);

                    text.setText(jsonResult.get(g).get("MobileNo"));
                    text.setTextColor(context.getResources().getColor(R.color.White));

                    final Button btnyes = (Button)convertView.findViewById(R.id.btnyes);
                    final Button btnno = (Button)convertView.findViewById(R.id.btnno);

                    btnyes.setVisibility(View.GONE);
                    btnno.setVisibility(View.GONE);

                    active_tasks.addView(convertView);

                }


            }
            else
            {
                Utill_G_S.showToast(jsonObject.optString("Message"), context);
            }


        } catch (JSONException e)  {
            e.printStackTrace();
        }

        super.onPostExecute(result);
    }


}


