package ameba.com.wherezat.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ameba.com.wherezat.R;
import ameba.com.wherezat.global_classes.Util_Constants;
import ameba.com.wherezat.global_classes.Utill_G_S;
import ameba.com.wherezat.services.InviteContacts;

/**
 * Created by Sharanpal on 8/19/2015.
 */
public
class Filter_Contacts_Adapter extends BaseAdapter
{
    Context con;
    ArrayList<HashMap<String, String>> list;
    LayoutInflater inflater;// = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    SharedPreferences sp;

    Utill_G_S util;

    public Filter_Contacts_Adapter(Context con,ArrayList<HashMap<String, String>> list)
    {
        this.con = con;
        this.list=list;
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        util=new Utill_G_S();
        sp = con.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public
    View getView(final int position, View row, ViewGroup parent)
    {


        row = inflater.inflate(R.layout.custom_filter_contacts, parent, false);

        TextView txtv_name=(TextView)row.findViewById(R.id.txtv_name);
      final  TextView txtv_number=(TextView)row.findViewById(R.id.txtv_number);

        ImageView wherezat_logo=(ImageView)row.findViewById(R.id.ic_wherezat);


        txtv_name.setText(list.get(position).get("name"));
        txtv_number.setText(list.get(position).get("contact"));

        if(list.get(position).get("is_found").equals("yes"))
        {
            wherezat_logo.setVisibility(View.VISIBLE);

            row.setOnClickListener(new View.OnClickListener() {
                @Override public
                void onClick(View v)
                {

                    String number = txtv_number.getText().toString();


                    new InviteContacts(con, sp.getString("CustomerId", ""), number, "9").execute();
                }
            });
        }
        else
        {
           final String number = list.get(position).get("contact");

            row.setOnClickListener(new View.OnClickListener()
            {
                @Override public
                void onClick(View v)
                {
                    try
                    {
                        util.show_super_dialog(con,number);

                    }
                    catch (Exception e)
                    {
                    }
                }
            });
        }


        return row;
    }

    public void add_data(ArrayList<HashMap<String, String>> list)
    {
        this.list=list;
    }

}
