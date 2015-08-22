package com.foxel.maxel.ld33.map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import com.foxel.maxel.ld33.constants.Constants;

public class Map implements TileBasedMap {

	// private ArrayList<Rectangle> mapColliders;
	private int blockedMap[][]; // / 0 = not blocked, 1 = blocked
	private final int TILESIZE;
	private TiledMap map;

	public Map() {
		this.TILESIZE = Constants.TILESIZE;
	}

	public void init() throws SlickException {
		map = new TiledMap(Constants.TEST_MAP_LOC);
		blockedMap = new int[map.getHeight()][map.getWidth()];

		for (int x = 0; x < map.getWidth(); ++x) {
			for (int y = 0; y < map.getWidth(); ++y) {
				int tileID = map.getTileId(x, y, 0); // Collisions detected from
														// wall tile layer
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)) {
					blockedMap[x][y] = 1;
				} else {
					blockedMap[x][y] = 0;
				}
			}
		}

		// printBlocked();

	}

	private void printBlocked() {
		// TODO remove after debuggin
		for (int i = 0; i < map.getHeight(); ++i) {
			for (int j = 0; j < map.getWidth(); ++j) {
				System.out.print(blockedMap[j][i]);
			}
			System.out.println();
		}
	}

	public void renderWallLayer() throws SlickException {
		// map.render(0, 0, 0);
		map.render(0, 0, 0);

	}

	public void renderAboveEntity(int[] data) {
		/*
		 * public void render(int x, int y, int sx, int sy, int width, int
		 * height, int l, boolean lineByLine)
		 */
		map.render(data[0], data[1], data[2], data[3], data[4], data[5], 1, false);
	}

	public void renderBelowEntity(int[] data) {
		map.render(data[0], data[1], data[2], data[3], data[4], data[5], 1, false);
	}

	public boolean isTileFree(Rectangle collider) {
		boolean isFree = true;
		for (int i = 0; i < map.getHeight(); ++i) {
			for (int j = 0; j < map.getWidth(); ++j) {
				if (blockedMap[j][i] == 1) {
					if (new Rectangle(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE)
							.intersects(collider))
						isFree = false;
				}
			}
		}
		return isFree;
	}

	public int getWidth() {
		return map.getWidth() * TILESIZE;
	}

	public int getHeight() {
		return map.getHeight() * TILESIZE;
	}

	@Override
	public boolean blocked(PathFindingContext pfc, int x, int y) {
		// TODO Auto-generated method stub
		return (blockedMap[x][y] == 1);
	}

	@Override
	public float getCost(PathFindingContext pfc, int x, int y) {
		// TODO Auto-generated method stub
		return 1.0f;
	}

	@Override
	public int getHeightInTiles() {
		// TODO Auto-generated method stub
		return map.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return map.getWidth();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
	}

	public Vector2f getPlayerStart() {
		return new Vector2f(map.getObjectX(0, 0) / TILESIZE, map.getObjectY(0, 0) / TILESIZE);
	}

	public Vector2f getTenantStart() {
		return new Vector2f(map.getObjectX(0, 1) / TILESIZE, map.getObjectY(0, 1) / TILESIZE);
	}
}