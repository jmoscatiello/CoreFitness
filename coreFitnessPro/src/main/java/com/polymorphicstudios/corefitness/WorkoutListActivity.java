package com.polymorphicstudios.corefitness;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.polymorphicstudios.actor.Workout;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.ImageModification;
import com.polymorphicstudios.db.DatabaseHandler;

public class WorkoutListActivity extends SherlockActivity{

	private DatabaseHandler db;
	private ArrayList<Workout> workoutList;
	
	//set font typeface
	private Typeface font;
	private Typeface font_light;
	private Context c;
	
	private ListView list;
	private ListAdapter adapter;
	public int screenHeight;
	public int screenWidth;
	private DisplayMetrics dm;
	
	private Typeface face;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.general_list_screen);
		
        //find view by id for list
        list=(ListView) findViewById(R.id.workout_list);
        
        c = this.getApplicationContext();
       
        //Set the ACTION BAR
	 	com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
	 	actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(R.drawable.icon_muscle_w);
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>workout lookup</font>"));
	 	
        try{ //add a custom font to title
        	face = Typeface.createFromAsset(getAssets(), Constants.LANGDON); 
        	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        	TextView yourTextView = (TextView) findViewById(titleId);
        	yourTextView.setTextColor(getResources().getColor(R.color.white));
        	yourTextView.setText("workout lookup");
        	yourTextView.setTypeface(face);
        }
		catch(Exception e)
		{
			
		}
        
	    //show the progressbar until loaded
	    setSupportProgressBarIndeterminateVisibility(true);
	    
	    //get display metrics
      	dm = new DisplayMetrics(); 
      	getWindowManager().getDefaultDisplay().getMetrics(dm); 
      	screenWidth = dm.widthPixels;
      	screenHeight = dm.heightPixels; 
      	
        font = Typeface.createFromAsset(getAssets(), Constants.GEARED); 
        font_light = Typeface.createFromAsset(getAssets(), Constants.GEARED_LIGHT); 
        adapter= new ListAdapter(this);     
        
        //run the async task
        CreateWorkoutListTask task = new CreateWorkoutListTask();
	    task.execute(new String[] { "" });
	}
	
	 
	 private class CreateWorkoutListTask extends AsyncTask<String, Void, String> {
			//This async task downloads the Tips from the Net

		    @Override
		    protected String doInBackground(String... urls) {
		    	//Call the database
		        db = new DatabaseHandler(c);
		        
		        //Try creating the database
		        try {
		        	db.createDataBase();
		        	db.openDataBase();
		 	    } catch (IOException ioe) {
		 		     throw new Error("Unable to create database");
		 	    }catch(SQLException sqle){
		 			throw sqle;
		 		}
		        
		        //populate workout list and specific, specific holds the unique specific muscle
		        workoutList = db.getWorkoutList("Obliques");
		        
		        db.close();
		        
		        return "";
		    }
			
			//run when the data is pulled online
			@Override
		    protected void onPostExecute(String result) {
				// Getting adapter by passing xml data ArrayList
		           
		        list.setAdapter(adapter);
		        
		        // Click event for single list row
		        list.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) { //AdapterView<?> parent, View view, int position, long id
							Intent intent = new Intent(c, WorkoutActivity.class);
							intent.putExtra("EXERCISE_ID", (Integer) v.getTag());
							startActivity(intent);
							overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);
							
					}
				 });
		        
		        setSupportProgressBarIndeterminateVisibility(false);
		    }
	 }
	 
	 public class ListAdapter extends BaseAdapter {
			
			private Activity activity;
			private LayoutInflater inflater = null;
			
			public ListAdapter(Activity a) {
				 activity = a;
				 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			
			public int getCount() {
				 return workoutList.size();
			}

			public Object getItem(int position) {
			      return position;
			}

			public long getItemId(int position) {
			        return position;
			}

			@SuppressLint("ResourceAsColor")
			public View getView(int position, View convertView, ViewGroup parent) {
				
				View view =convertView;
		        if(convertView==null){
		        	view = inflater.inflate(R.layout.list_row_workout, null);
		        }
				
				//Textview for workout name and body part
				TextView wName = (TextView)view.findViewById(R.id.workout_name); 
		        TextView wBodyArea = (TextView)view.findViewById(R.id.workout_body_area);
		        
		        //set the font for the textViews
		        wName.setTypeface(font);
		        wBodyArea.setTypeface(font_light);
		        
		        //Workout Image
		        ImageView thumb_image = (ImageView)view.findViewById(R.id.workout_thumb); // thumb image
		        ImageView arrow = (ImageView)view.findViewById(R.id.arrow);
		        
		        // Setting all values in listview
		        wName.setText(workoutList.get(position).getName());
		        
		        if(workoutList.get(position).getEquipment().equalsIgnoreCase("") || workoutList.get(position).getEquipment().equalsIgnoreCase(" ")){
		        	wBodyArea.setText("Equipment needed: None");
			    }
		        else{
		        	wBodyArea.setText("Equipment needed: " + workoutList.get(position).getEquipment());
		        }
		        
		        //set image
		        thumb_image.getLayoutParams().height = (int) (screenWidth * 0.16);
		        thumb_image.getLayoutParams().width = (int) (screenWidth * 0.16);
		        thumb_image.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), Constants.selectWorkoutThumb((workoutList.get(position).getImageID())), (int)(screenWidth * 0.18), (int)(screenWidth * 0.18)));
		        
		        arrow.getLayoutParams().height = (int) (screenWidth * 0.05);
		        arrow.getLayoutParams().width = (int) (screenWidth * 0.05);
		        arrow.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.icon_arrow, (int)(screenWidth * 0.08), (int)(screenWidth * 0.08)));
		        
		        view.setTag((workoutList.get(position).getworkoutID()));	        
				return view;
			}
			
		}
		
		 @Override
		 public boolean onOptionsItemSelected(MenuItem menuItem)
		 {       
			if(menuItem.getItemId() == android.R.id.home)
			  {
				  System.gc();
				  super.onBackPressed();
			      overridePendingTransition(R.anim.translate_left_onscreen, R.anim.translate_right_offscreen);
			      finish();
			  }
			 
			   return true;
		 }
		 
}
