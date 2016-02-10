package ameba.com.wherezat.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import ameba.com.wherezat.R;
import ameba.com.wherezat.adapters.Filter_Contacts_Adapter;
import ameba.com.wherezat.adapters.Invite_Logs_Adapter;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.services.GetInvites;

/**
 * Created by Sharanpal on 8/19/2015.
 */
public
class Invite_Logs extends Fragment
{

    Context con;
    ListView listv_invite_logs;

    SharedPreferences sh;

    UpdateRightSideBroadcastReceiver sReceiver;

    LinearLayout lay_no_invite;

    boolean mIsReceiverRegistered=false;

    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        con = getActivity();

        sh = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        refresh();

    }

    @Override public
    void onResume()
    {
        super.onResume();

        if (!mIsReceiverRegistered)
        {
            if (sReceiver == null)
            {
                sReceiver = new UpdateRightSideBroadcastReceiver();
            }

            getActivity().registerReceiver(sReceiver, new IntentFilter(Util_Constants.BROADCAST_UPDATERIGHTSIDE));
            mIsReceiverRegistered = true;
        }
    }

    @Override
    public
    void onPause()
    {
        super.onPause();
        if (mIsReceiverRegistered)
        {

            getActivity().unregisterReceiver(sReceiver);
            sReceiver = null;
            mIsReceiverRegistered = false;
        }

    }

    View v = null;

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (v == null)
        {
            v = inflater.inflate(R.layout.fragment_invite_logs, container, false);
            listv_invite_logs = (ListView) v.findViewById(R.id.listv_invite_logs);
            lay_no_invite=(LinearLayout)v.findViewById(R.id.lay_no_invite);
        }


        return v;
    }

    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    Invite_Logs_Adapter adapter;

    public
    void set_data(ArrayList<HashMap<String, String>> list)
    {
        listv_invite_logs.setVisibility(View.VISIBLE);
        lay_no_invite.setVisibility(View.GONE);

        if (this.list.size() == 0)
        {
            this.list = list;
            adapter = new Invite_Logs_Adapter(con, this.list, Invite_Logs.this);
            listv_invite_logs.setAdapter(adapter);
        }
        else
        {
            this.list = list;
            adapter.add_data(this.list);
            adapter.notifyDataSetChanged();
        }



        if (this.list.size() == 0)
        {
            listv_invite_logs.setVisibility(View.GONE);
            lay_no_invite.setVisibility(View.VISIBLE);
        }


    }


    public
    void refresh()
    {
        // Right Menu
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            new GetInvites(con, sh.getString("CustomerId", ""), Invite_Logs.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            new GetInvites(con, sh.getString("CustomerId", ""), Invite_Logs.this).execute();
        }

    }


    private
    class UpdateRightSideBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public
        void onReceive(Context context, Intent intent)
        {
            refresh();

        }
    }


}