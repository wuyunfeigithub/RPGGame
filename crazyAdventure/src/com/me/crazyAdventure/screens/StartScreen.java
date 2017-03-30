package com.me.crazyAdventure.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.crazyAdventure.CrazyAdventure;
import com.me.crazyAdventure.tools.Assets;

public class StartScreen implements Screen{
	CrazyAdventure game;
	
	Assets assets;
	
	SpriteBatch batch;
	Texture loadTexture;
	Animation animation;	
	TextureRegion localTextureRegion;
	
	public float statetime = 0;
	
	float width;
	float height;	
	
	public StartScreen(CrazyAdventure game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		statetime +=Gdx.graphics.getDeltaTime();
		
		localTextureRegion = this.animation.getKeyFrame(statetime, true);
		
		if (Assets.getAssetManager().update()) {
//			game.setScreen(new GameScreen(game));
			game.setScreen(game.menuScreen);
		}
		else{
			batch.begin();
			batch.draw(localTextureRegion, width/2-50, height/2-50, 100, 100);
			batch.end();
		}
		
	}

	@Override
	public void show() {
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		loadTexture = new Texture(Gdx.files.internal("data/loading.png"));
		batch = new SpriteBatch();
		setAni();
		assets = new Assets();
	}
	
	public void setAni() {
	    TextureRegion[][] arrayOfTextureRegion = TextureRegion.split(this.loadTexture, 100, 100);
	    TextureRegion[] arrayOfTextureRegion1 = new TextureRegion[12];
	    arrayOfTextureRegion1[0] = arrayOfTextureRegion[0][0];
	    arrayOfTextureRegion1[1] = arrayOfTextureRegion[0][1];
	    arrayOfTextureRegion1[2] = arrayOfTextureRegion[0][2];
	    arrayOfTextureRegion1[3] = arrayOfTextureRegion[0][3];
	    arrayOfTextureRegion1[4] = arrayOfTextureRegion[0][4];
	    arrayOfTextureRegion1[5] = arrayOfTextureRegion[1][0];
	    arrayOfTextureRegion1[6] = arrayOfTextureRegion[1][1];
	    arrayOfTextureRegion1[7] = arrayOfTextureRegion[1][2];
	    arrayOfTextureRegion1[8] = arrayOfTextureRegion[1][3];
	    arrayOfTextureRegion1[9] = arrayOfTextureRegion[1][4];
	    arrayOfTextureRegion1[10] = arrayOfTextureRegion[2][0];
	    arrayOfTextureRegion1[11] = arrayOfTextureRegion[2][1];
	    this.animation = new Animation(0.1f,arrayOfTextureRegion1);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		this.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
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
		batch.dispose();
		loadTexture.dispose();
	}
}
