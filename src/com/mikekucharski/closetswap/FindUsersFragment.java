package com.mikekucharski.closetswap;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class FindUsersFragment extends Fragment {
	
	private List<UserItem> mItems;
	private ListView lvUsers;
	private EditText etSearchBar;
	
	public void onActivityCreated(Bundle savedInstanceState) {
		  super.onActivityCreated(savedInstanceState);
		}
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
			setHasOptionsMenu(true);
			View rootView = inflater.inflate(R.layout.find_user_fragment, container, false);
			
			mItems = new ArrayList<UserItem>();
		    lvUsers = (ListView) rootView.findViewById(R.id.lvUsers);
		    etSearchBar = (EditText) rootView.findViewById(R.id.etSearch);
		    
		    lvUsers.setAdapter(new UserItemAdapter(this.getActivity(), 0, mItems));
		    
		    lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
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
					String textValue = etSearchBar.getText().toString().trim();
					refreshUserList(textValue);
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
		    });
		    
		    lvUsers.setEmptyView(rootView.findViewById(R.id.tvEmptyList));
			
			return rootView;
		}
		
		@Override
		public void onCreateOptionsMenu( Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.feed, menu);
		}
		
		private void refreshUserList(String text) {
			getActivity().setProgressBarIndeterminateVisibility(true); 
			
			// Refreshes caches data for current user
		    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		    ParseUser currentUser = ParseUser.getCurrentUser();
		    
		    try {
				currentUser = currentUser.fetch();
			} catch (ParseException e1) {
				Log.v("", e1.getMessage());
			}
		    // Get friends of current user
		    @SuppressWarnings("unchecked")
			ArrayList<String> friends = (ArrayList<String>) currentUser.get("friends");
		    
		    // Queries for users not friends with current user and matches email to text in ascending lexical order
		    if(friends != null)
		    	query.whereNotContainedIn("objectId", friends);
		    query.whereContains("email", text);
		    query.orderByAscending("lastName");
		    query.findInBackground(new FindCallback<ParseObject>() {
		 
		        @Override
		        public void done(List<ParseObject> userList, ParseException e) {
		            if (e == null) {
		                // If there are results, update the list of users and notify the adapter
		                mItems.clear();
		                for (ParseObject user : userList) { 
		                	
		                	// Converts first letter of first and last name to uppercase for cleanliness
		                	String fName = user.getString("firstName");
		                	String lName = user.getString("lastName");
		                	fName = Character.toUpperCase(fName.charAt(0)) + fName.substring(1);
		                	lName = Character.toUpperCase(lName.charAt(0)) + lName.substring(1);
		                	
		                	UserItem item = new UserItem(
		                			user.getObjectId(),
		                			fName + " " + lName,
		                			user.getString("email")
		                	);
		                    mItems.add(item);
		                }
		                // Reset the adapter so that items will be removed when text is changed
		                lvUsers.setAdapter(new UserItemAdapter(getActivity(), 0, mItems));
		               
		                getActivity().setProgressBarIndeterminateVisibility(false);
		            } else {
		                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
		            }
		        }
		    });
		}

}
