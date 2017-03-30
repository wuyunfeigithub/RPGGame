package com.me.crazyAdventure.elements;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Map {

	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRender;
	float mapWidth;
	float mapHeight;
	
	public Map(String path, float width,  float height){
		mapWidth = width;
		mapHeight = height;		
		this.loadMap(path);
	}
	
	public Map(TiledMap map, float width,  float height){
		mapWidth = width;
		mapHeight = height;		
		this.loadMap(map);		
	}
	
	public void loadMap(String path){
		map = new TmxMapLoader().load(path);
		mapRender = new OrthogonalTiledMapRenderer(map);
	}
	
	public void loadMap(TiledMap map){
		this.map = map;
		mapRender = new OrthogonalTiledMapRenderer(map);		
	}
	
	public void changeMap(TiledMap map, float width,  float height){
		mapWidth = width;
		mapHeight = height;		
		this.loadMap(map);	
	}
	
	public void mapRender(OrthographicCamera camera){
		mapRender.setView(camera);
		mapRender.render();
	}
	
    public TiledMap getMap(){
		return map;
	}
    
	public float getMapWidth(){
		return mapWidth;
	}
	
	public float getMapheight(){
		return mapHeight;
	}
	public void setMapWidth(float width){
	    mapWidth = width;
	}
	
	public void setMapheight(float height){
		mapHeight = height;
	}

	public void dipose(){
		map.dispose();
		mapRender.dispose();
	}
}
