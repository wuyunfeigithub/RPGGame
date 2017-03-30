package com.me.crazyAdventure.elements;

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.me.crazyAdventure.screens.GameScreen;

public class EnemyManager {

	private Vector<Vector2> enemyPositions;
	private Vector<Vector2> trapPositions;
	private Vector2 bossPostion;
	private Hero hero;
	public static Group enemySet;
	public static Group shotSet;
	GameScreen gameScreen;

	public EnemyManager(Hero hero, GameScreen gameScreen) {
		this.hero = hero;
		this.gameScreen = gameScreen;
		enemySet = new Group();
		shotSet = new Group();

		shotSet.addAction(Actions.forever(Actions.run(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (Actor shot : shotSet.getChildren()) {
					if (Hero.heroArea.contains(shot.getX() + shot.getOriginX(),
							shot.getY() + shot.getOriginY())) {
						// 英雄被击中
						Hero.currentHp -= 10;
						if (Hero.currentHp < 0) {
							Hero.currentHp = 0;
						}
						shotSet.removeActor(shot);
					}
				}
			}

		})));
	}

	public void loadLevelInfo(Vector<Vector2> positions, Vector<Vector2> trapPositions, Vector2 bossPosition) {
		enemyPositions = positions;
		this.bossPostion = bossPosition;
		this.trapPositions = trapPositions;
	}

	public void createEnemys() {
		// 初始化敌人
		for (Vector2 position : enemyPositions) {
			// 加入普通敌人
			Enemy tmp = new Enemy(position, hero, gameScreen, this);
			enemySet.addActor(tmp);
		}
		// 加入boss
		if (gameScreen.mapName.equals("level1")) {
			Boss bossTmp = new Boss(bossPostion, hero, gameScreen, this);
			bossTmp.setBossProperties("boss1", 110, 110, 2000, 2000);

			SequenceAction Sequence = Actions.sequence(
					myActions.moveDown(bossTmp), Actions.delay(5),
					myActions.moveUp(bossTmp), Actions.delay(5));
			RepeatAction forever = Actions.forever(Sequence);
			bossTmp.addAction(forever);
			
			SequenceAction Sequence1 = Actions.sequence(myActions.attack_1(bossTmp), Actions.delay(2f));
			RepeatAction forever1 = Actions.forever(Sequence1);
			bossTmp.addAction(forever1);

			enemySet.addActor(bossTmp);

		} else if (gameScreen.mapName.equals("level2")) {
			//加入boss
			Boss bossTmp = new Boss(bossPostion, hero, gameScreen, this);
			bossTmp.setBossProperties("boss2", 70, 100, 2000, 2000);
			
            bossTmp.addAction(myActions.moveToHero(bossTmp));
           
			SequenceAction Sequence = Actions.sequence(myActions.attack_1(bossTmp), Actions.delay(2f));
			RepeatAction forever1 = Actions.forever(Sequence);
			bossTmp.addAction(forever1);
			
			enemySet.addActor(bossTmp);
			//加入机关
			for (Vector2 position : trapPositions) {
				Trap tmp = new Trap(position.x, position.y);
				enemySet.addActor(tmp);
			}
		} else if (gameScreen.mapName.equals("level3")) {
			Boss bossTmp = new Boss(bossPostion, hero, gameScreen, this);
			bossTmp.setBossProperties("boss3", 70, 100, 2000, 2000);
			SequenceAction Sequence = Actions.sequence(myActions.moveStyle_1(bossTmp), myActions.moveBack(bossTmp));
			RepeatAction forever = Actions.forever(Sequence);
			
			SequenceAction Sequence1 = Actions.sequence(myActions.attack_2(bossTmp, hero, gameScreen, this), Actions.delay(3f));
			RepeatAction forever1 = Actions.forever(Sequence1);
			
			bossTmp.addAction(forever);
			bossTmp.addAction(forever1);
			 
			enemySet.addActor(bossTmp);
		} else if (gameScreen.mapName.equals("level4")) {
			Boss bossTmp = new Boss(bossPostion, hero, gameScreen, this);
			bossTmp.setBossProperties("boss4", 70, 100, 0.8f, 2000, 2000);
			
			bossTmp.addAction(myActions.moveToHero(bossTmp));
			
			SequenceAction Sequence = Actions.sequence(myActions.attack_3(bossTmp), Actions.delay(1.5f));
			RepeatAction forever = Actions.forever(Sequence);
			bossTmp.addAction(forever);
			
			enemySet.addActor(bossTmp);
		} else if (gameScreen.mapName.equals("level5")) {
			Boss bossTmp = new Boss(bossPostion, hero, gameScreen, this);
			bossTmp.setBossProperties("hero", 40, 60, 0.5f, 2000, 2000);
			
			SequenceAction Sequence = Actions.sequence(myActions.moveStyle_1(bossTmp), myActions.moveBack(bossTmp));
			RepeatAction forever = Actions.forever(Sequence);
			
			bossTmp.addAction(forever);
			
			SequenceAction Sequence1 = Actions.sequence(myActions.attack_4(bossTmp), Actions.delay(1.5f));
			RepeatAction forever1 = Actions.forever(Sequence1);
			
			bossTmp.addAction(forever1);
			
			enemySet.addActor(bossTmp);
		}
	}

	public void removeAll() {
		if (enemySet.hasChildren()) {
			enemySet.clearChildren();
		}
		if (enemyPositions.size() != 0) {
			enemyPositions.removeAllElements();
		}
		if (shotSet.hasChildren()) {
			shotSet.clearChildren();
		}
	}
}
