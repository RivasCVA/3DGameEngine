package renderEngine;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shader.TerrainShader;
import terrains.Terrain;
import toolBox.Maths;

public class TerrainRenderer {
	
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadTerrainTexturePack();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	//renders all entities
	public void render(ArrayList<Terrain> terrains) {
		for(Terrain terrain:terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain();
		}
	}
	
	//sets up the properties for each entity
	public void loadModelMatrix(Terrain terrain) {
		Matrix4f tranformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0.0f, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTranformationMatrix(tranformationMatrix);
	}
	
	//sets up the properties for each model
	public void prepareTerrain(Terrain terrain) {
		GL30.glBindVertexArray(terrain.getModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		shader.loadShineVariables(1, 0);
		bindTextures(terrain);
	}
	
	public void bindTextures(Terrain terrain) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTerrainTexturePack().getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTerrainTexturePack().getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTerrainTexturePack().getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTerrainTexturePack().getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTerrainTexturePack().getBlendMap().getTextureID());
	}
	
	//unbinds the model and VAO
	public void unbindTerrain() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
}
