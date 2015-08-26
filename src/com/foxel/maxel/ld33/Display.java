package com.foxel.maxel.ld33;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.states.StateOne;
import com.foxel.maxel.ld33.states.WinState;

public class Display extends StateBasedGame {

	public Display(String name) {
		super(name);
		this.addState(new StateOne(Constants.STATE_ONE_ID));
		this.addState(new WinState(Constants.WIN_STATE_ID));
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.enterState(Constants.STATE_ONE_ID);
	}

	public static void main(String args[]) {
		AppGameContainer agc;
		try {
			agc = new AppGameContainer(new Display(Constants.WINDOW_TITLE));
			agc.setDisplayMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);
			agc.setShowFPS(false);
			agc.setTargetFrameRate(60);
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
