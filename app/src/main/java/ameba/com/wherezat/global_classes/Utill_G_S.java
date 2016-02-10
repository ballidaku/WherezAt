package ameba.com.wherezat.global_classes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ameba.com.wherezat.R;
import ameba.com.wherezat.activity.Login;
import ameba.com.wherezat.activity.MainActivity;
import ameba.com.wherezat.activity.Splash;
import ameba.com.wherezat.services.GPSTracker;

/**
 * Created by Gagan on 8/6/2015.
 */
public
class Utill_G_S
{
    public static Toast toast;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public
    void ChangeStatusColor(Activity activity, int Color)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(Color));
        }
    }


    public static
    void showToast(String msg, Context context/*, boolean center*/)
    {
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        /*if (center)
        {
			toast.setGravity(Gravity.CENTER, 0, 0);
		}*/

        toast.show();

    }

    public
    Location get_location(Context con)
    {
        GPSTracker gps = new GPSTracker(con);
        if (gps.canGetLocation())
        {

//			double	latitude = gps.getLatitude();
//			double longitude = gps.getLongitude();

            //Log.e(""+latitude, ""+longitude);


            return gps.getLocation();

        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            // gps.showSettingsAlert();
            //  turnOnGPS(con);

        }

        return null;
    }

    public
    void turnOnGPS(Context con)
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        con.sendBroadcast(intent);

        String provider = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps"))
        { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            con.sendBroadcast(poke);

        }
    }


    public
    String get_Mac_Address(Context con)
    {
        WifiManager wifiManager = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }


    public
    void sendInviteWhatsApp(Context con, String msg)
    {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg);
        try
        {
            con.startActivity(whatsappIntent);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Utill_G_S.showToast("Whatsapp have not been installed.", con);
        }
    }


    public
    void sendSMS(Context con, String phoneNumber, String message) throws IOException
    {

        Intent intent = new Intent(con, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(con, 0, intent, 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);

        Utill_G_S.showToast("Invited", con);
    }


    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;
    public final static long MONTHS = ONE_DAY * 30;

    public
    String millisToLongDHMS(long duration)
    {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND)
        {

            temp = duration / MONTHS;
            if (temp > 0)
            {
                duration -= temp * MONTHS;
                res.append(temp).append(" month").append(temp > 1 ? "s" : "");
                return res.toString() + " ago";
                // .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_DAY;
            if (temp > 0)
            {
                duration -= temp * ONE_DAY;
                res.append(temp).append(" day").append(temp > 1 ? "(s)" : "");
                return res.toString() + " ago";
                // .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0)
            {
                duration -= temp * ONE_HOUR;
                res.append(temp).append(" hour").append(temp > 1 ? "s" : "");
                return res.toString() + " ago";
                // .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0)
            {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
                return res.toString() + " ago";
            }

            if (!res.toString().equals("") && duration >= ONE_SECOND)
            {
                res.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0)
            {
                res.append(temp).append(" second").append(temp > 1 ? "s" : "");
                return res.toString() + " ago";
            }
            return res.toString();
        }
        else
        {
            return "0 second ago";
        }
    }


    public
    String get_location_name(Context con, double latitude, double longitude)
    {
        String addrss = "";
        Log.e("weqrwerw1", "werwqer1");


        Geocoder coder = new Geocoder(con, Locale.getDefault());
        try
        {
            List<Address> address = coder.getFromLocation(latitude, longitude, 1);

            int g = address.get(0).getMaxAddressLineIndex();

            for (int j = 0; j < g; j++)
            {
                addrss = addrss + " " + address.get(0).getAddressLine(j);
            }
            //Log.e("addrss", ""+addrss);

            return addrss;
        }

        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*	}
        });

		Log.e("addrss", ""+addrss);*/

        return addrss;
    }


    String msg = "Hey you are invited to join WhereZat App " + Util_Constants.app_link;

    public
    void show_super_dialog(final Context con, final String number)
    {


        final Dialog super_dialog = new Dialog(con);
        super_dialog.setContentView(R.layout.dialog_message);
        super_dialog.setTitle("Invite");


        Button btn_whatsapp = (Button) super_dialog.findViewById(R.id.btn_whatsapp);
        Button btn_msg = (Button) super_dialog.findViewById(R.id.btn_msg);
        Button btn_cancel = (Button) super_dialog.findViewById(R.id.btn_cancel);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(super_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        super_dialog.show();
        super_dialog.getWindow().setAttributes(lp);

        btn_whatsapp.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public
            void onClick(View v)
            {

                sendInviteWhatsApp(con, msg);
                super_dialog.dismiss();
            }
        });

        btn_msg.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public
            void onClick(View v)
            {

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", msg);
                sendIntent.putExtra("address", number);
                sendIntent.setType("vnd.android-dir/mms-sms");
                con.startActivity(sendIntent);
                super_dialog.dismiss();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public
            void onClick(View v)
            {
                super_dialog.dismiss();

            }
        });

    }


    public
    boolean isOnline(Context con)
    {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean b= netInfo != null && netInfo.isConnectedOrConnecting();
        if(b==false)
        {
            buildAlertMessageNoInternet(con);
            return false;
        }
        else
        {
            return true;
        }
    }

    private
    void buildAlertMessageNoInternet(final Context con)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage("Your Internet seems to be disabled, plaese enable it?")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener()
                {
                    public
                    void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        ((Splash)con).check_gps_internet();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public
                    void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    public
    boolean check_gps(Context con)
    {
        final LocationManager manager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps(con);
            return false;
        }
        else
        {
            return true;
        }


    }
    private
    void buildAlertMessageNoGps(final Context con)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public
                    void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        con.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public
                    void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void already_registered_dialog(final Context con,final HashMap<String,String> map)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("Already Registrered")
                .setMessage("You are already registered with WherezAt. Would you like to update and re-verify your account?")
                .setCancelable(false)
                .setPositiveButton("Reverify", new DialogInterface.OnClickListener()
                {
                    public
                    void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        ((Login)con).reverify(map);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public
                    void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public boolean is_background_foreground(Context con)
    {
        ActivityManager am =(ActivityManager)con.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);

        for (int i = 0; i < tasks.size(); i++)
        {
            ActivityManager.RunningTaskInfo task = tasks.get(i); // get current task


            ComponentName rootActivity = task.baseActivity;


            if(rootActivity.getPackageName().equalsIgnoreCase("ameba.com.wherezat"))
            {
                //your app is open

                return true;
            }

        }

        return false;

    }



}
