package com.foxel.maxel.ld33.states;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.constants.Constants;
import com.foxel.maxel.ld33.entities.Entity;
import com.foxel.maxel.ld33.entities.Player;
import com.foxel.maxel.ld33.entities.Tenant;
import com.foxel.maxel.ld33.map.Interactable;
import com.foxel.maxel.ld33.map.NoiseMaker;
import com.foxel.maxel.ld33.map.Map;
import com.foxel.maxel.ld33.map.Target;
import com.foxel.maxel.ld33.resources.Camera;
import com.foxel.maxel.ld33.resources.XMLData;
import com.foxel.maxel.ld33.rendering.Renderer;

public class StateOne extends BasicGameState {

	private final int STATE_ID;
	private ArrayList<Entity> renderable;
	private Map map;
	private Camera camera;
	private Player player;
	private ArrayList<Interactable> interactables;
	private ArrayList<Polygon> allPolys;
	private Renderer renderer;
	private float spottedTimer = 0;
	private int targetCount = 0;
	private Image splash;
	private boolean hasBegun;

	public StateOne(int STATE_ID) {
		this.STATE_ID = STATE_ID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		splash = new Image(Constants.OPENING_WINDOW_LOC);
		hasBegun = false;
		renderable = new ArrayList<Entity>();

		map = new Map();
		map.init();

		XMLData.init(map);

		camera = new Camera(map.getWidth(), map.getHeight());

		player = new Player(map, Constants.ENTITY_PLAYER);
		player.init(gc, sbg);

		ArrayList<Tenant> tenants = map.getTenants(camera);
		for (int i = 0; i < tenants.size(); i++) {
			tenants.get(i).init(gc, sbg);
			renderable.add(tenants.get(i));
		}

		renderable.add(player);

		// zSort = new SortZAxis(player, map);

		interactables = new ArrayList<Interactable>();
		interactables = map.getInteractables();

		for (int i = 0; i < interactables.size(); ++i) {
			if (interactables.get(i).getID() == -1)
				++targetCount;
		}

		allPolys = new ArrayList<Polygon>();

		renderer = new Renderer(camera, player, map, renderable, interactables, allPolys);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (hasBegun) {
			camera.translate(g, player);

			renderer.render(gc, sbg, g, allPolys);
		} else {
			g.drawImage(splash, 0, 0);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (hasBegun) {
			boolean spotted = false;
			if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
				gc.exit();
			allPolys.clear();

			for (int i = 0; i < renderable.size(); ++i) {
				renderable.get(i).update(gc, sbg, delta);
				Tenant t = null;
				if (renderable.get(i) instanceof Tenant)
					t = (Tenant) renderable.get(i);
				if (t != null) {

					for (int j = 0; j < t.polys.length; j++) {
						allPolys.add(t.polys[j]);
						if (t.polys[j].intersects(player.getCollider()) && !player.isPlayerHiding()) {
							player.spotted();
							spotted = true;
							// ADD TENANT REACTION TO SPOTTING PLAYER HERE
						}
					}
				}
			}

			if (spotted) {
				spottedTimer += (delta / 1000.f);
				if (spottedTimer > 0.2f) {
					resetGame(gc, sbg);
					spottedTimer = 0.f;
				}
			} else {
				spottedTimer = 0.f;
			}

			for (int i = 0; i < renderable.size(); ++i) {
				renderable.get(i).update(gc, sbg, delta);
			}

			if (gc.getInput().isKeyPressed(Input.KEY_X))
				checkInteractables();

			if (targetCount <= 0)
				sbg.enterState(Constants.WIN_STATE_ID);
		} else {
			if (gc.getInput().isKeyPressed(Input.KEY_X))
				hasBegun = true;
		}
	}

	private void checkInteractables() {
		ArrayList<Interactable> tempList = new ArrayList<Interactable>();

		for (int i = 0; i < interactables.size(); ++i) {
			if (interactables.get(i).getActivationCircle().intersects(player.getCollider())) {
				tempList.add(interactables.get(i));
			}

		}
		Interactable tempInt = null;

		if (tempList.size() > 0) {
			float closestDistance = new Vector2f(tempList.get(0).getLocation().x + Constants.TILESIZE/2, tempList.get(0).getLocation().y)
					.distanceSquared(player.getPixelLocation());
			tempInt = tempList.get(0);

			if (tempList.size() > 1) {
				tempInt = tempList.get(0);

				for (int i = 1; i < tempList.size(); ++i) {
					float thisDistance = new Vector2f(tempList.get(i).getLocation().x + Constants.TILESIZE/2, tempList.get(i).getLocation().y)
					.distanceSquared(player.getPixelLocation());
					if (thisDistance < closestDistance) {
						closestDistance = thisDistance;
						tempInt = tempList.get(i);
					}
				}

			}
		}
		if (tempInt != null) {
			if (tempInt.getID() == Constants.TV_ID || tempInt.getID() == Constants.RADIO_ID) {

				NoiseMaker temp = (NoiseMaker) (tempInt);
				distractTenants(new Vector2f(temp.getLocation().x, temp.getLocation().y),
						temp.getDistractionCircle());
			}

			if (tempInt.getID() == Constants.BIN_ID || tempInt.getID() == Constants.FRIDGE_ID
					|| tempInt.getID() == Constants.CHAIR_ID
					|| tempInt.getID() == Constants.CLOSET_ID) {

				hidePlayer(tempInt);
			}

			if (tempInt.getID() == -1) {
				killTarget((Target) tempInt);
			}
		}
	}

	private void killTarget(Target target) {

		if (target.isTargetAlive()) {
			targetCount -= 1;
			target.activate();
		}
	}

	private void distractTenants(Vector2f source, Circle collider) {

		for (int i = 0; i < renderable.size(); ++i) {
			if (renderable.get(i).getClass().getSimpleName().equals(Constants.ENTITY_TENANT)) {

				Tenant temp = (Tenant) renderable.get(i);
				if (collider.intersects(temp.getCollider())) {
					temp.distract(source);
				}
			}
		}
	}

	private void hidePlayer(Interactable hidingPlace) {
		if (!player.isPlayerHiding() && !hidingPlace.isActivated()) {
			hidingPlace.activate();
			player.setHidden(true);
			player.setPlayerLocation(hidingPlace.getLocation().x, hidingPlace.getLocation().y);

		} else if (player.isPlayerHiding()) {
			player.setHidden(false);
			hidingPlace.deactivate();
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return STATE_ID;
	}

	private void resetGame(GameContainer gc, StateBasedGame sbg) throws SlickException {
		for (Entity e : renderable) {
			e.init(gc, sbg);
		}
		interactables.clear();
		interactables = map.getInteractables();
		targetCount = 0;
		for (int i = 0; i < interactables.size(); ++i) {
			if (interactables.get(i).getID() == -1)
				++targetCount;
		}
		renderer.setInteractables(interactables);
	}
}