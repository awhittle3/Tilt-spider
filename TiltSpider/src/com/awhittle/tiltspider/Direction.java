package com.awhittle.tiltspider;

import android.hardware.SensorEvent;

public class Direction {
	
	public static int getDirection(SensorEvent event){
		int dir;
		if (Math.abs(event.values[0]) > Math.abs(event.values[1])){
			if (event.values[0]>0)
				dir = 4; 	//Left
			else
				dir = 2;	//Right
		}
		else {
			if (event.values[1]>0)
				dir = 3;	//Bottom
			else
				dir = 1;	//Top
		}
		return dir;
	}

}
