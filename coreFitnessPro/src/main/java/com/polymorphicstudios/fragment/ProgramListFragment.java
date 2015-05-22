package com.polymorphicstudios.fragment;

import java.io.IOException;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.polymorphicstudios.actor.Program;
import com.polymorphicstudios.corefitness.ProgramListFragHandler;
import com.polymorphicstudios.corefitness.R;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.ImageModification;
import com.polymorphicstudios.db.DatabaseHandler;

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

  public class ProgramListFragment extends SherlockFragment{

	  private ArrayList<Program> programList;  
		private ListView list;
		private ListAdapter adapter;
		private DatabaseHandler db;
		private int levelNum;
		
		public Context c;
		private int screenWidth;
		
		//set font typeface
		private Typeface font_light;
		private Typeface fontLangdon;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {

		     	 View mainView = inflater
						.inflate(R.layout.general_list_screen, container, false);
		    
		     	 //get bundle from previous activity
		    	 Bundle extras = getArguments();
		    	 if (extras != null) {
		    		 levelNum = extras.getInt("LEVEL_NUMBER");
		    		 screenWidth = extras.getInt("SCREEN_WIDTH");
		    	 }
		    	 
		         //find view by id for list
		         list=(ListView) mainView.findViewById(R.id.workout_list);
		         font_light = Typeface.createFromAsset(getActivity().getAssets(), Constants.GEARED_LIGHT); 
		         fontLangdon = Typeface.createFromAsset(getActivity().getAssets(), Constants.LANGDON);
		         c = getActivity().getApplicationContext(); 
		         
		         //run the async task
		         CreateWorkoutListTask task = new CreateWorkoutListTask();
		 	     task.execute(new String[] { "" });
		        
		         return mainView;
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
			        
			        programList = db.getProgramList(levelNum);
			        db.close();
			        
			        return "";
			    }
			    
			  //run when the data is pulled online
				@Override
			    protected void onPostExecute(String result) {
					// Getting adapter by passing xml data ArrayList
			        adapter= new ListAdapter(getActivity());        
			        list.setAdapter(adapter);
			        
			        // Click event for single list row
			        list.setOnItemClickListener(new OnItemClickListener() {
				    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) { //AdapterView<?> parent, View view, int position, long id
								/*Intent intent = new Intent(getActivity().getApplicationContext(), WorkoutRoutineActivity.class);
								intent.putExtra("PROGRAM_ID", (Integer) v.getTag());
								intent.putExtra("TYPE_ID", 0);
								startActivity(intent);
								getActivity().overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);*/
				    	        System.out.println("THE TAG IS" + (Integer) v.getTag());
				    	        ((ProgramListFragHandler)getActivity()).sendTimedDialog((Integer) v.getTag(), 0);
						}
					 });
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
				return programList.size();
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
		        	view = inflater.inflate(R.layout.list_row_program, null);
		        }
				
				//Textview for workout name and body part
				TextView programName = (TextView)view.findViewById(R.id.program_name); 
		        TextView timeTaken = (TextView)view.findViewById(R.id.text_time_taken);
		        
		        ImageView arrow = (ImageView)view.findViewById(R.id.arrow);
		        ImageView im_watch = (ImageView)view.findViewById(R.id.im_watch);
		        
		        //set the font for the textViews
		        programName.setTypeface(fontLangdon);
		        timeTaken.setTypeface(font_light);
		        
		        // Setting all values in listview
		        programName.setText(programList.get(position).getName()); 
		        timeTaken.setText(" " + programList.get(position).getTime());
		        
		        im_watch.getLayoutParams().height = (int) (screenWidth * 0.04);
	 	        im_watch.getLayoutParams().width = (int) (screenWidth * 0.04);
	 	        im_watch.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.icon_stopwatch, (int)(screenWidth * 0.06), (int)(screenWidth * 0.06)));
		       
		        arrow.getLayoutParams().height = (int) (screenWidth * 0.05);
		        arrow.getLayoutParams().width = (int) (screenWidth * 0.05);
		        arrow.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), R.drawable.icon_arrow, (int)(screenWidth * 0.08), (int)(screenWidth * 0.08)));
		        
		        view.setTag(programList.get(position).getProgramWorkoutId());
		        
				return view;
			}
			
		}
}
