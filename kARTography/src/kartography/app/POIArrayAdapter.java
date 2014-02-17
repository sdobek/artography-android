package kartography.app;

import java.util.List;

import kartography.models.Poi;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PoiArrayAdapter extends ArrayAdapter<Poi> {

    public PoiArrayAdapter(Context context, List<Poi> images) {
        super(context, R.layout.item_list_poi, images);
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
        TextView tvArtist = (TextView) itemView.findViewById(R.id.tvArtist);
        TextView tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
        tvTitle.setMaxLines(1);
        //probably won't need to truncate right away. Keeping line here for quick access.
        //tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setText(imageInfo.getTitle());
        tvArtist.setText(imageInfo.getArtist());
        
        
        // This will need to be altered when we 
        tvDistance.setText("0.5 mi");

        ivImage.setImageResource(R.drawable.ican);

        Picasso.with(getContext())
                        .load(Uri.parse(imageInfo.getArtPhotoUrl()))
                        .noFade()
                        .centerCrop()
                        .into(ivImage);
        				//formerly .resize(100,100)
        
        return itemView;
    }
}