package com.mikekucharski.closetswap;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ListFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FeedFragment extends Fragment {
	
	private List<FeedItem> mItems;        // ListView items list

	public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

		Bitmap defaultImage;
	 	defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	 	
		mItems = new ArrayList<FeedItem>();
		for(int i = 0; i < 4; i++ ) {
			mItems.add(new FeedItem(defaultImage, "Mike Kucharski", "Shirt", "Medium"));
			mItems.add(new FeedItem(defaultImage, "Chris Culatti", "Pants", "32 waist"));
			mItems.add(new FeedItem(defaultImage, "Wesley Cai", "Belt", "Medium"));
		}
			
	    ListView lvPosts = (ListView) rootView.findViewById(R.id.lvPosts);
	    lvPosts.setAdapter(new FeedItemAdapter(this.getActivity(), 0, mItems));

	    return rootView;
    }
}
