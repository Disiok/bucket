package com.sporkinnovations.bucket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.sporkinnovations.bucket.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private Button mFacebookLoginButton;
	private Button mLoginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		// Set up the login buttons
		mLoginButton = (Button) findViewById(R.id.sign_in_button);
		mLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		
		mFacebookLoginButton = (Button) findViewById(R.id.facebook_login);
		mFacebookLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptFacebookLogin();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	private void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			
			ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException error) {
					if (error == null && user != null) {
						finishLogin();
						toast("Successful Login");
					}
					else if (error != null) {
						toast("What the noob");
					}
					else {
						toast("What the firetruck");
					}
				}
			});
//			mAuthTask = new UserLoginTask();
//			mAuthTask.execute((Void) null);
		}
	}

	private void attemptFacebookLogin() {
		mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
		showProgress(true);
		
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException error) {
				if (user != null && user.getObjectId() != null) {
					Log.d("Login", "Successful Facebook registration/login");
					finishLogin();
				} else {
					Log.d("Login", "Facebook login error: " + (error == null ? "" : error.toString()));
				}
			}
		});
	}

	private void finishLogin() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	private void showProgress(final boolean show) {
		int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

		mLoginStatusView.setVisibility(View.VISIBLE);
		mLoginStatusView.animate()
		.setDuration(shortAnimTime)
		.alpha(show ? 1 : 0)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			}
		});

		mLoginFormView.setVisibility(View.VISIBLE);
		mLoginFormView.animate()
		.setDuration(shortAnimTime)
		.alpha(show ? 0 : 1)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			}
		});
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
	
			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}
	
			// TODO: check for account matches
	
			// TODO: register the new account here.
			return true;
		}
	
		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);
	
			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}
	
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	
	}
	
	public void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();;
	}
}
