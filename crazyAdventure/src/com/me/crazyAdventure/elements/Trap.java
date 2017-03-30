package com.me.crazyAdventure.elements;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.crazyAdventure.tools.MyMath;

public class Trap extends Actor{

	public int delay = 180;
	
	public Trap(float x, float y){
		super.setX(x);
		super.setY(y);
	}
	
	public void act(float delta) {
		if (MyMath.getLength(Hero.x, Hero.y, super.getX(), super.getY()) <= 200) {
			delay--;
			if (delay==0) {
				EnemyManager.enemySet.addActor(AttackFactory.creatTrap(this));
				if (delay == 0) {
					delay = 180;
				}
			}
		}
	}
	
	public float getX(){
		return super.getX();
	}
	
	public float getY(){
		return super.getY();
	}
}
