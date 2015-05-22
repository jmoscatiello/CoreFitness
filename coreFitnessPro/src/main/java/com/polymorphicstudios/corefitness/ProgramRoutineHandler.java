package com.polymorphicstudios.corefitness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.CustomDialog;
import com.polymorphicstudios.db.DatabaseHandler;
import com.polymorphicstudios.fragment.CustomDialogFragment;
import com.polymorphicstudios.fragment.DecisionDialogFragment;
import com.polymorphicstudios.fragment.PageAdapter_Tabs;
import com.polymorphicstudios.fragment.WorkoutRoutineFragment;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

public class ProgramRoutineHandler extends SherlockFragmentActivity{

	 ViewPager mPager;
	 PageIndicator mIndicator;
	 private PagerAdapter mPagerAdapter;
	 private DatabaseHandler db;
	 
	 private int routineID;
	 private int type;
	 private ArrayList<Integer> workoutList;
	 
	 private DecisionDialogFragment editNameDialog;
	 private FragmentManager fm;
	 private Bundle routineBundle;
	 private Bundle messageBundle;
	 private Typeface face;
	 List<Fragment> fragments;
	 private com.actionbarsherlock.app.ActionBar actionBar;
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	        setContentView(R.layout.simple_underlines);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        super.onCreate( savedInstanceState );
			
			//initialize dialog listener
			fm = getSupportFragmentManager();
			routineBundle = new Bundle();
			routineBundle.putString("MESSAGE", "Quit the Routine");
			editNameDialog = new DecisionDialogFragment();
			editNameDialog.setArguments(routineBundle);
			
			//initialize the arraylist
			workoutList = new ArrayList<Integer>();
			
            Fragment frag = new Fragment();
	        fragments = new Vector<Fragment>();
	        
	        //pull routine number from bundle
	        Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	        	routineID = extras.getInt("PROGRAM_ID");
	        	type = extras.getInt("TYPE_ID"); // to determine if its coming from schedule or just programs screen
	        }
	        
	        com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		 	actionBar.setDisplayHomeAsUpEnabled(true);
	        actionBar.setLogo(R.drawable.icon_muscle_w);
	        actionBar.setTitle("programs");
	        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Routine: " + routineID + "</font>"));
	        
	        try{ //add a custom font to title
	        	face = Typeface.createFromAsset(getAssets(), Constants.LANGDON); 
	        	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
	        	TextView yourTextView = (TextView) findViewById(titleId);
	        	yourTextView.setTextColor(getResources().getColor(R.color.white));
	        	yourTextView.setText("Routine: " + routineID);
	        	yourTextView.setTypeface(face);
	        }
			catch(Exception e)
			{
				
			}
		    
		    //call the database
		    db = new DatabaseHandler(this.getApplicationContext());
		    try {
		          db.createDataBase();
		          db.openDataBase();
		     } catch (IOException ioe) {
		 		     throw new Error("Unable to create database");
		     }catch(SQLException sqle){
		 			throw sqle;
		    }
		     
	        //Set the workoutList from the database
	        workoutList = db.getRoutineWorkouts(routineID);
	        if(type == 1)
	        {
	        	db.createSqlStatement("UPDATE schedule_table SET complete = 1 WHERE program_id = " + routineID);
	        }
	        db.close();
	        
	        //Create dialog to tell people to stretch
	        CustomDialog.createCustomDialog(getString(R.string.routine_stretch_text), "Always Stretch Before and Cool Down After", R.drawable.stretchfitnesslogo, fm);
	        
	        Bundle bundle_args = new Bundle();
	        
	        //Add all fragments to a fragment list
	        for(int i = 0; i < workoutList.size(); i++)
	        {
	        	bundle_args = new Bundle();
	        	bundle_args.putInt("WORKOUT_ID",  workoutList.get(i));
	        	bundle_args.putInt("PW_NUMBER", routineID);
	        	bundle_args.putInt("EXERCISE_NUMBER", (i+1));
	        	bundle_args.putInt("NUM_WORKOUTS", workoutList.size());
	        	
	        	frag = Fragment.instantiate(this, WorkoutRoutineFragment.class.getName());
	 	        frag.setArguments(bundle_args);
	 	        fragments.add(frag);
	        }
	        
            this.mPagerAdapter  = new PageAdapter_Tabs(super.getSupportFragmentManager(), fragments);
	        
	        mPager = (ViewPager)findViewById(R.id.pager);
	        mPager.setAdapter(this.mPagerAdapter);
	        
	        UnderlinePageIndicator indicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
	        indicator.setViewPager(mPager);
	        indicator.setFades(false);
	        mIndicator = indicator;
	        
	 }
	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	       return super.onCreateOptionsMenu(menu);
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
		    	 editNameDialog.show(fm, "fragment_edit_name");
		 }
		 else if(menuItem.getItemId() == R.id.information_action_icon)
	 	 {
	 			fm = getSupportFragmentManager();
	 		    messageBundle = new Bundle();
	 		    messageBundle.putString("MESSAGE",  getString(R.string.program_help_text));
	 		    messageBundle.putString("TITLE", "Schedule Info");
	 		    messageBundle.putInt("DRAWABLE", R.drawable.hfb_logo);
	 		    CustomDialogFragment fragment1 = new CustomDialogFragment();   
	 		    fragment1.setArguments(messageBundle);
	 		    fragment1.show(fm, ""); 
	 	 }
		
		 
		  return true;
	 }
	 
	 public void setActionBarTitle(Spanned title)
	 {
		 actionBar.setTitle(title);
	 }
	 
	 public void switchView()
	 {
		 if(!(mPager.getCurrentItem() == workoutList.size()))
		 {
			 int current = mPager.getCurrentItem() + 1;
			 mPager.setCurrentItem(current, true);
		 }
		 else
		 {
			 //the user has finished the program
			 CustomDialog.createCustomDialog(getString(R.string.finished_program_text), "Workout Complete, Cool Down", R.drawable.stretchfitnesslogo, fm);
		 }
	 }
	 
	 
	 
}
