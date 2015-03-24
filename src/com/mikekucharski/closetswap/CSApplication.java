package com.mikekucharski.closetswap;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class CSApplication extends Application {
	private String APPLICATION_ID = "ig4vF25JcTy0zGwGc059F0KTchQdcc3xsoqJnMXo";
	private String CLIENT_KEY = "kRxn9lkfYVtF3y27kKsQ9Kw9wNOwKH12bBWQvU4R";
	@Override
	public void onCreate() {
	    super.onCreate();
	 
	    Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
	}
}
