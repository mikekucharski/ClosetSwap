package com.mikekucharski.closetswap;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FeedFragment extends Fragment {
	
	private List<FeedItem> mItems;        // ListView items list
	private Bitmap defaultImage;
	private ListView lvPosts;
	
	public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

		
	 	defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.closet_swap_icon);
	 	
		mItems = new ArrayList<FeedItem>();
		for(int i = 0; i < 4; i++ ) {
			mItems.add(new FeedItem("omg", defaultImage, "Mike Kucharski", "Shirt", "Medium"));
			mItems.add(new FeedItem("omg", defaultImage, "Chris Culatti", "Pants", "32 waist"));
			mItems.add(new FeedItem("omg", defaultImage, "Wesley Cai", "Belt", "Medium"));
		}
			
	    lvPosts = (ListView) rootView.findViewById(R.id.lvPosts);
	    lvPosts.setAdapter(new FeedItemAdapter(this.getActivity(), 0, mItems));
	    
	    lvPosts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	FeedItem itemClicked = mItems.get(position);
            	
            	Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            	Bundle b = new Bundle();
            	b.putString("itemId", itemClicked.id);
            	intent.putExtras(b);
            	startActivity(intent);
            }
        });

	    
	    refreshPostList();
	    return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.feed, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
			case R.id.action_refresh:
				refreshPostList();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void refreshPostList() {
		getActivity().setProgressBarIndeterminateVisibility(true); 
		
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

	    query.findInBackground(new FindCallback<ParseObject>() {
	 
	        @Override
	        public void done(List<ParseObject> postList, ParseException e) {
	            if (e == null) {
	                // If there are results, update the list of posts and notify the adapter
	                mItems.clear();
	                for (ParseObject post : postList) {
	                	FeedItem item = new FeedItem(post.getObjectId(), defaultImage, "Mike Kucharski", post.getString("itemType"), post.getString("itemSize"));
//	                    FeedItem item = new FeedItem(post.getObjectId(), post.getString("title"), post.getString("content"));
	                    mItems.add(item);
	                }
	                ((BaseAdapter) lvPosts.getAdapter()).notifyDataSetChanged();
	               
	                getActivity().setProgressBarIndeterminateVisibility(false);
	            } else {
	                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
	            }
	        }
	    });
	}
}
