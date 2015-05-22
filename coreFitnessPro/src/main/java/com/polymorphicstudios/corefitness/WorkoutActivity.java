package com.polymorphicstudios.corefitness;

import java.io.IOException;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polymorphicstudios.actor.Workout;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.ImageModification;
import com.polymorphicstudios.db.DatabaseHandler;

import android.graphics.Bitmap;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkoutActivity extends SherlockActivity{
	
	 private TextView title;
	 private TextView workoutDesc;
	 private TextView workoutEquipment;
	 private TextView imageNumber;
	 
	 private RelativeLayout titleLayout;
	 private RelativeLayout lowerLayout;
	 
	 private ImageView workoutIm;
	 private int exerciseNum;
	 private Workout workout;
	 
	 private Boolean viewsVisible = true;
	 private Typeface font;
	 private Typeface fontLangdon;
	 
	 private DisplayMetrics dm;
	 private int screenWidth;
	 private int screenHeight;
	 private DatabaseHandler db;
	 private int currentImInt = 0;
	 private int numMediaImages;
	 
	 private Typeface face;
	 DisplayImageOptions options;
	
	 Handler handler = new Handler();
	    Runnable runnable = new Runnable() {
	        public void run() {
	        	  afficher();
	        }
	 };
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        setContentView(R.layout.full_parent_image);
	        
	        title = (TextView) findViewById(R.id.workout_title);
	        workoutDesc = (TextView) findViewById(R.id.workout_description);
	        workoutEquipment = (TextView) findViewById(R.id.workout_equipment);
	        imageNumber = (TextView) findViewById(R.id.image_number);
	        workoutIm = (ImageView) findViewById(R.id.workout_image);
	        titleLayout = (RelativeLayout) findViewById(R.id.handle);
		    lowerLayout = (RelativeLayout) findViewById(R.id.lower_layout);
	        
		    //set title Visible
	        title.setVisibility(View.VISIBLE);
	        
	        //get bundle from previous class
	        Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	        	exerciseNum = extras.getInt("EXERCISE_ID");
	        }
	        
	        //Set the ACTION BAR
		 	com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		 	actionBar.setDisplayHomeAsUpEnabled(true);
		 	actionBar.setLogo(R.drawable.icon_muscle_w);
		    
		    //get display metrics
	      	dm = new DisplayMetrics(); 
	      	this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
	      	screenWidth = dm.widthPixels;
	      	screenHeight = dm.heightPixels;
		    
		    //set font
		    font = Typeface.createFromAsset(getAssets(), Constants.GEARED); 
		    fontLangdon = Typeface.createFromAsset(getAssets(), Constants.LANGDON);
		    
		    db = new DatabaseHandler(this);
	        
	        try {
	        	db.createDataBase();
	        	db.openDataBase();
	 	    } catch (IOException ioe) {
	 		     throw new Error("Unable to create database");
	 	    }catch(SQLException sqle){
	 			throw sqle;
	 		}
	 
	        //get the single workout passed into the page
	        workout = db.getSingleWorkout(exerciseNum);
	        numMediaImages = workout.getmediaArray().size();
	        
	        db.close();
	        
	        try{ //add a custom font to title
	        	face = Typeface.createFromAsset(getAssets(), Constants.LANGDON); 
	        	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
	        	TextView yourTextView = (TextView) findViewById(titleId);
	        	yourTextView.setTextColor(getResources().getColor(R.color.white));
	        	yourTextView.setText(workout.getName());
	        	yourTextView.setTypeface(face);
	        }
			catch(Exception e)
			{}
	        
	        //set Title
	        title.setTypeface(fontLangdon);
	        workoutDesc.setTypeface(font);
	        workoutEquipment.setTypeface(font);
	        imageNumber.setTypeface(font);
	        
	        title.setText(workout.getName().toLowerCase());
	        workoutDesc.setText(workout.getInstruction());
	        
	        if(!workout.getEquipment().equalsIgnoreCase(" "))
	        {
	        	workoutEquipment.setText(workout.getEquipment());
	        }
	        else
	        {
	        	workoutEquipment.setText("No Equipment");
	        }
	        
	        //set workout image height and width
	        options = new DisplayImageOptions.Builder()
			.cacheInMemory(false)
			.cacheOnDisk(false)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	        
	        workoutIm.getLayoutParams().height = (int) (screenHeight);
		    workoutIm.getLayoutParams().width = (int) (screenWidth);
	        ImageLoader.getInstance().displayImage("drawable://" + Constants.selectWorkout((workout.getmediaArray().get(0))), workoutIm, options);
		       
	        
	        bindListeners();
	        
	        runnable.run();
	 }
	 
	 public void bindListeners()
	 {
		 workoutIm.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(viewsVisible)
					{
						setViewsInvisible();
						viewsVisible = false;
					}
					else
					{
						setViewsVisible();
						viewsVisible = true;
					}
				}
			});
	 }
	 
	 public void afficher()
     {
		 
		 imageNumber.setText(((currentImInt) % (numMediaImages) + 1) + " / " + numMediaImages);
		 ImageLoader.getInstance().displayImage("drawable://" +  Constants.selectWorkout((workout.getmediaArray().get(currentImInt % numMediaImages))), workoutIm, options);
		 handler.postDelayed(runnable, 3000);
         currentImInt++;
         
     }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem)
	 {       
		if(menuItem.getItemId() == android.R.id.home)
		  {
			  workoutIm.setImageDrawable(null);
			  System.gc();
			  super.onBackPressed();
		      overridePendingTransition(R.anim.translate_left_onscreen, R.anim.translate_right_offscreen);
		      finish();
		  }
		 
		   return true;
	 }
	 
	 @Override
	 public void onBackPressed()
	 {
		 workoutIm.setImageDrawable(null);
		  System.gc();
		  super.onBackPressed();
	      overridePendingTransition(R.anim.translate_left_onscreen, R.anim.translate_right_offscreen);
	      finish();
	 }
	 
	 public void setViewsVisible()
     {
    	 titleLayout.setVisibility(View.VISIBLE);
    	 lowerLayout.setVisibility(View.VISIBLE);
     }
     
     public void setViewsInvisible()
     {
    	 titleLayout.setVisibility(View.GONE);
    	 lowerLayout.setVisibility(View.GONE);
     }
     
     @Override
 	public void onDestroy() {
 		workoutIm.setImageDrawable(null);
 		handler.removeCallbacks(runnable);
 		db.close();
 	    super.onDestroy();
 	 }
   


}
