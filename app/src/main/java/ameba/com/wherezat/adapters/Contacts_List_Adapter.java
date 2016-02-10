package ameba.com.wherezat.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ameba.com.wherezat.R;

public class Contacts_List_Adapter extends BaseAdapter
{
    Context cont;
    LayoutInflater inf;
    List<HashMap<String,String>> contacts;

    public Contacts_List_Adapter(Context ss,List<HashMap<String,String>> contacts) {
        cont = ss;
        inf = LayoutInflater.from(cont);
        this.contacts = contacts;
    }


    @Override
    public int getCount()
    {
        return contacts.size();

    }

    @Override
    public Object getItem(int position)

    {
        return position;
    }

    @Override
    public long getItemId(int position)


    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inf.inflate(R.layout.contact_adapter_contents, null);

        TextView contactName = (TextView) convertView.findViewById(R.id.contactName);
        TextView contactNumber = (TextView) convertView.findViewById(R.id.contactNumber);

        contactName.setText(contacts.get(position).get("name"));
        contactNumber.setText(contacts.get(position).get("number"));

        return convertView;
    }

}