package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity{
	
	private static final float RUN_SPEED = 40f;
	private static final float TURN_SPEED = 120f;
	private static final float JUMP_POWER = 30f;
	private static final float GRAVITY = -50f;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private float TERRAIN_HEIGHT = 20;
	
	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Terrain terrain) {
		getInput();
		super.increaseRotation(0f, currentTurnSpeed * DisplayManager.getDeltaSeconds(), 0f);
		float distance = currentSpeed * DisplayManager.getDeltaSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0f, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getDeltaSeconds();
		super.increasePosition(0f, upwardsSpeed * DisplayManager.getDeltaSeconds(), 0f);
		
		TERRAIN_HEIGHT = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);;
		if (super.getPosition().y < TERRAIN_HEIGHT) {
			upwardsSpeed = 0;
			isInAir = false;
			super.setPosition(new Vector3f(super.getPosition().x, TERRAIN_HEIGHT, super.getPosition().z));
		}
	}
	
	private void jump() {
		if (!isInAir) {
			isInAir = true;
			upwardsSpeed = JUMP_POWER;
		}
	}
	
	public float getCurrentSpeed() { 
		return currentSpeed; 
	}
	
	private void getInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			currentSpeed = -RUN_SPEED;
		} else {
			currentSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			currentTurnSpeed = TURN_SPEED;
		} else {
			currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
	
}
