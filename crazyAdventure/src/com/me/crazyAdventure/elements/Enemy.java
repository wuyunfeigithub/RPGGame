package com.me.crazyAdventure.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.crazyAdventure.screens.GameScreen;
import com.me.crazyAdventure.tools.Assets;
import com.me.crazyAdventure.tools.MyMath;
import com.me.crazyAdventure.tools.Recorder;

public class Enemy extends Actor {

	public float x;
	public float y;
	public float width;
	public float height;
	public Rectangle enemy_Area;
	public DIRECT direct;

	public int maxHp;
	public int currentHp;
	public int fireDelay = 0;

	public boolean isMove;

	Animation aniUp;
	Animation aniDown;
	Animation aniLeft;
	Animation aniRight;

	private float statetime;
	TextureRegion currentFrame;
	TextureRegion hp;
	TextureRegion hp_bg;
	TextureRegion fire;

	Hero hero;
	GameScreen gameScreen;
	EnemyManager manager;

	enum DIRECT {
		Up, Down, Left, Right
	}

	public Enemy(Vector2 position, Hero hero, GameScreen gameScreen, EnemyManager manager) {
		x = position.x;
		y = position.y;
		width = 40;
		height = 60;
		enemy_Area = new Rectangle(x, y, width, height);
		direct = DIRECT.Down;

		maxHp = 20;
		currentHp = 20;

		isMove = false;

		statetime = 0;
		
		this.gameScreen = gameScreen;
		this.manager = manager;
		this.hero = hero;
		
		setAnimation();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		statetime += Gdx.graphics.getDeltaTime();

		if (currentFrame!=null) {
			batch.draw(currentFrame, x, y, width, height);
		}
		batch.draw(hp, x, y + height, width * currentHp / maxHp, 6);
		batch.draw(hp_bg, x, y + height, width, 6);
	}

	public void act(float delta) {
		super.act(delta);

		this.check();
		this.update();
		moveToHero();
		beHurt();
		fire();
	}

	private void setAnimation() {
		TextureAtlas atlas = Assets.getAssetManager().get("data/pack.atlas",
				TextureAtlas.class);
		// 人物行走切图
		float delta = 0.1f;

		int random = MathUtils.random(1, 12);
		String Name = random + "";

		
		if(gameScreen.mapName.equals("level3")){
			Name = "boss3";
		}
		TextureRegion[][] tmp = atlas.findRegion(Name).split(160 / 4, 240 / 4);

		TextureRegion[] region_up = tmp[0];
		aniUp = new Animation(delta, region_up);
		TextureRegion[] region_down = tmp[1];
		aniDown = new Animation(delta, region_down);
		TextureRegion[] region_left = tmp[3];
		aniLeft = new Animation(delta, region_left);
		TextureRegion[] region_right = tmp[2];
		aniRight = new Animation(delta, region_right);
		// 血条
		hp = atlas.findRegion("hp");
		hp_bg = atlas.findRegion("hp_bg");
		fire = atlas.findRegion("enemy_shot");
	}

	private void check() {

		if (direct == DIRECT.Up) {
			currentFrame = aniUp.getKeyFrame(statetime, true);
		} else if (direct == DIRECT.Down) {
			currentFrame = aniDown.getKeyFrame(statetime, true);
		} else if (direct == DIRECT.Left) {
			currentFrame = aniLeft.getKeyFrame(statetime, true);
		} else if (direct == DIRECT.Right) {
			currentFrame = aniRight.getKeyFrame(statetime, true);
		}
	}

	private void update() {

		this.enemy_Area.setPosition(x, y);

		float speed = 1f;
		if (isMove) {
			if (direct == DIRECT.Left) {
				if (gameScreen.mapanalyzer.leftEnble(x - 1.5f, y, width,
						height / 2)) {
					x -= speed;
				}
			} else if (direct == DIRECT.Right) {
				if (gameScreen.mapanalyzer.rightEnble(x + 1.5f, y, width,
						height / 2)) {
					x += speed;
				}
			} else if (direct == DIRECT.Up) {
				if (gameScreen.mapanalyzer.upEnble(x, y + 1.5f, width,
						height / 2)) {
					y += speed;
				}
			} else if (direct == DIRECT.Down) {
				if (gameScreen.mapanalyzer.downEnble(x, y - 1.5f, width,
						height / 2)) {
					y -= speed;
				}
			}
		}

	}

	private void moveToHero() {

		if (MyMath.getLength(hero.x, hero.y, this.x, this.y) <= 20) {
			isMove = false;
			return;
		}

		if (MyMath.getLength(hero.x, hero.y, this.x, this.y) <= Recorder.height / 2) {

			if (Math.abs(hero.x - this.x) > 1) {
				if (hero.x >= this.x) {
					isMove = true;
					direct = DIRECT.Right;
					return;
				}
				if (hero.x < this.x) {
					isMove = true;
					direct = DIRECT.Left;
					return;
				}
			} else {
				if (hero.y >= this.y) {
					isMove = true;
					direct = DIRECT.Up;
					return;
				}
				if (hero.y < this.y) {
					isMove = true;
					direct = DIRECT.Down;
					return;
				}
			}
		} else {
			isMove = false;
		}
	}

	private void beHurt() {
		//被英雄普通攻击击中
		if (hero.isAttack && hero.attackArea.overlaps(enemy_Area) && hero.isLive) {
			if (currentHp>0) {
				currentHp--;
			}
			else {
				getParent().removeActor(this);
				
                if(hero.currentHp<=hero.maxHp-5){
                	hero.currentHp+=5;
				}
                if(hero.currentMagic<=hero.maxMagic-2){
                	hero.currentMagic+=2;
				}
                Hero.coins++;
			}
		}
		//被英雄大招击中
		if (hero.isBigAttack && hero.bigAttackArea.overlaps(enemy_Area) && hero.isLive) {
			if (currentHp>0) {
				currentHp--;
			}
			else {
				getParent().removeActor(this);
				
				if(hero.currentHp<=hero.maxHp-5){
                	hero.currentHp+=5;
				}
                if(hero.currentMagic<=hero.maxMagic-2){
                	hero.currentMagic+=2;
				}
                Hero.coins++;
			}
		}
	}

	private void fire(){
		if(MyMath.getLength(hero.x, hero.y, x, y) < height*2){
			if (fireDelay == 0) {
				manager.shotSet.addActor(AttackFactory.creatShot(fire, this));
				fireDelay = 60;
			}
			fireDelay --;
		}
	}
	
}
