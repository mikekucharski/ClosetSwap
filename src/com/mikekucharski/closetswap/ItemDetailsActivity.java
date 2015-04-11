package com.mikekucharski.closetswap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ItemDetailsActivity extends Activity {

	private TextView tvDescription, tvColor, tvSize, tvCategory, tvDate, tvNameEmail, tvTitle;
	private ImageView ivClothingImage;
	private Button bSendEmail;
	private String email, title;
	private Bitmap image = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_details);
		
		// Set up Action Bar up navigation
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("Item Details");
		
		email=title="";
		// Link layout elements to Java
		tvDescription = (TextView) this.findViewById(R.id.tvDescription);
		tvSize = (TextView) this.findViewById(R.id.tvSize);
		tvCategory = (TextView) this.findViewById(R.id.tvCategory);
		tvDate = (TextView) this.findViewById(R.id.tvDate);
		tvColor = (TextView) this.findViewById(R.id.tvColor);
		tvNameEmail = (TextView) this.findViewById(R.id.tvNameEmail);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		ivClothingImage = (ImageView) this.findViewById(R.id.ivClothingImage);
		bSendEmail = (Button) this.findViewById(R.id.bSendEmail);
		
		// Get objectId of the post from bundle
		String itemId = null;
		Bundle extras = getIntent().getExtras(); 
		if( extras != null && extras.getString("itemId") != null){
			itemId = extras.getString("itemId");
		}else{
			// destroy activity
			this.finish();
		}
		
		// Query for additional post info using objectId
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
		query.include("owner");
		query.getInBackground(itemId, new GetCallback<ParseObject>() {
			@Override
		    public void done(ParseObject post, ParseException e) {
		        if (e == null) {
		        	// get the user who made the post
		        	ParseObject user = post.getParseObject("owner");
		        	
		        	String dateFormatted = Utility.dateFormat(post.getCreatedAt());
		        	String fullName = Character.toUpperCase(user.getString("firstName").charAt(0)) + user.getString("firstName").substring(1) + 
		        			" " + 
		        			Character.toUpperCase(user.getString("lastName").charAt(0)) + user.getString("lastName").substring(1);
		        	
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
                		image = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
                	}
		        	
		        	// set up data used for sending email
		        	email = user.getString("email");
		        	title = post.getString("title");
		        	
		        	// add post data to layout
		        	tvNameEmail.setText(fullName + ",  " + email);
		        	tvTitle.setText(title);
		        	tvDescription.setText(post.getString("description"));
		        	tvColor.setText(post.getString("color"));
		        	tvCategory.setText(post.getString("itemType"));
		        	tvDate.setText(dateFormatted);
		        	tvSize.setText(post.getString("itemSize"));
		        	ivClothingImage.setImageBitmap(image);

		        } else {
		        	Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
		        }
			}
		});
		
		bSendEmail.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
				i.putExtra(Intent.EXTRA_SUBJECT, "Re: " + title);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				try {
				    startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(ItemDetailsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
