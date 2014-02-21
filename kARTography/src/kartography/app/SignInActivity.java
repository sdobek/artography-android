package kartography.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignInActivity extends Activity {
	private EditText username;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		Parse.initialize(this, "wN6gpXkwVEF0d9eTw1YzE0ISX2WM8ACdXM0ueuiu",
				"dGycMyN2IxdihwSV6kzDiCufYAL9UBBQEpOiRmMn");
		if (ParseUser.getCurrentUser() != null) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		}
		username = (EditText) findViewById(R.id.etUsername);
		password = (EditText) findViewById(R.id.etPassword);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}

	public void onSignIn(View v) {
		ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(),
				new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							// Hooray! The user is logged in.
							Intent i = new Intent(SignInActivity.this, MainActivity.class);
							startActivity(i);
						} else {
							// Signup failed. Look at the ParseException to see
							// what happened.
							e.printStackTrace();
						}
					}
				});
	}

	public void onCreateAccount(View v) {
		Intent i = new Intent(this, NewUserActivity.class);
		startActivity(i);
	}

}
