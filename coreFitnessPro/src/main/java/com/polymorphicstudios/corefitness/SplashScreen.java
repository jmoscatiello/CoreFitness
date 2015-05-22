package com.polymorphicstudios.corefitness;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polymorphicstudios.common.ImageModification;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.graphics.Bitmap;

public class SplashScreen extends Activity {

	private final Handler splashHandler = new Handler();
	private ImageView appLogo;
	
	private DisplayMetrics dm;
	private int screenWidth;
	private int screenHeight;
	
	DisplayImageOptions options;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
     	setContentView(R.layout.layout_splash_screen);
     	
     	//tie the image to the layout id
     	appLogo = (ImageView) findViewById(R.id.splashImage);
     	
     	//get display metrics
      	dm = new DisplayMetrics(); 
      	this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
      	screenWidth = dm.widthPixels;
      	screenHeight = dm.heightPixels;
      	
        appLogo.getLayoutParams().height = (int) (screenHeight);
		appLogo.getLayoutParams().width = (int) (screenWidth);
		
		options = new DisplayImageOptions.Builder()
			.cacheInMemory(false)
			.cacheOnDisk(false)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
      	ImageLoader.getInstance().displayImage("drawable://" + R.drawable.icon_muscle_w, appLogo, options);
	        
      	
      	
      	this.splashHandler.postDelayed(waitRunnable, 3000);
	}
	
	protected Runnable waitRunnable = new Runnable() {
		public void run() {
			
			startActivity(new Intent(SplashScreen.this, HomeActivity.class));
			finish();
		}
	};
    
}
