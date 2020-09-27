package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {
	
	public static RawModel loadObjModel(String fileName, Loader loader) {
		//opens the file into a FileReader
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("File could not be loaded!");
			e.printStackTrace();
		}
		
		//The Lists hold the data directly from the file
		//The Arrays hold the data from the Lists in a specific order for rendering
		ArrayList<Vector3f> verticies = new ArrayList<Vector3f>();
		ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Integer> indicies = new ArrayList<Integer>();
		float[] verticiesArray = null;
		float[] texturesArray = null;
		float[] normalsArray = null;
		int[] indiciesArray = null;
		
		//BufferedReader reads each line from a FileReader
		BufferedReader reader = new BufferedReader(fr);
		String line;
		
		try {
			//stores the vertex positions, vertex textures, and vertex normals into Lists
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					verticies.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					//sets the size of the texutres and normals array to the verticies size * number per vertex
					texturesArray = new float[verticies.size()*2];
					normalsArray = new float[verticies.size()*3];
					break;
				}
			}
			
			//loads the vertexIndex, textureCoord, and normals indexes from the 'f' lines in .Obj file
			//and store the correspending data from the Lists into their Array in the order set
			while(line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indicies, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2, indicies, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3, indicies, textures, normals, texturesArray, normalsArray);
			
				line = reader.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//sets the size for the vertex and index arrays
		verticiesArray = new float[verticies.size()*3];
		indiciesArray = new int[indicies.size()];
		
		//stores the verticies from the list directly to the float array
		//NOTE: the verticies are placed in the same order as the .Obj file had them in
		int vertexPointer = 0;
		for(Vector3f vertex:verticies) {
			verticiesArray[vertexPointer++] = vertex.x;
			verticiesArray[vertexPointer++] = vertex.y;
			verticiesArray[vertexPointer++] = vertex.z;
		}
		
		//stores the indicies from the list to the float array
		for(int i = 0; i < indicies.size(); i++) {
			indiciesArray[i] = indicies.get(i);
		}
		
		//creates a new RawModel and returns it
		return loader.loadToVao(verticiesArray, texturesArray, normalsArray, indiciesArray);
	}
	
	
	//processes each vertex from the 'f' lines in the .Obj file
	//each line has the vertex data for a triangle: index/UVcoord/normal
	  //-goal is to align all of the data in the same order within their array
	private static void processVertex(String[] vertexData, ArrayList<Integer> indicies, ArrayList<Vector2f> textures, ArrayList<Vector3f> normals, float[] texturesArray, float[] normalsArray) {
		
		//gets the vertex index from the vertexData
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indicies.add(currentVertexPointer);
		
		//gets the textureCoord for the current vertex index and stores it in the vertexArray
		Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
		texturesArray[currentVertexPointer*2] = currentTexture.x;
		texturesArray[currentVertexPointer*2 + 1] = 1 - currentTexture.y;
		
		//gets the normal for the current vertex index and stores it in the normalArray
		Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer*3] = currentNormal.x;
		normalsArray[currentVertexPointer*3 + 1] = currentNormal.y;
		normalsArray[currentVertexPointer*3 + 2] = currentNormal.z;
	}
	
}
