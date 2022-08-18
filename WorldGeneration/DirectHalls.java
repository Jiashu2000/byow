package byow.WorldGeneration;

import java.io.Serializable;
import java.util.Random;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class DirectHalls implements Serializable {
	private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    
	private TETile[][] world;
	private Position start;
	private Position end;
	private int height;
	private int width;
	private String dir;
	
	TETile hallwall = Tileset.WALL;
	TETile hallfloor = Tileset.WATER;

	public DirectHalls(TETile[][] world, Position start, Position end, String dir) {
		this.world = world;
		this.dir = dir;
		this.start = start;
		this.end = end;
		if (dir == "vertical") {
			this.height = Math.abs(this.start.getY() - this.end.getY()) + 1;
			this.width = 3;
		} else {
			this.height = 3;
			this.width = Math.abs(this.start.getX() - this.end.getX()) + 1;
		}
	}
	
	public DirectHalls() {
		
	}
	
	private boolean checkFloor(int x, int y) {
		if (world[x][y] != hallfloor) {
			return true;
		}
		return false;
	}

	public void addHall() {
		if (dir == "vertical") {
			for (int col = 0; col < width; col++) {
				Position colStart = new Position(start.getX() -1 + col, start.getY());
				addCol(col, colStart);
			}
		} else {
			for (int col = 0; col < width; col++) {
				Position colStart = new Position(start.getX() + col, start.getY() -1);
				addCol(col, colStart);
			}
		}

	}
	
	private void addCol(int colNum, Position p) {
		if (dir == "vertical") {
			if (colNum == 0 || colNum == width-1) {
				for (int i = 0; i < height; i++) {
					int xCoord = p.getX();
					int yCoord = p.getY() + i;
					if (checkFloor(xCoord,yCoord)) {
						world[xCoord][yCoord] = hallwall;
					}
				}
			} else {
				for (int i = 0; i < height; i++) {
					int xCoord = p.getX();
					int yCoord = p.getY() + i;
					world[xCoord][yCoord] = hallfloor;
				}
			}
		} else {
			if (checkFloor(p.getX(),p.getY())) {
				world[p.getX()][p.getY()] = hallwall;
			}
			if (checkFloor(p.getX(),p.getY()+height-1)) {
				world[p.getX()][p.getY()+height-1] = hallwall;
			}
			for (int i = 1; i < height-1; i++) {
				int xCoord = p.getX();
				int yCoord = p.getY() + i;
				world[xCoord][yCoord] = hallfloor;
			}
		}
	}
	

}
