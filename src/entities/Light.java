package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	Vector3f position;
	Vector3f color;
	Vector3f attenuation = new Vector3f(1.0f, 0.0f, 0.0f);
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	
}
