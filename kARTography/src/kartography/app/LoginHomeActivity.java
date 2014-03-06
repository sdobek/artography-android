package kartography.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseUser;

public class LoginHomeActivity extends FragmentActivity {
	
	ImageView bg;
	String username;
	String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_home);
		Parse.initialize(this, "wN6gpXkwVEF0d9eTw1YzE0ISX2WM8ACdXM0ueuiu",
				"dGycMyN2IxdihwSV6kzDiCufYAL9UBBQEpOiRmMn");
		if (ParseUser.getCurrentUser() != null) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
		}
		
	}
	
	//Use DialogFragments for Create Account and Login 
	public void onCreateAccount(View v){
		FragmentManager fm = getSupportFragmentManager();
		CreateAccount createDialog = CreateAccount.newInstance("New User");
		createDialog.show(fm, "fragment_new_user");
	}
	
	public void onLoginAccount(View v){
		FragmentManager fm = getSupportFragmentManager();
		LoginAccount loginDialog = LoginAccount.newInstance("Enter Username and Password");
		loginDialog.show(fm, "fragment_login");
	}

	
	
	
	
	
	
}
