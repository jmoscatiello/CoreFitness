package com.polymorphicstudios.common;

import com.polymorphicstudios.corefitness.R;


public class Constants {

	public final static String DATABASE_PATH = "/data/data/com.polymorphicstudios.corefitness/databases/";
	public final static String DATABASE_NAME = "coredb.db";
	public final static String PUBLISHER_ID = "a15325d0943cf44";
	public final static String WORKOUT_TABLE_NAME = "workout";
	public final static String PROGRAM_TABLE_NAME = "program_description_table";
	public final static String PROGRAM_WORKOUT_TABLE_NAME = "program_workout_table";
	public final static String GEARED = "fonts/gothic_regular.ttf";
	public final static String GEARED_LIGHT = "fonts/gothic_thin.ttf";
	public final static String GEARED_BOLD = "fonts/gothic_bold.ttf";
	public final static String LANGDON = "fonts/Langdon.ttf";
	
	public final static String base64EncodedPublicKey = "";
	
	public static final String SHARED_PREFERENCE_NAME = "appSettings";
	
	//opening message key
	public static final String  OPENING_MESSAGE_KEY = "opening_message";
	public static final boolean OPENING_MESSAGE_SEEN = true;
	public static final boolean OPENING_MESSAGE_NOT_SEEN = false;
	
	//difficulty
	public static final String SETTING_DIFFICULTY_KEY = "difficulty";
	public static final int DIFFICULTY_BEGINNER = 0;
	public static final int DIFFICULTY_INTERMEDIATE = 1;
	
	//IN app purchase
	public static final String SETTING_PURCHASE_KEY = "inAppPurchase";
	public static final Boolean PURCHASE_VERSION_ONE_PURCHASED = true;
	public static final Boolean PURCHASE_VERSION_ONE_NOT_PURCHASED = false;
	
	
	//Themes
	//public static final int THEME_SHERLOCK = R.style.Theme_Sherlock;
	
	//allocate a work out id with a asset
	public static int selectWorkout(int i)
	{
		switch(i){
		case 1:  return R.drawable.w1;
		
		case 2:  return R.drawable.w2;
		
		case 3:  return R.drawable.w3;
		
		case 4:  return R.drawable.w4;
		
		case 5:  return R.drawable.w5;
		
		case 6:  return R.drawable.w6;
		
		case 7:  return R.drawable.w7;
		
		case 8:  return R.drawable.w8;
		
		case 9:  return R.drawable.w9;
		
		case 10:  return R.drawable.w10;
		
		case 11:  return R.drawable.w11;
		
		case 12:  return R.drawable.w12;
		
		case 13:  return R.drawable.w13;
		
		case 14:  return R.drawable.w14;
		
		case 15:  return R.drawable.w15;
		
		case 16:  return R.drawable.w16;
		
		case 17:  return R.drawable.w17;
		
		case 18:  return R.drawable.w18;
		
		case 19:  return R.drawable.w19;
		
		case 20:  return R.drawable.w20;
		
		case 21:  return R.drawable.w21;
		
		case 22:  return R.drawable.w22;
		
		case 23:  return R.drawable.w23;
		
		case 24:  return R.drawable.w24;
		
		case 25:  return R.drawable.w25;
		
		case 26:  return R.drawable.w26;
		
		case 27:  return R.drawable.w27;
		
		case 28:  return R.drawable.w28;
		
		case 29:  return R.drawable.w29;
		
		case 30:  return R.drawable.w30;
		
		case 31:  return R.drawable.w31;
		
		case 32:  return R.drawable.w32;
		
		case 33:  return R.drawable.w33;
		
		case 34:  return R.drawable.w34;
		
		case 35:  return R.drawable.w35;
		
		case 36:  return R.drawable.w36;
		
		case 37:  return R.drawable.w37;
		
		case 38:  return R.drawable.w38;
		
		case 39:  return R.drawable.w39;
		
		case 40:  return R.drawable.w40;
		
		case 41:  return R.drawable.w41;
		
		case 42:  return R.drawable.w42;
		
		case 43:  return R.drawable.w43;
		
		case 44:  return R.drawable.w44;
		
		case 45:  return R.drawable.w45;
		
		case 46:  return R.drawable.w46;
		
		case 47:  return R.drawable.w47;

		case 48:  return R.drawable.w48;
		
		case 49:  return R.drawable.w49;
		
		case 50:  return R.drawable.w50;
		
		case 51:  return R.drawable.w51;
		
		case 52:  return R.drawable.w52;
		
		case 53:  return R.drawable.w53;
		
		case 54:  return R.drawable.w54;
		
		case 55:  return R.drawable.w55;
		
		case 56:  return R.drawable.w56;
		
		case 57:  return R.drawable.w57;
		
		case 58:  return R.drawable.w58;
		
		case 59:  return R.drawable.w59;
		
		case 60:  return R.drawable.w60;
		
		case 61:  return R.drawable.w61;
		
		case 62:  return R.drawable.w62;
		
		case 63:  return R.drawable.w63;
		
		case 64:  return R.drawable.w64;
		
		case 65:  return R.drawable.w65;
		
		case 66:  return R.drawable.w66;
		
		case 67:  return R.drawable.w67;
		
		case 68:  return R.drawable.w68;
		
		case 69:  return R.drawable.w69;
		
		case 70:  return R.drawable.w70;
		
		case 71:  return R.drawable.w71;
		
		case 72:  return R.drawable.w72;
		
		case 73:  return R.drawable.w73;
		
		case 74:  return R.drawable.w74;
		
		case 75:  return R.drawable.w75;
		
		case 76:  return R.drawable.w76;
		
		case 77:  return R.drawable.w77;
		
		default: return R.drawable.w3;
		}
	}
	
	
	public static int selectWorkoutThumb(int i)
	{
	   switch(i){
		case 1:  return R.drawable.th1;
		
		case 2:  return R.drawable.th2;
		
		case 3:  return R.drawable.th3;
		
		case 4:  return R.drawable.th4;
		
		case 5:  return R.drawable.th5;
		
		case 6:  return R.drawable.th6;
		
		case 7:  return R.drawable.th7;
		
		case 8:  return R.drawable.th8;
		
		case 9:  return R.drawable.th9;
		
		case 10:  return R.drawable.th10;
		
		case 11:  return R.drawable.th11;
		
		case 12:  return R.drawable.th12;
		
		case 13:  return R.drawable.th13;
		
		case 14:  return R.drawable.th16;
		
		case 15:  return R.drawable.th16;
		
		case 16:  return R.drawable.th16;
		
		case 17:  return R.drawable.th17;
		
		case 18:  return R.drawable.th18;
		
		case 19:  return R.drawable.th19;
		
		case 20:  return R.drawable.th20;
		
		case 21:  return R.drawable.th21;
		
		case 22:  return R.drawable.th22;
		
		case 23:  return R.drawable.th23;
		
		case 24:  return R.drawable.th24;
		
		case 25:  return R.drawable.th25;
		
		case 26:  return R.drawable.th26;
		
		case 27:  return R.drawable.th27;
		
		case 28:  return R.drawable.th28;
		
		case 29:  return R.drawable.th29;
		
		case 30:  return R.drawable.th30;
		
		case 31:  return R.drawable.th31;
		
		case 32:  return R.drawable.th32;
		
		case 33:  return R.drawable.th33;
		
		case 34:  return R.drawable.th34;
		
		case 35:  return R.drawable.th35;
		
		case 36:  return R.drawable.th36;
		
		case 37:  return R.drawable.th37;
		
		case 38:  return R.drawable.th38;
		
		case 39:  return R.drawable.th39;
		
		case 40:  return R.drawable.th40;
		
		case 41:  return R.drawable.th41;
		
		case 42:  return R.drawable.th42;
		
		case 43:  return R.drawable.th43;
		
		case 44:  return R.drawable.th44;
		
		case 45:  return R.drawable.th45;
		
		case 46:  return R.drawable.th46;
		
		case 47:  return R.drawable.th47;
		
		case 48:  return R.drawable.th48;
		
		case 49:  return R.drawable.th49;
		
		case 50:  return R.drawable.th50;
		
		case 51:  return R.drawable.th51;
		
		case 52:  return R.drawable.th52;
		
		case 53:  return R.drawable.th53;
		
		case 54:  return R.drawable.th54;
		
		case 55:  return R.drawable.th55;
		
		case 56:  return R.drawable.th56;
		
		case 57:  return R.drawable.th57;
		
		case 58:  return R.drawable.th58;
		
		case 59:  return R.drawable.th59;
		
		case 60:  return R.drawable.th60;
		
		case 61:  return R.drawable.th61;
		
		case 62:  return R.drawable.th62;
		
		case 63:  return R.drawable.th63;
		
		case 64:  return R.drawable.th64;
		
		case 65:  return R.drawable.th65;
		
		case 66:  return R.drawable.th66;
		
		case 67:  return R.drawable.th67;
		
		case 68:  return R.drawable.th68;
		
		case 69:  return R.drawable.th69;
		
		case 70:  return R.drawable.th70;
		
		case 71:  return R.drawable.th71;
		
		case 72:  return R.drawable.th72;
		
		case 73:  return R.drawable.th73;
		
		case 74:  return R.drawable.th74;
		
		case 75:  return R.drawable.th75;
		
		case 76:  return R.drawable.th76;
		
		case 77:  return R.drawable.th77;
		
		default: return R.drawable.th1;
	  }
	}
	
	
	
	
}
