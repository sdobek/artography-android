package kartography.app;

import kartography.models.PoiLocation;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.parse.Parse;
import com.parse.ParseAnalytics;

public class SetPOILocationActivity extends Activity {
	PoiLocation loc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 Parse.initialize(this, "wN6gpXkwVEF0d9eTw1YzE0ISX2WM8ACdXM0ueuiu", "dGycMyN2IxdihwSV6kzDiCufYAL9UBBQEpOiRmMn");
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
