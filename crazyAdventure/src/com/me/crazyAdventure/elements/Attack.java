package com.me.crazyAdventure.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.crazyAdventure.tools.Assets;
import com.me.crazyAdventure.tools.Recorder;

public class Attack extends Actor {

	Animation fireAni;
	
	Rectangle attackArea;
	int width;
	int height;
	
	public boolean isLive = false;
	float statetime = 0;
	
	Boss owner;
	
	public Attack(Boss boss, int width, int height, String fileName){
		owner = boss;
		super.setX(boss.x);
		super.setY(boss.y);
		this.width = width;
		this.height = height;
		attackArea = new Rectangle(boss.x, boss.y, this.width, this.height = height);
		this.setAnimation(fileName);
	}
	
	public Attack(float x, float y, int width, int height, String fileName){
		super.setX(x);
		super.setY(y);
		this.width = width;
		this.height = height;
		attackArea = new Rectangle(x, y, this.width, this.height = height);
		this.setAnimation(fileName);
	}
	
	public void act(float delta){
		if(isLive){
			super.act(delta);
			this.Actions();
		}
	}
	
	public void draw(SpriteBatch batch, float parentAlpha){
		
		statetime += Gdx.graphics.getDeltaTime();
		
		if (isLive) {
			batch.draw(fireAni.getKeyFrame(statetime, true), super.getX(),
					super.getY());
		}
	}
	
	private void setAnimation(String fileName) {
		TextureAtlas atlas = Assets.getAssetManager().get("data/pack.atlas",
				TextureAtlas.class);
		
		float delta = 0.1f;		
//		float delta = Recorder.delta;	
		// ´óÕÐÇÐÍ¼
//		TextureRegion[][] tmp_bigfire = atlas.findRegion(fileName).split(
//				400 / 8, 60);
		TextureRegion[][] tmp_bigfire = atlas.findRegion(fileName).split(
				width, height);
		TextureRegion[] region_bigfire = tmp_bigfire[0];
		
		if(fileName.equals("fire_smoke")){
			
			switch (owner.direct){
			case Up:
				for(TextureRegion region: region_bigfire){
					region.flip(false, false);
				}
				setPosition(super.getX()+owner.width/2-width/2, super.getY()+owner.height+15);
				break;
			case Down:
				for(TextureRegion region: region_bigfire){
					region.flip(false, true);
				}
				setPosition(super.getX()+owner.width/2-width/2, super.getY()-height-5);
				break;
			}
			
		}
		else if(fileName.equals("fire_smoke_rotate")){
			int i = 0;
			region_bigfire = new TextureRegion[9];
			for(TextureRegion[] regions: tmp_bigfire)
				for(TextureRegion region: regions){
					region_bigfire[i++] = region;
					
				}
			switch (owner.direct){
			case Left:
				for(TextureRegion region: region_bigfire){
					region.flip(true, false);
				}
				setPosition(super.getX()-width-10, super.getY()+owner.height/2-height/2);
				break;
			case Right:
				for(TextureRegion region: region_bigfire){
					region.flip(false, false);
				}
				setPosition(super.getX()+owner.width+10, super.getY()+owner.height/2-height/2);
				break;
			}
			
		}
		else if(fileName.equals("bigfire")){
			attackArea.height = attackArea.height/6;
		}
		fireAni = new Animation(delta, region_bigfire);
	}

	private void Actions(){
		//ÒÆ¶¯
//    	super.setX(Hero.x); 
//    	super.setX(Hero.y);
    	attackArea.x = super.getX();
    	attackArea.y = super.getY();
    	//¹¥»÷
    	if(attackArea.overlaps(Hero.heroArea)){
    		Hero.currentHp -= 0.4;
    		if (Hero.currentHp < 0) {
				Hero.currentHp = 0;
			}
    	}    	
    }
	
	
	
}
