package kartography.app;



import kartography.models.Poi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

public class EditDetailsActivity extends Activity {	
	private ProgressBar pb;
	private Button submitBtn;
	private EditText author;
	private EditText title;
	private EditText description;

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photo, menu);
		return true;
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
