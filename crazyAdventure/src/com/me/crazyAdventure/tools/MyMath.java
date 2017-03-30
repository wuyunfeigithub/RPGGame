package com.me.crazyAdventure.tools;

import com.badlogic.gdx.math.Vector2;

public class MyMath {

	public static float getLength(Vector2 point1, Vector2 point2){
		return (float) Math.sqrt(Math.pow(point1.x - point2.x, 2)-Math.pow(point1.y - point2.y, 2));
	}
	
	public static int getLength(float x1, float y1, float x2, float y2){
		return (int) Math.sqrt((double)(x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
}
