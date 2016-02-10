package ameba.com.wherezat.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import ameba.com.wherezat.R;

/**
 Created by Sharanpal on 8/19/2015. */
public class Settings extends Fragment implements View.OnClickListener
{
	SharedPreferences sharedpreferencesObj;
	Settings s;

	SeekBar seekbarUpdateRate;
	TextView txtvUpdateRate;
	SharedPreferences.Editor edit;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		Log.e("onCreate", getClass().getName());
		super.onCreate(savedInstanceState);

		sharedpreferencesObj = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

		edit=sharedpreferencesObj.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_settings, container, false);

		s=this;
		Log.e("onCreateView", getClass().getName());



		(v.findViewById(R.id.txtv_tutorial)).setOnClickListener(this);
		(v.findViewById(R.id.txtv_about)).setOnClickListener(this);
		(v.findViewById(R.id.txtv_legal)).setOnClickListener(this);
		(v.findViewById(R.id.txtv_contact_us)).setOnClickListener(this);



		seekbarUpdateRate=(SeekBar)v.findViewById(R.id.seekBarUpdateRate);
		txtvUpdateRate=(TextView)v.findViewById(R.id.txtvUpdateRate);

		txtvUpdateRate.setText(sharedpreferencesObj.getInt("UpdateRate", 5) + " min");
		seekbarUpdateRate.setProgress(sharedpreferencesObj.getInt("UpdateRate",5));

		seekbarUpdateRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override public
			void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				edit.putInt("UpdateRate",progress);
				edit.commit();

				txtvUpdateRate.setText(progress +" min");
			}

			@Override public
			void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override public
			void onStopTrackingTouch(SeekBar seekBar)
			{

			}
		});


		return v;
	}

	@Override public
	void onResume()
	{
		Log.e("onResume", getClass().getName());
		super.onResume();


	}



	private
	void clear_frag()
	{

		Log.e("Clear Frag","hello");
		if(getFragmentManager().getBackStackEntryCount()>0)
		{

			Log.e("Clear Frag", "IIIIIIIIIII"+getFragmentManager().getBackStackEntryCount());
			while(getFragmentManager().getBackStackEntryCount()!=0)
			{
				getFragmentManager().popBackStack();
				Log.e("Clear Frag", "BYEEEEEEE"+getFragmentManager().getBackStackEntryCount());
			}
		}

	}

	Fragment a,l,c,t;

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{

			case R.id.txtv_about:

				a=new About(s);
				change_fragment(a);


				break;
			case R.id.txtv_legal:
				l=new Legal(s);
				change_fragment(l);

				break;
			case R.id.txtv_contact_us:
				c=new Contact_Us(s);

				change_fragment(c);
				break;

			case R.id.txtv_tutorial:
				t=new Tutorial(s);

				change_fragment(t);
				break;




			default:
			break;

		}
	}

	public void change_fragment(Fragment fragment)
	{
		FragmentTransaction ft  = getFragmentManager().beginTransaction();
		ft.replace(R.id.realtabcontent2, fragment);
		if(!fragment.isAdded())
		{
			ft.addToBackStack(null);
		}

		ft.commit();
	}


	public void on_back(String str)
	{
		Fragment frag=null;

		if(str.equals("a"))
		{
			frag=a;
		}
		else if(str.equals("l"))
		{
			frag=l;
		}
		else if(str.equals("t"))
		{
			frag=t;
		//	clear_frag();
		}
		else
		{
			frag=c;
		}
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		trans.remove(frag);
		trans.commit();
		manager.popBackStack();
	}
}