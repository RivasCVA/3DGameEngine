package shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	//variable used to load matrix values
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		//loads the shader file into a shader and returns its ID
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		
		//creates the shader program
		programID = GL20.glCreateProgram();
		
		//attaches the shaders to the program
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		//defines the input shader variables from the attribute list in the VAO
		bindAttributes();
		
		//links the program to execute its shaders to their
		//specified programmable processors (vertex processor, fragment processor)
		GL20.glLinkProgram(programID);
		
		//checks to see if the executables(shaders) in the program can be successfully executed
		GL20.glValidateProgram(programID);
		
		//defines the uniform variables
		getAllUniformLocations();
		
	}
	
	protected abstract void bindAttributes();
	protected abstract void getAllUniformLocations();
	
	//binds the variables stated in the shaders to the attributes in the vao
	protected void bindAttribute(int position, String variableName) {
		GL20.glBindAttribLocation(programID, position, variableName);
	}
	
	//gets the location of a uniform variable in a shader file
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	//functions that load specific types of values into the corresponding uniform variable
	protected void loadFloat(int uniformLocation, float value) {
		GL20.glUniform1f(uniformLocation, value);
	}
	protected void loadInt(int uniformLocation, int value) {
		GL20.glUniform1i(uniformLocation, value);
	}
	protected void loadVector(int uniformLocation, Vector3f vector) {
		GL20.glUniform3f(uniformLocation, vector.x, vector.y, vector.z);
	}
	protected void loadVector2f(int uniformLocation, Vector2f vector) {
		GL20.glUniform2f(uniformLocation, vector.x, vector.y);
	}
	protected void loadBoolean(int uniformLocation, boolean value) {
		float toLoad = 0.0f;
		if (value == true) {
			toLoad = 1.0f;
		}
		GL20.glUniform1f(uniformLocation, toLoad);
	}
	protected void loadMatrix(int uniformLocation, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(uniformLocation, false, matrixBuffer);
	}
	
	//cleans up to save memory
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	//must be used each frame
	//before and after rendering
	public void start() {
		GL20.glUseProgram(programID);
	}
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	//COPY & PASTED CODE
	//reads the glsl shader and stores the code into a glShader
	//returns the glShaderID
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		  try{
		   BufferedReader reader = new BufferedReader(new FileReader(file));
		   String line;
		   while((line = reader.readLine())!=null){
		    shaderSource.append(line).append("//\n");
		   }
		   reader.close();
		  }catch(IOException e){
		   e.printStackTrace();
		   System.exit(-1);
		  }
		  int shaderID = GL20.glCreateShader(type);
		  GL20.glShaderSource(shaderID, shaderSource);
		  GL20.glCompileShader(shaderID);
		  if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
		   System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
		   System.err.println("Could not compile shader!");
		   System.exit(-1);
		  }
		  return shaderID;
	}

}
