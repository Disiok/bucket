package com.sporkinnovations.bucket;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	ActionBar mActionBar;
	BucketView mBucketView;
	BucketAdapter mBucketAdapter;
	
	Bucket mBucket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBucketView = (BucketView) findViewById(R.id.bucket_view);
		
		init();
		
	}
	protected void init() {
		mBucket = new Bucket();
		
		mBucketAdapter = new BucketAdapter(this,android.R.layout.simple_list_item_1, mBucket.getWishes());
		
		mBucketView.setAdapter(mBucketAdapter);
		mBucketView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setAlpha(0);
				view.animate().alpha(1);
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
}
