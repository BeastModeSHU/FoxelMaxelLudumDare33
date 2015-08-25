package com.foxel.maxel.ld33.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.constants.Constants;

public class WinState extends BasicGameState {
	private final int ID; 
	
	private Image winScreen;
	
	public WinState(int id){ 
		this.ID = id;
	}
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		winScreen = new Image(Constants.WIN_SCREEN_LOC);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(winScreen, 0, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
			gc.exit();
	}

	@Override
	public int getID() {

		return ID;
	}

}
