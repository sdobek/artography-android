package kartography.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginAccount extends DialogFragment {
	
	public static LoginAccount newInstance(String title) {
		LoginAccount ca = new LoginAccount();
		Bundle args = new Bundle();
		args.putString("title", title);
		ca.setArguments(args);
		return ca;
	}

	private EditText username;
	private EditText password;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container);
		username = (EditText) view.findViewById(R.id.etCreateUsername);
		password = (EditText) view.findViewById(R.id.etEnterPassword);
		
		getDialog().requestWindowFeature(STYLE_NO_TITLE);
		Button confirm = (Button) view.findViewById(R.id.btnLoginAccount);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseUser.logInInBackground(username.getText().toString(),
						password.getText().toString(), new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									// Hooray! The user is logged in.
									Intent i = new Intent(getActivity(),
											MainActivity.class);
									startActivity(i);
									getActivity().finish();
								} else {
									// Signup failed. Look at the ParseException
									// to see
									// what happened.
									Toast.makeText(getActivity(),
											"Invalid username or password",
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}
						});
			}
		});
		Button cancel = (Button) view.findViewById(R.id.btnLoginCancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginAccount.this.dismiss();
			}
		});

		return view;
	}
}
