package com.awhittle.tiltspider;

import android.content.SharedPreferences;

public class Saving {

	//Save high score for active flag if counter is higher than old high score
	public static void saveScore(SharedPreferences settings, int score){
		
		saveThis(settings, score);
		
		if(MainActivity.insanityFlag){
			if (GameActivity.counter > MainActivity.insaneHS){
				saveInsaneHS(settings, score);
			}
		} else if(MainActivity.hardFlag){
			if (GameActivity.counter > MainActivity.hardHS){
				saveHardHS(settings, score);
			}
		} else {
			if (GameActivity.counter > MainActivity.normalHS){
				saveNormalHS(settings, score);
			}
		}
	}
	
	private static void saveNormalHS(SharedPreferences settings, int score){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("NormalHS", score);
		editor.commit();
	}
	
	private static void saveHardHS(SharedPreferences settings, int score){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("HardHS", score);
		editor.commit();
	}
	
	private static void saveInsaneHS(SharedPreferences settings, int score){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("InsaneHS", score);
		editor.commit();
	}
	private static void saveThis(SharedPreferences settings, int score){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("ThisScore", score);
		editor.commit();
	}
}
