package kartography.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class NewUserActivity extends Activity {
	String un, em, pw, re_pw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_user, menu);
		return true;
	}

	public void onCreateUser(View v) {
		EditText username = (EditText) findViewById(R.id.etCreateUsername);
		un = username.getText().toString();
		EditText email = (EditText) findViewById(R.id.etEnterEmail);
		em = email.getText().toString();
		EditText password = (EditText) findViewById(R.id.etEnterPassword);
		pw = password.getText().toString();
		EditText re_enterpw = (EditText) findViewById(R.id.etReEnterPassword);
		re_pw = re_enterpw.getText().toString();
		if (un.equals("")) {
			Toast.makeText(this, "Enter Username", Toast.LENGTH_LONG).show();
		} else if (em.equals("")) {
			Toast.makeText(this, "Enter Email", Toast.LENGTH_LONG).show();
		} else if (pw.equals("") || re_pw.equals("")) {
			Toast.makeText(this, "Enter and Reneter Password",
					Toast.LENGTH_LONG).show();
		} else if (!pw.equals(re_pw)) {
			Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(this, "ALL GOOD", Toast.LENGTH_LONG).show();
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("username", un);
			query.findInBackground(new FindCallback<ParseUser>() {
				public void done(List<ParseUser> users, ParseException e) {
					if (e == null) {
						if (users.size() != 0) {
							Toast.makeText(NewUserActivity.this,
									"Username already taken.  CHoose another",
									Toast.LENGTH_LONG).show();
						}
						else {
							createUser();
						}
					} else {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void createUser() {
		ParseUser newUser = new ParseUser();
		newUser.setUsername(un);
		newUser.setEmail(em);
		newUser.setPassword(pw);
		newUser.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					ParseUser.logInInBackground(un, pw, new LogInCallback() {
						public void done(ParseUser user, ParseException e) {
							if (user != null) {
								Intent i = new Intent(NewUserActivity.this,
										MainActivity.class);
								startActivity(i);
							} else {
								e.printStackTrace();
							}
						}
					});
				} else {
					e.printStackTrace();
				}
			}
		});

	}

}
