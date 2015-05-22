package com.polymorphicstudios.corefitness;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polymorphicstudios.actor.Recipe;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.db.DatabaseHandler;

import java.io.IOException;

public class RecipeActivity extends SherlockFragmentActivity{

	private ImageView recipeIm;
	private TextView recipeName;
	private TextView recipeDescription;
	private TextView recipeServing;
	private TextView recipeInstructions;
	private TextView recipeIngredients;
	private TextView recipeTitleInstructions;
	private TextView recipeTitleIngrediants;
	String imageString = "";
	
	private DisplayMetrics dm;
	private int screenWidth;
	
	private Typeface font;
	private Typeface font_light;
	private Bundle messageBundle;
	private FragmentManager fm;
    DisplayImageOptions options;

	private DatabaseHandler db;
	private Recipe recipe;
	private int recipeId;
	private Typeface face;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    	this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        setContentView(R.layout.recipe_screen);
			
	        //Set the ACTION BAR
		 	com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		 	actionBar.setDisplayHomeAsUpEnabled(true);
		 	actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>recipes</font>"));
		 	actionBar.setLogo(R.drawable.icon_muscle_w);
	        
		 	try{ //add a custom font to title
	        	face = Typeface.createFromAsset(getAssets(), Constants.LANGDON); 
	        	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
	        	TextView yourTextView = (TextView) findViewById(titleId);
	        	yourTextView.setTextColor(getResources().getColor(R.color.white));
	        	yourTextView.setText("recipes");
	        	yourTextView.setTypeface(face);
	        }
			catch(Exception e)
			{
				
			}

			 Bundle extras = getIntent().getExtras();;
		 	 if (extras != null) {
				 recipeId = extras.getInt("RECIPE_ID");
			 }

		 	 //call database
		 	 db = new DatabaseHandler(this);
		 	 try {
				 db.createDataBase();
				 db.openDataBase();
			 } catch (IOException ioe) {
				 throw new Error("Unable to create database");
			 }catch(Exception sqle){
		 	 }
		 	recipe = db.getSingleRecipe(recipeId);

             options = new DisplayImageOptions.Builder()
                 .cacheInMemory(false)
                 .cacheOnDisk(false)
                 .considerExifParams(true)
                 .bitmapConfig(Bitmap.Config.RGB_565)
                 .build();
	        
	        //get screen metrics
	      	dm = new DisplayMetrics(); 
	      	getWindowManager().getDefaultDisplay().getMetrics(dm); 
	      	screenWidth = dm.widthPixels;
	        
	      	 //set font
	        font = Typeface.createFromAsset(getAssets(), Constants.GEARED); 
	        font_light = Typeface.createFromAsset(getAssets(), Constants.GEARED_LIGHT); 
	      	
	        recipeIm = (ImageView) findViewById(R.id.recipe_image);
	        recipeName = (TextView) findViewById(R.id.recipe_name);
	        recipeDescription = (TextView) findViewById(R.id.recipe_description);
	        recipeServing = (TextView) findViewById(R.id.recipe_serving);
	        recipeInstructions = (TextView) findViewById(R.id.instructions_text);
	        recipeIngredients = (TextView) findViewById(R.id.ingrediants_text);
	        recipeTitleInstructions = (TextView) findViewById(R.id.instructions_title_text);
	        recipeTitleIngrediants = (TextView) findViewById(R.id.ingrediants_title_text);
	        
	        recipeName.setTypeface(font);
	        recipeDescription.setTypeface(font_light);
	        recipeServing.setTypeface(font_light);
	        recipeInstructions.setTypeface(font);
	        recipeIngredients.setTypeface(font);
	        recipeTitleIngrediants.setTypeface(font);
	        recipeTitleInstructions.setTypeface(font);
	    	
	        //set image
	        recipeIm.getLayoutParams().height = (int) (screenWidth * 0.36);
	        recipeIm.getLayoutParams().width = (int) (screenWidth * 0.36);


            String tempInstructions = recipe.getInstructions().replace("\\n", System.getProperty("line.separator"));
            tempInstructions = tempInstructions.replace("\\r",  "");
            tempInstructions = tempInstructions.replace("\\",  System.getProperty("line.separator"));

            String tempIngredients = recipe.getIngredients().replace("\\n", System.getProperty("line.separator"));
            tempIngredients = tempIngredients.replace("\\r",  "");
            tempIngredients = tempIngredients.replace("\\",  System.getProperty("line.separator"));

            //set text of items
            recipeName.setText(recipe.getName());
            recipeInstructions.setText(tempInstructions + System.getProperty("line.separator"));
            recipeIngredients.setText(" " + tempIngredients + System.getProperty("line.separator"));
            recipeServing.setText(recipe.getServing().replace("\\n", System.getProperty("line.separator") + System.getProperty("line.separator")));
            recipeDescription.setText(recipe.getSummary());
            imageString = recipe.getIm_name();

            Resources resources = this.getResources();
            final int resourceId = resources.getIdentifier(recipe.getImageName().replace(".jpg", ""), "drawable",this.getPackageName());
            ImageLoader.getInstance().displayImage("drawable://" + resourceId,  recipeIm, options);

	    }
	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
         return true;
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
 	
 	@Override
 	public void onBackPressed() {
 			super.onBackPressed();
 	        overridePendingTransition(R.anim.translate_left_onscreen, R.anim.translate_right_offscreen);
 			finish();
 	}
}
