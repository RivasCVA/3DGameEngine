package engineTester;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GUIRenderer;
import gui.GUITexture;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import terrains.TerrainTexture;
import terrains.TerrainTexturePack;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		//create the window display
		DisplayManager.CreateDisplay();
		
		//creates loader that loads data into a VAO
		//returns a RawModel
		Loader loader = new Loader();
		
		
		//create a renderer to handle all rendering to screen
		//renders model
		MasterRenderer renderer = new MasterRenderer(loader);
		
		
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("res/tree.png"));
		TexturedModel treeTexturedModel = new TexturedModel(OBJLoader.loadObjModel("res/tree.obj", loader), treeTexture);
		
		ModelTexture lowPolyTreeTexture = new ModelTexture(loader.loadTexture("res/lowPolyTree.png"));
		TexturedModel lowPolyTreeTexturedModel = new TexturedModel(OBJLoader.loadObjModel("res/lowPolyTree.obj", loader), lowPolyTreeTexture);
		
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("res/grassTexture.png"));
		grassTexture.setHasTransparency(true);
		grassTexture.setHasFakeLighting(true);
		TexturedModel grassTexturedModel = new TexturedModel(OBJLoader.loadObjModel("res/grassModel.obj", loader), grassTexture);
		
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("res/fern.png"), 2);
		fernTexture.setHasTransparency(true);
		fernTexture.setHasFakeLighting(true);
		TexturedModel fernTexturedModel = new TexturedModel(OBJLoader.loadObjModel("res/fern.obj", loader), fernTexture);
		
		ModelTexture flowerTexture = new ModelTexture(loader.loadTexture("res/flower.png"));
		flowerTexture.setHasTransparency(true);
		flowerTexture.setHasFakeLighting(true);
		TexturedModel flowerTexturedModel = new TexturedModel(OBJLoader.loadObjModel("res/grassModel.obj", loader), flowerTexture);
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("res/grassy2.png"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("res/mud.png"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("res/grassFlowers.png"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("res/path.png"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("res/blendMap.png"));
		TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture, blendMap);
		Terrain terrain = new Terrain(0, -1, loader, terrainTexturePack, "res/heightMap.png");
		Terrain terrain2 = new Terrain(-1, -1, loader, terrainTexturePack, "res/heightMap.png");
		
		ArrayList<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 400; i++) {
			
			if (i % 4 == 0) {
				float x = random.nextFloat() * Terrain.getSize();
				float z = -random.nextFloat() * Terrain.getSize();
				entities.add(new Entity(treeTexturedModel, new Vector3f(x, terrain.getTerrainHeight(x, z), z), 0.0f, 0.0f, 0.0f, 5.0f));
			}
			if (i % 1 == 0) {
				float x = random.nextFloat() * Terrain.getSize();
				float z = -random.nextFloat() * Terrain.getSize();
				entities.add(new Entity(lowPolyTreeTexturedModel, new Vector3f(x, terrain.getTerrainHeight(x, z)-2, z), 0f, 0.0f, 0f, 1.0f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * Terrain.getSize();
				float z = -random.nextFloat() * Terrain.getSize();
				//entities.add(new Entity(grassTexturedModel, new Vector3f(x, terrain.getTerrainHeight(x, z), z), 0.0f, 0.0f, 0.0f, 4.0f));
			}
			if (i % 3 == 0) {
				float x = random.nextFloat() * Terrain.getSize();
				float z = -random.nextFloat() * Terrain.getSize();
				entities.add(new Entity(fernTexturedModel, random.nextInt(4), new Vector3f(x, terrain.getTerrainHeight(x, z), z), 0.0f, 0.0f, 0.0f, 1.0f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * Terrain.getSize();
				float z = -random.nextFloat() * Terrain.getSize();
				//entities.add(new Entity(flowerTexturedModel, new Vector3f(x, terrain.getTerrainHeight(x, z), z), 0.0f, 0.0f, 0.0f, 4.0f));
			}
		}
		
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture("res/playerTexture.png"));
		TexturedModel playerTexturedModel = new TexturedModel(OBJLoader.loadObjModel("res/person.obj", loader), playerTexture);
		Player player = new Player(playerTexturedModel, new Vector3f(10f, 0f, -10f), 0, 180, 0, 1f);
		
		
		Light sun = new Light(new Vector3f(1000, 1000, 1000), new Vector3f(1.0f, 1.0f, 1.0f));
		ArrayList<Light> lights = new ArrayList<Light>();
		lights.add(sun);
		lights.add(new Light(new Vector3f(120f, 15, -200f), new Vector3f(0.0f, 2.0f, 2.0f), new Vector3f(1.5f, 0.03f, 0.0004f)));
		
		ModelTexture lampTexture = new ModelTexture(loader.loadTexture("res/lamp.png"));
		lampTexture.setHasFakeLighting(true);
		TexturedModel lampTexturedModel = new TexturedModel(OBJLoader.loadObjModel("res/lamp.obj", loader), lampTexture);
		entities.add(new Entity(lampTexturedModel, new Vector3f(120f, 0f, -200f), 0f, 0f, 0f, 1f));
		
		
		Camera camera = new Camera(player);
		
		ArrayList<GUITexture> guis = new ArrayList<GUITexture>();
		GUITexture healthGUI = new GUITexture(loader.loadTexture("res/health.png"), new Vector2f(-1f + 0.2f, 1f - 0.05f), new Vector2f(0.2f, 0.2f));
		guis.add(healthGUI);
		
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		
		while (!Display.isCloseRequested()) {
			camera.updateMove();
			
			player.move(terrain);
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(player);
			for (int i = 0; i < entities.size(); i++) {
				renderer.processEntity(entities.get(i));
			}
			
			renderer.render(camera, lights);
			for (int i = 0; i < guis.size(); i++) {
				guiRenderer.render(guis);
			}
			
			//updates the screen
			DisplayManager.UpdateDisplay();
		}
		
		guiRenderer.cleanUp();
		//closing and clearing up memory
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.CloseDisplay();

	}

}
