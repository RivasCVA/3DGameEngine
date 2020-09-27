package models;

public class RawModel {
	
	//needs these variables to render model onto screen
	private int vaoID;
	private int vertexCount;
	
	//the basis for each model
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
}
