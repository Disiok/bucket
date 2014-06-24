package com.sporkinnovations.bucket;

import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Application;

public class BucketApplication extends Application {
	
	public BucketApplication() {
		super();
		Parse.enableLocalDatastore(this);
	}
	public void onCreate() {
		super.onCreate();
		
		// Initialize Parse
		Parse.initialize(this, Constants.APPLICATION_ID, Constants.CLIENT_KEY);
		ParseUser.enableAutomaticUser();
		
	}
}
