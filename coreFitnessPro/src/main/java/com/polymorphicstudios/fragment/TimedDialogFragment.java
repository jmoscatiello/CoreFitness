package com.polymorphicstudios.fragment;

import com.polymorphicstudios.corefitness.ProgramRoutineHandler;
import com.polymorphicstudios.corefitness.R;
import com.polymorphicstudios.corefitness.WorkoutRoutineActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TimedDialogFragment extends DialogFragment {

	String message;
	int typeID;
	int programID;
	
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        
		    Bundle extras = getArguments();

		    
	    	if (extras != null) {
	         	message = extras.getString("MESSAGE");
	         	programID = extras.getInt("PROGRAM_ID");
	         	typeID = extras.getInt("TYPE_ID");
	        }
	    	System.out.println("here");
		    // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(message)
	               .setPositiveButton("Timed", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       getActivity().finish();
	                       Intent intent = new Intent(getActivity().getBaseContext(), WorkoutRoutineActivity.class);
	                       intent.putExtra("PROGRAM_ID", programID);
						   intent.putExtra("TYPE_ID", typeID);
	                       getActivity().startActivity(intent);
	       				   getActivity().overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);
	                       dismiss();
	                   }
	               })
	               .setNegativeButton("Not Timed", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   getActivity().finish();
	                       Intent intent = new Intent(getActivity().getBaseContext(), ProgramRoutineHandler.class);
	                       intent.putExtra("PROGRAM_ID", programID);
						   intent.putExtra("TYPE_ID", typeID);
	                       getActivity().startActivity(intent);
	       				   getActivity().overridePendingTransition(R.anim.translate_left_offscreen, R.anim.translate_right_onscreen);
	                       dismiss();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
}
