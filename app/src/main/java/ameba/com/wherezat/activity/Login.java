package ameba.com.wherezat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import ameba.com.wherezat.R;
import ameba.com.wherezat.fragments.Invite_Friends;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;
import ameba.com.wherezat.services.Filter_Contacts_ProgressTask;
import ameba.com.wherezat.services.Reverification_ProgressTask;
import ameba.com.wherezat.services.User_registeration_service;


public
class Login extends Activity implements View.OnClickListener
{
    Button buttonverify;
    EditText username;
    EditText phonenumber;
    TextView textv_terms_condition,txtCountryCode;

    String DeviceID="";
    private GoogleCloudMessaging gcm;
    SharedPreferences sharedpreferencesObj;
    Animation alpha;
    Utill_G_S utill_g_s;
    Context con;

    Spinner spn_country_name;


    ArrayList<String> CountryFullName;

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            new Filter_Contacts_ProgressTask(Login.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            new Filter_Contacts_ProgressTask(Login.this).execute();
        }



        con = this;
        utill_g_s = new Utill_G_S();

        utill_g_s.ChangeStatusColor(Login.this, R.color.greybackground);

        alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        getRegisterationID();

        spn_country_name=(Spinner) findViewById(R.id.spn_country_name);

        txtCountryCode=(TextView)findViewById(R.id.txtCountryCode);
        username = (EditText) findViewById(R.id.edittext1);
        phonenumber = (EditText) findViewById(R.id.edittext2);
        (buttonverify = (Button) findViewById(R.id.btn_verify)).setOnClickListener(this);
        (textv_terms_condition=(TextView)findViewById(R.id.textv_terms_condition)).setOnClickListener(this);

        final String[] CountryCodeList=this.getResources().getStringArray(R.array.CountryCodes);

        CountryFullName=new ArrayList<>();
        for (String aCountryCodeList : CountryCodeList)
        {
            String [] g=aCountryCodeList.split(",");
            Locale loc = new Locale("",g[1]);
            CountryFullName.add(loc.getDisplayCountry());
        }



        spn_country_name.setAdapter(new ArrayAdapter<String>(Login.this, R.layout.spinner_adaptr, CountryFullName));

        spn_country_name.setPressed(false);

        txtCountryCode.setOnClickListener(this);
        spn_country_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id)
            {

                String[] g = CountryCodeList[position].split(",");
                txtCountryCode.setText("+" + g[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });


        GetCountryZipCode(getUserCountry());
//        spn_country_name.setSelection();
//        txtCountryCode.setText(GetCountryZipCode(getUserCountry()));



        phonenumber.addTextChangedListener(txtWatcher);
        username.addTextChangedListener(txtWatcher);




    }



    TextWatcher txtWatcher =    new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

            if (phonenumber.getText().toString().length() == 10 && !username.getText().toString().trim().isEmpty())
            {
                buttonverify.setVisibility(View.VISIBLE);
                textv_terms_condition.setVisibility(View.VISIBLE);
                buttonverify.startAnimation(alpha);
            }
            else
            {
                buttonverify.setVisibility(View.INVISIBLE);
                textv_terms_condition.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };




    public
    void getRegisterationID()
    {

        new AsyncTask<Object, Object, Object>()
        {
            @Override
            protected
            Object doInBackground(Object... params)
            {

                String msg = "";
                try
                {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
                    }
                    DeviceID = gcm.register(Util_Constants.SENDER_ID);


                    msg = "Device registered, registration ID=" + DeviceID;

                }
                catch (Exception ex)
                {
                    DeviceID = "";
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }


            protected
            void onPostExecute(Object result)
            {

                Log.e("GCM Message",""+result.toString());
                if (!DeviceID.equals(""))
                {
                    sharedpreferencesObj = getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editPref = sharedpreferencesObj.edit();
                    Log.e("Before device id", "" + DeviceID);
                    editPref.putString("device_id", DeviceID);
                    Log.e("After device id", "" + DeviceID);
                    editPref.apply();
                }
                else
                {
                    getRegisterationID();
                }


            }
        }.execute();
    }


    @Override
    public
    void onClick(View v)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch (v.getId())
        {
            case R.id.btn_verify:
                if (username.getText().toString().trim().isEmpty())
                {
                    username.setError("enter user name");
//                    Utill_G_S.showToast("Please enter username.",con);

                }
                else if (phonenumber.getText().toString().trim().isEmpty())
                {
//                    Utill_G_S.showToast("Please enter phone number.", con);
                    phonenumber.setError("enter phone number");
                }
                else if(DeviceID.isEmpty())
                {
                    Utill_G_S.showToast("Device Id not found. Please try after some time ",con);
                    getRegisterationID();
                }
                else
                {

                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("FirstName", username.getText().toString());
                    data.put("MobileNo", phonenumber.getText().toString());
                    data.put("ApplicationID", DeviceID);
                    data.put("MobilePrefix", "+91");
                    data.put("mac", utill_g_s.get_Mac_Address(con));


                    new User_registeration_service(Login.this, data).execute();

                }

                break;

            case R.id.txtCountryCode:

                    spn_country_name.performClick();

                break;

            default:
                break;

        }

    }


    public void reverify(HashMap<String,String> map)
    {
        new Reverification_ProgressTask(con,map).execute();
    }



    private String getUserCountry()
    {
        String  CountryID="";
        try {
            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            //getNetworkCountryIso
            CountryID= manager.getSimCountryIso().toUpperCase();

            if (CountryID.isEmpty())
            { // SIM country code is available
                if (manager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA)
                { // device is not 3G (would be unreliable)
                    String networkCountry = manager.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2)
                    { // network country code is available
                       return networkCountry.toUpperCase();
                    }

                    return "IN";
                }
                else
                {
                    CountryID="IN";
                }
            }

            return CountryID;

        }
        catch (Exception e) {
            e.printStackTrace();
            return "IN";

        }

    }

    private void GetCountryZipCode(String CountryID)
    {

        String CountryZipCode="";

        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
//        for (String aRl : rl)
//        {
//            String[] g = aRl.split(",");
//            if (g[1].trim().equals(CountryID.trim()))
//            {
//                CountryZipCode = g[0];
//                break;
//            }
//        }

        for (int i = 0; i < rl.length; ++i)
        {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim()))
                spn_country_name.setSelection(i);
        }



    }

}
