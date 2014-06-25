package com.sporkinnovations.bucket;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	// Views
	BucketView mBucketView;
	TextView mVotePowerView;

	AlertDialog.Builder mNewWishDialog;

	// Adapters
	BucketAdapter mBucketAdapter;

	// Models
	Bucket mBucket;
	VoteManager mVoteManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setting main activity layout
		setContentView(R.layout.activity_main);

		// Resolving views
		mBucketView = (BucketView) findViewById(R.id.bucket_view);
		mVotePowerView = (TextView) findViewById(R.id.bucket_vote_power_indicator);

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
	}
}
