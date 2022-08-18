package byow.Core;

import java.awt.Color;

import java.awt.Font;
import java.io.Serializable;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGeneration.Avatar;
import byow.WorldGeneration.Position;
import byow.WorldGeneration.RoomMapMST;
import edu.princeton.cs.introcs.StdDraw;


public class WorldGeneration implements Serializable {
	
	private static final int WIDTH = 50;
	private static final int HEIGHT = 50;
	private RoomMapMST myWorld;
	private TERenderer ter;
	private TETile[][] world;
	private Avatar avatar;
	private int seed;
	
	public WorldGeneration() {
		
	}
	
	public WorldGeneration(int seed) {
		this.seed = seed;
		this.world = new TETile[WIDTH][HEIGHT];
		this.myWorld = new RoomMapMST(world, seed); 
		this.ter = new TERenderer();
		this.avatar = myWorld.getAvatar();
		 for (int x = 0; x < WIDTH; x += 1) {
		    	for (int y = 0; y < HEIGHT; y += 1) {
		    		world[x][y] = Tileset.NOTHING;
		    	}
		    }

			myWorld.drawRoomMap();
			ter.initialize(50, 50);
			ter.renderFrame(world);
	}
	
	public void initialize() {
	    for (int x = 0; x < WIDTH; x += 1) {
	    	for (int y = 0; y < HEIGHT; y += 1) {
	    		world[x][y] = Tileset.NOTHING;
	    	}
	    }

		myWorld.drawRoomMap();
		ter.initialize(50, 50);
		ter.renderFrame(world);
	}
	
	public void HUD() {
		int mouseX = (int) Math.floor(StdDraw.mouseX());
		int mouseY = (int) Math.floor(StdDraw.mouseY());
		TETile mouse = world[mouseX][mouseY];
		Font smallFont = new Font("Monaco", Font.BOLD, 20);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.setFont(smallFont);
		StdDraw.textLeft(1, HEIGHT - 1, mouse.description());
		StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
		StdDraw.show();
		ter.renderFrame(world);
	}
	
	public void moveAvatar(char dir) {
		avatar.moveAvatar(dir);
		ter.renderFrame(world);
	}

}
