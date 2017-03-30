package com.me.crazyAdventure.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.crazyAdventure.CrazyAdventure;
import com.me.crazyAdventure.elements.myActions;
import com.me.crazyAdventure.tools.Assets;
import com.me.crazyAdventure.tools.Recorder;

public class MenuScreen implements Screen {
	CrazyAdventure game;

	Stage stage;
	Skin skin;
	
	ImageButton newGame;
	ImageButton resume;
	ImageButton music_open;
	ImageButton music_close;
	
	Image title;
	
	Music music;
	
	SpriteBatch batch;
	Animation bgAni;
	TextureRegion currentFrame;
	public float statetime = 0;

	Window exitMessage;

	public MenuScreen(CrazyAdventure game) {
		this.game = game;
	}

	public void createScreen() {
		skin = Assets.getAssetManager()
				.get("data/defaultskin.json", Skin.class);
		// Skin skin = new Skin(Gdx.files.internal("data/defaultskin.json"));

		stage = new Stage(Recorder.width, Recorder.height, false);
//		batch = new SpriteBatch();
        batch = stage.getSpriteBatch();
		
		setAnimation();
//		setImage();
		setMusic();
		setButton();
		setWindow();

//		stage.addActor(title);
		stage.addActor(newGame);
		stage.addActor(resume);
		if (Recorder.isMusicPlaying) {
			stage.addActor(music_open);
		}
		else{
			stage.addActor(music_close);
		}

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		inputHandle();
		statetime += Gdx.graphics.getDeltaTime();
		currentFrame = bgAni.getKeyFrame(statetime, true);

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(currentFrame, 0, 0);
		batch.end();
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		createScreen();
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

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

	}

	
	private void inputHandle() {
//		if (Gdx.input.justTouched()) {
//			System.out.println(Gdx.input.getX() + " " + (480 - Gdx.input.getY()));
//		}
		if(Gdx.input.isKeyPressed(Input.Keys.BACK)|| Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
//			Gdx.app.log("提示", "退出");
			stage.addActor(exitMessage);
		}
	}

	private void setAnimation(){
		TextureRegion[] regions = new TextureRegion[3];
		
//		regions[0] = new TextureRegion(Assets.getAssetManager().get("data/rain11.jpg", Texture.class));
//		regions[1] = new TextureRegion(Assets.getAssetManager().get("data/rain12.jpg", Texture.class));
//		regions[2] = new TextureRegion(Assets.getAssetManager().get("data/rain13.jpg", Texture.class));
		
		regions[0] = new TextureRegion(Assets.getAssetManager().get("data/rain21.jpg", Texture.class));
		regions[1] = new TextureRegion(Assets.getAssetManager().get("data/rain22.jpg", Texture.class));
		regions[2] = new TextureRegion(Assets.getAssetManager().get("data/rain23.jpg", Texture.class));
	
		bgAni = new Animation(0.07f, regions);
	}
	
	private void setMusic(){
		music = Assets.getAssetManager().get("data/1.ogg", Music.class);
		music.setVolume(0.5f);
		music.setLooping(true);
		if (Recorder.isMusicPlaying) {
			music.play();
		}
	}
	
	private void setImage(){
		title = new Image(Assets.getAssetManager().get("data/pack.atlas", TextureAtlas.class).findRegion("title"));
		title.setPosition(Recorder.width/2-title.getWidth()/2, Recorder.height-title.getHeight()-10);
		title.setScaleY(0f);
		
		ScaleToAction scale = Actions.scaleTo(1, 1, 0.5f);
		title.addAction(scale);
	}
	
 	private void setButton() {
		//新游戏按钮
		TextureRegion newbg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("bt_new");
		TextureRegion imageUp = new TextureRegion(newbg, 0, 0, 150, 150);
		TextureRegion imageDown = new TextureRegion(newbg, 150, 0, 150, 150);
		newGame = new ImageButton(new TextureRegionDrawable(imageUp),
				new TextureRegionDrawable(imageDown));
		newGame.setPosition(0-newGame.getWidth()-10, Recorder.height/2-newGame.getHeight()/2);
		newGame.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				game.setScreen(game.gameScreen);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		//继续按钮
		TextureRegion resumebg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("bt_resume");
		TextureRegion imageUp1 = new TextureRegion(resumebg, 0, 0, 150, 150);
		TextureRegion imageDown1 = new TextureRegion(resumebg, 150, 0, 150, 150);
		resume = new ImageButton(new TextureRegionDrawable(imageUp1),
				new TextureRegionDrawable(imageDown1));
		resume.setPosition(Recorder.width+10, Recorder.height/2-resume.getHeight()/2);
		resume.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				game.setScreen(game.gameScreen);
				game.gameScreen.loadOldGameData();
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		//音乐开关按钮
		TextureRegion musicOpenBg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("music_open");
		TextureRegion musicColseBg = Assets.getAssetManager()
				.get("data/pack.atlas", TextureAtlas.class)
				.findRegion("music_close");
		TextureRegion imageUp2 = new TextureRegion(musicOpenBg);
		TextureRegion imageDown2 = new TextureRegion(musicColseBg);
		music_open = new ImageButton(new TextureRegionDrawable(imageUp2));
		music_open.setPosition(20, 20);
		music_open.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				Recorder.isMusicPlaying = false;
				music.pause();
				stage.getRoot().removeActor(music_open);
				stage.addActor(music_close);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		music_close = new ImageButton(new TextureRegionDrawable(imageDown2));
		music_close.setPosition(20, 20);
		music_close.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				Recorder.isMusicPlaying = true;;
				music.play();
				stage.getRoot().removeActor(music_close);
				stage.addActor(music_open);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		MoveToAction moveto = Actions.moveTo(Recorder.width/2-newGame.getWidth()-10, Recorder.height/2-newGame.getHeight()/2, 0.5f);
		SequenceAction sequence = Actions.sequence(
				myActions.buttonMoveUp(newGame), Actions.delay(1),
				myActions.buttonMoveDown(newGame), Actions.delay(1));
		RepeatAction forever = Actions.forever(sequence);
		newGame.addAction(moveto);
		newGame.addAction(forever);
		
		MoveToAction moveto1 = Actions.moveTo(Recorder.width/2+10, Recorder.height/2-newGame.getHeight()/2, 0.5f);
		SequenceAction sequence1 = Actions.sequence(
				myActions.buttonMoveUp(resume), Actions.delay(0.7f),
				myActions.buttonMoveDown(resume), Actions.delay(1.1f));
		RepeatAction forever1 = Actions.forever(sequence1);
		resume.addAction(moveto1);
		resume.addAction(forever1);
		

	}

	private void setWindow() {
		// 退出游戏提示框
		exitMessage = new Window("", skin);
		exitMessage.setModal(true);
		exitMessage.setSize(330, 120);
		exitMessage.setPosition(400 - exitMessage.getWidth() / 2,
				240 - exitMessage.getHeight() / 2);

		Label lbExit = new Label(" 确定退出游戏?", skin);
		Label lbOk = new Label(" 确  定 ", skin);
		Label lbCencel = new Label(" 取  消 ", skin);
		Button ok = new Button(lbOk, skin);
		ok.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				System.out.println("确定");
				Gdx.app.exit();
				System.exit(0);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		Button cencel = new Button(lbCencel, skin);
		cencel.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				System.out.println("取消");
				stage.getRoot().removeActor(exitMessage);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
//				return super.touchDown(event, x, y, pointer, button);
			}
		});

		exitMessage.add(lbExit).top().center();
		exitMessage.row();
		exitMessage.add(ok).left();
		exitMessage.add(cencel).left();
	}
}
