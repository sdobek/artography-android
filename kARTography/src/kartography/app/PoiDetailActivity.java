package kartography.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kartography.models.Poi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

public class PoiDetailActivity extends Activity {
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_detail);
		
		//initialize
		ivImage = (ImageView) findViewById(R.id.ivArt);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvArtist  = (TextView) findViewById(R.id.tvArtist);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
//		 = (TextView) findViewById(R.id);
//		 = (TextView) findViewById(R.id);
		//
		
		Intent i = getIntent();
		objectId = (String) i.getStringExtra("id");
		List<Poi> ohHaiPoi = null;
		ParseQuery<Poi> query = ParseQuery.getQuery(Poi.class).whereEqualTo("objectId", objectId);
		query.findInBackground(new FindCallback<Poi>() {
		    public void done(List<Poi> itemList, ParseException e) {
		        if (e == null) {
		           Poi poi = itemList.get(0);
		           tvTitle.setText(poi.getTitle());
		           tvArtist.setText(poi.getArtist());
		           tvDescription.setText(poi.getDescription());
		           SimpleDateFormat sdf = new SimpleDateFormat();
		           //give null right now :(
//		           tvDate.setText(sdf.format(poi.getCreatedAt()));
		           String pf = poi.getPhotoFileScaled().getUrl();
		           Picasso.with(getBaseContext()).load(Uri.parse(pf)).into(ivImage);
//		           Toast.makeText(PoiDetailActivity.this, poi.getArtist(), Toast.LENGTH_LONG).show();
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
		return true;
	}

}
