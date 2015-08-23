package com.foxel.maxel.ld33.map;

import java.util.ArrayList;

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
	private final int FLOOR_LAYER_ID, WALL_LAYER_ID;

	public Map() {
		this.TILESIZE = Constants.TILESIZE;
		this.FLOOR_LAYER_ID = Constants.FLOOR_LAYER_ID;
		this.WALL_LAYER_ID = Constants.WALL_LAYER_ID;
	}

	public void init() throws SlickException {
		map = new TiledMap(Constants.TEST_MAP_LOC);
		blockedMap = new int[map.getHeight()][map.getWidth()];

		for (int x = 0; x < map.getWidth(); ++x) {
			for (int y = 0; y < map.getWidth(); ++y) {
				int tileID = map.getTileId(x, y, WALL_LAYER_ID); // Collisions detected from
														// wall tile layer
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)) {
					blockedMap[x][y] = 1;
				} else {
					blockedMap[x][y] = 0;
				}
			}
		}

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

	public void renderFloorLayer() throws SlickException {
		map.render(0, 0, FLOOR_LAYER_ID);
	}

	public void renderWallLayer() throws SlickException{
		map.render(0,0,WALL_LAYER_ID);
	}

	public void renderLayerSection(int x, int y, int startX, int startY, int width, int height, int layer) {
		/*
		 * Function which will render a portion of the ceiling tiles to allow
		 * for split rendering for z-sorting */
		
		map.render(x, y, startX, startY, width, height, layer, false);
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

	public boolean isPointFree(Vector2f point) {
		boolean isFree = true;
		for (int i = 0; i < map.getHeight(); ++i) {
			for (int j = 0; j < map.getWidth(); ++j) {
				if (blockedMap[j][i] == 1) {
					if (new Rectangle(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE).contains(
							point.x, point.y))
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

	public Vector2f getSpot(String name) {
		Vector2f spot = new Vector2f(0, 0);
		int group = 1;
		for (int i = 0; i < map.getObjectCount(group); i++) {
			if (map.getObjectName(group, i).equals(name)) {
				spot = new Vector2f(map.getObjectX(group, i) / TILESIZE, map.getObjectY(group, i)
						/ TILESIZE);
			}
		}

		return spot;
	}

	public ArrayList<Interactable> getInteractables() {
		ArrayList<Interactable> list = new ArrayList<Interactable>();
		int interact = Constants.INTERACTABLES_OBJECT_LAYER;

		for (int i = 0; i < map.getObjectCount(interact); ++i) {
			switch (map.getObjectName(interact, i)) {
			case Constants.NOISEMAKER_OBJECT:
				list.add(new NoiseMaker(map.getObjectX(interact, i), map.getObjectY(interact, i),
						400f));
				break;
			case Constants.HIDINGSPOT_OBJECT:
				list.add(new HidingPlace(map.getObjectX(interact, i), map.getObjectY(interact, i)));
				break;
			}

		}
		return list;

	}

}
