package ameba.com.wherezat.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ameba.com.wherezat.R;
import ameba.com.wherezat.adapters.Filter_Contacts_Adapter;
import ameba.com.wherezat.services.Filter_Contacts_ProgressTask;

/**
 * Created by Sharanpal on 8/18/2015.
 */
public
class Invite_Friends extends Fragment
{

    Context con;
    ListView listv_filter_contacts;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        con = getActivity();

        //**********************************Filter contacts on server side************************************
        // Left Menu
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//        {
//            new Filter_Contacts_ProgressTask(con, Invite_Friends.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        }
//        else
//        {
//            new Filter_Contacts_ProgressTask(con, Invite_Friends.this).execute();
//        }


    }

    View v = null;

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if(v == null)
        {
            v = inflater.inflate(R.layout.fragment_invite_friend, container, false);


//           TODO GAGAN
            swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override public
                void onRefresh()
                {

                        new Filter_Contacts_ProgressTask(con, Invite_Friends.this).execute();



                }
            });
//          TODO GAGAN END












            listv_filter_contacts = (ListView) v.findViewById(R.id.listv_filter_contacts);

            EditText ed = (EditText) v.findViewById(R.id.search);
            //tv.setText(this.getTag() + " Content");

            ed.addTextChangedListener(new TextWatcher()
            {
                @Override public
                void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override public
                void onTextChanged(CharSequence s, int start, int before, int count)
                {


                    ArrayList<HashMap<String, String>> tempList = new ArrayList<>();


                    for(HashMap<String, String> data : list)
                    {

                        try
                        {
                            if((data.get("name").toLowerCase()).contains(s.toString().toLowerCase()) || (data.get("name").toLowerCase()).contains(s.toString().toLowerCase()))
                            {
                                tempList.add(data);
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("name", "" + data.get("name"));
                            e.printStackTrace();
                        }

                    }

                    adapter = new Filter_Contacts_Adapter(con, tempList);
                    listv_filter_contacts.setAdapter(adapter);


                }

                @Override public
                void afterTextChanged(Editable s)
                {

                }
            });
        }

        SharedPreferences sp = con.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE);

        if(sp.contains("contacts"))
        {
            set_data(getContactLocally(sp.getString("contacts", "")));
        }

        return v;
    }

    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    Filter_Contacts_Adapter adapter;

    public
    void set_data(ArrayList<HashMap<String, String>> list)
    {

        if(this.list.size() == 0)
        {
            this.list = list;
            adapter = new Filter_Contacts_Adapter(getActivity(), this.list);
            listv_filter_contacts.setAdapter(adapter);
        }
        else
        {
            this.list = list;
            adapter.add_data(this.list);
            adapter.notifyDataSetChanged();
        }

        swipeRefreshLayout.setRefreshing(false);

    }


    private
    ArrayList<HashMap<String, String>> getContactLocally(String data)
    {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<>();
//
//
        try
        {
//            data=data.replace("[","");
//            data=data.replace("]","");
//
//           String [] contactListG = data.split("},");
////
////
//            for(String aContactList : contactListG)
//            {
//
//                String value = aContactList;
//                value = value.substring(1, value.length()-1);           //remove curly brackets
//                String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
//                HashMap<String, String> map = new HashMap<>();
//
//                for(String pair : keyValuePairs)                        //iterate over the pairs
//                {
//                    String[] entry = pair.split("=");                   //split the pairs to get key and value
//                    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
//                }
//
//
//                contactList.add(map);
//
//            }


            JSONArray jArray = new JSONArray(data);

            for(int i = 0; i < jArray.length(); i++)
            {
                JSONObject job = jArray.getJSONObject(i);

                HashMap<String, String> map = new HashMap<>();

                map.put("name", job.getString("name"));
                map.put("contact", job.getString("contact"));
                map.put("is_found", job.getString("is_found"));

                contactList.add(map);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return contactList;
    }


}
