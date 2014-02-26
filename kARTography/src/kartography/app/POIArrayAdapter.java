package kartography.app;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kartography.models.Poi;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

public class POIArrayAdapter extends ArrayAdapter<Poi> {

    public POIArrayAdapter(Context context, List<Poi> images) {
        super(context, R.layout.item_list_poi, images);
       
        if(images != null){
//        	Log.d("REBUG", images.toString());	
        }else{
        	Log.d("DEBUG", "ljkfgkljdfgkhj");
        }
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Poi imageInfo = this.getItem(position);


        View itemView;
        if(convertView == null){
            LayoutInflater inflator = LayoutInflater.from(getContext());
            itemView = inflator.inflate(R.layout.item_list_poi, parent,  false);
        } else {
            itemView = convertView;
        }
        
        
        ImageView ivImage = (ImageView) itemView.findViewById(R.id.ivThumbnail);
        TextView tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        TextView tvArtist = (TextView) itemView.findViewById(R.id.tvAddress);
        TextView tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
        tvTitle.setMaxLines(1);
        //probably won't need to truncate right away. Keeping line here for quick access.
        //tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setText(imageInfo.getTitle());
        tvArtist.setText(imageInfo.getArtist());
        
        
        double lat = imageInfo.getLocation().getLatitude();
		double longitude = imageInfo.getLocation().getLongitude();
		String address = getAddress(lat, longitude);
		tvDistance.setText(address);
        
        // This will need to be altered when we get ParseGeoLocations into our DB. 
//        tvDistance.setText("0.5 mi");


//        ivImage.setImageResource(R.drawable.ican);
        		
		
        String pf = imageInfo.getPhotoFileScaled().getUrl();
        Picasso.with(getContext()).load(Uri.parse(pf)).into(ivImage);
      //formerly .resize(100,100)
//		.noFade()
//		.centerCrop()
        
        //hey steven, I managed to make this doable with picasso as seen above. 
        /*ParseFile pf = imageInfo.getPhotoFileScaled();
        byte[] byteArray;
		try {
			byteArray = pf.getData();
			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			
			ivImage.setImageBitmap(bmp);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
        
//        ivImage.setImageResource(R.drawable.ican);
//
//        Picasso.with(getContext())
//                        .load(Uri.parse(imageInfo.getArtPhotoUrl()))
//                        .into(ivImage);

        				
        
        return itemView;
    }
    
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
//                result.append(address.getThoroughfare()).append("\n");
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
}