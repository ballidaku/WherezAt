package ameba.com.wherezat.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ameba.com.wherezat.R;


public
class Tutorial extends Fragment
{


    Fragment frag;
    private int[] Images = new int[]{R.drawable.one, R.drawable.two, R.drawable.three};

    public
    Tutorial()
    {
        // Required empty public constructor
    }

    public
    Tutorial(Fragment frag)
    {
        this.frag = frag;
    }

    ViewPager pager;

    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_tutorial, container, false);

        pager = (ViewPager) v.findViewById(R.id.pagerTutorial);
        frag=this;

        // pager.setAdapter(new PlaceSlidesFragmentAdapter(getFragmentManager()));
        pager.setAdapter(new ViewPagerAdapter());

        TextView back = (TextView) v.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public
            void onClick(View v)
            {
                clear();
//                ((Settings) frag).on_back("t");
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

    public
    class ViewPagerAdapter extends PagerAdapter
    {

        public
        ViewPagerAdapter()
        {
        }

        public
        int getCount()
        {
            return Images.length;
        }

        public
        Object instantiateItem(View collection, int position)
        {
            ImageView view = new ImageView(getActivity());
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setBackgroundResource(Images[position]);
            ((ViewPager) collection).addView(view, 0);
            return view;
        }

        @Override
        public
        void destroyItem(View arg0, int arg1, Object arg2)
        {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public
        boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0 == ((View) arg1);
        }

        @Override
        public
        Parcelable saveState()
        {
            return null;
        }
    }

   public void  clear()
    {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(frag);
        trans.commit();
        manager.popBackStack();
    }


}
