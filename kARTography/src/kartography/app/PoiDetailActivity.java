package kartography.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kartography.app.ConfirmFlag.ConfirmFlagListener;
import kartography.models.Poi;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

public class PoiDetailActivity extends FragmentActivity implements ConfirmFlagListener{
	private final int UPDATE_POI = 24;

	String objectId;
	ImageView ivImage;
	TextView tvTitle;
	TextView tvArtist;
	TextView tvLocation;
	TextView tvDate;
	TextView tvDescription;
	Date dateUploaded;
	String title;
	String artist;
	String distance;
	String description;
	String uploader;
	private String pfs;
	private Poi poi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_detail);

		// initialize
		ivImage = (ImageView) findViewById(R.id.ivArt);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvArtist = (TextView) findViewById(R.id.tvArtist);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvDescription = (TextView) findViewById(R.id.tvDescription);

		Intent i = getIntent();
		objectId = (String) i.getStringExtra("id");
		ParseQuery<Poi> query = ParseQuery.getQuery(Poi.class).whereEqualTo(
				"objectId", objectId);
		query.findInBackground(new FindCallback<Poi>() {

			@SuppressLint("NewApi")
			public void done(List<Poi> itemList, ParseException e) {
				if (e == null) {
					poi = itemList.get(0);
					tvTitle.setText(poi.getTitle());
					ActionBar ab = getActionBar();
					if (poi.getTitle() != "") {
						ab.setTitle(poi.getTitle());
					} else {
						ab.setTitle("Title Unknown");
					}
					tvArtist.setText(poi.getArtist());
					tvDescription.setText(poi.getDescription());
					SimpleDateFormat sdf = new SimpleDateFormat();
					// give null right now :(
					// tvDate.setText(sdf.format(poi.getCreatedAt()));
					String pf = poi.getPhotoFile().getUrl(); // I dont think it
																// should be
																// scaled
					pfs = poi.getPhotoFileScaled().getUrl();
					Picasso.with(getBaseContext()).load(Uri.parse(pf))
							.into(ivImage);
					// Toast.makeText(PoiDetailActivity.this, poi.getArtist(),
					// Toast.LENGTH_LONG).show();
					uploader = poi.getUploadedByUser();
				} else {
					Log.d("item", "Error: " + e.getMessage());
					Log.d("DEBUG", "Oh noooooooooooooooooooooooo");
				}
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poi_detail, menu);
		MenuItem item = menu.findItem(R.id.edit_detailed);
		item.setVisible(true);
		return true;
	}

	public void onEditDetails(MenuItem mi) {
		Intent i = new Intent(this, EditDetailsActivity.class);
		i.putExtra("artist", poi.getArtist());
		i.putExtra("title", poi.getTitle());
		i.putExtra("description", poi.getDescription());
		i.putExtra("photoUrl", pfs);
		startActivityForResult(i, UPDATE_POI);
	}

	public void onFlagPoi(MenuItem mi) {
		FragmentManager fm = getSupportFragmentManager();
		ConfirmFlag confirmFlagDialog = ConfirmFlag.newInstance("Some Title");
		confirmFlagDialog.show(fm, "fragment_edit_name");
	}
	
	public void onShowFullscreen(View v){
		Intent i = new Intent(this, DisplayDetailedPhotoActivity.class);
		i.putExtra("photoUrl", poi.getPhotoFile().getUrl());
		startActivity(i);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == UPDATE_POI) {

			poi.put("title", data.getStringExtra("title"));
			poi.put("artist", data.getStringExtra("artist"));
			poi.put("description", data.getStringExtra("description"));
			poi.saveInBackground();

			tvTitle.setText(poi.getTitle());
			ActionBar ab = getActionBar();
			if (poi.getTitle() != "") {
				ab.setTitle(poi.getTitle());
			} else {
				ab.setTitle("Title Unknown");
			}
			tvArtist.setText(poi.getArtist());
			tvDescription.setText(poi.getDescription());

		}
	}
	
	public void onFlagSuccess(){
		poi.setFlagged(true);
		poi.saveInBackground();
		this.finish();
	}

}
