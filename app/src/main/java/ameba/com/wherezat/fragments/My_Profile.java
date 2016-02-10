package ameba.com.wherezat.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ameba.com.wherezat.R;

/**
 * Created by Sharanpal on 8/18/2015.
 */
public
class My_Profile extends Fragment
{
    SharedPreferences sharedpreferencesObj;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sharedpreferencesObj = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        TextView txtv_name = (TextView) v.findViewById(R.id.txtv_name);
        TextView txtv_number = (TextView) v.findViewById(R.id.txtv_number);
        TextView txtv_acc_verified = (TextView) v.findViewById(R.id.txtv_acc_verified);
        TextView txtv_location = (TextView) v.findViewById(R.id.txtv_location);
        TextView txtv_location_last_updated = (TextView) v.findViewById(R.id.txtv_location_last_updated);
        TextView txtv_speed_last_updated = (TextView) v.findViewById(R.id.txtv_speed_last_updated);

        txtv_name.setText(sharedpreferencesObj.getString("name",""));
        txtv_number.setText(sharedpreferencesObj.getString("number",""));
        txtv_acc_verified.setText(sharedpreferencesObj.getString("acc_verified","").equals("true")?"Yes":"No");
        txtv_location.setText(sharedpreferencesObj.getString("last_location",""));
        txtv_location_last_updated.setText(sharedpreferencesObj.getString("last_location_updated",""));
        txtv_speed_last_updated.setText(sharedpreferencesObj.getString("speed_last_recorded",""));


        return v;
    }
}