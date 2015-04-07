package com.mikekucharski.closetswap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewPostFragment extends Fragment {

	private EditText etTitle, etColor, etDescription, etContactInfo;
	private ImageView imageView;
	private Button bUploadBtn, bCreatePost;
	private Spinner sItemType, sItemSize, sCondition;
	private String title, color, description, contactInfo, type, size, condition;
	private StringBuilder errorDialog;
	private Drawable defaultEditTextBackground;
	private Drawable defaultSpinnerBackground;
	private static final int CAMERA_ID = 1888;
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		  super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.new_post_fragment, container, false);
		
		etTitle = (EditText)rootView.findViewById(R.id.postTitle);
		
		etColor = (EditText)rootView.findViewById(R.id.colorName);
		etDescription = (EditText)rootView.findViewById(R.id.descriptionText);
		etContactInfo = (EditText)rootView.findViewById(R.id.contactText);
		imageView = (ImageView) rootView.findViewById(R.id.newPic_imageView);
		
		
		bUploadBtn = (Button)rootView.findViewById(R.id.bUploadBtn);
		bCreatePost = (Button)rootView.findViewById(R.id.createPost);
		
		sItemType = (Spinner)rootView.findViewById(R.id.itemList);
		sItemSize = (Spinner)rootView.findViewById(R.id.sizeList);
		sCondition = (Spinner)rootView.findViewById(R.id.conditionList);
		
		bUploadBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_ID);

			}
		});
		
		
		
		bCreatePost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				errorDialog = new StringBuilder();
				title = etTitle.getText().toString().trim();
				color = etColor.getText().toString().trim();
				description = etDescription.getText().toString().trim();
				contactInfo = etContactInfo.getText().toString().trim();
				type = sItemType.getSelectedItem().toString().trim();
				size = sItemSize.getSelectedItem().toString().trim();
				condition = sCondition.getSelectedItem().toString().trim();
				
				defaultEditTextBackground = etColor.getBackground();
				defaultSpinnerBackground = sItemSize.getBackground();
				if(title.isEmpty() || type.isEmpty() || size.isEmpty() || condition.isEmpty() || color.isEmpty() || contactInfo.isEmpty())
				{
					if(title.isEmpty())
					{
						errorDialog.append("Title is missing\n");
						etTitle.setBackgroundColor(Color.parseColor("#ff0000"));
					}
					else
					{
						etTitle.setBackgroundResource(android.R.drawable.editbox_background);
					}
					
					if(type.isEmpty())
					{
						errorDialog.append("Item Type is missing\n");
						sItemType.setBackgroundColor(Color.parseColor("#ff0000"));
					}
					else
					{
						sItemType.setBackgroundResource(android.R.drawable.spinner_background);
					}
					
					if(size.isEmpty())
					{
						errorDialog.append("Item Size is missing\n");
						sItemSize.setBackgroundColor(Color.parseColor("#ff0000"));
					}
					else
					{
						sItemSize.setBackgroundResource(android.R.drawable.spinner_background);
					}
					
					if(condition.isEmpty())
					{
						errorDialog.append("Condition is missing\n");
						sCondition.setBackgroundColor(Color.parseColor("#ff0000"));
					}
					else
					{
						sCondition.setBackgroundResource(android.R.drawable.spinner_background);
					}
					
					if(color.isEmpty())
					{
						errorDialog.append("Color is missing\n");
						etColor.setBackgroundColor(Color.parseColor("#ff0000"));
					}
					else
					{
						etColor.setBackgroundResource(android.R.drawable.editbox_background);
					}
					
					if(contactInfo.isEmpty())
					{
						errorDialog.append("ContactInfo is missing\n");
						etContactInfo.setBackgroundColor(Color.parseColor("#ff0000"));
					}
					else
					{
						etContactInfo.setBackgroundResource(android.R.drawable.editbox_background);
					}
					
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(errorDialog)
                           .setTitle("Missing Values")
                           .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
				}
				else
				{
                    getActivity().setProgressBarIndeterminateVisibility(true);
                    
                    ParseObject newPost = new ParseObject("Post");
                    newPost.put("owner", ParseUser.getCurrentUser());
                    newPost.put("title", title);
                    newPost.put("description", description);
                    newPost.put("itemType", type);
                    newPost.put("itemSize", size);
                    newPost.put("condition", condition);
                    newPost.put("color", color);
                    newPost.put("contactInfo", contactInfo);
                    newPost.saveInBackground(new SaveCallback() {
                    	
                    	@Override
                        public void done(ParseException e) {
                            getActivity().setProgressBarIndeterminateVisibility(false);
 
                            if (e == null) {
                                // Success!
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(e.getMessage())
                                    .setTitle("An error has occurred when trying to save post!")
                                    .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }); // End of saveInBackground()
                } // end else			
			}	
		});
		
		return rootView;
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(CAMERA_ID == requestCode && resultCode == Activity.RESULT_OK){
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(photo);
		}
	}
	
	
}
