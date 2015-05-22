package com.polymorphicstudios.fragment;

import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polymorphicstudios.actor.Workout;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.corefitness.R;
import com.polymorphicstudios.db.DatabaseHandler;

import java.io.IOException;

public class WorkoutRoutineFragment extends SherlockFragment {

	 int visibleToUser = 0; 
	
	 private TextView title;
	 private TextView workoutDesc;
	 private TextView workoutEquipment;
	 private TextView imageNumber;
	 private TextView workoutNumber;
	 private TextView workoutRepTV;
	 private ImageView workoutIm;
	 
	 private RelativeLayout titleLayout;
	 private RelativeLayout lowerLayout;
	 
	 private int exerciseNum;
	 private int numMediaImages;
	 
	 private int screenHeight;
	 private int screenWidth;
	 private DisplayMetrics dm;
	 
	//private TextView workoutInstruction;
	private Workout workout;
	private Typeface font;
	private Typeface font_light;
	private Typeface font_langdon;
	
	private DatabaseHandler db;
    private int workoutId;
    private int programId;
    
    private Boolean viewsVisible = true;
	
	private int currentImInt = 0;
	private int numWorkouts;
	private DisplayImageOptions options;

	Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        
    public void run() {
    		if(isAdded())
            {
    			afficher();
            }
        }
    };
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		//set the view for the fragment
        View view = inflater.inflate(R.layout.full_parent_image, container, false);
		
        //This is to allow the Action bar to be called in individual fragments
        setHasOptionsMenu(true);

		//set workout image height and width
		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();


		Bundle extras = getArguments();
    	 if (extras != null) {
         	workoutId = extras.getInt("WORKOUT_ID");
         	programId = extras.getInt("PW_NUMBER");
         	exerciseNum = extras.getInt("EXERCISE_NUMBER");
         	numWorkouts = extras.getInt("NUM_WORKOUTS");
         }
    	 
    	 title = (TextView) view.findViewById(R.id.workout_title);
	     workoutDesc = (TextView) view.findViewById(R.id.workout_description);
	     workoutEquipment = (TextView) view.findViewById(R.id.workout_equipment);
	     imageNumber = (TextView) view.findViewById(R.id.image_number);
	     workoutIm = (ImageView) view.findViewById(R.id.workout_image);
	     workoutNumber = (TextView) view.findViewById(R.id.workout_im_num);
	     workoutRepTV = (TextView) view.findViewById(R.id.workout_rep);
	     titleLayout = (RelativeLayout) view.findViewById(R.id.handle);
	     lowerLayout = (RelativeLayout) view.findViewById(R.id.lower_layout);
	     
	     
	     workoutNumber.setVisibility(View.VISIBLE);
	     workoutRepTV.setVisibility(View.VISIBLE);
	     
	     //set font
		 font = Typeface.createFromAsset(getActivity().getAssets(), Constants.GEARED); 
		 font_light = Typeface.createFromAsset(getActivity().getAssets(), Constants.GEARED_LIGHT);
    	 font_langdon = Typeface.createFromAsset(getActivity().getAssets(), Constants.LANGDON);
		 
         //set font
         title.setTypeface(font_langdon);
         workoutDesc.setTypeface(font);
	     workoutEquipment.setTypeface(font);
	     imageNumber.setTypeface(font);
	     workoutNumber.setTypeface(font_light);
	     workoutRepTV.setTypeface(font_light);

		 //get display metrics
	     dm = new DisplayMetrics(); 
	     getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); 
	     screenWidth = dm.widthPixels;
	     screenHeight = dm.heightPixels;
        
         db = new DatabaseHandler(getActivity());
         
         try {
         	db.createDataBase();
        	db.openDataBase();
 	     } catch (IOException ioe) {
 		     throw new Error("Unable to create database");
 	     }catch(SQLException sqle){
 			throw sqle;
 		 }
 
          //get the single workout passed into the page
          workout = db.getSingleWorkoutRoutine(programId, workoutId);
          db.close();
          
          title.setText(workout.getName());
          workoutDesc.setText(workout.getInstruction());
          workoutNumber.setText("Workout # " + exerciseNum + " of " + numWorkouts);
          workoutRepTV.setText(workout.getRep());
          numMediaImages = workout.getmediaArray().size();
          workoutRepTV.setVisibility(View.VISIBLE);
          workoutNumber.setVisibility(View.VISIBLE);
          
          //set the equipment text
	      if(!workout.getEquipment().equalsIgnoreCase(" "))
	      {
	          workoutEquipment.setText(workout.getEquipment());
	      }
	      else
	      {
	          workoutEquipment.setText("No Equipment");
	      }
          
          //Make the hidden views visibile
	      //set workout image height and width
	      workoutIm.getLayoutParams().height = (int) (screenHeight);
	      workoutIm.getLayoutParams().width = (int) (screenWidth);
          
	      bindListener();
        
          return view;
    }
    
    
    public void bindListener()
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
    
    @Override
    public void onResume()
    {
    	super.onResume();
    }

    @Override
	public void onPause() {
	    super.onPause();
	}   
    
     public void afficher()
     {
    	 if(visibleToUser==1)
    	 {
    	 	imageNumber.setText(((currentImInt) % (numMediaImages) + 1) + " / " + numMediaImages);
			 ImageLoader.getInstance().displayImage("drawable://" + Constants.selectWorkout((workout.getmediaArray().get(currentImInt % numMediaImages))), workoutIm, options);;
    	 	currentImInt++;
    	 	handler.postDelayed(runnable, 3000);
    	 }
    	 else{
    		 if(workoutIm.getDrawable() != null)
    		 {
    			 workoutIm.setImageDrawable(null);
    		 }
    	 }
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
     public void setUserVisibleHint(boolean isVisibleToUser) {
         super.setUserVisibleHint(isVisibleToUser);

         try {
             if (isVisibleToUser) { //frag is visible
                 handler.post(runnable);
                 visibleToUser = 1;

             } else { //if the frag is not visible
                 visibleToUser = 0;
                 handler.removeCallbacks(runnable);
             }
         }catch(Exception e)
         {

         }
     }
     
     @Override
     public void onDestroy() {
    	  try {
              if (!(workoutIm == null)) {
                  workoutIm.setImageDrawable(null);
              }
              handler.removeCallbacks(runnable);
              db.close();
              super.onDestroy();
          }
          catch(Exception e){
              super.onDestroy();
          }
     }
      
      @Override
      public void onDestroyView() {
    	  	 if(!(workoutIm == null))
    	   	 {
  	    		workoutIm.setImageDrawable(null);
    	  	 }	
        	 handler.removeCallbacks(runnable);
        	 db.close();
        	 onDestroy();
        	 super.onDestroyView();
       }


}