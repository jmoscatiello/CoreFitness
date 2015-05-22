package com.polymorphicstudios.fragment;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.polymorphicstudios.actor.WeekDay;
import com.polymorphicstudios.corefitness.ScheduleFragHandler;
import com.polymorphicstudios.corefitness.R;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.ImageModification;
import com.polymorphicstudios.db.DatabaseHandler;

public class WeekFragment extends SherlockFragment{

	private int weekNumber;
	private String[] dayList = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	
	//set font typeface
	private Typeface font;
	private Typeface font_light;
	private DatabaseHandler db;
	private int screenWidth;
	
	private ListView list;
	private ListAdapter adapter;
	private ArrayList<WeekDay> weekArray;	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//set the view for the fragment
		View view = inflater.inflate(R.layout.week_schedule_screen, container, false);
    
		getActivity().setProgressBarIndeterminateVisibility(true);
		
		 //set font
        font = Typeface.createFromAsset(getActivity().getAssets(), Constants.GEARED); 
        font_light = Typeface.createFromAsset(getActivity().getAssets(), Constants.GEARED_LIGHT); 
		
        
		Bundle extras = getArguments();

		if (extras != null) {
			weekNumber = extras.getInt("WEEK_NUMBER");
			screenWidth = extras.getInt("SCREEN_WIDTH");
        }
		
        //find view by id for list
        list=(ListView) view.findViewById(R.id.day_list);
        //Getting adapter by passing xml data ArrayList
        adapter= new ListAdapter(getActivity());        
        
        CreateWorkoutListTask task = new CreateWorkoutListTask();
	    task.execute(new String[] { "" });
        
		return view;
	}
	
	private class CreateWorkoutListTask extends AsyncTask<String, Void, String> {

	    @Override
	    protected String doInBackground(String... urls) {
	    	//Call the database
	        db = new DatabaseHandler(getActivity().getApplicationContext());
	        
	        //Try creating the database
	        try {
	        	db.createDataBase();
	        	db.openDataBase();
	 	    } catch (IOException ioe) {
	 		     throw new Error("Unable to create database");
	 	    }catch(SQLException sqle){
	 			throw sqle;
	 		}
	        
	        weekArray = db.getWeekDay(weekNumber);
	        
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
					if((Integer) v.getTag() > 0){	
						//create the timed dialog
		    	        ((ScheduleFragHandler) getActivity()).sendTimedDialog((Integer) v.getTag(), 1);
					}
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
			 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return weekArray.size();
		}

		public Object getItem(int position) {
		        return position;
		}

		public long getItemId(int position) {
		        return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view =convertView;
	        if(convertView==null){
	        	view = inflater.inflate(R.layout.list_row_weekday, null);
	        }
			
			//Textview for workout name and body part
			TextView dayName = (TextView)view.findViewById(R.id.day_name); 
	        TextView programLevel = (TextView)view.findViewById(R.id.text_program_level);
	        TextView timeTaken = (TextView)view.findViewById(R.id.text_time_taken);
	        ImageView thumb_image = (ImageView)view.findViewById(R.id.image_thumb); // thumb image
	        ImageView arrow = (ImageView)view.findViewById(R.id.arrow);
	        ImageView im_watch = (ImageView)view.findViewById(R.id.im_watch);
	        ImageView im_running_man = (ImageView)view.findViewById(R.id.im_desc_workout);
	        
	        //set the font for the textViews
	        dayName.setTypeface(font);
	        programLevel.setTypeface(font_light);
	        timeTaken.setTypeface(font_light);
	        
	        // Setting all values in listview
	        dayName.setText(dayList[position]); 
	        programLevel.setText(" " + weekArray.get(position).getProgramName() + " ");
	       
	        thumb_image.getLayoutParams().height = (int) (screenWidth * 0.08);
	        thumb_image.getLayoutParams().width = (int) (screenWidth * 0.08);
	        
	        if(weekArray.get(position).getProgramID() < 0)
	        {
	        	 //set image
		        thumb_image.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.calendar_sleep, (int)(screenWidth * 0.12), (int)(screenWidth * 0.12)));
	        }
	        else if(weekArray.get(position).getComplete() == 0) //not complete and not the next one
	        {
	        	 //set image
		        thumb_image.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.calendar_incomplete, (int)(screenWidth * 0.12), (int)(screenWidth * 0.12)));
	        }
	        else if(weekArray.get(position).getComplete() == 1){ // not complete but next in line
	        	 //set image
		        thumb_image.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.calendar_complete, (int)(screenWidth * 0.12), (int)(screenWidth * 0.12)));
	        }
	       
	        arrow.getLayoutParams().height = (int) (screenWidth * 0.05);
	        arrow.getLayoutParams().width = (int) (screenWidth * 0.05);
	        arrow.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.icon_arrow, (int)(screenWidth * 0.08), (int)(screenWidth * 0.08)));
	        
	        im_running_man.getLayoutParams().height = (int) (screenWidth * 0.04);
	        im_running_man.getLayoutParams().width = (int) (screenWidth * 0.04);
	        im_running_man.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.icon_muscle, (int)(screenWidth * 0.06), (int)(screenWidth * 0.06)));
	        
	        if(!(weekArray.get(position).getProgramID() == -1)) // its not a rest day
	        {
	        	im_watch.getLayoutParams().height = (int) (screenWidth * 0.04);
	 	        im_watch.getLayoutParams().width = (int) (screenWidth * 0.04);
	 	        im_watch.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.icon_stopwatch, (int)(screenWidth * 0.06), (int)(screenWidth * 0.06)));
	 	        timeTaken.setText(" " + weekArray.get(position).getTimeTaken());
	        }
	       
	        view.setTag((weekArray.get(position).getProgramID()));
	        
			return view;
		}
		
	 }
	
	 
}