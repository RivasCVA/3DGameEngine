package renderEngine;

import java.util.ArrayList;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.TexturedModel;
import shader.StaticShader;
import toolBox.Maths;

public class EntityRenderer {
	
	StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	//renders all entities
	public void render(Map<TexturedModel, ArrayList<Entity>> entities) {
		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			ArrayList<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				prepareEntity(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	//sets up the properties for each entity
	public void prepareEntity(Entity entity) {
		Matrix4f tranformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTranformationMatrix(tranformationMatrix);
		shader.loadTextureAtlasOffset(entity.getTextureAtlasOffsetX(), entity.getTextureAtlasOffsetY());
	}
	
	//sets up the properties for each model
	public void prepareTexturedModel(TexturedModel model) {
		GL30.glBindVertexArray(model.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		if (model.getModelTexture().getHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadNumberOfRowsOrColumns(model.getModelTexture().getNumberOfRowsOrColumns());
		shader.loadHasFakeLighting(model.getModelTexture().getHasFakeLighting());
		shader.loadShineVariables(model.getModelTexture().getShineDamper(), model.getModelTexture().getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getTextureID());
	}
	
	//unbinds the model and VAO
	public void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	/* Old rendering method;inneficient
	public void render(Entity entity, StaticShader shader) {
		
		TexturedModel texturedModel = entity.getModel();
		RawModel rawModel = texturedModel.getRawModel();
		
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getTextureID());
		
		Matrix4f tranformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTranformationMatrix(tranformationMatrix);
		
		shader.loadShineVariables(texturedModel.getModelTexture().getShineDamper(), texturedModel.getModelTexture().getReflectivity());
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	*/
	
}
