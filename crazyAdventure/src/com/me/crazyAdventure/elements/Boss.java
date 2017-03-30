package com.me.crazyAdventure.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.crazyAdventure.screens.GameScreen;
import com.me.crazyAdventure.tools.Assets;
import com.me.crazyAdventure.tools.MyMath;

public class Boss extends Actor {

	String bossName;

	public float x;
	public float y;
	public Vector2 birthPosition;
	public float width;
	public float height;
	public Rectangle boss_Area;
	public DIRECT direct;
	float speed = 1f;

	public int maxHp;
	public int currentHp;

	public boolean isMove;
	public boolean isUpdate = false;

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

	public Boss(Vector2 position, Hero hero, GameScreen gameScreen,
			EnemyManager manager) {

		x = position.x;
		y = position.y;
		birthPosition = new Vector2(position);

		direct = DIRECT.Down;

		isMove = false;

		statetime = 0;

		this.hero = hero;
		this.gameScreen = gameScreen;
		this.manager = manager;
	}

	public void setBossProperties(String name, float width, float height,
			float speed, int maxHp, int currentHp) {
		bossName = name;
		this.width = width;
		this.height = height;
		this.speed = speed;
		boss_Area = new Rectangle(x, y, this.width, this.height);

		this.maxHp = maxHp;
		this.currentHp = currentHp;

		setAnimation();
	}

	public void setBossProperties(String name, float width, float height,
			int maxHp, int currentHp) {
		bossName = name;
		this.width = width;
		this.height = height;
		boss_Area = new Rectangle(x, y, this.width, this.height);

		this.maxHp = maxHp;
		this.currentHp = currentHp;

		setAnimation();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		statetime += Gdx.graphics.getDeltaTime();

		if (currentFrame == null) {
			System.out.println("null");
		}
		batch.draw(currentFrame, x, y, width, height);
		batch.draw(hp, x, y + height, width * currentHp / maxHp, 6);
		batch.draw(hp_bg, x, y + height, width, 6);
	}

	public void act(float delta) {

		if (!gameScreen.mapName.equals("level1")) {
			super.act(delta);
			this.check();
		} else {
			if (MyMath.getLength(Hero.x, Hero.y, this.x, this.y) <= 200) {
				super.act(delta);
			}
		}
		this.update();
		beHurt();
	}

	private void setAnimation() {
		TextureAtlas atlas = Assets.getAssetManager().get("data/pack.atlas",
				TextureAtlas.class);
		if (gameScreen.mapName.equals("level1")) {
			currentFrame = atlas.findRegion(bossName);
		} else {
			// 人物行走切图
			float delta = 0.1f;
			int region_width = 120;
			int region_height = 240;
			int cols = 3;
			if (gameScreen.mapName.equals("level4")) {
				region_width = 140;
				region_height = 200;
			} else if (gameScreen.mapName.equals("level5")) {
				region_width = 160;
				region_height = 264;
				cols = 4;
			}
			TextureRegion[][] tmp = atlas.findRegion(bossName).split(
					region_width / cols, region_height / 4);
			TextureRegion[] region_up = tmp[0];
			aniUp = new Animation(delta, region_up);
			TextureRegion[] region_down = tmp[1];
			aniDown = new Animation(delta, region_down);
			TextureRegion[] region_left = tmp[3];
			aniLeft = new Animation(delta, region_left);
			TextureRegion[] region_right = tmp[2];
			aniRight = new Animation(delta, region_right);
		}
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

		this.boss_Area.setPosition(x, y);

		isUpdate = false;

		if (isMove) {
			if (direct == DIRECT.Left) {
				if (gameScreen.mapanalyzer.leftEnble(x - 1.5f, y, width,
						height / 2)) {
					x -= speed;
					isUpdate = true;
				}
			} else if (direct == DIRECT.Right) {
				if (gameScreen.mapanalyzer.rightEnble(x + 1.5f, y, width,
						height / 2)) {
					x += speed;
					isUpdate = true;
				}
			} else if (direct == DIRECT.Up) {
				if (gameScreen.mapanalyzer.upEnble(x, y + 1.5f, width,
						height / 2)) {
					y += speed;
					isUpdate = true;
				}
			} else if (direct == DIRECT.Down) {
				if (gameScreen.mapanalyzer.downEnble(x, y - 1.5f, width,
						height / 2)) {
					y -= speed;
					isUpdate = true;
				}
			}
		}

	}

	private void beHurt() {
		// 被英雄普通攻击击中
		if (hero.isAttack && hero.attackArea.overlaps(boss_Area) && hero.isLive) {
			if (currentHp > 0) {

				currentHp--;

				if (hero.currentHp <= hero.maxHp - 0.1 && hero.currentHp>=1) {
					hero.currentHp += 0.1;
				}
				if (hero.currentMagic <= hero.maxMagic - 0.1) {
					hero.currentMagic += 0.1;
				}
			} else {
				getParent().removeActor(this);
				if(bossName.equals("boss1")){
					Hero.pack[0]++;
				} 
				else if(bossName.equals("boss2")){
					Hero.pack[1]++;
				}
				else if(bossName.equals("boss3")){
					Hero.pack[2]++;
				}
                else if(bossName.equals("boss4")){
                	Hero.pack[3]++;
				}
                else if(bossName.equals("hero")){
                	Hero.pack[4]++;
                }
			}
		}
		// 被英雄大招击中
		if (hero.isBigAttack && hero.bigAttackArea.overlaps(boss_Area)
				&& hero.isLive) {
			if (currentHp > 0) {
				currentHp--;

				if (hero.currentHp <= hero.maxHp - 0.1) {
					hero.currentHp += 0.1;
				}
				if (hero.currentMagic <= hero.maxMagic - 0.1) {
					hero.currentMagic += 0.1;
				}
			} else {
				getParent().removeActor(this);
				if(bossName.equals("boss1")){
					Hero.pack[0]++;
				} 
				else if(bossName.equals("boss2")){
					Hero.pack[1]++;
				}
				else if(bossName.equals("boss3")){
					Hero.pack[2]++;
				}
                else if(bossName.equals("boss4")){
                	Hero.pack[3]++;
				}
                else if(bossName.equals("hero")){
                	Hero.pack[4]++;
                }
			}
		}
	}

}