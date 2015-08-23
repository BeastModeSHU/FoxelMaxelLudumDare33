package com.foxel.maxel.ld33.resources;

import java.util.ArrayList;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.entities.Entity;
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
	//TODO rename to getBelowEntities();
	public int[] getBelowPlayer() {
		/*
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

		return new int[] { x, y, startX, startY, width, height };*/ 
		
		return null;
	}
	//TODO rename to getAboveEntities();
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

	public void sortRenderableByZ(ArrayList<Entity> renderable) {
		@SuppressWarnings("unchecked")
		ArrayList<Entity> renderableCopy = new ArrayList<Entity>();// (ArrayList<Entity>)
																	// renderable.clone();
		boolean foundPlayer = false;
		int counter = 0;

		while (counter < renderable.size() && !renderable.get(counter).equals(player)) {
			++counter;
		}

		for (int i = 0; i < renderable.size(); ++i) {
			if (!renderable.get(i).equals(player)) {
				float playerY = player.getPixelLocation().y + player.getEntityDimensions().y;
				float tenantY = renderable.get(i).getPixelLocation().y
						+ renderable.get(i).getEntityDimensions().y;

				if (playerY > tenantY) {
					renderableCopy.add(renderable.get(i));
				} else {
					renderableCopy.add(renderable.get(counter));
				}
			}
		}

		renderable = renderableCopy;
	}
}