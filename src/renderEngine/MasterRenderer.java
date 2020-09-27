package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shader.StaticShader;
import shader.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 1.0f;
	private static final float FAR_PLANE = 1000.0f;
	private Matrix4f projectionMatrix;
	
	private float RED = 0.7f;
	private float GREEN = 0.7f;
	private float BLUE = 0.7f;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private Map<TexturedModel, ArrayList<Entity>> entities = new HashMap<TexturedModel, ArrayList<Entity>>();
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	
	public MasterRenderer(Loader loader) {
		enableCulling();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
	}
	
	public void prepare() {
		//clears and prepares the screen for drawing
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1.0f);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	//renders everything onto the screen
	public void render(Camera camera, ArrayList<Light> lights) {
		prepare();
		
		shader.start();
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadViewMatrix(camera);
		shader.loadLights(lights);
		renderer.render(entities);
		shader.stop();
		entities.clear();
		
		terrainShader.start();
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		
		skyboxRenderer.render(camera);
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	//places the passed entity into the hashmap
	public void processEntity(Entity entity) {
		ArrayList<Entity> batch = entities.get(entity.getModel());
		if (batch == null) {
			//if the model does not exist yet it is added along with the entity
			ArrayList<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entity.getModel(), newBatch);
		} else {
			//if the model exists the entity is added to the existing list
			batch.add(entity);
			entities.put(entity.getModel(), batch);
		}
	}
	
	//cleans up rendering suff
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	//creates the Projection Matrix (copied code)
		private void createProjectionMatrix() {
	        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
	        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
	        float x_scale = y_scale / aspectRatio;
	        float frustum_length = FAR_PLANE - NEAR_PLANE;
	        
	        projectionMatrix = new Matrix4f();
	        projectionMatrix.m00 = x_scale;
	        projectionMatrix.m11 = y_scale;
	        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
	        projectionMatrix.m23 = -1;
	        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
	        projectionMatrix.m33 = 0;
	    }
	
}
