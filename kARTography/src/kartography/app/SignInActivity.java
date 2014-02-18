package kartography.app;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class SignInActivity extends Activity {

	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Parse.initialize(this, "wN6gpXkwVEF0d9eTw1YzE0ISX2WM8ACdXM0ueuiu",
				"dGycMyN2IxdihwSV6kzDiCufYAL9UBBQEpOiRmMn");
		ParseFacebookUtils.initialize(getString(R.string.app_id));

		loginButton = (Button) findViewById(R.id.btn_login);
		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			showMainActivity();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void showMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void onLogIn(View v) {
		List<String> permissions = Arrays.asList("basic_info", "user_about_me");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, com.parse.ParseException e) {
				if (user == null) {
					Log.d("FB_LOGIN","Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d("FB_LOGIN","User signed up and logged in through Facebook!");
					showMainActivity();
				} else {
					Log.d("FB_LOGIN","User logged in through Facebook!");
					showMainActivity();
				}

			}

		});
	}

}
