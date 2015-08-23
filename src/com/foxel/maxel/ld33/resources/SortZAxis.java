package com.foxel.maxel.ld33.resources;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.entities.Player;
import com.foxel.maxel.ld33.map.Map;

public class SortZAxis {
	/*
	 * ### MACE ###
	 */
	private Player player;
	private Map map;

	public SortZAxis(Player player, Map map) {
		this.player = player;
		this.map = map;
	}

	public void renameMethod() {

	}

	public int[] getBelowPlayer() {

		float playerY = player.getPixelLocation().y + player.getEntityDimensions().y;

		boolean stopLoop = false;
		int counter = 0;

		while (!stopLoop && counter < map.getHeightInTiles()) {
			float tileY = (counter * Constants.TILESIZE) + Constants.TILESIZE;

			if (playerY < tileY) {
				stopLoop = true;
			}
			++counter;
		}

		int x = 0;
		int y = 0;
		int startX = x;
		int startY = y;
		int width = 15;
		int height = 0 + (counter);

		return new int[] { x, y, startX, startY, width, height };
	}

	public int[] getAbovePlayer() {

		float playerY = player.getPixelLocation().y + player.getEntityDimensions().y;
		boolean stopLoop = false;
		int counter = 0;

		while (!stopLoop && counter < map.getHeightInTiles()) {
			float tileY = (counter * Constants.TILESIZE) + Constants.TILESIZE;
			if (playerY < tileY) {
				stopLoop = true;
			}
			++counter;
		}
		int x = 0;
		int y = (counter - 1);
		int width = 15;
		int height = 16 - (counter);

		return new int[] { x, y * Constants.TILESIZE, x, y, width, height };
	}
}
