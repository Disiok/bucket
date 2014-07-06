package com.sporkinnovations.bucket.activity;

import com.parse.ParseUser;
import com.sporkinnovations.bucket.R;
import com.sporkinnovations.bucket.R.id;
import com.sporkinnovations.bucket.R.layout;
import com.sporkinnovations.bucket.R.menu;
import com.sporkinnovations.bucket.controller.BucketAdapter;
import com.sporkinnovations.bucket.controller.SessionManager;
import com.sporkinnovations.bucket.controller.VoteManager;
import com.sporkinnovations.bucket.model.Bucket;
import com.sporkinnovations.bucket.model.Wish;
import com.sporkinnovations.bucket.view.BucketView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	// Views
	BucketView mBucketView;
	TextView mVotePowerView;
	
	// Temporary
	Button mLogoutButton;

	AlertDialog.Builder mNewWishDialog;

	// Adapters
	BucketAdapter mBucketAdapter;

	// Models
	Bucket mBucket;
	VoteManager mVoteManager;
	SessionManager mSessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setting main activity layout
		setContentView(R.layout.activity_main);
		
		mSessionManager = new SessionManager(this);
		mSessionManager.checkLogin();
		
		// Resolving views
		mBucketView = (BucketView) findViewById(R.id.bucket_view);
		mVotePowerView = (TextView) findViewById(R.id.bucket_vote_power_indicator);
		
		mLogoutButton = (Button) findViewById(R.id.log_out_button);

		// Initializing
		init();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_new:
			showNewWishDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void init() {
		// Creating model instances
		mBucket = new Bucket(this);
		mVoteManager = new VoteManager();
		mBucketAdapter = new BucketAdapter(this,
				android.R.layout.simple_list_item_1, mBucket.getWishes());

		setupBucketView();
		mBucket.load(this, mBucketAdapter);
		refreshVotePowerIndicator();
		
		mLogoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				logOut();
				mSessionManager.checkLogin();
			}
			
		});
	}

	private void refreshVotePowerIndicator() {
		mVotePowerView.setText("Vote Power: "
				+ (int) (mVoteManager.getVotePower() * 100) + "%");
	}

	private void showNewWishDialog() {
		// Creating instance
		mNewWishDialog = new AlertDialog.Builder(this);

		mNewWishDialog.setTitle("New Wish");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		final FrameLayout frame = new FrameLayout(this);
		frame.setPadding(50, 25, 50, 0);
		frame.addView(input);

		mNewWishDialog.setView(frame);

		mNewWishDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		mNewWishDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String message = input.getText().toString();
						Wish wish = new Wish(message);
						wish.save(getApplicationContext());
						mBucket.addWish(wish);
						mBucket.sortWishes();
						mBucketAdapter.notifyDataSetChanged();
						refreshVotePowerIndicator();
					}
				});
		mNewWishDialog.show();
	}

	private void setupBucketView() {
		final Activity activity = this;
		mBucketView.setAdapter(mBucketAdapter);
		mBucketView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Wish clickedWish = (Wish) parent.getItemAtPosition(position);
				clickedWish.addVote(mVoteManager.getVote());
				view.setAlpha(0);
				view.animate().alpha(1).setDuration(100)
						.setListener(new AnimatorListener() {

							@Override
							public void onAnimationStart(Animator animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animator animation) {
								mBucket.sortWishes();
								mBucketAdapter.notifyDataSetChanged();
								refreshVotePowerIndicator();
							}

							@Override
							public void onAnimationCancel(Animator animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animator animation) {
								// TODO Auto-generated method stub

							}

						});
			}

		});
		mBucketView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Wish clickedWish = (Wish) parent.getItemAtPosition(position);
				clickedWish.delete(activity);
				mBucket.remove(position);
				mBucketAdapter.notifyDataSetChanged();
				return true;
			}
			
		});
	}
	private void logOut() {
		ParseUser.logOut();
	}
}
