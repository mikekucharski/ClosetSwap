package com.mikekucharski.closetswap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FeedFragment extends Fragment {
	
	private List<FeedItem> mItems;        // ListView items list
	private List<FeedItem> showItems;
	private Bitmap categoryIcon;
	private ListView lvPosts;
	private EditText etSearchBar;
	private Bitmap image;
	private Activity mActivity;
	
	public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

		mItems = new ArrayList<FeedItem>();
		showItems = new ArrayList<FeedItem>();
	    lvPosts = (ListView) rootView.findViewById(R.id.lvPosts);
	    etSearchBar = (EditText) rootView.findViewById(R.id.etSearchBar);
	    
	    lvPosts.setAdapter(new FeedItemAdapter(this.getActivity(), 0, showItems));
	    
	    lvPosts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	FeedItem itemClicked = showItems.get(position);
            	
            	Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            	Bundle b = new Bundle();
            	b.putString("itemId", itemClicked.id);
            	intent.putExtras(b);
            	startActivity(intent);
            }
        });
	    
	    etSearchBar.addTextChangedListener(new TextWatcher()
	    {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String textValue = etSearchBar.getText().toString().trim().toLowerCase();
				//refreshPostList(textValue);
				
				if(textValue.isEmpty()){
					refreshPostList();
					return;
				}
				
				showItems.clear();
				Iterator<FeedItem> mItemsIterator = mItems.iterator();
				while (mItemsIterator.hasNext()) {
					FeedItem next = mItemsIterator.next();
					if( next.title.toLowerCase().contains(textValue) || next.size.toLowerCase().contains(textValue) ||
						next.description.toLowerCase().contains(textValue) || next.size.toLowerCase().contains(textValue)){
						showItems.add(next);
					}
				}
				
				lvPosts.setAdapter(new FeedItemAdapter(getActivity(), 0, showItems));
				((BaseAdapter) lvPosts.getAdapter()).notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
	    	
	    });
	    
	    refreshPostList();
	    lvPosts.setEmptyView(rootView.findViewById(R.id.empty_list));
	    
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
	    query.orderByDescending("createdAt");
	    query.findInBackground(new FindCallback<ParseObject>() {
	    	
	        @Override
	        public void done(List<ParseObject> postList, ParseException e) {
	            if (e == null) {
	                // If there are results, update the list of posts and notify the adapter
	                mItems.clear();
	                for (ParseObject post : postList) {  	
	                	String imageFilename = Utility.getImageName(post.getString("itemType"));
	                	int imageResource = mActivity.getResources().getIdentifier(imageFilename, "drawable", mActivity.getPackageName());
	                	categoryIcon = BitmapFactory.decodeResource(mActivity.getResources(), imageResource);
	                	
	                	ParseFile img = (ParseFile) post.get("image");
	                	if(img != null)
	                	{
							try {
								byte[] bitmapdata = img.getData();
								image = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata.length);
							} catch (ParseException e1) {
								Log.v("", e1.getMessage());
							}
	                	}
	                	else
	                	{
	                		image = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.app_icon);
	                	}
	                	
	                	FeedItem item = new FeedItem(post.getObjectId(), 
	                								 image, 
	                								 categoryIcon, 
	                								 post.getString("title"), 
	                								 Utility.dateFormat(post.getCreatedAt()), 
	                								 post.getString("itemSize"),
	                								 post.getString("description"),
	                								 post.getString("itemType"),
	                								 post.getString("color"));
	                    mItems.add(item);
	                }
	                
	                showItems.clear();
	                Iterator<FeedItem> mItemsIterator = mItems.iterator();
					while (mItemsIterator.hasNext()) {
						showItems.add(mItemsIterator.next());
					}
	                ((BaseAdapter) lvPosts.getAdapter()).notifyDataSetChanged();
	               
	                mActivity.setProgressBarIndeterminateVisibility(false);
	            } else {
	                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
	            }
	        }
	    });
	}
}
