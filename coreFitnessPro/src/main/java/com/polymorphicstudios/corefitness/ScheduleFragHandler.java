package com.polymorphicstudios.corefitness;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.gms.ads.AdView;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.common.CustomDialog;
import com.polymorphicstudios.fragment.PageAdapter_Tabs;
import com.polymorphicstudios.fragment.WeekFragment;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;
import java.util.Vector;

public class ScheduleFragHandler extends SherlockFragmentActivity{

	private String[] TAB_TITLES = new String[] { "Week 1", "Week 2", "Week 3"};
	 
	 ViewPager mPager;
	 PageIndicator mIndicator;
	 private PagerAdapter mPagerAdapter;
	 public DisplayMetrics dm;
	 
	 private Bundle messageBundle;
     private FragmentManager fm;
     
     private Typeface face;
     
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        setContentView(R.layout.simple_tabs);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        super.onCreate( savedInstanceState );
			
	        //Set the ACTION BAR
		 	com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		 	actionBar.setDisplayHomeAsUpEnabled(true);
	        actionBar.setLogo(R.drawable.icon_muscle_w);
	        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>schedule</font>"));

	        try{ //add a custom font to title
	        	face = Typeface.createFromAsset(getAssets(), Constants.LANGDON); 
	        	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
	        	TextView yourTextView = (TextView) findViewById(titleId);
	        	yourTextView.setTextColor(getResources().getColor(R.color.white));
	        	yourTextView.setText("schedule");
	        	yourTextView.setTypeface(face);
	        }
			catch(Exception e)
			{
				
			}

			 AdView mAdView = (AdView) findViewById(R.id.adView);
		 	com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
		 	mAdView.loadAd(adRequest);
	        
	        //initialize the display metrics
	    	fm = getSupportFragmentManager();
		    dm = new DisplayMetrics(); 
	      	getWindowManager().getDefaultDisplay().getMetrics(dm); 
		    
			Fragment frag = new Fragment();
	        List<Fragment> fragments = new Vector<Fragment>();
	        
	        //Add all fragments to a fragment list
	        for(int i = 0; i < TAB_TITLES.length; i++)
	        {
	        	Bundle bundle_args = new Bundle();
	        	bundle_args.putInt("WEEK_NUMBER",  i+1);
	        	bundle_args.putInt("SCREEN_HEIGHT", dm.heightPixels);
	        	bundle_args.putInt("SCREEN_WIDTH", dm.widthPixels);
	        	frag = Fragment.instantiate(this, WeekFragment.class.getName());
	 	        frag.setArguments(bundle_args);
	 	        fragments.add(frag);
	        }
	        
	        this.mPagerAdapter  = new PageAdapter_Tabs(super.getSupportFragmentManager(), fragments, TAB_TITLES);
	        
	        mPager = (ViewPager)findViewById(R.id.pager);
	        mPager.setAdapter(this.mPagerAdapter);
	 
	        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
	        mIndicator.setViewPager(mPager);
	 }
	 
	 @Override
     public void onBackPressed() {
				super.onBackPressed();
				overridePendingTransition(R.anim.fade_in_screen, R.anim.fade_out_screen);
				finish();
				
     }
	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		  /* MenuInflater inflater = getSupportMenuInflater();
	       inflater.inflate(R.menu.info_action_provider, menu);*/
	       return super.onCreateOptionsMenu(menu);
	 }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem menuItem)
	 {       
	      if(menuItem.getItemId() == android.R.id.home)
	      {
		    	  super.onBackPressed();
		          overridePendingTransition(R.anim.translate_left_onscreen, R.anim.translate_right_offscreen);
				  finish();
		   }
		  return true;
	 }

	 public void sendTimedDialog(int programID, int typeID)
	 {
		 CustomDialog.timerDescisionDialog("Do you want the routine to be timed", programID, typeID, fm);
	 }
	 
}
