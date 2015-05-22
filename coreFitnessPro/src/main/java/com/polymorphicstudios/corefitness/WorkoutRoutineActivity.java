package com.polymorphicstudios.corefitness;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import java.io.IOException;
import java.util.ArrayList;

import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polymorphicstudios.actor.Workout;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.CustomDialog;
import com.polymorphicstudios.common.ImageModification;
import com.polymorphicstudios.db.DatabaseHandler;
import com.polymorphicstudios.fragment.DecisionDialogFragment;
import android.graphics.Bitmap;

import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkoutRoutineActivity extends SherlockFragmentActivity{

	 //private TextView title;
	 private TextView workoutDesc;
	 private TextView workoutEquipment;
	 private TextView imageNumber;
	 private TextView workoutNumber;
	 private TextView workoutRepTV;
	 private TextView timerAlt;
	 private TextView beginSetTV;
	 
	 private RelativeLayout titleLayout;
	 private RelativeLayout lowerLayout;
	 private ImageView workoutIm;
	 
	 private Boolean viewsVisible = true;
	 private Typeface font;
	 private Typeface font_light;
	 private Typeface font_langdon;
	 
	 private DisplayMetrics dm;
	 private int screenWidth;
	 private int screenHeight;
	 private DatabaseHandler db;
	 private int currentImInt = 0;
	 private int numMediaImages;
	 
	 private int routineID;
	 private int type;
	 private ArrayList<Workout> workoutList;
	 private int currentIndex = 0;
	 
	 //timer
	 private CountDownTimer countDownTimer;
	 private TextView timerValue;
	 long timeInMilliseconds = 0L;
	 long timeSwapBuff = 0L;
	 long updatedTime = 0L;
	 Boolean isTimed = false;
	 Boolean isFirst = false;
	 Boolean started = false;
		
    //Exit Handler
	 private DecisionDialogFragment editNameDialog;
     private FragmentManager fm;
     private Bundle routineBundle;
	 private com.actionbarsherlock.app.ActionBar actionBar;
	 
	 DisplayImageOptions options;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        setContentView(R.layout.workout_timer_screen);
	        
	         //title = (TextView) view.findViewById(R.id.workout_title);
		     workoutDesc = (TextView) findViewById(R.id.workout_description);
		     workoutEquipment = (TextView) findViewById(R.id.workout_equipment);
		     imageNumber = (TextView) findViewById(R.id.image_number);
		     workoutIm = (ImageView) findViewById(R.id.workout_image);
		     workoutNumber = (TextView) findViewById(R.id.workout_im_num);
		     workoutRepTV = (TextView) findViewById(R.id.workout_rep);
		     titleLayout = (RelativeLayout) findViewById(R.id.handle);
		     lowerLayout = (RelativeLayout) findViewById(R.id.lower_layout);
		     timerValue = (TextView) findViewById(R.id.timerValue1);
		     timerAlt = (TextView) findViewById(R.id.timerValueAlt);
		     beginSetTV = (TextView) findViewById(R.id.clickBeginText);
		    //set title Visible
	        //title.setVisibility(View.VISIBLE);
	        
		     options = new DisplayImageOptions.Builder()
				.cacheInMemory(false)
				.cacheOnDisk(false)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		     
	        //get bundle from previous class
	        Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	        	routineID = extras.getInt("PROGRAM_ID");
	        	type = extras.getInt("TYPE_ID"); // to determine if its coming from schedule or just programs screen
	        }
	        
	        //Set the ACTION BAR
		 	actionBar = getSupportActionBar();
		 	actionBar.setDisplayHomeAsUpEnabled(true);
		 	actionBar.setLogo(R.drawable.icon_muscle_w);
		    
		    //get display metrics
	      	dm = new DisplayMetrics(); 
	      	this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
	      	screenWidth = dm.widthPixels;
	      	screenHeight = dm.heightPixels;
		    
			//initialize dialog listener
			fm = getSupportFragmentManager();
			routineBundle = new Bundle();
			routineBundle.putString("MESSAGE", "Quit the Routine");
			editNameDialog = new DecisionDialogFragment();
			editNameDialog.setArguments(routineBundle);
			
			//initialize the arraylist
			workoutList = new ArrayList<Workout>();
		    
		    db = new DatabaseHandler(this);
	        
	        try {
	        	db.createDataBase();
	        	db.openDataBase();
	 	    } catch (IOException ioe) {
	 		     throw new Error("Unable to create database");
	 	    }catch(SQLException sqle){
	 			throw sqle;
	 		}
	 
	        //Set the workoutList from the database
	        workoutList = db.getAllWorkoutsForRoutine(routineID);
	        if(type == 1)
	        {
	        	db.createSqlStatement("UPDATE schedule_table SET complete = 1 WHERE program_id = " + routineID);
	        }
	        
	        db.close();
	        
	        //set font
			font = Typeface.createFromAsset(getAssets(), Constants.GEARED); 
			font_light = Typeface.createFromAsset(getAssets(), Constants.GEARED_LIGHT);
			font_langdon = Typeface.createFromAsset(getAssets(), Constants.LANGDON);
	        
			try{ //add a custom font to title
	        	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
	        	TextView yourTextView = (TextView) findViewById(titleId);
	        	yourTextView.setTextColor(getResources().getColor(R.color.white));
	        	yourTextView.setText(workoutList.get(currentIndex).getName());
	 		    yourTextView.setTypeface(font_langdon);
	        }
			catch(Exception e)
			{}
		    
		    //set font
	        workoutDesc.setTypeface(font);
		    workoutEquipment.setTypeface(font);
		    imageNumber.setTypeface(font);
		    workoutNumber.setTypeface(font_light);
		    workoutRepTV.setTypeface(font_light);
		    timerValue.setTypeface(font_langdon);
		    timerAlt.setTypeface(font_langdon);
		    beginSetTV.setTypeface(font_langdon);
	        
	        //set workout image height and width
	        workoutIm.getLayoutParams().height = (int) (screenHeight);
		    workoutIm.getLayoutParams().width = (int) (screenWidth);
	        ImageLoader.getInstance().displayImage("drawable://" + Constants.selectWorkout((workoutList.get(currentIndex).getmediaArray().get(0))), workoutIm, options);
	       
	        bindListeners();
	        setViewsInVisible();
	        setTextViews(0);
	        
	 }
	 
	 //create cound down timer
   public class MyCountDownTimer extends CountDownTimer {
  	  public MyCountDownTimer(long startTime, long interval) {
  		  super(startTime, interval);
  	  }

  	  @Override
  	  public void onFinish() { //when the count down is finished, ends if its the last workout
  		   if(!((currentIndex + 1) > (workoutList.size()-1)))
  		   {
  			   currentIndex++;
  			   numMediaImages = workoutList.get(currentIndex).getmediaArray().size();
  			   setTextViews(currentIndex);
  			   currentImInt = 0;
  			   started = false;
  			   setViewsInVisible();
  			   workoutIm.setImageDrawable(null);
  			   beginSetTV.setText(R.string.click_to_begin_set);
               beginSetTV.setVisibility(View.VISIBLE);  			   
  		   }
  		   else{
  			   //the user has finished the program
 			   CustomDialog.createExitDialog(getString(R.string.finished_program_text), "Workout Complete, Cool Down", R.drawable.stretchfitnesslogo, fm);
  		       countDownTimer.cancel();
  		   }
  	  }

  	  @Override
  	  public void onTick(long millisUntilFinished) {
  		        int secs = (int) (millisUntilFinished / 1000); 
  		  		int mins = secs / 60; //0
  		  		secs = secs % 60;
  		  		timerValue.setText("" + mins + ":" + String.format("%02d", secs));
  		  		timerAlt.setText("" + mins + ":" + String.format("%02d", secs));
   		 
  		  		if(secs % 2 == 0) //every third second change image
  		  		{
  		  			imageNumber.setText(((currentImInt) % (numMediaImages) + 1) + " / " + numMediaImages);
  		  		    ImageLoader.getInstance().displayImage("drawable://" + Constants.selectWorkout((workoutList.get(currentIndex).getmediaArray().get(currentImInt % numMediaImages))), workoutIm, options);
  		  			currentImInt++;
  		  		}
  	     }
  		  		
   }
   
   public void bindListeners()
   {
   	workoutIm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//if the user just begins a set just begin workout
				if(beginSetTV.getVisibility() == View.GONE)
			   	{
					if(viewsVisible)
					{
						 setViewsInVisible();
				  	     viewsVisible = false;
					}
					else
					{
						setViewsVisible();
					   	viewsVisible = true;
					}
			   	}
				else{//the user has just started a new set
					 //begin counter
			        countDownTimer = new MyCountDownTimer((workoutList.get(currentIndex).getTimeInSec() * 1000), 1000);
		   	        countDownTimer.start();
		   	        beginSetTV.setVisibility(View.GONE);
		   	        setViewsVisible();
		   	        started = true;
		   	    }
				
			}
		});
   }
   
   public void setTextViews(int index)
   {
	  workoutDesc.setText(workoutList.get(index).getInstruction());
      workoutNumber.setText("Workout # " + (index+1) + " of " + workoutList.size());
      workoutRepTV.setText(workoutList.get(index).getRep());
      numMediaImages = workoutList.get(index).getmediaArray().size();
      workoutRepTV.setVisibility(View.VISIBLE);
      workoutNumber.setVisibility(View.VISIBLE);
      actionBar.setTitle(workoutList.get(index).getName());
	}
   
   public void setViewsVisible()
   {
	    titleLayout.setVisibility(View.VISIBLE);
	   	lowerLayout.setVisibility(View.VISIBLE);
	   	timerAlt.setVisibility(View.INVISIBLE);
	   	viewsVisible = true;
   }
   
   public void setViewsInVisible()
   {
	     titleLayout.setVisibility(View.GONE);
	     lowerLayout.setVisibility(View.GONE);
	     timerAlt.setVisibility(View.VISIBLE);
	     viewsVisible = false;
   }
   
   @Override
	public void onDestroy() {
		workoutIm.setImageDrawable(null);
		if(started){
		   countDownTimer.cancel();}
		db.close();
	    super.onDestroy();
	 }
   
   @Override
	 public void onBackPressed() {
		    editNameDialog.show(fm, "fragment_edit_name");
	 }
   
    @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem)
	 { 
		 if(menuItem.getItemId() == android.R.id.home)
	 	 {
		    	 onBackPressed();
		 }
		 return false;
	 }
   
  
}
