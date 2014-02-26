package kartography.app;



import kartography.models.Poi;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EditDetailsActivity extends Activity {	
	private ProgressBar pb;
	private Button submitBtn;
	private EditText author;
	private EditText title;
	private EditText description;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);
		
		pb = (ProgressBar) findViewById(R.id.pbLoading);
		submitBtn = (Button) findViewById(R.id.btn_submitPhoto);
		submitBtn.setEnabled(true);
		
		ImageView photoView = (ImageView) findViewById(R.id.iv_Photo);
		photoView.setClickable(false);
		String photoUrl = getIntent().getStringExtra("photoUrl");
		Picasso.with(getBaseContext()).load(Uri.parse(photoUrl)).into(photoView);
		author = (EditText)findViewById(R.id.et_author);
		author.setText(getIntent().getStringExtra("artist"));
		title = (EditText)findViewById(R.id.et_title);
		title.setText(getIntent().getStringExtra("title"));
		description = (EditText)findViewById(R.id.et_description);
		description.setText(getIntent().getStringExtra("description"));		
		ActionBar actionb = getActionBar();
		actionb.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photo, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
            // app icon in action bar clicked; go home
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}
	public void onSavePOI(View v){
		submitBtn.setEnabled(false);
		
		String author = ((EditText)findViewById(R.id.et_author)).getText().toString();
		String title = ((EditText)findViewById(R.id.et_title)).getText().toString();
		String description = ((EditText)findViewById(R.id.et_description)).getText().toString();

		// guards against empty fields. 
		if(title.isEmpty()){
			title = "Unknown";
		}
		
		if(author.isEmpty()){
			author = "Unknown";
		}
		
		Intent poi_data = new Intent();
		poi_data.putExtra("artist", author);
		poi_data.putExtra("title", title);
		poi_data.putExtra("description", description);
		
		pb.setVisibility(ProgressBar.VISIBLE);
		setResult(RESULT_OK, poi_data); // set result code and bundle data for response
		finish();
	}
	
	
	
	

}
