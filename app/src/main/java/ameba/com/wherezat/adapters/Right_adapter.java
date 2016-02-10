package ameba.com.wherezat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ameba.com.wherezat.R;

/**
 * Created by Parambir Singh on 7/10/2015.
 */
public class Right_adapter extends BaseAdapter
{
    Context cont;
    LayoutInflater inf;
    boolean b;

    public Right_adapter(Context ss) {
        cont = ss;
        inf = LayoutInflater.from(cont);
    }
    public Right_adapter(Context ss,boolean b) {
        cont = ss;
        inf = LayoutInflater.from(cont);
        this.b = b;
    }


    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inf.inflate(R.layout.custom_invite_logs, null);

        TextView text = (TextView) convertView.findViewById(R.id.text);
        ImageView icon = (ImageView)convertView.findViewById(R.id.icon);

        Button btnyes = (Button)convertView.findViewById(R.id.btnyes);
        Button btnno = (Button)convertView.findViewById(R.id.btnno);

        if(b == true)
        {
            btnyes.setVisibility(View.GONE);
            btnno.setVisibility(View.GONE);
        }

        return convertView;
    }

}
