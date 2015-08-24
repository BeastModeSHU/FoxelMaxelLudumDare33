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
import com.foxel.maxel.ld33.resources.Camera;
import com.foxel.maxel.ld33.resources.VisionCone;
import com.foxel.maxel.ld33.resources.XMLData;
import com.foxel.maxel.ld33.rendering.Renderer;


public class StateOne extends BasicGameState {

	private final int STATE_ID;
	private ArrayList<Entity> renderable;
	private Map map;
	private Camera camera;
	private Player player;
	private ArrayList<Interactable> interactables;
	private ArrayList<Interactable> playerInteractables;
	private ArrayList<Polygon> allPolys;
	private Renderer renderer;


	public StateOne(int STATE_ID) {
		this.STATE_ID = STATE_ID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		renderable = new ArrayList<Entity>();

		map = new Map();
		map.init();
		XMLData.init(map);
		camera = new Camera(map.getWidth(), map.getHeight());

		player = new Player(map);
		player.init(gc, sbg);

		ArrayList<Tenant> tenants = map.getTenants();
		for (int i = 0; i < tenants.size(); i++) {
			tenants.get(i).init(gc, sbg);
			renderable.add(tenants.get(i));
		}

		renderable.add(player);

		//zSort = new SortZAxis(player, map);

		interactables = new ArrayList<Interactable>();
		interactables = map.getInteractables();
		
		allPolys = new ArrayList<Polygon>();
		
		renderer = new Renderer(player, map, renderable, allPolys);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		camera.translate(g, player);
		renderer.render(gc,sbg, g, allPolys);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
			gc.exit();

		allPolys.clear();
		for (int i = 0; i < renderable.size(); ++i) {
			renderable.get(i).update(gc, sbg, delta);
			Tenant t = null;
			if (renderable.get(i) instanceof Tenant) t = (Tenant)renderable.get(i);
			if (t != null) {
				for (int j = 0; j < t.polys.length; j++) {
					allPolys.add(t.polys[j]);
					if (t.polys[j].intersects(player.collider)) {
						player.spotted();
						//ADD TENANT REACTION TO SPOTTING PLAYER HERE
					}
				}
			}
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
		// TODO Fix function to cehck if an entity is a tenant, then to check
		// for colliders
		for (int i = 0; i < renderable.size(); i++) {

			Entity e = renderable.get(i);
			Tenant t = null;
			if (e instanceof Tenant) t = (Tenant)e;
			if (t != null) {
				if (collider.intersects(t.collider))
					t.distract(source);
			}
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return STATE_ID;
	}
}