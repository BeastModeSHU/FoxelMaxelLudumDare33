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
import com.foxel.maxel.ld33.map.HidingPlace;
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
	private Tenant tenant;
	private VisionCone vis;
	private Polygon[] polys;
	private Image tex;
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

		player = new Player(map, Constants.ENTITY_PLAYER);
		player.init(gc, sbg);

		tenant = new Tenant(map, Constants.ENTITY_TENANT, 2, 2);
		tenant.init(gc, sbg);

		Tenant snep = new Tenant(map, Constants.ENTITY_TENANT, 10, 10);
		snep.init(gc, sbg);

		renderable.add(player);
		renderable.add(tenant);
		renderable.add(snep);

		// zSort = new SortZAxis(player, map);

		interactables = new ArrayList<Interactable>();

		vis = new VisionCone(tenant.getPixelLocation().x, tenant.getPixelLocation().y,
				tenant.angle, (float) (Math.PI / 2), 30, 32f, 4f, map);
		polys = vis.updateCone(tenant.getPixelLocation().x, tenant.getPixelLocation().y,
				tenant.angle);
		tex = new Image(Constants.VISIONCONE_LOC, false, Image.FILTER_NEAREST);

		interactables = map.getInteractables();

		renderer = new Renderer(player, map, renderable, interactables);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		camera.translate(g, player);
		renderer.render(gc, sbg, g);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
			gc.exit();

		for (int i = 0; i < renderable.size(); ++i) {
			renderable.get(i).update(gc, sbg, delta);
		}

		if (gc.getInput().isKeyPressed(Input.KEY_X))
			checkInteractables();

		polys = vis.updateCone(tenant.getPixelLocation().x + 32f,
				tenant.getPixelLocation().y + 32f, tenant.angle);
	}

	private void checkInteractables() {

		for (int i = 0; i < interactables.size(); ++i) {

			if (interactables.get(i).getActivationCircle().intersects(player.getCollider())) {

				switch (interactables.get(i).getClass().getSimpleName()) {
				case Constants.NOISEMAKER_OBJECT:
					NoiseMaker temp = (NoiseMaker) (interactables.get(i));
					distractTenants(new Vector2f(temp.getLocation().x, temp.getLocation().y),
							temp.getDistractionCircle(), i);
					break;
				case Constants.HIDINGSPOT_OBJECT:
					System.out.println("Hey");
					hidePlayer(i);
					break;
				}

			}

		}
	}

	private void distractTenants(Vector2f source, Circle collider, int ID) {

		for (int i = 0; i < renderable.size(); ++i) {
			if (renderable.get(i).getClass().getSimpleName().equals(Constants.ENTITY_TENANT)) {

				Tenant temp = (Tenant) renderable.get(i);
				if (collider.intersects(temp.getCollider())) {
					temp.distract(source, ID);
				}
			}
		}
	}

	private void hidePlayer(int index) {
		if (!player.isPlayerHiding() && !interactables.get(index).isActivated()) {
			interactables.get(index).activate();
			player.setHidden(true);
		} else if (player.isPlayerHiding()) {
			player.setHidden(false);
			interactables.get(index).deactivate();
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return STATE_ID;
	}
}