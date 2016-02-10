

package ameba.com.wherezat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ameba.com.wherezat.R;

/**
 * Simple Fragment used to display some meaningful content for each page in the sample's
 * {@link android.support.v4.view.ViewPager}.
 */
public class ContentFragment extends Fragment {

	private static final String KEY_TITLE = "img";
	/**
	 * @return a new instance of {@link ContentFragment}, adding the parameters into a bundle and
	 * setting them as arguments.
	 */
	public static ContentFragment newInstance(int title) {
		Bundle bundle = new Bundle();
//		bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_TITLE, title);

		ContentFragment fragment = new ContentFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
	) {
		return inflater.inflate(R.layout.pager_item, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Bundle args = getArguments();

		if (args != null) {
			ImageView img = (ImageView) view.findViewById(R.id.imgV_pager);
             img.setBackgroundResource(args.getInt(KEY_TITLE));
		}
	}
}
