package kartography.app;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ConfirmFlag extends DialogFragment {
	
	public static interface ConfirmFlagListener {
		public void onFlagSuccess();
	}
	
	public static ConfirmFlag newInstance(String title){
		ConfirmFlag cf = new ConfirmFlag();
		Bundle args = new Bundle();
		args.putString("title", title);
		cf.setArguments(args);
		return cf;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.confirm_flag, container);
		getDialog().setTitle("Flag as False");
		Button confirm = (Button) view.findViewById(R.id.btnConfirm);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConfirmFlagListener listener = (ConfirmFlagListener) getActivity();
				listener.onFlagSuccess();
				ConfirmFlag.this.dismiss();	
			}
		});
		Button cancel = (Button) view.findViewById(R.id.btnCancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConfirmFlag.this.dismiss();	
			}
		});
		
		
		return view;
	}

}
