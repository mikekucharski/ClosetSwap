package com.mikekucharski.closetswap;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedItemAdapter extends ArrayAdapter<FeedItem>{
	private LayoutInflater inflater;
	
	public FeedItemAdapter(Context context, int resource, List<FeedItem> objects) {
		super(context, resource, objects);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.v("test", "getView() called!");
		FeedItem item = (FeedItem) this.getItem(position);
		
		// use the layout file to generate a view
		View view = inflater.inflate(R.layout.feed_item, null);
		
		ImageView ivCategory = (ImageView) view.findViewById(R.id.ivCategory);
		ivCategory.setImageBitmap(item.categoryIcon);
		
		ImageView ivThumbnail = (ImageView) view.findViewById(R.id.ivThumbnail);
		ivThumbnail.setImageBitmap(item.thumbnail);
		
		TextView tvSize = (TextView) view.findViewById(R.id.tvItemSize);
		tvSize.setText(item.size);
		
		TextView tvDate = (TextView) view.findViewById(R.id.tvPostDate);
		tvDate.setText(item.datePosted);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		tvTitle.setText(item.title);
		
		return view;
	}
}
