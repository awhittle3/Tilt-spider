package com.awhittle.tiltspider;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static boolean isPhone;
	public static boolean hardFlag;
	public static boolean insanityFlag;
	public static int stepTime;
	public static int normalHS;
	public static int hardHS;
	public static int insaneHS;
	public static final String PREF_NAME = "MyHighScores";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		isPhone = getDeviceType(); //True if phone, false if tablet
		
		//Load high scores
		SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
		normalHS = getHS(settings, "NormalHS");
		hardHS = getHS(settings, "HardHS");
		insaneHS = getHS(settings, "InsaneHS");
		setHS(R.id.textView3, normalHS);
		setHS(R.id.textView4, hardHS);
		setHS(R.id.textView5, insaneHS);
		
		//Display previous score, if there was one
		int thisScore = settings.getInt("ThisScore", 0);
		if (thisScore != 0){
			Toast.makeText(this, "GAME OVER\nScore: " + thisScore,Toast.LENGTH_LONG).show();
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("ThisScore", 0);
			editor.commit();
		}
			
	}

	@Override
	public void onBackPressed() {
		//End application
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private boolean getDeviceType() {
		//Test for large or xlarge screen
		if (((getResources().getConfiguration().screenLayout &
				Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) ||
				(getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 
				Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			//Device likely a tablet
			return false;

			} else {
				//Device likely a phone
				return true;
			}
	}
	
	private int getHS(SharedPreferences settings, String name){
	    int score = settings.getInt(name, 0);
	    return score;
	}
	
	private void setHS(int textViewID, int counter){
		TextView tv = (TextView)findViewById(textViewID);
		tv.setText(Integer.toString(counter));
	}
	

	public void gotoGameNormal(View v) {
		stepTime = 500;
		hardFlag = false;
		insanityFlag = false;
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void gotoGameHard(View v) {
		stepTime = 300;
		hardFlag = true;
		insanityFlag = false;
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void gotoGameInsanity(View v) {
		stepTime = 150;
		hardFlag = false;
		insanityFlag = true;
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	
}
