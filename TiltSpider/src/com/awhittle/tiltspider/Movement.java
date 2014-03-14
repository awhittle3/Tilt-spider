package com.awhittle.tiltspider;

public class Movement {

	//private static int previousDir = 0;
	
	public static void moveSnake(int dir) {
		
		float units = 0.1f;
		
		//Do not suddenly reverse direction, do a u-turn
		/*if (dir == oppositeDir(previousDir)){
			if (dir != 4) {
				dir += 1;
			} else {
				dir = 1;
			}
		}*/
		
		if (MainActivity.isPhone){
			//Device is likely a phone with default portrait orientation
			if (!MainActivity.insanityFlag){
				switch(dir){
					case 1: setHeadCoords(units, 0.0f);
					setHeadLocation(1,0);
						break;
					case 2:	setHeadCoords(0.0f, units);
					setHeadLocation(0,1);
						break;
					case 3:	setHeadCoords(-1.0f * units, 0.0f);
					setHeadLocation(-1,0);
						break;
					case 4:	setHeadCoords(0.0f, -1.0f*units);
					setHeadLocation(0,-1);
						break;
				} 
			} else { //Insanity mode active. Reverse directions!
				switch(dir){
					case 1:	setHeadCoords( -1.0f * units, 0.0f);
					setHeadLocation(-1,0);
						break;
					case 2:setHeadCoords(0.0f, -1.0f * units);
					setHeadLocation(0,-1);
						break;
					case 3:	setHeadCoords( units, 0.0f);
					setHeadLocation(1,0);
						break;
					case 4:	setHeadCoords( 0.0f, units);
					setHeadLocation(0,1);
						break;
				}
			}
			
		} else {
			//Device is likely a tablet with default landscape orientation
			if (!MainActivity.insanityFlag){
				switch(dir){
					case 1:	setHeadCoords( 0.0f, units);
					setHeadLocation(0,1);
						break;
					case 2:setHeadCoords( -1.0f * units, 0.0f);
					setHeadLocation(-1,0);
						break;
					case 3:setHeadCoords( 0.0f, -1.0f*units);
					setHeadLocation(0,-1);
						break;
					case 4:	setHeadCoords( units, 0.0f);
					setHeadLocation(1,0);
						break;
				}
			} else { //Insanity mode active. Reverse directions!
				switch(dir){
					case 1:	setHeadCoords( 0.0f, -1.0f*units);
					setHeadLocation(0,-1);
						break;
					case 2:setHeadCoords( units, 0.0f);
					setHeadLocation(1,0);
						break;
					case 3:setHeadCoords( 0.0f, units);
					setHeadLocation(0,1);
						break;
					case 4:	setHeadCoords(-1.0f*units, 0.0f);
					setHeadLocation(-1,0);
						break;
				}
			}
		}
		
		//previousDir = dir;
		
	}

	private static void setHeadCoords(float x, float y) {
		
		Head.headCoords = ShapeTools.translateMatrix(Head.headCoords, x, y);
		
	}

	private static void setHeadLocation(int x, int y) {
		Head.headLocation[0] += x;
		Head.headLocation[1] += y;
	}

	/*private static int oppositeDir(int direction) {
		switch(direction){
		case 1:	direction = 3;
			break;
		case 2:	direction = 4;
			break;
		case 3:	direction = 1;
			break;
		case 4:	direction = 2;
			break;
		}
		
		return direction;
	}*/
}
