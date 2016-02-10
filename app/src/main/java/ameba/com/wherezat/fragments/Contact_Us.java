package ameba.com.wherezat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ameba.com.wherezat.R;

public class Contact_Us extends Fragment
{

	Fragment frag;
	public Contact_Us(Fragment frag)
	{
		this.frag=frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

		TextView back = (TextView) v.findViewById(R.id.back);

		back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public
			void onClick(View v)
			{
				((Settings) frag).on_back("c");
			}
		});

		return v;
	}

	@Override public
	void onResume()
	{
		super.onResume();
		Log.e("onResume", getClass().getName());
	}

}
