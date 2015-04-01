package com.mikekucharski.closetswap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikekucharski.closetswap.R.id;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ItemDetailsActivity extends Activity {

	private TextView tvDescription, tvSize, tvCategory, tvDate;
	private ImageView ivClothingImage;
	private Button bSendEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_details);
		
		// Set up Action Bar up navigation
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("Item Details");
		
		// Link layout elements to Java
		tvDescription = (TextView) this.findViewById(R.id.tvDescription);
		tvSize = (TextView) this.findViewById(R.id.tvSize);
		tvCategory = (TextView) this.findViewById(R.id.tvCategory);
		tvDate = (TextView) this.findViewById(R.id.tvDate);
		ivClothingImage = (ImageView) this.findViewById(R.id.ivClothingImage);
		bSendEmail = (Button) this.findViewById(R.id.bSendEmail);
		
		// Get objectId of the post from bundle
		String itemId = null;
		Bundle extras = getIntent().getExtras(); 
		if( extras != null && extras.getString("itemId") != null){
			itemId = extras.getString("itemId");
			Log.v("test",itemId);
		}else{
			// destroy activity
			this.finish();
		}
		
		// Query for additional post info using objectId
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
		query.getInBackground(itemId, new GetCallback<ParseObject>() {
			@Override
		    public void done(ParseObject post, ParseException e) {
		        if (e == null) {
		        	// format date
		        	DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm");
		        	String dateFormatted = formatter.format(post.getCreatedAt());
		        	
		        	tvDescription.setText(post.getString("description"));
		        	tvCategory.setText(post.getString("itemType"));
		        	tvDate.setText(dateFormatted);
		        	tvSize.setText(post.getString("itemSize"));
		        	
		        	
		        	Log.v("test", post.getString("itemSize"));
		        } else {
		        	Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
		        }
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if(id == android.R.id.home) {
			onBackPressed();
	        return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
