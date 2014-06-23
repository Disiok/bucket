package com.sporkinnovations.bucket;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.ActionBar;
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

public class MainActivity extends Activity {

	ActionBar mActionBar;
	BucketView mBucketView;
	BucketAdapter mBucketAdapter;

	Bucket mBucket;
	VoteManager mVoteManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBucketView = (BucketView) findViewById(R.id.bucket_view);

		init();

	}

	protected void init() {
		mBucket = new Bucket();
		mVoteManager = new VoteManager();

		mBucketAdapter = new BucketAdapter(this,
				android.R.layout.simple_list_item_1, mBucket.getWishes());

		mBucketView.setAdapter(mBucketAdapter);
		mBucketView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Wish clickedWish = (Wish) parent.getItemAtPosition(position);
				clickedWish.addVote(mVoteManager.getVote());
				view.setAlpha(0);
				view.animate().alpha(1)
						.setListener(new AnimatorListener() {

							@Override
							public void onAnimationStart(Animator animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animator animation) {
								mBucketAdapter.notifyDataSetChanged();

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
			newWish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void newWish() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("New Wish");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String message = input.getText().toString();
				Wish wish = new Wish(message);
				mBucket.addWish(wish);
				mBucketAdapter.notifyDataSetChanged();
			}
		});

		alert.show();
	}
}
