package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Entity {
	
	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	
	private int textureAtlasIndex = 0;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Entity(TexturedModel model, int textureAtlasIndex, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.textureAtlasIndex = textureAtlasIndex;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public float getTextureAtlasOffsetX() {
		int column = textureAtlasIndex % model.getModelTexture().getNumberOfRowsOrColumns();
		float xOffset = (float)column / (float)model.getModelTexture().getNumberOfRowsOrColumns();
		return xOffset;
	}
	
	public float getTextureAtlasOffsetY() {
		int row = (int) Math.floor(textureAtlasIndex / model.getModelTexture().getNumberOfRowsOrColumns());
		float yOffset = (float)row / (float)model.getModelTexture().getNumberOfRowsOrColumns();
		return yOffset;
	}

	public int getTextureAtlasIndex() {
		return textureAtlasIndex;
	}

	public void setTextureAtlasIndex(int textureAtlasIndex) {
		this.textureAtlasIndex = textureAtlasIndex;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		position.x += dx;
		position.y += dy;
		position.z += dz;
	}
	
	public void increaseRotation(float rx, float ry, float rz) {
		rotX += rx;
		rotY += ry;
		rotZ += rz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
}
