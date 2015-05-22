package com.polymorphicstudios.fragment;

import java.io.IOException;
import java.util.ArrayList;

import com.polymorphicstudios.actor.Workout;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.ImageModification;
import com.polymorphicstudios.corefitness.R;
import com.polymorphicstudios.corefitness.WorkoutActivity;
import com.polymorphicstudios.db.DatabaseHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WorkoutListFragment extends Fragment{

	private DatabaseHandler db;
	private ArrayList<Workout> workoutList;
	
	//The muscle the user clicked on
	private String selectedMuscle;
	
	//set font typeface
	private Typeface font;
	
	private ListView list;
	private ListAdapter adapter;
	public int screenHeight;
	public int screenWidth;
	public View view;
	
	public Context c;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//set the view for the fragment
        view = inflater.inflate(R.layout.list_screen, container, false);
        
        //This is to allow the Action bar to be called in individual fragments
        this.setHasOptionsMenu(true);
        
        //find view by id for list
        list=(ListView) view.findViewById(R.id.list);
        c = getActivity().getApplicationContext(); 
        
        //set font
    	font = Typeface.createFromAsset(getActivity().getAssets(), Constants.LANGDON);
        
    	Bundle extras = getArguments();
    	if (extras != null) {
         	selectedMuscle = extras.getString("MUSCLE");
         	screenHeight = extras.getInt("SCREEN_HEIGHT");
         	screenWidth = extras.getInt("SCREEN_WIDTH");
        }
    	
    	getActivity().setProgressBarIndeterminateVisibility(true);
    	
    	//run the async task
        CreateWorkoutListTask task = new CreateWorkoutListTask();
	    task.execute(new String[] { "" });
        	
        return view;
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
		        workoutList = db.getWorkoutList(selectedMuscle);

		        db.close();
		        
		        return "";
		    }
			
			//run when the data is pulled online
			@Override
		    protected void onPostExecute(String result) {
				// Getting adapter by passing xml data ArrayList
		           
				list=(ListView) view.findViewById(R.id.list);
				
				// Getting adapter by passing xml data ArrayList
		        adapter= new ListAdapter(getActivity());        
		        list.setAdapter(adapter);
		        
		        // Click event for single list row
		        list.setOnItemClickListener(new OnItemClickListener() {

			    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) { //AdapterView<?> parent, View view, int position, long id
							Intent intent = new Intent(getActivity().getBaseContext(), WorkoutActivity.class);
							intent.putExtra("EXERCISE_ID", (Integer) v.getTag());
							startActivity(intent);
							getActivity().overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);
					}
				 });
		        
		        getActivity().setProgressBarIndeterminateVisibility(false);
		    }
	 }
	
	public class ListAdapter extends BaseAdapter {
		
		private Activity activity;
		private LayoutInflater inflater = null;
		
		public ListAdapter(Activity a) {
			 activity = a;
			 inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	        //wBodyArea.setTypeface(font_light);
	        
	        //Workout Image
	        ImageView thumb_image = (ImageView)view.findViewById(R.id.workout_thumb); // thumb image
	        ImageView arrow = (ImageView)view.findViewById(R.id.arrow);
	        
	        // Setting all values in listview
	        wName.setText(workoutList.get(position).getName().toUpperCase());
	        
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
}
