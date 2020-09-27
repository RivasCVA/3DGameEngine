package terrains;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import toolBox.Maths;

public class Terrain {
	
	private static final int SIZE = 800;
	private static final float MAX_HEIGHT = 20;
	private static final int MAX_RGB_COLOR = 255 + 255 + 255;
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack terrainTexturePack;
	
	private float heights[][];
	
	public Terrain(float gridX, float gridZ, Loader loader, TerrainTexturePack terrainTexturePack, String heightMap) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		model = generateTerrain(loader, heightMap);
		this.terrainTexturePack = terrainTexturePack;
	}
	
	public static int getSize() {
		return SIZE;
	}


	public TerrainTexturePack getTerrainTexturePack() {
		return terrainTexturePack;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}
	
	public float getTerrainHeight(float worldX, float worldZ) {
		//gets the position of player in terms of terrain coordinates
		//terrain x,z start from top-left
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float SizeOfGridSquare = SIZE / (float)(heights.length - 1);
		
		//checks to see on what grid square(2 triangles) the player is standing
		//PLAYER SHOULD BE ON THE TERRAIN PASSED IN THE MOVE() FUNCTION
		int gridX = (int) Math.floor(terrainX/SizeOfGridSquare);
		int gridZ = (int) Math.floor(terrainZ/SizeOfGridSquare);
		if (gridX < 0 || gridX >= heights.length || gridX < 0 || gridX >= heights.length) {
			return 0f;
		}
		
		//gets the position of player in terms of a terrain grid square which is (0,0) to (1,1)
		float xCoordOnGridSquare = (terrainX % SizeOfGridSquare) / SizeOfGridSquare;
		float zCoordOnGridSquare = (terrainZ % SizeOfGridSquare) / SizeOfGridSquare;
		
		float finalHeight;
		//Checks to see on what triangle of the grid square the player is on
		if(xCoordOnGridSquare <= 1 - zCoordOnGridSquare) {
			finalHeight = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(0, heights[gridX][gridZ+1], 1), new Vector3f(1, heights[gridX+1][gridZ], 0), new Vector2f(xCoordOnGridSquare, zCoordOnGridSquare));
		}
		else {
			finalHeight = Maths.barryCentric(new Vector3f(1, heights[gridX+1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ+1], 1), new Vector3f(1, heights[gridX+1][gridZ+1], 1), new Vector2f(xCoordOnGridSquare, zCoordOnGridSquare));
		}
		return finalHeight;
	}

	private RawModel generateTerrain(Loader loader, String heightMap){
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(heightMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				float height = getHeight(image, j, i);
				heights[j][i] = height;
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = getNormal(image, j, i);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVao(vertices, textureCoords, normals, indices);
	}
	
	private float getHeight(BufferedImage image, int x, int z) {
		if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0f;
		}
		
		Color color = new Color(image.getRGB(x, z));
		float totalColor = color.getRed() + color.getGreen() + color.getBlue();
		totalColor -= (float) (MAX_RGB_COLOR / 2);
		totalColor /= (float) (MAX_RGB_COLOR/ 2);
		totalColor *= MAX_HEIGHT;
		float height = totalColor;
		return height;
	}
	
	private Vector3f getNormal(BufferedImage image, int x, int z) {
		float heightL = getHeight(image, x-1, z);
		float heightR = getHeight(image, x+1, z);
		float heightD = getHeight(image, x, z-1);
		float heightU = getHeight(image, x, z+1);
		Vector3f normal = new Vector3f(heightL-heightR, 2.0f, heightD-heightU);
		normal.normalise();
		return normal;
	}
	
}
