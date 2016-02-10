package ameba.com.wherezat.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

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

import ameba.com.wherezat.R;
import ameba.com.wherezat.fragments.Invite_Friends;
import ameba.com.wherezat.fragments.Invite_Logs;
import ameba.com.wherezat.fragments.My_Profile;
import ameba.com.wherezat.fragments.Settings;
import ameba.com.wherezat.global_classes.Route;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;
import ameba.com.wherezat.services.GetCurrentLocation;
import ameba.com.wherezat.services.GetInvites;
import ameba.com.wherezat.services.UpdateLocation_ProgressTask;

public
class MainActivity extends FragmentActivity //implements /*GoogleMap.OnMyLocationChangeListener,*/
{
    //private LinearLayout received_requests, sent_requests;
    private MapView mMapView;
    private GoogleMap googleMap;

    public MenuDrawer left, right;


    //LinearLayout active_tasks;

    MyBroadcastReceiver mReceiver;


    private boolean mIsReceiverRegistered = false;

    Utill_G_S util;
    private Bundle mBundle;

    SharedPreferences sh;

    Context con;

    //    ArrayList<LatLng> la=new ArrayList<>();
    public static Location my_location = null;

    GetCurrentLocation get_current_location;

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main);

        con = this;

        get_current_location = new GetCurrentLocation(con);

        if (get_current_location.checkPlayServices())
        {

            // Building the GoogleApi client
            get_current_location.buildGoogleApiClient();

            get_current_location.createLocationRequest();
        }

        /*Intent intnt = new Intent(MainActivity.this, BackgroundService.class);
        startService(intnt);*/


        util = new Utill_G_S();
        util.ChangeStatusColor(MainActivity.this, R.color.greybackground);
        sh = getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE);

        setMenuDrawer();
// MAPS STUFF

        MapsInitializer.initialize(MainActivity.this);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded();


// MAPS STUFF END




       /* la.add(new LatLng(30.707748, 76.702552));
        la.add(new LatLng(30.707305, 76.721821));
        la.add(new LatLng(30.645592, 76.810226));
        la.add(new LatLng(30.705387, 76.721821));
*/

        new CountDownTimer(1000, 1000)
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
                get_current_location.togglePeriodicLocationUpdates();
            }
        }.start();

    }

    @Override protected
    void onStart()
    {
        super.onStart();
        get_current_location.onStart();
    }

    @Override
    public
    void onResume()
    {
        super.onResume();

        get_current_location.onResume();
        mMapView.onResume();

        if (!mIsReceiverRegistered)
        {
            if (mReceiver == null)
            {
                mReceiver = new MyBroadcastReceiver();
            }
            registerReceiver(mReceiver, new IntentFilter(Util_Constants.BROADCAST_UPDATEMAP));
            mIsReceiverRegistered = true;
        }


        refresh();



        Util_Constants.isOpen=true;
        Util_Constants.time=Util_Constants.timeSCreenActive;
    }


    private
    void setUpMapIfNeeded()
    {
        if (googleMap == null)
        {
            //  googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap = ((MapView) findViewById(R.id.map)).getMap();


            //   googleMap.setOnMyLocationChangeListener(this);

            if (googleMap != null)
            {
                this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);// to hide upper right icon on maps
                googleMap.setMyLocationEnabled(false);
                //googleMap.setOnInfoWindowClickListener(this);


            }

            //  Log.e("Loca","..."+   googleMap.getMyLocation().getLatitude());
        }
    }

    public
    void refresh()
    {
        if (my_location == null)
        {
            // locationMy = util.get_location(con);
            my_location = get_current_location.displayLocation();
        }

        if (my_location == null)
        {
            my_location = util.get_location(con);
        }


        Log.e("Location",""+my_location);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_location.getLatitude(), my_location.getLongitude()), 14.0f));
        // Log.e("locationMy", "..." + my_location);


        Util_Constants.time_duration_of_hit = 0;
        Intent i = new Intent(Util_Constants.BROADCAST_UPDATEMAP);
        sendBroadcast(i);


    }

    @Override
    public
    void onPause()
    {
        super.onPause();
        mMapView.onPause();
        if (mIsReceiverRegistered)
        {
            unregisterReceiver(mReceiver);
            mReceiver = null;
            mIsReceiverRegistered = false;
        }





        Util_Constants.time=Util_Constants.updateInterval(MainActivity.this);

    }

    @Override
    public
    void onDestroy()
    {
        mMapView.onDestroy();
        Util_Constants.isOpen=false;

        super.onDestroy();
    }


    public
    void change_location(Location lo)
    {
        my_location = lo;
        refresh_map_users();

    }

    private
    void setMenuDrawer()
    {
        set_Left_Menu_Drawer();
        set_Right_Menu_Drawer();
    }

    public
    void set_Left_Menu_Drawer()
    {
        left = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        left.setContentView(R.layout.activity_main);
        left.setMenuView(R.layout.tabs);

        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(con, getSupportFragmentManager(), R.id.realtabcontent);


        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Invite Friend", getResources().getDrawable(R.mipmap.invite)), Invite_Friends.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("My Profile", getResources().getDrawable(R.mipmap.people)), My_Profile.class, null);

    }


    public
    void set_Right_Menu_Drawer()
    {
        right = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.RIGHT, MenuDrawer.MENU_DRAG_WINDOW);
        right.setContentView(R.layout.activity_main);
        right.setMenuView(R.layout.tab2);

        FragmentTabHost mTabHost2 = (FragmentTabHost) findViewById(R.id.tabhost2);
        mTabHost2.setup(con, getSupportFragmentManager(), R.id.realtabcontent2);


        mTabHost2.addTab(mTabHost2.newTabSpec("tab3").setIndicator("Invite Logs", getResources().getDrawable(R.mipmap.invite)), Invite_Logs.class, null);

        mTabHost2.addTab(mTabHost2.newTabSpec("tab4").setIndicator("Settings", getResources().getDrawable(R.mipmap.people)), Settings.class, null);

        mTabHost2.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override public
            void onTabChanged(String tabId)
            {
                Log.e("Tab_Id",tabId);

            }
        });
    }


    public
    void handleRightButtonClick(View v)
    {
        boolean drawerOpen = right.isSelected();
        if (drawerOpen)
        {
            right.closeMenu();
        }
        else
        {
            right.openMenu();
        }
    }

    public
    void handleLeftButtonClick(View v)
    {
        boolean drawerOpen = left.isSelected();
        if (drawerOpen)
        {
            left.closeMenu();
        }
        else
        {
            left.openMenu();
        }
    }

    public
    void draw_route(LatLng other)
    {

        LatLng my = new LatLng(my_location.getLatitude(), my_location.getLongitude());
        Route r = new Route();
        r.drawRoute(googleMap, MainActivity.this, my, other, "en");
    }


//    public static Location last_known_location = null;

    //*************************************** UPDATE MAP ********************************************
    private
    class MyBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public
        void onReceive(Context context, Intent intent)
        {
            new CountDownTimer(Util_Constants.time_duration_of_hit, 1000)
            {
                public
                void onTick(long millisUntilFinished) { }

                public
                void onFinish()
                {
                    update_map();
                }
            }.start();
        }
    }


    public
    void update_map()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            new GetActiveLocation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            new GetActiveLocation().execute();
        }
    }


    public
    void update_my_location(Location lo)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            new UpdateLocation_ProgressTask(MainActivity.this, lo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            new UpdateLocation_ProgressTask(MainActivity.this, lo).execute();
        }
    }


















    public
    float getDistance(Location l1, Location l2)
    {
        return l1.distanceTo(l2);
    }


    public ArrayList<HashMap<String, String>> map_users_list = new ArrayList<>();


    // Get all location of active users and show on map
    public
    class GetActiveLocation extends AsyncTask<Void, Void, Void>
    {

        String CustomerId;
        HttpResponse response;
        String ResponseString;

        public
        GetActiveLocation()
        {
            map_users_list.clear();

            CustomerId = sh.getString("CustomerId", "");


        }

        protected
        void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected
        Void doInBackground(Void... params)
        {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Util_Constants.GetActiveLocation);
            try
            {

                List<NameValuePair> paramsL = new LinkedList<>();

                paramsL.add(new BasicNameValuePair("CustomerId", CustomerId));   //changes

                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                httpPost.setHeader("Accept", "application/json");
                HttpEntity entity = new UrlEncodedFormEntity(paramsL, "UTF-8");
                httpPost.setEntity(entity);

                response = httpClient.execute(httpPost);

                ResponseString = EntityUtils.toString(response.getEntity());

                // Log.e("ResponseString", "" + ResponseString);
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


            try
            {
                JSONObject jsonObject = new JSONObject(ResponseString);
                if (jsonObject.getString("Status").equals("success"))
                {
                    if (Util_Constants.is_continuous_hit)
                    {
                        Util_Constants.time_duration_of_hit = 60000;
                    }

                   /* if (my_location != null)
                    {
                       *//* googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(locationMy.getLatitude(), locationMy.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.meicon))
                                .title("Me").snippet("1000" + "," + locationMy.getSpeed()));*//*


                    }*/

                    JSONArray MessageArray = jsonObject.getJSONArray("Message");
                    for (int i = 0; i < MessageArray.length(); i++)
                    {

                        HashMap<String, String> map = new HashMap<>();
                        // Log.e("===== in main activity", "=====" + ResponseString);
                        JSONObject data = MessageArray.getJSONObject(i);

                        /*String CustomerId = ;
                        String MobileVerifyCode = ;
                        String IsMobileVerified = ;
                        String MobilePrefix = data.getString("MobilePrefix");
                        String MobileNo = data.getString("MobileNo");
                        String Latitude = data.getString("Latitude");
                        String Longitude = data.getString("Longitude");
                        final String Name = data.getString("Name");
                        final String LastUpdated = data.getString("MiliSeconds");
                        final String speed = data.getString("SpeedLastRecorded");
*/

                        map.put("CustomerId", data.getString("CustomerId"));
                        map.put("MobileVerifyCode", data.getString("MobileVerifyCode"));
                        map.put("IsMobileVerified", data.getString("IsMobileVerified"));
                        map.put("MobilePrefix", data.getString("MobilePrefix"));
                        map.put("MobileNo", data.getString("MobileNo"));
                        map.put("Latitude", data.getString("Latitude"));
                        map.put("Longitude", data.getString("Longitude"));
                        map.put("Name", data.getString("Name"));
                        map.put("LastUpdated", data.getString("MiliSeconds"));
                        map.put("speed", data.getString("SpeedLastRecorded"));

                        map_users_list.add(map);

                      /*  if (count == 0)
                        {
                            innerHashmap.put("Latitude", "30.6500");
                            innerHashmap.put("Longitude", "76.7800");
                            count++;
                        }
                        else
                        {
                            innerHashmap.put("Latitude", "30.7500");
                            innerHashmap.put("Longitude", "76.8200");
                        }*/


                    }

                    refresh_map_users();

//                    Utill_G_S.showToast("Map Updated", con);
                }
                else
                {
                    Utill_G_S.showToast(jsonObject.optString("Message"), con);
                }

                //Util_Constants.time_duration_of_hit=0;

                Intent i = new Intent(Util_Constants.BROADCAST_UPDATEMAP);
                sendBroadcast(i);
            }
            catch (JSONException e)
            {

                e.printStackTrace();
            }

            super.onPostExecute(result);
        }


    }


    public static String selected_user_name = "";
    public String zoom_particular_user="";


    public
    void refresh_map_users()
    {
        mMapView.clearFocus();
        googleMap.clear();

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(my_location.getLatitude(), my_location.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.meicon))
                .title("Me").snippet("1000" + "," + my_location.getSpeed()));


        for (int i = 0; i < map_users_list.size(); i++)
        {
            Log.e("selected_user_name", "h.." + selected_user_name);
            Log.e("Name", "h.." + map_users_list.get(i).get("Name"));


            //********************************** To Draw root betwwe users**************************************
            if (!selected_user_name.isEmpty() && selected_user_name.equals(map_users_list.get(i).get("Name")))
            {
                LatLng other = new LatLng(Double.parseDouble(map_users_list.get(i).get("Latitude")), Double.parseDouble(map_users_list.get(i).get("Longitude")));
                draw_route(other);

            }

            double lat=Double.parseDouble(map_users_list.get(i).get("Latitude"));
            double lon=Double.parseDouble(map_users_list.get(i).get("Longitude"));

             googleMap.addMarker(new MarkerOptions()
                     .position(new LatLng(lat, lon))
                     .icon(BitmapDescriptorFactory.fromResource(R.mipmap.friend))
                     .title(map_users_list.get(i).get("Name"))
                     .snippet(map_users_list.get(i).get("LastUpdated") + "," + map_users_list.get(i).get("speed")));


            if(!zoom_particular_user.isEmpty() && zoom_particular_user.equals(map_users_list.get(i).get("Name")))
            {
                CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(lat, lon));
               // CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

                googleMap.moveCamera(center);
              //  googleMap.animateCamera(zoom);
            }

        }

        selected_user_name="";


        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            @Override
            public
            View getInfoWindow(Marker arg0)
            {
                return null;
            }

            @Override
            public
            View getInfoContents(Marker marker)
            {
                View v = getLayoutInflater().inflate(R.layout.marker, null);

                TextView title = (TextView) v.findViewById(R.id.title);
                TextView info1 = (TextView) v.findViewById(R.id.info1);
                TextView info2 = (TextView) v.findViewById(R.id.info2);

                title.setText(marker.getTitle());

                String LastUPdated = marker.getSnippet().substring(0, marker.getSnippet().indexOf(","));
                String speed = marker.getSnippet().substring(marker.getSnippet().indexOf(",") + 1);

                info1.setText("Last updated : " + util.millisToLongDHMS(Long.parseLong(LastUPdated)));
                info2.setText("Last updated speed : " + speed + " km/hr");


                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                {
                    @Override public
                    void onInfoWindowClick(Marker marker)
                    {
                        selected_user_name = marker.getTitle();
                        LatLng d = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                        draw_route(d);
                    }
                });

                return v;
            }
        });



    }


}