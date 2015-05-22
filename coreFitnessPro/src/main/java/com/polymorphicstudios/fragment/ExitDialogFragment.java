package com.polymorphicstudios.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ExitDialogFragment extends DialogFragment{

	 String message;
		
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        
		    Bundle extras = getArguments();

	    	if (extras != null) {
	         	message = extras.getString("MESSAGE");
	        }
	    	
		    // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(message)
	               .setPositiveButton("Complete Workout!", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       getActivity().finish();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
}
