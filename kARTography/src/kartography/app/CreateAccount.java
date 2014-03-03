package kartography.app;

import java.util.List;

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

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccount extends DialogFragment {
	String un, em, pw, re_pw;
	EditText username;
	EditText password;
	EditText re_password;
	EditText email;

	public static CreateAccount newInstance(String title) {
		CreateAccount ca = new CreateAccount();
		Bundle args = new Bundle();
		args.putString("title", title);
		ca.setArguments(args);
		return ca;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_user, container);
		username = (EditText) view.findViewById(R.id.etCreateUsername);
		email = (EditText) view.findViewById(R.id.etEnterEmail);
		password = (EditText) view.findViewById(R.id.etEnterPassword);
		re_password = (EditText) view.findViewById(R.id.etReEnterPassword);

		getDialog().setTitle("New User");
		Button confirm = (Button) view.findViewById(R.id.btnLoginAccount);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				un = username.getText().toString();
				em = email.getText().toString();
				pw = password.getText().toString();
				re_pw = re_password.getText().toString();

				if (un.equals("")) {
					Toast.makeText(getActivity(), "Enter Username",
							Toast.LENGTH_LONG).show();
				} else if (em.equals("")) {
					Toast.makeText(getActivity(), "Enter Email",
							Toast.LENGTH_LONG).show();
				} else if (pw.equals("") || re_pw.equals("")) {
					Toast.makeText(getActivity(), "Enter and Reneter Password",
							Toast.LENGTH_LONG).show();
				} else if (!pw.equals(re_pw)) {
					Toast.makeText(getActivity(), "Passwords don't match",
							Toast.LENGTH_LONG).show();
				} else {
					ParseQuery<ParseUser> query = ParseUser.getQuery();
					query.whereEqualTo("username", un);
					query.findInBackground(new FindCallback<ParseUser>() {
						public void done(List<ParseUser> users, ParseException e) {
							if (e == null) {
								if (users.size() != 0) {
									Toast.makeText(
											getActivity(),
											"Username already taken.  Choose another",
											Toast.LENGTH_LONG).show();
								} else {
									ParseUser newUser = new ParseUser();
									newUser.setUsername(un);
									newUser.setEmail(em);
									newUser.setPassword(pw);
									newUser.signUpInBackground(new SignUpCallback() {
										public void done(ParseException e) {
											if (e == null) {
												ParseUser.logInInBackground(un,
														pw,
														new LogInCallback() {
															public void done(
																	ParseUser user,
																	ParseException e) {
																if (user != null) {
																	Intent i = new Intent(
																			getActivity(),
																			MainActivity.class);
																	startActivity(i);
																	getActivity()
																			.finish();
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
							} else {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		Button cancel = (Button) view.findViewById(R.id.btnCreateCancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreateAccount.this.dismiss();
			}
		});

		return view;
	}
}
