package com.me.crazyAdventure.tools;

import java.util.Vector;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class MapAnalyzer {

	private static int[][] collision;
	private Vector2 hero;
	private Vector2 boss;
	private Vector<Vector2> enemys;
	private Vector<Vector2> traps;
	private int rowCounts; 
	private int colCounts; 
	private int tile_width; 
	private int tile_height;
	

	public MapAnalyzer(TiledMap map, int rowCounts, int colCounts, int tile_width, int tile_height) {

		enemys = new Vector<Vector2>();
		traps = new Vector<Vector2>();
		this.update(map, rowCounts, colCounts, tile_width, tile_height);
	}
	
	public void update(TiledMap map, int rowCounts, int colCounts, int tile_width, int tile_height){
		
		collision = new int[rowCounts][colCounts];
		this.rowCounts = rowCounts;
		this.colCounts = colCounts;
		this.tile_width = tile_width;
		this.tile_height = tile_height;
		
		
		MapLayers layers = map.getLayers();
		for (MapLayer layer : layers) {
			//分析碰撞
			if (layer.getName().equals("collision")) {
				if (layer instanceof TiledMapTileLayer) {

					TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
					// j为高（行） i为宽（列）
					for (int j = this.rowCounts-1; j >= 0; j--) {
						for (int i = 0; i < this.colCounts; i++) {
							// getCell(列，行) 纵坐标翻转
							Cell cell = tileLayer.getCell(i, j);
							if (cell != null) {
								collision[j][i] = 1;
							}
						}
					}
				}
			}
			//分析objs
			else if(layer.getName().equals("objects")){
				MapObjects objects = layer.getObjects();
				for (MapObject object : objects) {

					RectangleMapObject ro = (RectangleMapObject) object;
					if (ro.getName().equals("hero")) {
						Vector2 tmp = new Vector2();
						tmp.x = ro.getRectangle().x;
						tmp.y = ro.getRectangle().y;	
						hero = tmp;
					}
					else if (ro.getName().equals("enemy")) {
						Vector2 tmp = new Vector2();
						tmp.x = ro.getRectangle().x;
						tmp.y = ro.getRectangle().y;	
						enemys.add(tmp);
					} 
					else if (ro.getName().equals("boss")){
						Vector2 tmp = new Vector2();
						tmp.x = ro.getRectangle().x;
						tmp.y = ro.getRectangle().y;
						boss = tmp;
					}
					else if (ro.getName().equals("trap")){
						Vector2 tmp = new Vector2();
						tmp.x = ro.getRectangle().x;
						tmp.y = ro.getRectangle().y;
						traps.add(tmp);
					}
				}
			}
		}
		
//		for (int i = 0; i < rowCounts; ++i)
//			for (int j = 0; j < colCounts; ++j) {
//				{
//					if (j % colCounts == 0)
//						System.out.println();
//					System.out.print(collision[i][j] + "+");
//
//				}
//			}
//		System.out.println();
//		System.out.println("hero: " + hero.x + " " + hero.y);
//
//		for (Vector2 v : enemys) {
//			int i = 1;
//			System.out.println("enemy" + i + ": " + v.x + " " + v.y);
//			++i;
//		}
	}
	
	public Vector2 getHeroPosition(){
		
		return hero;		
	}
	
    public Vector2 getBossPosition(){
		
		return boss;		
	}
	
	public Vector<Vector2> getEnemysPosition(){
		
		return enemys;		
	}

    public Vector<Vector2> getTrapsPosition(){
		
		return traps;		
	}
	
	private boolean passEnble(float x1, float y1, float x2, float y2, float x3,	float y3) {

		boolean isMove = false;
		
		int j1 = (int) y1 / tile_height;
		int i1 = (int) x1 / tile_width;
		int j2 = (int) y2 / tile_height;
		int i2 = (int) x2 / tile_width;
		int j3 = (int) y3 / tile_height;
		int i3 = (int) x3 / tile_width;
		
		if(j1>rowCounts-1) j1=rowCounts-1;
		if(i1>colCounts-1) i1=colCounts-1;
		if(j2>rowCounts-1) j2=rowCounts-1;
		if(i2>colCounts-1) i2=colCounts-1;
		if(j3>rowCounts-1) j3=rowCounts-1;
		if(i3>colCounts-1) i3=colCounts-1;
		
		if (collision[j1][i1] == 0
				&& collision[j2][i2] == 0
				&& collision[j3][i3] == 0)
			isMove = true;
		return isMove;
	}

	public boolean upEnble(float x1, float y1, float width, float height) {
		
		y1 = y1 + height;
		float x2 = (x1+width/2f) ;
		float y2 = y1;
		float x3 = x1+width;
		float y3 = y1;
		
		return this.passEnble(x1+0.03f, y1-0.03f, x2, y2-0.03f, x3-0.03f, y3-0.03f);
	}

	public boolean downEnble(float x1, float y1, float width, float height) {
		
		float x2 = (x1+width/2f) ;
		float y2 = y1;
		float x3 = x1+width;
		float y3 = y1;
		
		return this.passEnble(x1+0.03f, y1+0.03f, x2, y2+0.03f, x3-0.03f, y3+0.03f);
	}

	public boolean leftEnble(float x1, float y1, float width, float height) {
		
		float x2 = x1;
		float y2 = (y1+height/2f);
		float x3 = x1;
		float y3 = y1+height;
		
		return this.passEnble(x1+0.03f, y1+0.03f, x2+0.03f, y2, x3+0.03f, y3-0.03f);
	}

	public boolean rightEnble(float x1, float y1, float width, float height) {
		
		x1 = x1+width;
		float x2 = x1;
		float y2 = (y1+height/2f);
		float x3 = x1;
		float y3 = y1+height;
		
		return this.passEnble(x1-0.03f, y1+0.03f, x2-0.03f, y2, x3-0.03f, y3-0.03f);
	}
}
