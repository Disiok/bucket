package com.sporkinnovations.bucket.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.sporkinnovations.bucket.R;
import com.sporkinnovations.bucket.R.id;
import com.sporkinnovations.bucket.R.layout;
import com.sporkinnovations.bucket.R.menu;
import com.sporkinnovations.bucket.R.string;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.sporkinnovations.bucket.extra.EMAIL";

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
						if (error.getCode() == ParseException.OBJECT_NOT_FOUND) {
							attemptSignUp();
						}
						showProgress(false);
					}
					else {
						toast("What the firetruck");
						showProgress(false);
					}
					
				}
			});
//			mPasswordView.setError(getString(R.string.error_incorrect_password));
//			mPasswordView.requestFocus();
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
					Log.d("Login", "Facebook login error: " + (error == null ? "null" : error.toString()));
					showProgress(false);
				}
			}
		});
	}
	
	private void attemptSignUp() {
		ParseUser user = new ParseUser();
		user.setUsername(mEmail);
		user.setPassword(mPassword);
		user.setEmail(mEmail);
		
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException error) {
		    if (error == null) {
		      finishLogin();
		    } else {
		    	toast(error.toString());
		    }
		  }
		});
		
	}

	private void finishLogin() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
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
		
		if (show) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	public void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
