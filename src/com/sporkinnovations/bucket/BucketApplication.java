package com.sporkinnovations.bucket;

import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Application;

public class BucketApplication extends Application {
	public void onCreate() {
		super.onCreate();
		
		// Initialize Parse
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, Constants.APPLICATION_ID, Constants.CLIENT_KEY);
		ParseUser.enableAutomaticUser();
	}
}
