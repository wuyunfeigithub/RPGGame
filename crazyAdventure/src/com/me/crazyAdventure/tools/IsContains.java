package com.me.crazyAdventure.tools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IsContains {
	private static Rectangle rec1 = new Rectangle();	
	private static Rectangle rec2 = new Rectangle();
	private static Vector2 point = new Vector2();
	private static Vector3 point1 = new Vector3();
	
	public IsContains(){
		rec1 = new Rectangle();
		rec2 = new Rectangle();
		point = new Vector2();
		point1 = new Vector3();
	}
	
	public static boolean isContains(Rectangle rec, Vector2 point){
		return rec.contains(point);
	}
	public static boolean isContains(Rectangle rec, float point_x, float point_y){
		return rec.contains(point_x, point_y);
	}
	public static boolean isContains(float rectangle_x, float rectangle_y, float rectangle_width, float rectangle_height, float point_x, float point_y){
		rec1.x = rectangle_x;
		rec1.y = rectangle_y;
		rec1.width = rectangle_width;
		rec1.height = rectangle_height;
		return rec1.contains(point_x, point_y);
	}
	public static boolean isContains(float rectangle_x, float rectangle_y, float rectangle_width, float rectangle_height, Vector2 point){
		rec1.x = rectangle_x;
		rec1.y = rectangle_y;
		rec1.width = rectangle_width;
		rec1.height = rectangle_height;
		return rec1.contains(point);
	}
	
	public static boolean isContains(Rectangle rec1, Rectangle rec2){
		return rec1.overlaps(rec2);		
	}
}
