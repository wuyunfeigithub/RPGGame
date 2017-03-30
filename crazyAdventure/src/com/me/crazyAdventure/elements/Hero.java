package com.me.crazyAdventure.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.me.crazyAdventure.screens.GameScreen;
import com.me.crazyAdventure.tools.Assets;
import com.sun.org.apache.bcel.internal.generic.ISUB;

public class Hero extends Actor implements Disposable {

	public static float x;
	public static float y;
	public float width;
	public float height;

	public float maxHp;
	public static float currentHp;
	public float maxMagic;
	public float currentMagic;

	public boolean isLive;
	public boolean isMove;
	public boolean isAttack;
	public boolean isBigAttack;
	private int bigFireLife;

	public Vector2 BFPosition;
	public static DIRECT direct;

	public static int pack[];
	public static int coins;
	
	public enum DIRECT {
		Up, Down, Left, Right
	}

	Animation aniUp;
	Animation aniDown;
	Animation aniLeft;
	Animation aniRight;

	Animation aniUpAttack;
	Animation aniDownAttack;
	Animation aniLeftAttack;
	Animation aniRightAttack;

	Animation bigfire;

	Sprite fireLeft;
	Sprite fireRight;
	Sprite fireUp;
	Sprite fireDown;

	public static Rectangle heroArea;
	public Rectangle attackArea;
	public Rectangle bigAttackArea;

	private float statetime;
	TextureRegion currentFrame;

	GameScreen gameScreen;

	public Hero(Vector2 position, GameScreen screen) {

		x = position.x;
		y = position.y;
		width = 40;
		height = 60;
		isMove = false;
		isAttack = false;
		isLive = true;
		direct = DIRECT.Right;

		maxHp = 100;
		currentHp = 100;
		maxMagic = 50;
		currentMagic = 50;

		isBigAttack = false;
		bigFireLife = 32;
		BFPosition = new Vector2(x, y);

		heroArea = new Rectangle(0, 0, width, height / 2);
		attackArea = new Rectangle();
		bigAttackArea = new Rectangle(0, 0, 100, 100);

		coins = 0;
		pack = new int[5];
//		pack[0] = 1;
//		pack[1] = 1;
//		pack[2] = 1;
//		pack[3] = 1;
//		pack[4] = 1;
		
		gameScreen = screen;
		setAnimation();
		setFireSprite();
	}

	public void act(float delta) {
		if (isLive) {
			super.act(delta);
			this.check();
			this.update();
		}
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		statetime += Gdx.graphics.getDeltaTime();

		if (isLive) {
			if (currentFrame!=null) {
				batch.draw(currentFrame, x, y, width, height);
			}
			if (isAttack) {
				drawFire(batch);
			}
			if (isBigAttack) {
				drawBigFire(batch);
			}
		} 
		else {
			batch.draw(
					Assets.getAssetManager()
							.get("data/pack.atlas", TextureAtlas.class)
							.findRegion("hero_death"), x, y, height, width);
		}
	}

	private void check() {

		if (isMove) {
			if (direct == DIRECT.Up) {
				currentFrame = aniUp.getKeyFrame(statetime, true);
			} else if (direct == DIRECT.Down) {
				currentFrame = aniDown.getKeyFrame(statetime, true);
			} else if (direct == DIRECT.Left) {
				currentFrame = aniLeft.getKeyFrame(statetime, true);
			} else if (direct == DIRECT.Right) {
				currentFrame = aniRight.getKeyFrame(statetime, true);
			}
		} else if (!isMove && !isAttack) {
			if (direct == DIRECT.Up) {
				currentFrame = aniUp.getKeyFrame(0, true);
			} else if (direct == DIRECT.Down) {
				currentFrame = aniDown.getKeyFrame(0, true);
			} else if (direct == DIRECT.Left) {
				currentFrame = aniLeft.getKeyFrame(0, true);
			} else if (direct == DIRECT.Right) {
				currentFrame = aniRight.getKeyFrame(0, true);
			}
		} else if (isAttack) {
			if (direct == DIRECT.Up) {
				currentFrame = aniUpAttack.getKeyFrame(statetime, true);
			} else if (direct == DIRECT.Down) {
				currentFrame = aniDownAttack.getKeyFrame(statetime, true);
			} else if (direct == DIRECT.Left) {
				currentFrame = aniLeftAttack.getKeyFrame(statetime, true);
			} else if (direct == DIRECT.Right) {
				currentFrame = aniRightAttack.getKeyFrame(statetime, true);
			}
		}

	}

	private void update() {

		if(currentHp<=0){
			isLive = false;
			gameScreen.uiStage.addActor(gameScreen.deathMessage);
		}
		if(coins>=10000){
			coins = 9999;
		}
		
		heroArea.x = x;
		heroArea.y = y;

		// float mapWidth = gameScreen.map.getMapWidth();
		// float mapHeight = gameScreen.map.getMapheight();

		float speed = 1.5f;
		if (isMove) {
			if (direct == DIRECT.Left) {
				if (gameScreen.mapanalyzer.leftEnble(x - 1.5f, y, width,
						height / 2)) {
					x -= speed;
				}
				// if (x < 0)
				// x = 0;
			} else if (direct == DIRECT.Right) {
				if (gameScreen.mapanalyzer.rightEnble(x + 1.5f, y, width,
						height / 2)) {
					x += speed;
				}
				// if (x > mapWidth - width)
				// x = mapWidth - width;
			} else if (direct == DIRECT.Up) {
				if (gameScreen.mapanalyzer.upEnble(x, y + 1.5f, width,
						height / 2)) {
					y += speed;
				}
				// if (y > mapHeight - height)
				// y = mapHeight - height;
			} else if (direct == DIRECT.Down) {
				if (gameScreen.mapanalyzer.downEnble(x, y - 1.5f, width,
						height / 2)) {
					y -= speed;
				}
				// if (y < 0)
				// y = 0;
			}
		}
	}

	private void drawFire(SpriteBatch batch) {

		int offset = 5;

		if (direct == DIRECT.Left) {
			fireLeft.setPosition(x - fireLeft.getWidth() / 2, y + height / 2
					- fireLeft.getHeight());
			fireLeft.draw(batch);

			attackArea.x = fireLeft.getX() - offset;
			attackArea.y = fireLeft.getY();
			attackArea.width = fireLeft.getWidth();
			attackArea.height = fireLeft.getHeight();

		} else if (direct == DIRECT.Right) {
			fireRight.setPosition(x + fireRight.getWidth() * 3 / 2, y + height
					/ 2 - fireRight.getHeight());
			fireRight.draw(batch);

			attackArea.x = fireRight.getX() + offset;
			attackArea.y = fireRight.getY();
			attackArea.width = fireRight.getWidth();
			attackArea.height = fireRight.getHeight();
		} else if (direct == DIRECT.Up) {
			fireUp.setPosition(x + fireRight.getWidth() / 4, y + height * 5 / 7);
			fireUp.draw(batch);

			attackArea.x = fireUp.getX();
			attackArea.y = fireUp.getY() + offset;
			attackArea.width = fireUp.getWidth();
			attackArea.height = fireUp.getHeight();
		} else if (direct == DIRECT.Down) {
			fireDown.setPosition(x + width / 2 + fireRight.getWidth() / 4, y);
			fireDown.draw(batch);

			attackArea.x = fireDown.getX();
			attackArea.y = fireDown.getY() - offset;
			attackArea.width = fireDown.getWidth();
			attackArea.height = fireDown.getHeight();
		}

	}

	private void drawBigFire(SpriteBatch batch) {
		batch.draw(bigfire.getKeyFrame(statetime, true), BFPosition.x - 32,
				BFPosition.y);

		bigFireLife--;
		if (bigFireLife == 0) {
			isBigAttack = false;
			bigFireLife = 32;
		}
	}

	private void setAnimation() {
		TextureAtlas atlas = Assets.getAssetManager().get("data/pack.atlas",
				TextureAtlas.class);
		// 人物行走切图
		// Texture hero = Assets.hero;
		float delta = 0.1f;
		// TextureRegion[][] tmp = TextureRegion.split(hero, 160 / 4, 264 / 4);
		TextureRegion[][] tmp = atlas.findRegion("hero")
				.split(160 / 4, 264 / 4);

		TextureRegion[] region_up = tmp[0];
		aniUp = new Animation(delta, region_up);
		TextureRegion[] region_down = tmp[1];
		aniDown = new Animation(delta, region_down);
		TextureRegion[] region_left = tmp[3];
		aniLeft = new Animation(delta, region_left);
		TextureRegion[] region_right = tmp[2];
		aniRight = new Animation(delta, region_right);
		// 人物出拳切图
		// Texture hero_actions = Assets.hero_actions;
		// TextureRegion[][] tmp_actions = TextureRegion.split(hero_actions,
		// 80 / 2, 264 / 4);
		TextureRegion[][] tmp_actions = atlas.findRegion("hero_actions").split(
				80 / 2, 264 / 4);

		TextureRegion[] region_up_actions = tmp_actions[0];
		aniUpAttack = new Animation(delta, region_up_actions);
		TextureRegion[] region_down_actions = tmp_actions[1];
		aniDownAttack = new Animation(delta, region_down_actions);
		TextureRegion[] region_left_actions = tmp_actions[3];
		aniLeftAttack = new Animation(delta, region_left_actions);
		TextureRegion[] region_right_actions = tmp_actions[2];
		aniRightAttack = new Animation(delta, region_right_actions);
		// 人物大招切图
		// Texture hero_bigfire = Assets.hero_bigfire;
		// TextureRegion[][] tmp_bigfire = TextureRegion.split(hero_bigfire,
		// 513 / 8, 240);
		TextureRegion[][] tmp_bigfire = atlas.findRegion("bigfire").split(
				513 / 8, 240);

		TextureRegion[] region_bigfire = tmp_bigfire[0];
		bigfire = new Animation(delta / 5, region_bigfire);
	}

	private void setFireSprite() {
		TextureAtlas atlas = Assets.getAssetManager().get("data/pack.atlas",
				TextureAtlas.class);
		fireUp = new Sprite(atlas.findRegion("fireUp"));
		fireDown = new Sprite(atlas.findRegion("fireDown"));
		fireLeft = new Sprite(atlas.findRegion("fireLeft"));
		fireRight = new Sprite(atlas.findRegion("fireRight"));

		float scale = 0.5f;
		fireUp.scale(scale);
		fireDown.scale(scale);
		fireLeft.scale(scale);
		fireRight.scale(scale);
	}

	public void bigFire(Vector3 doubleClickPosition) {
		if (currentMagic >= 10 && isLive && !isAttack) {
			BFPosition.x = doubleClickPosition.x;
			BFPosition.y = doubleClickPosition.y;
			isBigAttack = true;
			currentMagic -= 10;
			bigAttackArea.x = BFPosition.x - bigAttackArea.width / 2;
			bigAttackArea.y = BFPosition.y - bigAttackArea.width / 2;
		}
	}

	public DIRECT getDirection(double x, double y) {

		if (x > 0.71) {
			return DIRECT.Right;
		} else if (x < -0.71) {
			return DIRECT.Left;
		}
		if (y > 0.71) {
			return DIRECT.Up;
		} else if (y < -0.71) {
			return DIRECT.Down;
		}

		return DIRECT.Up;

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
