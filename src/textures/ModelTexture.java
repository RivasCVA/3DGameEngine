package textures;

public class ModelTexture {

	private int textureID;
	float shineDamper = 1.0f;
	float reflectivity = 0.0f;
	
	boolean hasTransparency = false;
	boolean hasFakeLighting = false;
	
	private int numberOfRowsOrColumns = 1;
	
	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public ModelTexture(int textureID, int numberOfRowsOrColumns) {
		this.textureID = textureID;
		this.numberOfRowsOrColumns = numberOfRowsOrColumns;
	}
	
	public int getNumberOfRowsOrColumns() {
		return numberOfRowsOrColumns;
	}

	public void setNumberOfRowsOrColumns(int numberOfRowsOrColumns) {
		this.numberOfRowsOrColumns = numberOfRowsOrColumns;
	}

	public int getTextureID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean getHasTransparency() {
		return hasTransparency;
	}

	public boolean getHasFakeLighting() {
		return hasFakeLighting;
	}

	public void setHasFakeLighting(boolean hasFakeLighting) {
		this.hasFakeLighting = hasFakeLighting;
	}
	
}
