package ameba.com.wherezat.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ameba.com.wherezat.R;
import ameba.com.wherezat.activity.MainActivity;
import ameba.com.wherezat.services.GetInvites;
import ameba.com.wherezat.services.RespondToInvite;
import ameba.com.wherezat.services.Revoke_ProgressTask;

/**
 * Created by Sharanpal on 8/19/2015.
 */
public
class Invite_Logs_Adapter extends BaseAdapter
{
    Context con;
    ArrayList<HashMap<String, String>> list;
    LayoutInflater inflater;// = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    SharedPreferences sp;
    Fragment fragment;

    public
    Invite_Logs_Adapter(Context con, ArrayList<HashMap<String, String>> list,Fragment fragment)
    {
        this.con = con;
        this.list = list;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragment=fragment;
        sp = con.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public
    int getCount()
    {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public
    Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public
    long getItemId(int arg0)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    int sent = 0, recieved = 0, active = 0;

    @Override
    public
    View getView(final int position, View row, ViewGroup parent)
    {
        row = inflater.inflate(R.layout.custom_invite_logs, null);
      //  Log.e("Position", "" + position);
        if (position == 0)
        {
            sent = 0;
            recieved = 0;
            active = 0;
        }


        if (list.get(position).get("RequestType").equals("sent"))
        {
            if (sent == 0)
            {
                TextView title = (TextView) row.findViewById(R.id.title12);
                title.setVisibility(View.VISIBLE);
                title.setText("Sent");
                sent++;

            }
            TextView text1 = (TextView) row.findViewById(R.id.text);
            ImageView icon = (ImageView) row.findViewById(R.id.icon);
            icon.setImageResource(R.mipmap.sendarrow);
            String textToShow1 = "You have requested to see <font color='red'><b>" + list.get(position).get("Name") + "</b></font>" + "'s location.";

            text1.setText(Html.fromHtml(textToShow1));


        }
        else if (list.get(position).get("RequestType").equals("received"))
        {
            if (recieved == 0)
            {
                TextView title = (TextView) row.findViewById(R.id.title12);
                title.setVisibility(View.VISIBLE);
                title.setText("Recieved");
                recieved++;
            }


            final TextView text = (TextView) row.findViewById(R.id.text);
            final ImageView icon = (ImageView) row.findViewById(R.id.icon);
            icon.setImageResource(R.mipmap.recieve);

            String textToShow = "<font color='red'><b>" + list.get(position).get("Name") + "</b></font>" + " has requested you to see your location.";

            text.setText(Html.fromHtml(textToShow));

            final Button btnyes = (Button) row.findViewById(R.id.btnyes);
            final Button btnno = (Button) row.findViewById(R.id.btnno);

            btnyes.setVisibility(View.VISIBLE);
            btnno.setVisibility(View.VISIBLE);

            final String customerIdToSend = list.get(position).get("CustomerId");
            final String invitationidToSend = list.get(position).get("InvitationId");

            btnno.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public
                void onClick(View view)
                {
                    //reject
                    new RespondToInvite(con, customerIdToSend, invitationidToSend, "3",fragment).execute();

                }
            });

            btnyes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public
                void onClick(View view)
                {
                    //accept
                    new RespondToInvite(con, customerIdToSend, invitationidToSend, "2",fragment).execute();

                }
            });


        }
        else if (list.get(position).get("RequestType").equals("active"))
        {


            if (active == 0)
            {
                TextView title = (TextView) row.findViewById(R.id.title12);


                title.setVisibility(View.VISIBLE);
                title.setText("Active");

                active++;

            }

            final TextView text1 = (TextView) row.findViewById(R.id.text);
            final ImageView icon = (ImageView) row.findViewById(R.id.icon);
            Button btn_revoke = (Button) row.findViewById(R.id.btn_revoke);

            btn_revoke.setVisibility(View.VISIBLE);
            icon.setImageResource(R.mipmap.timericon);

            String textToShow1 = "<font color='grey'><b>" + list.get(position).get("Name") + "</b></font>";
            text1.setText(Html.fromHtml(textToShow1));

            btn_revoke.setOnClickListener(new View.OnClickListener()
            {
                @Override public
                void onClick(View v)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    {
                        new Revoke_ProgressTask(con, list.get(position).get("InvitationId"),fragment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    else
                    {
                        new Revoke_ProgressTask(con, list.get(position).get("InvitationId"),fragment).execute();
                    }

                }
            });

            row.setOnClickListener(new View.OnClickListener()
            {
                @Override public
                void onClick(View v)
                {
                    ((MainActivity) con).right.closeMenu();
                    ((MainActivity)con).zoom_particular_user=list.get(position).get("Name");
                    ((MainActivity)con).refresh_map_users();
                }
            });
        }


        return row;
    }

    public
    void add_data(ArrayList<HashMap<String, String>> list)
    {

        this.list = list;

    }

}
