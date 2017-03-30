package com.me.crazyAdventure.tools;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class packWindow {

	public Window dialog;
	ArrayList<Vector2> position;
	
	public packWindow(){
		TextureRegionDrawable WindowDrable = new TextureRegionDrawable(Assets
				.getAssetManager().get("data/pack.atlas", TextureAtlas.class)
				.findRegion("wupin"));
		WindowStyle style = new WindowStyle(new BitmapFont(), Color.RED,
				WindowDrable);

		dialog = new Window("", style);
		dialog.setMovable(true);
		dialog.setSize(300, 300);
		dialog.setPosition((Recorder.width - dialog.getWidth()) / 2,
				(Recorder.height - dialog.getHeight()) / 2);
		dialog.setName("pack");
		
		Vector2 p1 = new Vector2(30, 225);
		Vector2 p2 = new Vector2(95, 203);
		Vector2 p3 = new Vector2(165, 203);
		Vector2 p4 = new Vector2(235, 203);
		Vector2 p5 = new Vector2(27, 140);
		Vector2 p6 = new Vector2(95, 140);
		
		position = new ArrayList<Vector2>(6);
		position.add(p1);
		position.add(p2);
		position.add(p3);
		position.add(p4);
		position.add(p5);
		position.add(p6);
	}
	
	public void add(Image actor){
		
		switch (dialog.getChildren().size){
		case 0:
			actor.setPosition(position.get(0).x, position.get(0).y);
			break;
		case 2:
			actor.setPosition(position.get(1).x, position.get(1).y);
			break;
		case 3:
			actor.setPosition(position.get(2).x, position.get(2).y);
			break;
		case 4:
			actor.setPosition(position.get(3).x, position.get(3).y);
			break;
		case 5:
			actor.setPosition(position.get(4).x, position.get(4).y);
			break;
		case 6:
			actor.setPosition(position.get(5).x, position.get(5).y);
			break;
		}
		dialog.addActor(actor);
	}
}
