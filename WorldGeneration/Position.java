package byow.WorldGeneration;

import java.io.Serializable;
import java.util.Random;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Position implements Serializable {
	private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void buildConnect(TETile[][] world) {
		TETile hallfloor = Tileset.SAND;
		world[x][y] = hallfloor;
	}

}
