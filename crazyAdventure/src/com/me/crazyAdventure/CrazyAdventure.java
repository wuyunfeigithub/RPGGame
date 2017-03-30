package com.me.crazyAdventure;

import com.badlogic.gdx.Game;
import com.me.crazyAdventure.screens.GameScreen;
import com.me.crazyAdventure.screens.MenuScreen;
import com.me.crazyAdventure.screens.StartScreen;

public class CrazyAdventure extends Game{

	public StartScreen startScreen;
	public MenuScreen menuScreen;
	public GameScreen gameScreen;
	
	@Override
 	public void create() {
		// TODO Auto-generated method stub
		startScreen = new StartScreen(this);
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		
		this.setScreen(startScreen);
		
//		this.setScreen(menuScreen);
	}
	
}
