package com.polymorphicstudios.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.polymorphicstudios.actor.Program;
import com.polymorphicstudios.actor.Recipe;
import com.polymorphicstudios.actor.WeekDay;
import com.polymorphicstudios.actor.Workout;
import com.polymorphicstudios.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper{

	//Initialize SQL Database
	SQLiteDatabase db;
	
	//Set up constants for the database handles
	private static String DB_PATH = Constants.DATABASE_PATH;
	private static final String DB_NAME = Constants.DATABASE_NAME;
	private static final int DATABASE_VERSION =7;
	
	//Set up context
	private Context myContext;
	
    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override  
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
	    //Toast.makeText(myContext, "onUpgrade called!", Toast.LENGTH_LONG).show();  

	     if (oldVersion < newVersion) {
	        try {

                copyDataBase();
	        } catch (IOException e) {

	            System.out.println("Error upgrading");
	        } 
	    } 
	}  
	
	 public void OverwriteOld() throws IOException{
	 		
		    this.getReadableDatabase();
		    this.getReadableDatabase().close();
		   
		   try {
 			   copyDataBase();
 		   } catch (IOException e) {
     		    throw new Error("Error copying database");
         }
   	
      }

    //retrieve multiple workouts for WorkoutListActivity
    public ArrayList<Recipe> getAllRecipeIds()
    {
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

        //get single workout for each thing

        //Select all query
        String selectQuery = "SELECT recipe_id, recipe_name, im_name FROM recipe_table WHERE enabled = 1";
        //create the db query
        Cursor cursor = db.rawQuery(selectQuery ,new String [] {});
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
                recipeList.add(recipe);

            } while (cursor.moveToNext());
        }

        // return contact list
        return recipeList;
    }


    //Get single work out through an ID
    public Recipe getSingleRecipe(int id) {

        String number = Integer.toString(id);

        // Retrieve single work out info for one exercise
        String selectQuery = "Select recipe_name, recipe_summary, recipe_ingredients, recipe_instructions, recipe_serving, im_name From recipe_table WHERE  recipe_id = '" + number + "'";

        //create the db query
        Cursor cursor = db.rawQuery(selectQuery,new String [] {});

        if (cursor != null)
            cursor.moveToFirst();

        //create a workout object and return it
        Recipe recipe = new Recipe(cursor.getString(0), cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        // return workout
        return recipe;
    }

	//Get single work out through an ID
    public Workout getSingleWorkout(int id) {
 
    	String number = Integer.toString(id);
    	
    	// Retrieve single work out info for one exercise
    	String selectQuery = "Select w.workout_id, w.name, w.instruction, w.special_item From workout w WHERE  w.workout_id = '" + number + "'";
    	 
        //create the db query
        Cursor cursor = db.rawQuery(selectQuery,new String [] {});
        		
        if (cursor != null)
            cursor.moveToFirst();
        
        //create a workout object and return it
        Workout workout = new Workout(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2), cursor.getString(3), getMediaIDBasedOnWID(id));
        
        // return workout
        return workout;
    }
    
    //Get single work out through an ID
    public Workout getSingleWorkoutRoutine(int pw_id, int workout_id) {
 
    	// Retrieve single work out info for one exercise
    	String selectQuery = "SELECT w.workout_id, w.name, w.instruction, w.special_item, p.rep, p.time_in_seconds FROM workout w, program_workout_table p WHERE w.workout_id = p.workout_id AND p.pw_id = '" + pw_id + "' AND w.workout_id = '" + workout_id + "' limit 1";
    	 
        //create the db query
        Cursor cursor = db.rawQuery(selectQuery,new String [] {});
        		
        if (cursor != null)
            cursor.moveToFirst();
        
        //create a workout object and return it
        Workout workout = new Workout(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)) ,getMediaIDBasedOnWID(workout_id));
        
        // return workout
        return workout;
    }
    
  //Get single work out through an ID
    public ArrayList<Workout> getAllWorkoutsForRoutine(int pw_id) 
    {
    	ArrayList<Integer> intList = new ArrayList<Integer>();    
    	ArrayList<Workout> workoutList = new ArrayList<Workout>();
    	intList = getRoutineWorkouts(pw_id);
    	
    	System.out.println("After gather intlist" );
    	
    	for(int i = 0; i < intList.size(); i++)
    	{
    		System.out.println("After gather intlist" + intList.get(i));
    		workoutList.add(getSingleWorkoutRoutine(pw_id, intList.get(i)));
    	}
    	
    	return workoutList;
    }
    
    
    public ArrayList<Program> getProgramList(int level)
    {
    	ArrayList<Program> programList = new ArrayList<Program>();
        
    	//Select all query
    	String selectQuery = "SELECT program_id, name, time_taken, pw_id, level, enabled FROM program_description_table WHERE program_id > 0 AND level = '" + level + "'";
    	System.out.println(selectQuery);
    	
        //create the db query
        Cursor cursor = db.rawQuery(selectQuery ,new String [] {});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Program program = new Program(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
            	// Adding contact to list
                programList.add(program);
            } while (cursor.moveToNext());
        }
        
        // return contact list
        return programList;	
    }
    
    public void createSqlStatement(String query)
    {
    	db=this.getWritableDatabase();   
        db.execSQL(query);
    }
    
    
    //retrieve multiple workouts for WorkoutListActivity
    public ArrayList<Workout> getWorkoutList(String main_muscle)
    {
    	
    	ArrayList<Workout> workoutList = new ArrayList<Workout>();
        
    	//get single workout for each thing 
    	
    	//Select all query
    	String selectQuery = "SELECT w.workout_id, w.name, w.instruction, w.special_item, m.image_id FROM workout w, media_table m WHERE w.main_muscle = '" + main_muscle + "' AND m.image_id IN(SELECT media_table.image_id FROM media_table WHERE NOT EXISTS (SELECT * FROM media_table t WHERE t.media_id = media_table.media_id AND t.image_id = media_table.image_id - 1) ORDER BY image_id) AND m.media_id = w.media_id";
    	
        //create the db query
        Cursor cursor = db.rawQuery(selectQuery ,new String [] {});
        if (cursor.moveToFirst()) {
            do {
            	Workout workout = new Workout(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
            	// Adding contact to list
                workoutList.add(workout);
            } while (cursor.moveToNext());
        }
        
        // return contact list
        return workoutList;
    }
    
    //retrieve information about weekday
    public ArrayList<WeekDay> getWeekDay(int weekNumber)
    {
    	
    	ArrayList<WeekDay> weekDayList = new ArrayList<WeekDay>();
        
    	// Select All Query
        String selectQuery = "SELECT s.day_number, s.program_id, m.name, m.time_taken, m.level, s.complete, s.enabled FROM schedule_table s, program_description_table m WHERE s.program_id = m.program_id AND s.week_number = '" + weekNumber + "' ";
 
        System.out.println(selectQuery);
        //create the db query
        Cursor cursor = db.rawQuery(selectQuery ,new String [] {});
        		
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	WeekDay weekday = new WeekDay(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3) , Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)));
            	// Adding contact to list
            	weekDayList.add(weekday);
            } while (cursor.moveToNext());
        }
        
        // return contact list
        return weekDayList;
    }
    
    
    //retrieve multiple routines names and ID
    public ArrayList<Integer> getRoutineWorkouts(int routineID)
    {
    	ArrayList<Integer> workoutList = new ArrayList<Integer>();
        
    	// Select All Query
        String selectQuery = "SELECT workout_id from " + Constants.PROGRAM_WORKOUT_TABLE_NAME + " WHERE pw_id = '" + routineID + "'";
 
        //create the db query
        Cursor cursor = db.rawQuery(selectQuery ,new String [] {});
        		
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                workoutList.add(Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        
        // return contact list
        return workoutList;
    }
    
    private ArrayList<Integer> getMediaIDBasedOnWID(int id)
    {
    	ArrayList<Integer> mediaID = new ArrayList<Integer>();
    	//create new query to get media id's
        String selectQuery = "Select m.image_id From workout w, media_table m WHERE w.media_id = m.media_id AND w.workout_id = '" + id + "'";
        
        //create the db query
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	mediaID.add(Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        
        return mediaID;
    }
    
    /**
     * Creates an empty database on the system and rewrites it with your own database.
    * */
    public void createDataBase() throws IOException{
		//By calling this method and empty database will be created into the default system path
        //of your application so we are gonna be able to overwrite that database with our database.
//    	this.getReadableDatabase();
    	File dbFile= myContext.getDatabasePath(DB_PATH + DB_NAME);
    	//File f = new File(DB_PATH);
    	if (!dbFile.exists()) {
    	  dbFile.mkdir();
    	}
    	
    	Boolean exists = checkDataBase();
		
    	if(!exists)//!dbFile.exists())
		{
		    this.getReadableDatabase();
		    //this.getReadableDatabase().close();
		   
		    try {
		       this.close();
   			   copyDataBase();
   		    } catch (IOException e) {
       		    throw new Error("Error copying database");
           }
		}
		else
		{
            try {
                this.getReadableDatabase();
                this.getReadableDatabase().close();
            }catch(Exception e)
            {
//                this.getReadableDatabase().close();
            }
		}
  	
    }
    
    private boolean checkDataBase() 
    {
        SQLiteDatabase checkDB = null;
        try 
        {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        catch (SQLiteException e) 
        {
        }
        if (checkDB != null)
        {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException
    {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) 
        {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
 
    public void openDataBase() throws SQLException{
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
 
    @Override
	public synchronized void close() {
    	    if(db != null)
    		    db.close();
 
    	    super.close();
 
	}

   

}
