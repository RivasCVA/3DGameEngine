package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	private static final int FPS_CAP = 60;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void CreateDisplay() {
		
		//sets up the display attributes
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		try {
			//sets the type and size of the display
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			//creates the display
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("My First Game!");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//defines the area of the screen we want to draw on
		GL11.glViewport(0,0, WIDTH, HEIGHT);
		
		//runs the game loop at specified fps and sleeps the loop automatically
		Display.sync(FPS_CAP); //replaced by vsync
		Display.setVSyncEnabled(true);
		
		lastFrameTime = getCurrentTime();
	}
	
	
	public static void UpdateDisplay() {
		
		//updates the window
		Display.update();
		
		long currentTime = getCurrentTime();
		delta = (currentTime - lastFrameTime) / 1000f;
		lastFrameTime = currentTime;
	}
	
	public static void CloseDisplay() {
		//destroys the display
		Display.destroy();
		
	}
	
	public static float getDeltaSeconds() {
		return delta;
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
}
