package ameba.com.wherezat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import ameba.com.wherezat.R;
import ameba.com.wherezat.global_classes.Utill_G_S;
import ameba.com.wherezat.services.Filter_Contacts_ProgressTask;
import ameba.com.wherezat.services.GPSTracker;


public
class Splash extends Activity
{

    SharedPreferences sp;

    Utill_G_S util;

    Context con;
    CountDownTimer countDownTimer;


    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        con=this;

        util=new Utill_G_S();

        sp = getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE);


        Intent startlocation=new Intent(Splash.this, GPSTracker.class);
        startService(startlocation);


        countDownTimer=new CountDownTimer(4000, 1000)
        {

            @Override
            public
            void onTick(long millisUntilFinished)
            {
            }

            @Override
            public
            void onFinish()
            {
                try
                {

                    if (sp.getString("is_logged_in", "").equals("true") && sp.getString("is_number_verified", "true").equals("true"))
                    {
                        Intent in1 = new Intent(Splash.this, MainActivity.class);
                        startActivity(in1);
                    }
                    else
                    {
                        Intent in2 = new Intent(Splash.this, Login.class);
                        startActivity(in2);
                    }
                    finish();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };/*.start();*/


    }


    @Override protected
    void onResume()
    {
        super.onResume();

        check_gps_internet();

    }


    public void check_gps_internet()
    {
        boolean gps= util.check_gps(con);
        if(gps && util.isOnline(con))
        {
            countDownTimer.start();
        }

        Log.e("GPS",""+gps);
    }
}
