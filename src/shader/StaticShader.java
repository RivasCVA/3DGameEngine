package shader;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolBox.Maths;

public class StaticShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "src/shader/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shader/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int[] location_lightPosition;
	private int[] location_lightColor;
	private int[] location_attenuation;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_hasFakeLighting;
	private int location_skyColor;
	private int location_textureNumberOfRowsOrColumns;
	private int location_textureAtlasOffset;
	
	public StaticShader() {
		//constructor of the ShaderProgram
		super(VERTEX_FILE, FRAGMENT_FILE);
		
	}

	//binds the vao attributes to the shader variables
	//An Abstract method, so it must be done!
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_hasFakeLighting = super.getUniformLocation("hasFakeLighting");
		location_skyColor = super.getUniformLocation("skyColor");
		location_textureNumberOfRowsOrColumns = super.getUniformLocation("textureNumberOfRowsOrColumns");
		location_textureAtlasOffset = super.getUniformLocation("textureAtlasOffset");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	public void loadNumberOfRowsOrColumns(int numberOfRowsOrColumns) {
		super.loadFloat(location_textureNumberOfRowsOrColumns, numberOfRowsOrColumns);
	}
	
	public void loadTextureAtlasOffset(float x, float y) {
		super.loadVector2f(location_textureAtlasOffset, new Vector2f(x, y));
	}
	
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(location_skyColor, new Vector3f(r, g, b));
	}
	
	public void loadHasFakeLighting(boolean isFake) {
		super.loadBoolean(location_hasFakeLighting, isFake);
	}
	
	public void loadLights(ArrayList<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}
			else {
				super.loadVector(location_lightPosition[i], new Vector3f(0f, 0f, 0f));
				super.loadVector(location_lightColor[i], new Vector3f(0f, 0f, 0f));
				super.loadVector(location_attenuation[i], new Vector3f(1f, 0f, 0f));
			}
		}
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	//loads the Transformation Matrix into its corresponding uniform variable
	public void loadTranformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	//loads the Projection Matrix into its corresponding uniform variable
	//Done once
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	//loads the Projection Matrix into its corresponding uniform variable
	public void loadViewMatrix(Camera camera) {
		Matrix4f view = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, view);
	}

}
