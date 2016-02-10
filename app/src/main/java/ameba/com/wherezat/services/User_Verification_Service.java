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

import java.util.LinkedList;
import java.util.List;

import ameba.com.wherezat.global_classes.Utill_G_S;
import ameba.com.wherezat.activity.MainActivity;
import ameba.com.wherezat.global_classes.Util_Constants;


public class User_Verification_Service extends AsyncTask<Void, Void, Void>
{

	ProgressDialog pd;
	HttpResponse   response;
	String         ResponseString, CustomerId, MobileVerifyCode,ApplicationID,DeviceSerialNo;
	SharedPreferences sharedpreferencesObj;
	Context           context;


	public User_Verification_Service(Context context, String CustomerId, String MobileVerifyCode,String ApplicationID,String DeviceSerialNo)
	{
		this.context = context;
		this.CustomerId = CustomerId;
		this.MobileVerifyCode = MobileVerifyCode;
		this.ApplicationID=ApplicationID;
		this.DeviceSerialNo=DeviceSerialNo;
		sharedpreferencesObj = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
	}


	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("Processing...");
		pd.show();

	}

	@Override
	protected Void doInBackground(Void... params)
	{


		HttpClient httpClient = new DefaultHttpClient();
		HttpPost   httpPost   = new HttpPost(Util_Constants.verifyCodeURL);
		try
		{
			List<NameValuePair> paramsL = new LinkedList<>();

			paramsL.add(new BasicNameValuePair("CustomerId", CustomerId));
			paramsL.add(new BasicNameValuePair("MobileVerifyCode", MobileVerifyCode));
			paramsL.add(new BasicNameValuePair("DeviceType", "android"));
			paramsL.add(new BasicNameValuePair("ApplicationID", ApplicationID));
			paramsL.add(new BasicNameValuePair("DeviceSerialNo", DeviceSerialNo));


			Log.e("paramsL",""+paramsL);

			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("Accept", "application/json");

			HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
			httpPost.setEntity(entity);

			response = httpClient.execute(httpPost);

			ResponseString = EntityUtils.toString(response.getEntity());


		} catch (Exception e)
		{
			e.printStackTrace();
			ResponseString = "ERROR";
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid)
	{


		if (pd.isShowing())
		{
			pd.dismiss();
		}

		try
		{

			JSONObject jsonObject = new JSONObject(ResponseString);



			if (jsonObject.getString("Status").equals("success"))
			{
				JSONObject jobj = jsonObject.getJSONObject("Message");
				if (jobj.optString("IsMobileVerified").equals("true"))
				{
					Utill_G_S.showToast("Account Verified .", context);
					SharedPreferences.Editor editor = sharedpreferencesObj.edit();

					editor.putString("CustomerId", jobj.getString("CustomerId"));
					editor.putString("is_logged_in", "true");
					editor.putString("is_number_verified", "true");
					editor.commit();

					Intent inn = new Intent(context, MainActivity.class);
					context.startActivity(inn);
					((Activity) context).finish();
				}
				else
				{
					Utill_G_S.showToast("Account not verified yet.", context);
				}
			}
			else
			{
				Utill_G_S.showToast("Please try again later.", context);
			}


		}
		catch (JSONException e)
		{
			Utill_G_S.showToast("Error",context);
			e.printStackTrace();
		}

		super.onPostExecute(aVoid);
	}


}