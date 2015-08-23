package com.foxel.maxel.ld33.states;

import java.util.ArrayList;

import javafx.scene.shape.Shape;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.foxel.maxel.ld33.entities.Entity;
import com.foxel.maxel.ld33.entities.Player;
import com.foxel.maxel.ld33.entities.Tenant;
import com.foxel.maxel.ld33.map.Interactable;
import com.foxel.maxel.ld33.map.NoiseMaker;
import com.foxel.maxel.ld33.map.Map;
import com.foxel.maxel.ld33.resources.Camera;
import com.foxel.maxel.ld33.resources.SortZAxis;

public class StateOne extends BasicGameState {

	private final int STATE_ID;
	private ArrayList<Entity> renderable;
	private Map map;
	private Camera camera;
	private Player player;
	private NoiseMaker bep;
	private ArrayList<Interactable> interactables;
	private Tenant tenant;
	private ArrayList<Tenant> tenants;
	private SortZAxis zSort;

	public StateOne(int STATE_ID) {
		this.STATE_ID = STATE_ID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		renderable = new ArrayList<Entity>();

		map = new Map();
		map.init();
		camera = new Camera(map.getWidth(), map.getHeight());
		player = new Player(map);
		player.init(gc, sbg);

		tenants = new ArrayList<Tenant>();
		tenant = new Tenant(map, 2, 2);
		tenant.init(gc, sbg);
		tenants.add(tenant);

		Tenant snep = new Tenant(map, 10, 10);
		snep.init(gc, sbg);
		tenants.add(snep);

		zSort = new SortZAxis(player, map);

		interactables = new ArrayList<Interactable>();
		interactables = map.getInteractables();

		/*
		 * bep = new NoiseMaker(3, 4, 400f); interactables.add(bep);
		 */

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		camera.translate(g, player);
		map.renderWallLayer();
		for (Interactable i : interactables) {
			i.render(g);
		}
		map.renderBelowEntity(zSort.getBelowPlayer());
		player.render(gc, sbg, g);
		map.renderAboveEntity(zSort.getAbovePlayer());
		for (int i = 0; i < tenants.size(); i++) {
			tenants.get(i).render(gc, sbg, g);
		}

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
			gc.exit();
		player.update(gc, sbg, delta);
		for (int i = 0; i < tenants.size(); i++) {
			tenants.get(i).update(gc, sbg, delta);
		}

		checkInteractables();
	}

	private void checkInteractables() {
		player.interactables.clear();
		for (int i = 0; i < interactables.size(); i++) {
			Interactable boop = interactables.get(i);
			if (boop.getActivationCircle().intersects(player.collider))
				player.interactables.add(boop);
			if (boop.activated) {
				boop.confirmActivation();
				if (boop.id.equals("noisemaker")) {
					distractTenants(new Vector2f(boop.x, boop.y),
							((NoiseMaker) (boop)).distractionCircle);
				}
			}
		}
	}

	private void distractTenants(Vector2f source, Circle collider) {
		for (int i = 0; i < tenants.size(); i++) {
			if (collider.intersects(tenants.get(i).collider))
				tenants.get(i).distract(source);
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return STATE_ID;
	}
}