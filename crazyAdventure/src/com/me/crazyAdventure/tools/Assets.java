package com.me.crazyAdventure.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

	public static AssetManager assetManager;
	
	public Assets(){
		if(assetManager == null){
			assetManager = new AssetManager();	
			assetManager.setLoader(TiledMap.class, new TmxMapLoader());
		}
		load();
	}
	
	public static AssetManager getAssetManager(){
		return assetManager;		
	}
	
	public static void load(){
		assetManager.load("data/pack.atlas", TextureAtlas.class);
		assetManager.load("data/defaultskin.json", Skin.class);
		assetManager.load("map/index1.tmx", TiledMap.class);
		assetManager.load("map/level1.tmx", TiledMap.class);
		assetManager.load("map/level2.tmx", TiledMap.class);
		assetManager.load("map/level3.tmx", TiledMap.class);
		assetManager.load("map/level4.tmx", TiledMap.class);
		assetManager.load("map/level5.tmx", TiledMap.class);
		assetManager.load("data/1.ogg", Music.class);
		assetManager.load("data/2.ogg", Music.class);
//		assetManager.load("data/rain11.jpg", Texture.class);
//		assetManager.load("data/rain12.jpg", Texture.class);
//		assetManager.load("data/rain13.jpg", Texture.class);
		assetManager.load("data/rain21.jpg", Texture.class);
		assetManager.load("data/rain22.jpg", Texture.class);
		assetManager.load("data/rain23.jpg", Texture.class);
	}
	
    public static void unLoad(){
    	assetManager.unload("data/pack.atlas");
    	assetManager.unload("data/defaultskin.json");
    	assetManager.unload("map/index1.tmx");
		assetManager.unload("map/level1.tmx");
		assetManager.unload("map/level2.tmx");
		assetManager.unload("map/level3.tmx");
		assetManager.unload("map/level4.tmx");
		assetManager.unload("map/level5.tmx");
		assetManager.unload("data/1.ogg");
		assetManager.unload("data/2.ogg");
//		assetManager.unload("data/rain11.jpg");
//		assetManager.unload("data/rain12.jpg");
//		assetManager.unload("data/rain13.jpg");
		assetManager.unload("data/rain21.jpg");
		assetManager.unload("data/rain22.jpg");
		assetManager.unload("data/rain23.jpg");
	}
    
}
