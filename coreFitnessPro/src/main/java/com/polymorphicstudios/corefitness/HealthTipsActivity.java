package com.polymorphicstudios.corefitness;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.polymorphicstudios.actor.HealthTip;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.fragment.CustomDialogFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HealthTipsActivity extends SherlockFragmentActivity {

	private ListView list;
	private ListAdapter adapter;
	
	private ArrayList<HealthTip> tipArray = new ArrayList<HealthTip>();
	
	//set font typeface
    private Typeface fontNormal;
    
    private Bundle messageBundle;
	private FragmentManager fm;
	
	private Typeface face;
	
	private AdView adView;
    private LinearLayout adHolder;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.general_list_screen);
        
        //Set the ACTION BAR
	 	com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
	 	actionBar.setDisplayHomeAsUpEnabled(true);
	 	actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>health tips</font>"));
        actionBar.setLogo(R.drawable.icon_muscle_w);
	 	
        try{ //add a custom font to title
        	face = Typeface.createFromAsset(getAssets(), Constants.LANGDON); 
        	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        	TextView yourTextView = (TextView) findViewById(titleId);
        	yourTextView.setTextColor(getResources().getColor(R.color.white));
        	yourTextView.setText("health tips");
        	yourTextView.setTypeface(face);
        }
		catch(Exception e)
		{
			
		}
	    //grab from xml
        list=(ListView) findViewById(R.id.workout_list);
		//Set up ads
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

        adapter= new ListAdapter(this);
        
	    //show interminate progress
	    setSupportProgressBarIndeterminateVisibility(true);
	    
        //set font
    	fontNormal = Typeface.createFromAsset(getAssets(), Constants.GEARED);
        
        //run the async task
        DownloadWebPageTask task = new DownloadWebPageTask();
	    task.execute(new String[] { "http://www.pursefitness.com/php_scripts/tips_json.php" });
	}
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		//This async task downloads the Tips from the Net
		String json = "";
		@Override
	    protected String doInBackground(String... urls) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost(urls[0]);
			    HttpResponse response = httpclient.execute(httppost);
			    HttpEntity entity = response.getEntity();
			     
				if (entity != null) {
					json = EntityUtils.toString(entity);
				}
			} catch (ClientProtocolException e) {
				Log.e("Error", e.getMessage());
			} catch (IOException e) {
				Log.e("Error", e.getMessage());
			}
	      return json;
	    }
		
		//run when the data is pulled online
		@Override
	    protected void onPostExecute(String result) {
			 
			try {
				//put result into json array and parse
				JSONArray jsonArray = new JSONArray(result);
				JSONObject json_data;
				
				for (int i=jsonArray.length()-1; i >= 0; i--) {
					json_data = jsonArray.getJSONObject(i);
					tipArray.add( new HealthTip("Tip # " + (i + 1), json_data.getString("tip")));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			//Set the list
	        list.setAdapter(adapter);
	        
	        //stop the progress bar
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
			// TODO Auto-generated method stub
			return tipArray.size();
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
	        	view = inflater.inflate(R.layout.list_row_two_text, null);
	        }
			
			//Textview for workout name and body part
			TextView tipTitle = (TextView)view.findViewById(R.id.row_title); 
	        TextView tipContent = (TextView)view.findViewById(R.id.row_content);
	        
	        //set the font for the Title
	        tipTitle.setTypeface(fontNormal);
	        tipContent.setTypeface(fontNormal);
	        
	        // Setting all values in listview
	        tipTitle.setText(tipArray.get(position).getTitle().toUpperCase());
	        tipContent.setText(tipArray.get(position).getContent());
			
			return view;
		}
		
	}
    

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		   MenuInflater inflater = getSupportMenuInflater();
	       inflater.inflate(R.menu.info_action_provider, menu);
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
    	else if(menuItem.getItemId() == R.id.information_action_icon)
    	{
    		fm = getSupportFragmentManager();
 		    messageBundle = new Bundle();
 		    messageBundle.putString("MESSAGE",  getString(R.string.tips_dialog_text));
 		    messageBundle.putString("TITLE", "Health Tips Info");
 		    messageBundle.putInt("DRAWABLE", R.drawable.workoutloft_logo);
 		    CustomDialogFragment fragment1 = new CustomDialogFragment();   
 		    fragment1.setArguments(messageBundle);
 		    fragment1.show(fm, ""); 
    	}
		
		return true;
    }
    
    @Override
	public void onBackPressed() {
			super.onBackPressed();
	        overridePendingTransition(R.anim.translate_left_onscreen, R.anim.translate_right_offscreen);
			finish();
	}
	
}
