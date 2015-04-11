package com.mikekucharski.closetswap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.parse.ParseUser;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity {
	
	private String[] drawerStrings;
	private int[] drawerIcons;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    @SuppressWarnings("deprecation")
	private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private FragmentManager fragmentManager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
		    loadLoginView();
		}
		
		fragmentManager = getFragmentManager();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		try {
		Field mDragger = mDrawerLayout.getClass().getDeclaredField(
			    "mLeftDragger");//mRightDragger or mLeftDragger based on Drawer Gravity
			mDragger.setAccessible(true);
			ViewDragHelper draggerObj = (ViewDragHelper) mDragger
			    .get(mDrawerLayout);

			Field mEdgeSize;
	
			mEdgeSize = draggerObj.getClass().getDeclaredField(
				    "mEdgeSize");

			mEdgeSize.setAccessible(true);
			int edge = mEdgeSize.getInt(draggerObj);

			mEdgeSize.setInt(draggerObj, edge * 3); 
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
	    mDrawerList = (ListView) findViewById(R.id.left_drawer);
	    mActivityTitle = getTitle().toString();
	    
	    List<NavListItem> list = new ArrayList<NavListItem>();
	    Bitmap defaultImage;
	 	defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
	    
	    drawerStrings = getResources().getStringArray(R.array.nav_drawer_items);
	    //drawerIcons = getResources().getStringArray(R.array.nav_drawer_icons);
	    drawerIcons = new int[]{R.drawable.ic_action_view_as_list, R.drawable.ic_action_new, 
	    		R.drawable.ic_action_search, R.drawable.ic_action_settings, R.drawable.ic_action_undo};
	    
	    for(int i = 0; i < drawerStrings.length; i++){
	    	Bitmap icon = BitmapFactory.decodeResource(getResources(), drawerIcons[i]);
	    	list.add(new NavListItem(icon, drawerStrings[i]));
	    }
	    
		
		// create our list item adapter.
		NavListItemAdapter myAdapter = new NavListItemAdapter(this, 0, list);
	    
	    mDrawerList.setAdapter(myAdapter);
	    
	    // Set the list's click listener
	    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	    
	    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 
	    			    R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle("Navigation!");
                getActionBar().setIcon(R.drawable.app_icon);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mActivityTitle);
                getActionBar().setIcon(R.drawable.app_icon);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
	    
	    
        getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		if (savedInstanceState == null) {
	        selectItem(0);
		}
	}
	
	// Loads login activity for users not signed in
	// sets flags to not allow users to press back button to return to MainActivity
	private void loadLoginView() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    startActivity(intent);
	}

	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		NavListItem item = (NavListItem) mDrawerList.getItemAtPosition(position);
		String name = item.name;
		
		Toast.makeText(MainActivity.this, "Clicked " + name, Toast.LENGTH_SHORT).show();
		
		Fragment nextFragment;
		if( name.equals("Feed") ) {
			nextFragment = new FeedFragment();
		} else if( name.equals("New Post") ) {
			nextFragment = new NewPostFragment();
		} else if( name.equals("Find Users") ) {
			nextFragment = new FindUsersFragment();
		} else if( name.equals("Logout") ) {
			confirmLogout();
    	    return;
		} else if( name.equals("Settings") ) {
			nextFragment = new SettingsFragment();
		} else {
			nextFragment = new FeedFragment();
		}
		
		// IF WE NEED TO ADD DATA TO THE FRAGMENT
//	    Bundle b = new Bundle();
//	    b.putInt("Wat", position);
//	    nextFragment.setArguments(b);

	    // Insert the fragment by replacing any existing fragment
	    fragmentManager.beginTransaction()
	                   .replace(R.id.content_frame, nextFragment)
	                   .commit();

	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(drawerStrings[position]);
	    mDrawerLayout.closeDrawer(mDrawerList);
	}

	private void confirmLogout() {
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Logout of Closet Swap")
        .setMessage("Are you sure you want to leave the app?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	    		ParseUser.logOut();
	    	    loadLoginView();   
	        }
	
	    })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	selectItem(0);
	        }
	
	    })
	    .show();		
	}

	public void setTitle(String title) {
		mActivityTitle = title;
	    getActionBar().setTitle(mActivityTitle);
	}
}
