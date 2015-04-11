package com.mikekucharski.closetswap;

import java.util.List;

import com.parse.ParseException;
import com.parse.ParseUser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class UserItemAdapter extends ArrayAdapter<UserItem>{
	
	private LayoutInflater inflater;
	
	public UserItemAdapter(Context context, int resource, List<UserItem> objects) {
		super(context, resource, objects);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		
		if (view == null) {
            view = inflater.inflate(R.layout.user_item, null);
        }
		
		UserItem item = (UserItem) getItem(position);
		Button add = (Button) view.findViewById(R.id.bAddUser);
		
		TextView name = (TextView) view.findViewById(R.id.tvName);
		name.setText(item.name);
		
		TextView email = (TextView) view.findViewById(R.id.tvEmail);
		email.setText(item.email);
		
		add.setOnClickListener(new ButtonOnClickListener(item));
		
		return view;
	}
	
	// Custom OnClickListener class for add button
	public class ButtonOnClickListener implements OnClickListener {

		UserItem item;
		
		public ButtonOnClickListener(UserItem item)
		{
			this.item = item;
		}

	    public void onClick(View v) {

	    	Button add = (Button) v;
			if(add.getText().toString().compareTo("+") == 0)
			{			
				// If user isn't added yet, add user objectId to friends array
				ParseUser currentUser = ParseUser.getCurrentUser();
				String id = item.userId;
				currentUser.add("friends", id);
				try {
					currentUser.save();
				} catch (ParseException e) {
					Log.v("", e.getMessage());
				}
				add.setText("✓");
			}
	    }

	}

}
