package com.awhittle.tiltspider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class GameActivity extends Activity {

    private GLSurfaceView mGLView;
	private SensorManager mManager;
	private Sensor mGravSensor;
	private static final int sType = Sensor.TYPE_ACCELEROMETER;	//Sensor type
	public static int dir;
	public static boolean snakeAlive = true;
	public static int counter = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Keep screen awake
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    
	    //Initialize sensor and sensor manager
		mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mGravSensor = mManager.getDefaultSensor(sType);
		
		// Create a GLSurfaceView instance and set it
	    // as the ContentView for this Activity.
	    mGLView = new MyGLSurfaceView(this);
	    setContentView(mGLView);
		
    }

	private SensorEventListener mSensorListener = new SensorEventListener() {
		
		@Override
	    public void onSensorChanged(SensorEvent event) {
			
			setDirection(event);
			
			//If snake is dead, end game
			if(!snakeAlive){
				gotoEnd(mGLView);
			}	
		}
	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    	//Not needed for game
	    }
	  };
	  
	  @Override
	  protected void onResume() {
		  super.onResume();
		  mGLView.onResume();
		  //Register sensor
		  mManager.registerListener(mSensorListener, mManager.getDefaultSensor(sType), SensorManager.SENSOR_DELAY_GAME);
	  }

	  @Override
	  protected void onPause() {
		  super.onPause();
		  mGLView.onPause();
		  //Unregister sensor
		  mManager.unregisterListener(mSensorListener);
	  }
	  
	  @Override
	  protected void onDestroy() {
		  super.onDestroy();
		  //Unregister sensor, if it hasn't been so already
		  if (mGravSensor != null) {
			  mManager.unregisterListener(mSensorListener);
		  } 
	  }
	  
	  @Override
	  public void onBackPressed(){
		  //End game
	      gotoEnd(mGLView); 
	  }
	  
		public void gotoEnd(View v) {
			//Save high score
			SharedPreferences settings = getSharedPreferences(MainActivity.PREF_NAME, 0);
			Saving.saveScore(settings, counter);
			
			//Unregister sensor, if it hasn't been so already
			if (mGravSensor != null) {
				mManager.unregisterListener(mSensorListener);
			} 
			  
			//Terminate activity
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			
			//Go back to start screen
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	  
	  private void setDirection(SensorEvent event){
		  dir = Direction.getDirection(event);
	  }
	  
}
