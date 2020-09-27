package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

public class Camera {
	
	private float angleAroundPlayer = 0f;
	private float distanceFromPlayer = 50f;
	
	private final float MAX_ZOOM = 100;
	private final float MIN_ZOOM = 10;
	private final float ZOOM_SPEED = 0.01f;
	private final float PITCH_SPEED = 0.2f;
	private final float YAW_SPEED = 0.2f;
	
	
	private Vector3f position = new Vector3f(0f, 0f, 0f);
	private float pitch = 10;
	private float yaw;
	private float roll;
	
	private Player player;
	
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public void updateMove() {
		//Updates mouse inputs
		calculateZoom();
		calculateAngleAroundPlayer();
		calculatePitch();
		limitCameraMovement();
		
		//Distances from player
		float vD = calculateVerticalDistance();
		float hD = calculateHorizontalDistance();
		
		//Distances to move camera
		float dZ = (float) (hD * Math.cos(Math.toRadians(player.getRotY() + angleAroundPlayer)));
		float dX = (float) (hD * Math.sin(Math.toRadians(player.getRotY() + angleAroundPlayer)));
	
		//updates camera position and rotation
		updateCamPos(dX, vD, dZ);
		updateCamRot();
		centerCameraYaw();
	}
	
	private void centerCameraYaw() {
		//slowly centers the camera yaw when the player moves forward
		if (player.getCurrentSpeed() > 0) {
			if (angleAroundPlayer > 0 && angleAroundPlayer > 1) {
				angleAroundPlayer -= 60f * DisplayManager.getDeltaSeconds();
			} else if (angleAroundPlayer < 0 && angleAroundPlayer < -1) {
				angleAroundPlayer += 60f * DisplayManager.getDeltaSeconds();
			}
		}
	}
	
	private void limitCameraMovement() {
		//limits the camera pitch from going under the map or over the player
		if (position.y < 1) {
			position.y = 1;
		} else if (pitch > 90) {
			pitch = 90;
		}
		
		//limits the camera from zooming too far or too close
		if (distanceFromPlayer > MAX_ZOOM) {
			distanceFromPlayer = MAX_ZOOM;
		} else if (distanceFromPlayer < MIN_ZOOM) {
			distanceFromPlayer = MIN_ZOOM;
		}
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(this.pitch)));
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(this.pitch)));
	}
	
	private void updateCamPos(float dX, float dY, float dZ) {
		position.y = player.getPosition().y + dY;
		position.x = player.getPosition().x - dX;
		position.z = player.getPosition().z - dZ;
	}
	
	private void updateCamRot() {
		yaw = 180 - player.getRotY() - angleAroundPlayer;
	}
	
	private void calculateZoom() {
		if (distanceFromPlayer <= MAX_ZOOM && distanceFromPlayer >= MIN_ZOOM) {
			float zoomLevel = Mouse.getDWheel() * ZOOM_SPEED;
			distanceFromPlayer -= zoomLevel;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float mouseXMovement = Mouse.getDX() * YAW_SPEED;
			angleAroundPlayer -= mouseXMovement;
		}
	}
	
	private void calculatePitch() {
		if (Mouse.isButtonDown(0)) {
			float mouseYMovement = Mouse.getDY() *PITCH_SPEED;
			this.pitch -= mouseYMovement;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
}
