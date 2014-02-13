package kartography.app;

import kartography.models.Location;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class SetPOILocationActivity extends Activity {
	Location loc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_poilocation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_poilocation, menu);
		return true;
	}
	
	public void onSetLocation(View v){
		//Return location data using intent
	}

}
