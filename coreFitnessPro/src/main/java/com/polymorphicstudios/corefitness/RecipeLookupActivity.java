package com.polymorphicstudios.corefitness;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polymorphicstudios.actor.Recipe;
import com.polymorphicstudios.common.Constants;
import com.polymorphicstudios.db.DatabaseHandler;

import java.io.IOException;
import java.util.ArrayList;

public class RecipeLookupActivity extends SherlockActivity {

    DatabaseHandler db;
    DisplayImageOptions options;
    private ArrayList<Recipe> recipeList;
    public Context c;

    //The muscle the user clicked on
    private String selectedMuscle;

    TextView actionBarTitle;
    Typeface face;
    GridView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_lookup);

        face = Typeface.createFromAsset(getAssets(), Constants.LANGDON);
        //Set the ACTION BAR
        com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setLogo(R.drawable.icon_muscle_w);
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Recipe Lookup</font>"));

        try{
            int titleId = getResources().getIdentifier("action_bar_title", "id","android");
            TextView yourTextView = (TextView) findViewById(titleId);
            yourTextView.setTextColor(getResources().getColor(R.color.white));
            yourTextView.setText("core fitness");
            yourTextView.setTypeface(face);
        }
        catch(Exception e)
        {

        }
        c = getApplicationContext();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        recipeList = new ArrayList<Recipe>();
        listView = (GridView) findViewById(R.id.gridview);
    }

    private class CreateRecipeTask extends AsyncTask<String, Void, String> {
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
            }catch(Exception sqle){

            }

            recipeList = db.getAllRecipeIds();

            db.close();

            return "";
        }

        //run when the data is pulled online
        @Override
        protected void onPostExecute(String result) {
            // Getting adapter by passing xml data ArrayList

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getBaseContext(), RecipeActivity.class);
                    intent.putExtra("RECIPE_ID", recipeList.get(position).getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);

                }
            });
            ((GridView) listView).setAdapter(new ImageAdapter());

           setProgressBarIndeterminateVisibility(false);
        }
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        TextView textView;
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return recipeList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                holder.textView = (TextView) view.findViewById(R.id.workout_title);
                view.setTag(holder);
                //view.setTag(imageUrls.get(position).getTrackArtURL());
            } else {
                holder = (ViewHolder) view.getTag();
            }

            Resources resources = c.getResources();
            final int resourceId = resources.getIdentifier(recipeList.get(position).getImageName().replace(".jpg", ""), "drawable",c.getPackageName());
            holder.textView.setText(recipeList.get(position).getName()); //imageUrls.get(position).getTrackName()
            ImageLoader.getInstance().displayImage("drawable://" + resourceId,  holder.imageView, options);
            holder.progressBar.setVisibility(View.GONE);
            //view.setTag((int) recipeList.get(position).getworkoutID());
            return view;
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        CreateRecipeTask task = new CreateRecipeTask();
        task.execute(new String[] { "" });
    }
}
