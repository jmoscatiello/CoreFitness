package com.polymorphicstudios.corefitness;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.CustomDialog;
import com.polymorphicstudios.db.DatabaseHandler;
import com.polymorphicstudios.fragment.AlertDialogFragment;

import java.io.IOException;

public class HomeActivity extends SherlockFragmentActivity {

	private ImageView workoutBtn;
	private ImageView tipsBtn;
    private ImageView scheduleBtn;
    private ImageView programBtn;
    private ImageView recipeBtn;
    
    private TextView workoutTV;
	private TextView tipsTV;
    private TextView scheduleTV;
    private TextView programTV;
    private TextView recipeTV;
    
	private DisplayMetrics dm;
	private Typeface font_light;
	private ConnectivityManager conMgr;
	
	private AlertDialogFragment alertDialog;
	private FragmentManager fm;
	private Bundle argBundle;
	private DatabaseHandler db;
	private int screenWidth;
	
	private AdView adView;
    private LinearLayout adHolder;
	
	private Typeface face;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	    setContentView(R.layout.home_three_rows);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    super.onCreate( savedInstanceState );
	    
        workoutBtn = (ImageView) findViewById(R.id.btn_main_workout);
    	tipsBtn = (ImageView) findViewById(R.id.btn_main_tip);
        scheduleBtn = (ImageView) findViewById(R.id.btn_main_schedule);
        programBtn = (ImageView) findViewById(R.id.btn_main_program);
        recipeBtn = (ImageView) findViewById(R.id.btn_recipe);
        
        workoutTV = (TextView) findViewById(R.id.tv_workout);
    	tipsTV = (TextView) findViewById(R.id.tv_tip);
        scheduleTV = (TextView) findViewById(R.id.tv_schedule);
        programTV = (TextView) findViewById(R.id.tv_program);
        recipeTV = (TextView) findViewById(R.id.tv_recipe);
        adHolder = (LinearLayout) findViewById(R.id.adholder);
        
        //Set the ACTION BAR
	 	com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
	 	actionBar.setDisplayHomeAsUpEnabled(false);
	 	actionBar.setLogo(R.drawable.icon_muscle_w);
	 	actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>core fitness</font>"));
	 	
	 	try{
	 		face = Typeface.createFromAsset(getAssets(), Constants.LANGDON); 
		 	int titleId = getResources().getIdentifier("action_bar_title", "id","android");
	 		TextView yourTextView = (TextView) findViewById(titleId);
	 		yourTextView.setTextColor(getResources().getColor(R.color.white));
	 		yourTextView.setText("core fitness");
	 		yourTextView.setTypeface(face);
	 	}
	 	catch(Exception e)
	 	{
	 		
	 	}

		 //Set up ads
		 AdView mAdView = (AdView) findViewById(R.id.adView);
		 AdRequest adRequest = new AdRequest.Builder().build();
		 mAdView.loadAd(adRequest);

		 db = new DatabaseHandler(this);
        
        try {
        	db.createDataBase();
        } catch (IOException ioe) {
		     throw new Error("Unable to create database");
	    }catch(SQLException sqle){
			throw sqle;
		}
        
        //get display metrics
      	dm = new DisplayMetrics(); 
      	getWindowManager().getDefaultDisplay().getMetrics(dm); 
      	screenWidth = dm.widthPixels;

        //initialize dialog listener
		fm = getSupportFragmentManager();
		argBundle = new Bundle();
		alertDialog = new AlertDialogFragment();
      	
      	//set type face
      	font_light = Typeface.createFromAsset(getAssets(), Constants.LANGDON); 
      	
      	workoutTV.setTypeface(font_light);
      	tipsTV.setTypeface(font_light);
      	scheduleTV.setTypeface(font_light);
      	programTV.setTypeface(font_light);
      	recipeTV.setTypeface(font_light);
      	
      	
        //set workout image height and width
      	workoutBtn.getLayoutParams().height = (int) (screenWidth * 0.25);
      	workoutBtn.getLayoutParams().width = (int) (screenWidth * 0.25);
       
      	//set workout image height and width
      	scheduleBtn.getLayoutParams().height = (int) (screenWidth * 0.25);
      	scheduleBtn.getLayoutParams().width = (int) (screenWidth * 0.25);
        
      	 //set workout image height and width
      	programBtn.getLayoutParams().height = (int) (screenWidth * 0.25);
      	programBtn.getLayoutParams().width = (int) (screenWidth * 0.25);
       
      	 //set workout image height and width
      	tipsBtn.getLayoutParams().height = (int) (screenWidth * 0.25);
      	tipsBtn.getLayoutParams().width = (int) (screenWidth * 0.25);
      	
      	 //set workout image height and width
      	recipeBtn.getLayoutParams().height = (int) (screenWidth * 0.25);
      	recipeBtn.getLayoutParams().width = (int) (screenWidth * 0.25);
      	
      	//get connection manager
      	conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      	
        bindListeners();
        
    }
    
    private void bindListeners() {
    	
		workoutBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent(getBaseContext(), WorkoutListHandler.class);
				startActivity(intent);
				overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);
				
			}
		});
		scheduleBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ScheduleFragHandler.class);
				startActivity(intent);
				overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);
			}
		});
		programBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ProgramListFragHandler.class);
				startActivity(intent);
				overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);
			}
		});
		tipsBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//check if user is connected to internet
				try
				{
					if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED 
						 ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED  ) {
						//User is connected to internet/wifi
						Intent intent = new Intent(getBaseContext(), HealthTipsActivity.class); // FitnessBlog.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fade_in_screen, R.anim.fade_out_screen);
					}
					else if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED 
				                ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
				    	
						//notify user you are not online
						argBundle.putString("MESSAGE", getString(R.string.internet_connection_needed_tips));
						alertDialog.setArguments(argBundle);
						alertDialog.show(fm, "Alert Diag");
					}
				}
				catch(NullPointerException e)
				{					
				}
			}
		});
		recipeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//check if user is connected to internet

						Intent intent = new Intent(getBaseContext(), RecipeLookupActivity.class); // FitnessBlog.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fade_in_screen, R.anim.fade_out_screen);
			}
		});
	}
    
    public boolean onCreateOptionsMenu(Menu menu) {
	   	// Inflate your menu.
	   getSupportMenuInflater().inflate(R.menu.go_pro_menu, menu);

	   // Set file with share history to the provider and set the share intent.
	   MenuItem actionItem = menu.findItem(R.id.wm_go_pro);
	   actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	   actionItem.setTitle("Go Pro!");
	   //actionItem.set
		
	   return true;
   }
    
   @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
 	 {       
 	      if(menuItem.getItemId() == R.id.wm_go_pro)
 	      {
 	    	  //redirect to pro version
 	    	  Intent intent = new Intent(Intent.ACTION_VIEW);
 			  intent.setData(Uri.parse("market://details?id=com.polymorphicstudios.corefitnesspro"));
 			  startActivity(intent);
 	      }
 			
 		  return true;
    }
    
     @Override
	 public void onBackPressed() {
    	   CustomDialog.decisionDialog("Exit app?", fm);  
	 }
    
   
}