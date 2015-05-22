package com.polymorphicstudios.fragment;

import com.actionbarsherlock.view.Window;
import com.polymorphicstudios.corefitness.R;
import com.polymorphicstudios.common.ImageModification;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialogFragment extends DialogFragment{

	 String message;
	 String titleString;
	 int drawableIm;
	 
	 TextView titleTV;
	 TextView contentTV;
	 ImageView titleIV;
	 Button submitBtn;
	
	 DisplayMetrics dm;
	 int screenWidth;
	 
	 
	 
	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
	       
		  Bundle extras = getArguments();

	    	if (extras != null) {
	         	message = extras.getString("MESSAGE");
	         	titleString = extras.getString("TITLE");
	         	drawableIm = extras.getInt("DRAWABLE");
	        }
	    	  
	      //get display metrics
	      dm = new DisplayMetrics(); 
	      getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); 
	      screenWidth = dm.widthPixels;
		 
		  final Dialog dialog = new Dialog(getActivity());
		  dialog.getWindow().requestFeature((int) Window.FEATURE_NO_TITLE);
		  dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		  WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		 
	      dialog.setContentView(R.layout.custom_dialog_screen);  
	      dialog.getWindow().setBackgroundDrawable(  
	      new ColorDrawable(Color.WHITE));  
	    	  
	      titleTV = (TextView) dialog.findViewById(R.id.title_tv);  
	    	  contentTV = (TextView) dialog.findViewById(R.id.content_tv);  
	    	  titleIV =  (ImageView) dialog.findViewById(R.id.title_image);
	    	  submitBtn = (Button) dialog.findViewById(R.id.submit_button);
	    	  
	    	  contentTV.setText(message);
	    	  titleTV.setText(titleString);
	    	  
	    	  titleIV.getLayoutParams().height = (int) (screenWidth * 0.30);
	    	  titleIV.getLayoutParams().width = (int) (screenWidth * 0.30);
	    	  titleIV.setImageBitmap(ImageModification.decodeSampledBitmapFromResource(getResources(), drawableIm, (int)(screenWidth * 0.70), (int)(screenWidth * 0.70)));

	    	  submitBtn.setOnClickListener(new OnClickListener() {  
	    		  @Override  
	    		  public void onClick(View v) {  
	    	         dismiss();  
	    		  }  
	    	  }); 
	    	  
	    	  return dialog;  
	  }
	 
}
