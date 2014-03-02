package kartography.app;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class LoginHomeActivity extends Activity {
	
	ImageView bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_home);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_home, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlLoginScreen);
		bg = (ImageView) findViewById(R.id.ivLoginBG);
		BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_signin);
		Bitmap new_bg = Bitmap.createScaledBitmap(bd.getBitmap(), rl.getWidth(), rl.getHeight(), false);
		bg.setImageBitmap(new_bg);
		bg.setAlpha(75);
		
    }
	
}
