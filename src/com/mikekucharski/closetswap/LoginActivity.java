package com.mikekucharski.closetswap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	private EditText etEmail, etPassword;
	private Button bLogin, bGoRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().setTitle("Login");
		
		etEmail = (EditText) this.findViewById(R.id.etEmail);
		etPassword = (EditText) this.findViewById(R.id.etPassword);
		bLogin = (Button) this.findViewById(R.id.bLogin);
		bGoRegister = (Button) this.findViewById(R.id.bGoRegister);
		
		bGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
		
		bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                
                if (email.isEmpty() || password.isEmpty()) {
                	AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("You need to fill out all of the input boxes.")
                           .setTitle("Missing Values")
                           .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                	setProgressBarIndeterminateVisibility(true);
                	 
                    ParseUser.logInInBackground(email, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
 
                            if (e == null) {
                                // Success!
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                // Fail
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(e.getMessage())
                                    .setTitle("Log in attempt failed")
                                    .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
