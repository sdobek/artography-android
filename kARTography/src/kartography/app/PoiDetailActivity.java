package kartography.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kartography.app.ConfirmFlag.ConfirmFlagListener;
import kartography.models.Poi;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PoiDetailActivity extends FragmentActivity implements ConfirmFlagListener{
	private final int UPDATE_POI = 24;

	String objectId;
	ImageView ivImage;
	ImageView ivFavorited;
	TextView tvTitle;
	TextView tvArtist;
	TextView tvLocation;
	TextView tvDate;
	TextView tvDescription;
	TextView tvUser;
	
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
		ivFavorited = (ImageView) findViewById(R.id.ivFavoritedDetail);
		tvTitle = (TextView) findViewById(R.id.tvPhotoTitle);
		tvUser = (TextView) findViewById(R.id.tvUploaderHeader);
		tvArtist = (TextView) findViewById(R.id.tvArtist);
		tvDate = (TextView) findViewById(R.id.tvDateText);
		tvDescription = (TextView) findViewById(R.id.tvDescriptionText);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		
		
		ActionBar actionb = getActionBar();
		actionb.setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		objectId = (String) i.getStringExtra("id");
		ParseQuery<Poi> query = ParseQuery.getQuery(Poi.class).whereEqualTo(
				"objectId", objectId);
		query.findInBackground(new FindCallback<Poi>() {

			@SuppressLint("NewApi")
			public void done(List<Poi> itemList, ParseException e) {
				if (e == null) {
					poi = itemList.get(0);
					if (poi.getFavorited()){
						ivFavorited.setImageResource(R.drawable.ic_fav_selected);
					}
					else {
						ivFavorited.setImageResource(R.drawable.ic_fav_unselected);
					}
					if (poi.getTitle() != "") {
						tvTitle.setText(poi.getTitle());
					} else {
						tvTitle.setText("Untitiled");
					}
					if (poi.getArtist() != "") {
						tvArtist.setText("Artist - " + poi.getArtist());
					} else {
						tvArtist.setText("Artist - Unknown");
					}
					tvDescription.setText(poi.getDescription());
					tvUser.setText(poi.getUploadedByUser());
					double lat = poi.getLocation().getLatitude();
					double longitude = poi.getLocation().getLongitude();
					String address = getAddress(lat, longitude);
					tvLocation.setText(address);
					SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
					// give null right now :(
					Date date = (Date) poi.getCreatedAt();
					
					tvDate.setText("Uploaded on "+sdf.format(date));
					String pfUrl = poi.getPhotoFile().getUrl();

					pfs = poi.getPhotoFileScaled().getUrl();
					Picasso.with(getBaseContext()).load(Uri.parse(pfUrl)).fit()
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
            // app icon in action bar clicked; go home
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}
	
	public void onEditDetails(MenuItem mi) {
		Intent i = new Intent(this, EditDetailsActivity.class);
		i.putExtra("artist", poi.getArtist());
		i.putExtra("title", poi.getTitle());
		i.putExtra("description", poi.getDescription());
		i.putExtra("photoUrl", pfs);
		startActivityForResult(i, UPDATE_POI);
	}
	public void onShareDetails(MenuItem mi) throws IOException {
//		Intent sendIntent = new Intent();
//		sendIntent.setAction(Intent.ACTION_SEND);
//		sendIntent.putExtra(Intent.EXTRA_TEXT, poi.getPhotoFile().getUrl());
////		sendIntent.setType("text/plain");
//		startActivity(sendIntent);
		URL url = new URL(poi.getPhotoFile().getUrl());
		
		Bitmap icon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/jpeg");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
		try {
		    f.createNewFile();
		    FileOutputStream fo = new FileOutputStream(f);
		    fo.write(bytes.toByteArray());
		} catch (IOException e) {                       
		        e.printStackTrace();
		}
		share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
		startActivity(Intent.createChooser(share, "Share Image"));
	}

	public void onFlagPoi(MenuItem mi) {
		FragmentManager fm = getSupportFragmentManager();
		ConfirmFlag confirmFlagDialog = ConfirmFlag.newInstance("Some Title");
		confirmFlagDialog.show(fm, "fragment_edit_name");
	}
	
	public void onLogout(MenuItem mi) {
		ParseUser.getCurrentUser().logOut();
		Intent i = new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		i.putExtra("finish", true);
		startActivity(i);
		finish();

//		Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
		
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
	
	private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getThoroughfare()).append("\n");
//                result.append(address.getFeatureName()).append("\n");
                result.append(address.getLocality());
//                .append("\n");
//                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
	
	public void onSetFavorited(View v){
		
		if (poi.getFavorited()){
			ivFavorited.setImageResource(R.drawable.ic_fav_unselected);
			poi.setFavorited(false);
		}
		else {
			Animation animUp = AnimationUtils.loadAnimation(this, R.anim.favorite_scale_up);
			ivFavorited.startAnimation(animUp);
			animUp.setAnimationListener(new AnimationListener() {
			    @Override
			    public void onAnimationStart(Animation animation) {
			        // Fires when animation starts
			    }

			    @Override
			    public void onAnimationEnd(Animation animation) {
			    	ivFavorited.setImageResource(R.drawable.ic_fav_selected);
			    	Animation animDown = AnimationUtils.
			    							loadAnimation(PoiDetailActivity.this, R.anim.favorite_scale_down);
					ivFavorited.startAnimation(animDown);
			    }

			    @Override
			    public void onAnimationRepeat(Animation animation) {
			       // ...			
			    }
			});
			
			poi.setFavorited(true);
		}
		poi.saveInBackground();
		
	}
	
	public void onFlagSuccess(){
		poi.setFlagged(true);
		poi.saveInBackground();
		this.finish();
	}

}
