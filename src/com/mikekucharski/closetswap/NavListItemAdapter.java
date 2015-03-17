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


public class NavListItemAdapter extends ArrayAdapter<NavListItem>{
	private LayoutInflater inflator;
	private List<NavListItem> navItems;
	
	public NavListItemAdapter(Context context, int resource, List<NavListItem> objects) {
		super(context, resource, objects);
		inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		navItems=objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.v("test", "getView() called!");
		//NavListItem item = (NavListItem) this.getItem(position);
		
		// use the layout file to generate a view
		View view = inflator.inflate(R.layout.drawer_list_item, null);
		
		// set image
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		icon.setImageBitmap(navItems.get(position).icon);
		
		// set user name
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(navItems.get(position).name);
		
		return view;
	}
}
