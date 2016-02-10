package ameba.com.wherezat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import ameba.com.wherezat.R;


public class Legal extends Fragment
{
	Fragment frag;
	public Legal(Fragment frag)
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
		View v = inflater.inflate(R.layout.fragment_legal, container, false);


		WebView web = (WebView) v.findViewById(R.id.web_view);

		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl("http://112.196.34.42:8094/TermPrivacy.html");


		TextView back = (TextView) v.findViewById(R.id.back);

		back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((Settings) frag).on_back("l");
			}
		});

		return v;
	}




}
