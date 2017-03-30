package com.me.crazyAdventure.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.crazyAdventure.CrazyAdventure;
import com.me.crazyAdventure.elements.EnemyManager;
import com.me.crazyAdventure.elements.Hero;
import com.me.crazyAdventure.elements.Hero.DIRECT;
import com.me.crazyAdventure.elements.Map;
import com.me.crazyAdventure.elements.myActions;
import com.me.crazyAdventure.tools.Assets;
import com.me.crazyAdventure.tools.IsContains;
import com.me.crazyAdventure.tools.MapAnalyzer;
import com.me.crazyAdventure.tools.Recorder;
import com.me.crazyAdventure.tools.packWindow;

public class GameScreen implements Screen {

	CrazyAdventure game;

	Stage gameStage;
	public Stage uiStage;

	InputMultiplexer inputMultiplexer;
	Vector3 doubleClickPosition;

	float width = Recorder.width;
	float height = Recorder.height;

	ImageButton angel;
	// uiStage组件
	Touchpad touchpad;
	ImageButton fireButton;
	Label FPS;
	TextureRegion hpInfo_bg;
	TextureRegion hp;
	TextureRegion magic;
	ImageButton pause;
	ImageButton pack;

	public Window deathMessage;// 死亡弹出窗
	Window pauseMessage;
	Window message;
	Window finishGame;
	Window noFinishGame;
	Label lbNoMoney;
	Button btNoMoney;
	Window noMoney;
	packWindow packMessage;

	// pack中的物品
	Image coin;
	Label coinNum;
	Image gem_red;
	Image gem_yellow;
	Image gem_green;
	Image gem_blue;
	Image gem_black;

	Rectangle padArea;
	Rectangle buttonArea;
	Vector3 ClickPositionInUi;

	Hero hero;
	EnemyManager enemyManager;

	public Map map;
	public MapAnalyzer mapanalyzer;

	public String mapName = "index1";
	boolean isRunning = true;
	private boolean isChangeMap = false;

	public GameScreen(CrazyAdventure game) {
		this.game = game;

		mapName = "index1";
		isRunning = true;
		isChangeMap = false;

	}

	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

		// if(Gdx.input.justTouched()){
		// System.out.println(Gdx.input.getX()+" "+(480-Gdx.input.getY()));
		// }

		updatePack();
		updateCamera();
		touchpadHandle();
		passHandle();
		check();

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		map.mapRender((OrthographicCamera) gameStage.getCamera());

		if (isRunning) {
			gameStage.act();
		}
		gameStage.draw();

//		FPS.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
//		if (mapName == "index1") {
//			FPS.setText("主城");
//		} else if (mapName == "level1") {
//			FPS.setText("新手训练营");
//		} else if (mapName == "level2") {
//			FPS.setText("妖风寨");
//		} else if (mapName == "level3") {
//			FPS.setText("遗失的古船");
//		} else if (mapName == "level4") {
//			FPS.setText("千年古刹");
//		} else if (mapName == "level5") {
//			FPS.setText("另一个自己");
//		}

		draw_HP_MAGIC(uiStage.getSpriteBatch());

		uiStage.act();
		uiStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		mapName = "index1";
		isRunning = true;
		isChangeMap = false;

		setMap();
		inputMultiplexer = new InputMultiplexer();
		setUIStage();
		setGameStage();
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		// dispose();
		Preferences prefs = Gdx.app.getPreferences("myGameData");
		prefs.putString("mapName", mapName);
		prefs.putFloat("hero.x", hero.x);
		prefs.putFloat("hero.y", hero.y);
		prefs.putInteger("hero.coins", hero.coins);
		for (int i = 0; i < 5; i++) {
			if(hero.pack[i]==0){
				prefs.putInteger("hero.pack["+i+"]", 0);
			}
			else{
				prefs.putInteger("hero.pack["+i+"]", 1);
			}
		}
		prefs.putBoolean("hasData", true);
		prefs.flush();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		// gameStage.dispose();
		// uiStage.dispose();
	}

	
	
	public void setGameStage() {
		gameStage = new Stage(Recorder.width, Recorder.height, false);
		hero = new Hero(mapanalyzer.getHeroPosition(), this);
		enemyManager = new EnemyManager(hero, this);
		
		TextureRegion fireButtonBg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("angel");
		TextureRegion imageUp = new TextureRegion(fireButtonBg);
		angel = new ImageButton(new TextureRegionDrawable(imageUp));
		angel.setPosition(730, 340);
		angel.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				uiStage.addActor(message);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		angel.setName("angel");
		
		gameStage.addActor(angel);
		gameStage.addActor(enemyManager.shotSet);
		gameStage.addActor(enemyManager.enemySet);
		gameStage.addActor(hero);

		doubleClickPosition = new Vector3(0, 0, 0);
		ClickPositionInUi = new Vector3(0, 0, 0);

		inputMultiplexer.addProcessor(gameStage);
		inputMultiplexer.addProcessor(new GestureDetector(new GestureAdapter() {
			public boolean tap(float x, float y, int count, int button) {

				ClickPositionInUi.x = x;
				ClickPositionInUi.y = y;

				uiStage.getCamera().unproject(ClickPositionInUi);
				// if (count == 2
				// && !padArea.contains(ClickPositionInUi.x,
				// ClickPositionInUi.y)
				// && !buttonArea.contains(ClickPositionInUi.x,
				// ClickPositionInUi.y)) {
				if (count == 2) {
					doubleClickPosition.x = x;
					doubleClickPosition.y = y;
					gameStage.getCamera().unproject(doubleClickPosition);
					hero.bigFire(doubleClickPosition);
					System.out.println(doubleClickPosition);
				}
				return false;
			}

			public boolean longPress(float x, float y) {

				doubleClickPosition.x = x;
				doubleClickPosition.y = y;
				gameStage.getCamera().unproject(doubleClickPosition);
				hero.x = doubleClickPosition.x;
				hero.y = doubleClickPosition.y;

				return false;
			}

			// public boolean pinch(Vector2 initialPointer1, Vector2
			// initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			// ((OrthographicCamera) gameStage.getCamera()).zoom += 0.02;
			//
			// return false;
			// }
			//
			// public boolean zoom(float initialDistance, float distance){
			// ((OrthographicCamera) gameStage.getCamera()).zoom -= 0.02;
			//
			// return false;
			// }
		}));
	}

	public void setUIStage() {
		uiStage = new Stage(Recorder.width, Recorder.height, false);

		// FPS
		LabelStyle style = new LabelStyle(new BitmapFont(Gdx.files.internal("data/font.fnt"), false), Color.WHITE);
		FPS = new Label("Jilin University", style);
		FPS.setFontScale(0.5f, 0.5f);
//		FPS = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), style);
		FPS.setHeight(height * 0.1f);
		FPS.setWidth(FPS.getWidth()/2);
		FPS.setPosition(width / 2 - FPS.getWidth() / 2,
				height - FPS.getHeight());

		// fireButton
		TextureRegion fireButtonBg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("fire_button");
		TextureRegion imageUp = new TextureRegion(fireButtonBg, 0, 0, 64, 64);
		TextureRegion imageDown = new TextureRegion(fireButtonBg, 64, 0, 64, 64);
		fireButton = new ImageButton(new TextureRegionDrawable(imageUp),
				new TextureRegionDrawable(imageDown));
		fireButton.setWidth(width * 0.1f);
		fireButton.setHeight(width * 0.1f);
		fireButton.setPosition(width - fireButton.getWidth() - 35, 20);
		fireButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				hero.isAttack = false;
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				hero.isAttack = true;
				return true;
			}
		});
		buttonArea = new Rectangle(fireButton.getX(), fireButton.getY(),
				fireButton.getWidth(), fireButton.getHeight());

		// touchpad
		TextureRegion touchpadBg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("touchpad");
		TextureRegion tmp = new TextureRegion(touchpadBg, 0, 0, 64, 64);
		TextureRegionDrawable background = new TextureRegionDrawable(tmp);
		TextureRegion tmp1 = new TextureRegion(touchpadBg, 64, 0, 64, 64);
		TextureRegionDrawable knobRegion = new TextureRegionDrawable(tmp1);
		TouchpadStyle touchpadStyle = new TouchpadStyle(background, knobRegion);
		touchpad = new Touchpad(15, touchpadStyle);
		touchpad.setBounds(0, 0, 130, 130);
		touchpad.setPosition(20, 20);
		padArea = new Rectangle(touchpad.getX(), touchpad.getY(),
				touchpad.getWidth(), touchpad.getHeight());

		// hp and magic infomation
		hpInfo_bg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("hpInfo_bg");
		hp = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class).findRegion("hp");
		magic = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class).findRegion("magic");

		// pause
		TextureRegion pauseBg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("button_pause");
		pause = new ImageButton(new TextureRegionDrawable(pauseBg));
		pause.setPosition(width - pause.getWidth() - 20,
				height - pause.getHeight() - 10);

		pause.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				uiStage.addActor(pauseMessage);
				isRunning = false;
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		// pack
		TextureRegion packBg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("button_pack");
		pack = new ImageButton(new TextureRegionDrawable(packBg));
		pack.setPosition(pause.getX() - pack.getWidth() - 10,
				height - pack.getHeight() - 10);

		pack.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub

				if (uiStage.getRoot().findActor("pack") != null) {
					uiStage.getRoot().removeActor(packMessage.dialog);
				} else {
					uiStage.addActor(packMessage.dialog);
				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		uiStage.addActor(FPS);
		uiStage.addActor(fireButton);
		uiStage.addActor(touchpad);
		uiStage.addActor(pause);
		uiStage.addActor(pack);

		// 产生window
		this.setWindow();

		// Gdx.input.setInputProcessor(uiStage);
		inputMultiplexer.addProcessor(uiStage);
	}

	public void setWindow() {
		Skin skin = Assets.getAssetManager().get("data/defaultskin.json",
				Skin.class);
		//集齐宝石
		Label lbFinishGame = new Label("恭喜你拯救了村子", skin);
		lbFinishGame.setScale(0.7f);
		Label lbBtFinishGame = new Label("知道了", skin);
		Button btFinish = new Button(lbBtFinishGame, skin);
		btFinish.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				uiStage.getRoot().removeActor(finishGame);
				uiStage.getRoot().removeActor(message);
			}
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		finishGame = new Window("", skin);
		finishGame.setModal(true);
		finishGame.setSize(300, 120);
		finishGame.setPosition(width/2-finishGame.getWidth()/2, height/2-finishGame.getHeight()/2);		
		
		finishGame.add(lbFinishGame);
		finishGame.row();
		finishGame.add(btFinish);
		
		//未集齐宝石
		Label lbNoFinishGame = new Label("你还没有集齐宝石", skin);
		lbFinishGame.setScale(0.7f);
		Label lbBtNoFinishGame = new Label("知道了", skin);
		Button btNoFinish = new Button(lbBtNoFinishGame, skin);
		btNoFinish.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				uiStage.getRoot().removeActor(noFinishGame);
			}
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		noFinishGame = new Window("", skin);
		noFinishGame.setModal(true);
		noFinishGame.setSize(300, 120);
		noFinishGame.setPosition(width/2-finishGame.getWidth()/2, height/2-finishGame.getHeight()/2);		
		
		noFinishGame.add(lbNoFinishGame);
		noFinishGame.row();
		noFinishGame.add(btNoFinish);
		
		//金额不足
		lbNoMoney = new Label("需"+Recorder.needCoins+"金，金币不足", skin);
		lbNoMoney.setScale(0.7f);
		lbNoMoney.setName("lbNoMoney");
		Label lbBtlbNoMoney = new Label("知道了", skin);
		btNoMoney = new Button(lbBtlbNoMoney, skin);
		btNoMoney.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				uiStage.getRoot().removeActor(noMoney);
			}
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		noMoney = new Window("", skin);
		noMoney.setModal(true);
		noMoney.setSize(350, 120);
		noMoney.setPosition(width/2-finishGame.getWidth()/2, height/2-finishGame.getHeight()/2);		
		
		noMoney.add(lbNoMoney);
		noMoney.row();
		noMoney.add(btNoMoney);
		//总剧情板
		TextureRegionDrawable WindowDrable = new TextureRegionDrawable(Assets
				.getAssetManager().get("data/pack.atlas", TextureAtlas.class)
				.findRegion("juqing"));
		WindowStyle style = new WindowStyle(new BitmapFont(), Color.RED,
				WindowDrable);

		message = new Window("", style);
		message.setModal(true);
		message.setSize(368, 450);
		message.setPosition((Recorder.width - message.getWidth()) / 2,
				(Recorder.height - message.getHeight()) / 2);
		Label lbFinish = new Label("我集齐了宝石", skin);
		lbFinish.setFontScaleX(0.7f);
		Button finish = new Button(lbFinish, skin);
		finish.setPosition(45, 40);
		finish.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				for(int i: Hero.pack){
					if(i==0)
					{
						uiStage.addActor(noFinishGame);	
						return;
					}					
				}
				uiStage.addActor(finishGame);				
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		Label lbNoFinish = new Label("还没有集齐", skin);
		lbNoFinish.setFontScaleX(0.7f);
		Button noFinish = new Button(lbNoFinish, skin);
		noFinish.setPosition(205, 40);
		noFinish.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				uiStage.getRoot().removeActor(message);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		message.addActor(finish);
		message.addActor(noFinish);
		
		// 死亡提示框
		deathMessage = new Window("", skin);
		deathMessage.setModal(true);
		deathMessage.setSize(310, 120);
		deathMessage.setPosition(400 - deathMessage.getWidth() / 2,
				240 - deathMessage.getHeight() / 2);

		Label lbDeath = new Label(" 你已经死了", skin);
		Label lbReviveInHome = new Label("主城复活", skin);
		Label lbReviveInHere = new Label("原地复活", skin);
		Button reviveInHome = new Button(lbReviveInHome, skin);
		reviveInHome.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub

				Recorder.relives = 0;
				
				enemyManager.removeAll();
				isChangeMap = false;
				mapName = "index1";
				map.changeMap(
						Assets.getAssetManager().get("map/index1.tmx",
								TiledMap.class), 40 * 32, 25 * 32);
				mapanalyzer.update(
						Assets.getAssetManager().get("map/index1.tmx",
								TiledMap.class), 25, 40, 32, 32);

				hero.x = mapanalyzer.getHeroPosition().x;
				hero.y = mapanalyzer.getHeroPosition().y;

				hero.currentHp = hero.maxHp;
				hero.currentMagic = hero.maxMagic;
				hero.isLive = true;

				uiStage.getRoot().removeActor(deathMessage);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		Button reviveInHere = new Button(lbReviveInHere, skin);
		reviveInHere.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub				
				
				int needCoins = (int) Math.pow(10, Recorder.relives+1);
				if (Hero.coins >= needCoins) {
					Recorder.relives++;
					Hero.coins -= needCoins;
					
					hero.currentHp = hero.maxHp;
					hero.currentMagic = hero.maxMagic;
					hero.isLive = true;
					uiStage.getRoot().removeActor(deathMessage);
				}
				else{
					lbNoMoney.setText("需"+needCoins+"金，金币不足");
					uiStage.addActor(noMoney);
				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		deathMessage.add(lbDeath).top().center();
		deathMessage.row();
		deathMessage.add(reviveInHome).left();
		deathMessage.add(reviveInHere).left();

		// 暂停提示框
		pauseMessage = new Window("", skin);
		pauseMessage.setModal(true);
		pauseMessage.setSize(300, 120);
		pauseMessage.setPosition(400 - pauseMessage.getWidth() / 2,
				240 - pauseMessage.getHeight() / 2);

		Label lbPause = new Label(" 游戏暂停中", skin);
		Label lbResume = new Label(" 恢  复 ", skin);
		Label lbMenu = new Label(" 菜  单 ", skin);
		Button resume = new Button(lbResume, skin);
		resume.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				isRunning = true;
				uiStage.getRoot().removeActor(pauseMessage);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		Button menu = new Button(lbMenu, skin);
		menu.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				isRunning = true;
				game.setScreen(game.menuScreen);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		pauseMessage.add(lbPause).top().center();
		pauseMessage.row();
		pauseMessage.add(resume).left();
		pauseMessage.add(menu).left();

		// 包裹
		packMessage = new packWindow();
		
		// pack中的物品
		coin = new Image(Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class).findRegion("coin"));
		coin.setScale(0.3f);
		coin.setPosition(0, 0);
		coinNum = new Label(Hero.coins+"", skin);
		coinNum.setPosition(38, 192);
		coinNum.setFontScale(0.5f);
		gem_red = new Image(Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("gem_red"));
		gem_yellow = new Image(Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("gem_yellow"));
		gem_green = new Image(Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("gem_green"));
		gem_blue = new Image(Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("gem_blue"));
		gem_black = new Image(Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("gem_black"));
		
		packMessage.add(coin);
		packMessage.dialog.addActor(coinNum);

//		uiStage.addActor(message);
//		 uiStage.addActor(pauseMessage);
//		 uiStage.addActor(exitMessage);
//		 uiStage.addActor(deathMessage);
//		 uiStage.addActor(packMessage);
	}

	public void setMap() {
		// map = new Map("map/index1.tmx", 200 * 16, 150 * 16);
		// map = new Map(Assets.index1, 200 * 16, 150 * 16);
		map = new Map(Assets.getAssetManager().get("map/index1.tmx",
				TiledMap.class), 40 * 32, 25 * 32);
		mapanalyzer = new MapAnalyzer(map.getMap(), 25, 40, 32, 32);
		// map = new Map(Assets.level1, 100 * 32, 100 * 32);
		// mapanalyzer = new MapAnalyzer(map.getMap(), 100, 100, 32, 32);
	}

	public void draw_HP_MAGIC(SpriteBatch batch) {
		batch.begin();
		batch.draw(hpInfo_bg, 20, 470 - hpInfo_bg.getRegionHeight());
		batch.draw(hp, 79, 449, hp.getRegionWidth() * hero.currentHp
				/ hero.maxHp, hp.getRegionHeight());
		batch.draw(magic, 78, 430, magic.getRegionWidth() * hero.currentMagic
				/ hero.maxMagic, magic.getRegionHeight());
		batch.end();
	}

	public void check() {

		if (mapName.equals("index1")) {
			if (gameStage.getRoot().findActor("angel") == null) {
				gameStage.getRoot().addActorBefore(hero, angel);
			}
		}
		else{
			if (gameStage.getRoot().findActor("angel") != null) {
				gameStage.getRoot().removeActor(angel);
			}
		}
	}
	
	public void updatePack(){
		coinNum.setText(Hero.coins+"");
		if(Hero.pack[0]==1){
			packMessage.add(gem_red);
			Hero.pack[0]++;
		} else if (Hero.pack[1] == 1) {
			packMessage.add(gem_yellow);
			Hero.pack[1]++;
		} else if (Hero.pack[2] == 1) {
			packMessage.add(gem_green);
			Hero.pack[2]++;
		} else if (Hero.pack[3] == 1) {
			packMessage.add(gem_blue);
			Hero.pack[3]++;
		} else if (Hero.pack[4] == 1) {
			packMessage.add(gem_black);
			Hero.pack[4]++;
		}
//		for (int i = 0; i < Hero.pack.length; i++) {
//			System.out.print(Hero.pack[i] + " ");
//		}
//		System.out.println();
//		System.out.println(Hero.coins);
	}
	
 	public void updateCamera() {
		float mapWidth = map.getMapWidth();
		float mapHeight = map.getMapheight();
		float x = hero.x;
		float y = hero.y;
		float width = Recorder.width;
		float height = Recorder.height;

		if (hero.x <= width / 2) {
			x = width / 2;
		}
		if (hero.x >= mapWidth - width / 2) {
			x = mapWidth - width / 2;
		}
		if (hero.y <= height / 2) {
			y = height / 2;
		}
		if (hero.y >= mapHeight - height / 2) {
			y = mapHeight - height / 2;
		}
		gameStage.getCamera().position.set(x, y, 0);
	}

	private void touchpadHandle() {
		if (touchpad.isTouched()) {
			hero.direct = hero.getDirection(touchpad.getKnobPercentX(),
					touchpad.getKnobPercentY());
			hero.isMove = true;
		} else {
			hero.isMove = false;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)
				|| Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			isRunning = false;
			uiStage.addActor(pauseMessage);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			((OrthographicCamera) gameStage.getCamera()).zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			((OrthographicCamera) gameStage.getCamera()).zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			hero.direct = DIRECT.Up;
			hero.isMove = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			hero.direct = DIRECT.Down;
			hero.isMove = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			hero.direct = DIRECT.Left;
			hero.isMove = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			hero.direct = DIRECT.Right;
			hero.isMove = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			if (hero.isAttack) {
				hero.isAttack = false;
			} else {
				hero.isAttack = true;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			hero.currentHp = hero.maxHp;
			hero.isLive = true;
		}
	}

	private void passHandle() {
		if (mapName.equals("index1")) {
			float rectangle_width = 192;
			float rectangle_height = 128;
			if (IsContains.isContains(70, 628, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				isChangeMap = true;
				if (isChangeMap) {
					// 切换地图
					mapName = "level1";
					map.changeMap(
							Assets.getAssetManager().get("map/level1.tmx",
									TiledMap.class), 150 * 32, 100 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/level1.tmx",
									TiledMap.class), 100, 150, 32, 32);
					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;
					// 添加怪兽
					enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
							mapanalyzer.getTrapsPosition(),
							mapanalyzer.getBossPosition());
					enemyManager.createEnemys();

					isChangeMap = false;

				}
			}
			if (IsContains.isContains(555, 646, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				isChangeMap = true;
				if (isChangeMap) {
					// 切换地图
					mapName = "level2";
					map.changeMap(
							Assets.getAssetManager().get("map/level2.tmx",
									TiledMap.class), 58 * 32, 49 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/level2.tmx",
									TiledMap.class), 49, 58, 32, 32);
					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;
					// 添加怪兽
					enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
							mapanalyzer.getTrapsPosition(),
							mapanalyzer.getBossPosition());
					enemyManager.createEnemys();

					isChangeMap = false;
				}
			}
			if (IsContains.isContains(1084, 632, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				isChangeMap = true;
				if (isChangeMap) {
					// 切换地图
					mapName = "level3";
					map.changeMap(
							Assets.getAssetManager().get("map/level3.tmx",
									TiledMap.class), 56 * 32, 26 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/level3.tmx",
									TiledMap.class), 26, 56, 32, 32);
					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;
					// 添加怪兽
					enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
							mapanalyzer.getTrapsPosition(),
							mapanalyzer.getBossPosition());
					enemyManager.createEnemys();

					isChangeMap = false;
				}
			}
			if (IsContains.isContains(1059, 25, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				isChangeMap = true;
				if (isChangeMap) {
					// 切换地图
					mapName = "level4";
					map.changeMap(
							Assets.getAssetManager().get("map/level4.tmx",
									TiledMap.class), 50 * 32, 36 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/level4.tmx",
									TiledMap.class), 36, 50, 32, 32);
					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;
					// 添加怪兽
					enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
							mapanalyzer.getTrapsPosition(),
							mapanalyzer.getBossPosition());
					enemyManager.createEnemys();

					isChangeMap = false;
				}
			}
			if (IsContains.isContains(57, 20, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				isChangeMap = true;
				if (isChangeMap) {
					// 切换地图
					mapName = "level5";
					map.changeMap(
							Assets.getAssetManager().get("map/level5.tmx",
									TiledMap.class), 32 * 32, 18 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/level5.tmx",
									TiledMap.class), 18, 32, 32, 32);
					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;
					// 添加怪兽
					enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
							mapanalyzer.getTrapsPosition(),
							mapanalyzer.getBossPosition());
					enemyManager.createEnemys();

					isChangeMap = false;
				}
			}
		} else if (mapName.equals("level1")) {
			float rectangle_width = 192;
			float rectangle_height = 128;
			if (IsContains.isContains(4550, 1510, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				this.enemyManager.removeAll();

				isChangeMap = true;
				if (isChangeMap) {

					mapName = "index1";

					map.changeMap(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 40 * 32, 25 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 25, 40, 32, 32);

					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;

					isChangeMap = false;
				}
			}
		} else if (mapName.equals("level2")) {
			float rectangle_width = 192;
			float rectangle_height = 128;
			if (IsContains.isContains(848, 263, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				this.enemyManager.removeAll();

				isChangeMap = true;
				if (isChangeMap) {

					mapName = "index1";

					map.changeMap(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 40 * 32, 25 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 25, 40, 32, 32);

					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;

					isChangeMap = false;
				}
			}
		} else if (mapName.equals("level3")) {
			float rectangle_width = 192;
			float rectangle_height = 128;
			if (IsContains.isContains(1100, 327, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				this.enemyManager.removeAll();

				isChangeMap = true;
				if (isChangeMap) {

					mapName = "index1";

					map.changeMap(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 40 * 32, 25 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 25, 40, 32, 32);

					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;

					isChangeMap = false;
				}
			}
		} else if (mapName.equals("level4")) {
			float rectangle_width = 192;
			float rectangle_height = 128;
			if (IsContains.isContains(38, 835, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				this.enemyManager.removeAll();

				isChangeMap = true;
				if (isChangeMap) {

					mapName = "index1";

					map.changeMap(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 40 * 32, 25 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 25, 40, 32, 32);

					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;

					isChangeMap = false;
				}
			}
		} else if (mapName.equals("level5")) {
			float rectangle_width = 128;
			float rectangle_height = 110;
			if (IsContains.isContains(450, 447, rectangle_width,
					rectangle_height, hero.x, hero.y)) {
				this.enemyManager.removeAll();

				isChangeMap = true;
				if (isChangeMap) {

					mapName = "index1";

					map.changeMap(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 40 * 32, 25 * 32);
					mapanalyzer.update(
							Assets.getAssetManager().get("map/index1.tmx",
									TiledMap.class), 25, 40, 32, 32);

					hero.x = mapanalyzer.getHeroPosition().x;
					hero.y = mapanalyzer.getHeroPosition().y;

					isChangeMap = false;
				}
			}
		}
	}

	public void loadOldGameData() {
		Preferences prefs = Gdx.app.getPreferences("myGameData");

		if (prefs.getBoolean("hasData")) {
			mapName = prefs.getString("mapName");
			hero.x = prefs.getFloat("hero.x");
			hero.y = prefs.getFloat("hero.y");
			hero.coins  = prefs.getInteger("hero.coins");
			hero.pack[0] = prefs.getInteger("hero.pack[0]");
			hero.pack[1] = prefs.getInteger("hero.pack[1]");
			hero.pack[2] = prefs.getInteger("hero.pack[2]");
			hero.pack[3] = prefs.getInteger("hero.pack[3]");
			hero.pack[4] = prefs.getInteger("hero.pack[4]");
			if (mapName.equals("index1")) {
				map.changeMap(
						Assets.getAssetManager().get("map/index1.tmx",
								TiledMap.class), 40 * 32, 25 * 32);
				mapanalyzer.update(
						Assets.getAssetManager().get("map/index1.tmx",
								TiledMap.class), 25, 40, 32, 32);
			} else if (mapName.equals("level1")) {
				map.changeMap(
						Assets.getAssetManager().get("map/level1.tmx",
								TiledMap.class), 150 * 32, 100 * 32);
				mapanalyzer.update(
						Assets.getAssetManager().get("map/level1.tmx",
								TiledMap.class), 100, 150, 32, 32);
				// 添加怪兽
				enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
						mapanalyzer.getTrapsPosition(),
						mapanalyzer.getBossPosition());
				enemyManager.createEnemys();
			} else if (mapName.equals("level2")) {
				map.changeMap(
						Assets.getAssetManager().get("map/level2.tmx",
								TiledMap.class), 58 * 32, 49 * 32);
				mapanalyzer.update(
						Assets.getAssetManager().get("map/level2.tmx",
								TiledMap.class), 49, 58, 32, 32);
				// 添加怪兽
				enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
						mapanalyzer.getTrapsPosition(),
						mapanalyzer.getBossPosition());
				enemyManager.createEnemys();
			} else if (mapName.equals("level3")) {
				map.changeMap(
						Assets.getAssetManager().get("map/level3.tmx",
								TiledMap.class), 56 * 32, 26 * 32);
				mapanalyzer.update(
						Assets.getAssetManager().get("map/level3.tmx",
								TiledMap.class), 26, 56, 32, 32);
				// 添加怪兽
				enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
						mapanalyzer.getTrapsPosition(),
						mapanalyzer.getBossPosition());
				enemyManager.createEnemys();
			} else if (mapName.equals("level4")) {
				map.changeMap(
						Assets.getAssetManager().get("map/level4.tmx",
								TiledMap.class), 50 * 32, 36 * 32);
				mapanalyzer.update(
						Assets.getAssetManager().get("map/level4.tmx",
								TiledMap.class), 36, 50, 32, 32);
				// 添加怪兽
				enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
						mapanalyzer.getTrapsPosition(),
						mapanalyzer.getBossPosition());
				enemyManager.createEnemys();
			} else if (mapName.equals("level5")) {
				map.changeMap(
						Assets.getAssetManager().get("map/level5.tmx",
								TiledMap.class), 32 * 32, 18 * 32);
				mapanalyzer.update(
						Assets.getAssetManager().get("map/level5.tmx",
								TiledMap.class), 18, 32, 32, 32);
				// 添加怪兽
				enemyManager.loadLevelInfo(mapanalyzer.getEnemysPosition(),
						mapanalyzer.getTrapsPosition(),
						mapanalyzer.getBossPosition());
				enemyManager.createEnemys();
			}
		}
	}
}
