package com.mikekucharski.closetswap;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SettingsFragment extends Fragment {
	
	private TextView tvTitle;
	private Button bSubmit;
	private EditText etEmail, etPassword, etFirstName, etLastName;
	private ParseUser current;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_register, container, false);
		
		tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
		bSubmit = (Button) rootView.findViewById(R.id.bSignup);
		etEmail = (EditText) rootView.findViewById(R.id.etEmail);
		etPassword = (EditText) rootView.findViewById(R.id.etPassword);
		etFirstName = (EditText) rootView.findViewById(R.id.etFirstName);
		etLastName = (EditText) rootView.findViewById(R.id.etLastName);
		
		current = ParseUser.getCurrentUser();
		
		bSubmit.setText("Save Changes");
		tvTitle.setText("Edit Account Settings");
		etEmail.setText(current.getString("email"));
		etFirstName.setText(current.getString("firstName"));
		etLastName.setText(current.getString("lastName"));
		
		bSubmit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String email = etEmail.getText().toString().trim(),
					   password = etPassword.getText().toString().trim(),
					   firstName = etFirstName.getText().toString().trim(),
					   lastName = etLastName.getText().toString().trim();
				Boolean updatePassword = false;
					   
				if (email.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
					callAlertDialog("Missing Values", "You need to fill out all of the input boxes.");
					return;
                } else if(!isValidEmailAddress(email)){
                	callAlertDialog("Invalid Email Address", "You need to enter a valid email address.");
                	return;
                } 
				
				if(!password.isEmpty()) {
					updatePassword = true;
                	if(password.length() != password.replaceAll("\\s","").length() ||  password.length() < 6){
                		callAlertDialog("Invalid Email Address", "You need to enter a valid email address.");
                		return;
                	}
                }
				
				// Should have returned by this point
				getActivity().setProgressBarIndeterminateVisibility(true);
                
                current.setUsername(email);
                if(updatePassword) { current.setPassword(password); }
                current.setEmail(email);
                current.put("firstName", firstName);
                current.put("lastName", lastName);
                current.saveInBackground(new SaveCallback() {
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
                        	callAlertDialog("An error has occurred when trying to sign up!", e.getMessage());
                        }
                    }
                }); // end signUpInBackground
				
			}
		});
		
        // Inflate the layout for this fragment
        return rootView;
    }
	
	public void callAlertDialog(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
               .setTitle(title)
               .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
	}
	
	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
}
