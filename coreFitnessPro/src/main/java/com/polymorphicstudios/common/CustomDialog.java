package com.polymorphicstudios.common;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.polymorphicstudios.fragment.CustomDialogFragment;
import com.polymorphicstudios.fragment.DecisionDialogFragment;
import com.polymorphicstudios.fragment.ExitDialogFragment;
import com.polymorphicstudios.fragment.TimedDialogFragment;

public class CustomDialog {

	static Bundle messageBundle; 
	
	public static void createCustomDialog(String message, String title, int drawable, FragmentManager fm)
	{
		messageBundle = new Bundle();
	    messageBundle.putString("MESSAGE",  message);
	    messageBundle.putString("TITLE", title);
	    messageBundle.putInt("DRAWABLE", drawable);
	    CustomDialogFragment fragment1 = new CustomDialogFragment();   
	    fragment1.setArguments(messageBundle);
	    fragment1.show(fm, ""); 
	}
	
	public static void createExitDialog(String message, String title, int drawable, FragmentManager fm)
	{
		messageBundle = new Bundle();
	    messageBundle.putString("MESSAGE",  message);
	    messageBundle.putString("TITLE", title);
	    messageBundle.putInt("DRAWABLE", drawable);
	    ExitDialogFragment fragment1 = new ExitDialogFragment();   
	    fragment1.setArguments(messageBundle);
	    fragment1.show(fm, ""); 
	}
	
	public static void decisionDialog(String message, FragmentManager fm)
	{
		messageBundle = new Bundle();
	    messageBundle.putString("MESSAGE",  message);
	    DecisionDialogFragment fragment1 = new DecisionDialogFragment();   
	    fragment1.setArguments(messageBundle);
	    fragment1.show(fm, ""); 
	}
	
	public static void timerDescisionDialog(String message, int programID, int typeID, FragmentManager fm)
	{
		messageBundle = new Bundle();
	    messageBundle.putString("MESSAGE",  message);
	    messageBundle.putInt("PROGRAM_ID", programID);
	    messageBundle.putInt("TYPE_ID",  typeID);
	    TimedDialogFragment fragment1 = new TimedDialogFragment();   
	    fragment1.setArguments(messageBundle);
	    fragment1.show(fm, ""); 
	}
}
