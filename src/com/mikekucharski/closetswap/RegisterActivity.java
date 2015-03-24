package com.mikekucharski.closetswap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity {
	
	private EditText etEmail, etPassword, etFirstName, etLastName;
	private Button bSignup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setTitle("Register");
		setContentView(R.layout.activity_register);
		
		etEmail = (EditText) this.findViewById(R.id.etEmail);
		etPassword = (EditText) this.findViewById(R.id.etPassword);
		etFirstName = (EditText) this.findViewById(R.id.etFirstName);
		etLastName = (EditText) this.findViewById(R.id.etLastName);
		bSignup = (Button) this.findViewById(R.id.bSignup);
		
		bSignup.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                
                if (email.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("You need to fill out all of the input boxes.")
                           .setTitle("Missing Values")
                           .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(!isValidEmailAddress(email)){
                	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("You need to enter a valid email address.")
                           .setTitle("Invalid Email Address")
                           .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if(password.length() != password.replaceAll("\\s","").length() ||  password.length() < 6) {
                	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Your password must be at least 6 characters long and not contain white spaces.")
                           .setTitle("Invalid Password")
                           .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    setProgressBarIndeterminateVisibility(true);
                    
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(email);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
 
                            if (e == null) {
                                // Success!
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(e.getMessage())
                                    .setTitle("An error has occurred when trying to sign up!")
                                    .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }); // end signUpInBackground
                }// end else
			} // end onClick
		}); // end onClickListener
	} // end onCreate
	
	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
}
