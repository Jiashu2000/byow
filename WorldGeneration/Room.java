package byow.WorldGeneration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Room implements Serializable{
	private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    
	private TETile[][] world;
	private Position start;
	private Position end;
	private int height;
	private int width;
	private Position center;
	private Room closest;

	public Room(TETile[][] world, Position p, int maxH, int maxW) {
		this.world = world;
		this.start = p;
		this.height = byow.Core.RandomUtils.uniform(RANDOM,3,maxH);
		this.width = byow.Core.RandomUtils.uniform(RANDOM,3,maxW);
		this.end = new Position(p.getX()+width-1, p.getY()+height-1);
		this.center = new Position((start.getX() + end.getX())/2 , (start.getY() + end.getY())/2);
	}
	public Room() {
		
	}
	
	public void addRoom() {
		for (int col = 0; col < width; col++) {
			Position colStart = new Position(start.getX() + col, start.getY());
			addCol(col, colStart);
		}
	}
	
	private void addCol(int colNum, Position p) {
		TETile wall = Tileset.WALL;
		TETile floor = Tileset.WATER;
		if (colNum == 0 || colNum == width-1) {
			for (int i = 0; i < height; i++) {
				int xCoord = p.getX();
				int yCoord = p.getY() + i;
				world[xCoord][yCoord] = wall;
			}
		} else {
			world[p.getX()][p.getY()] = wall;
			world[p.getX()][p.getY()+height-1] = wall;
			for (int i = 1; i < height-1; i++) {
				int xCoord = p.getX();
				int yCoord = p.getY() + i;
				world[xCoord][yCoord] = floor;
			}
		}
	}
	
	public Position getEnd() {
		return end;
	}
	
	public Position getStart() {
		return start;
	}
	
	public Room getClosest() {
		return this.closest;
	}
	
	public Position getCenter() {
		return center;
	}
	
	public boolean overlap(Room y) {
		if (start.getX() == end.getX() ||
			start.getY() == end.getY() ||
			y.start.getX() == y.end.getX() ||
			y.start.getY() == y.end.getY()) {
			return false;
		}
		if (end.getX() < y.start.getX() ||
			y.end.getX() < start.getX()) {
			return false;
		}
		if (end.getY() < y.start.getY() ||
			y.end.getY() < start.getY()) {
			return false;
		}
		return true;
	}
	 
	public boolean checkOverlap(ArrayList<Room> roomList) {
		if (roomList.size() == 0) {
			return false;
		}
		for (Room r : roomList) {
			if (this.overlap(r)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkOutBound() {
		if (this.start.getY() < 0 || 
			this.end.getY() >= world.length ||
			this.start.getX() < 0 ||
			this.end.getX() >= world[0].length) {
			return true;
		}
		return false;
	}
	
	public double calDist(Room r) {
		return Math.sqrt((Math.pow(this.center.getX() - r.center.getX(),2) +
		Math.pow(this.center.getY() - r.center.getY(),2)));
	}
	
	public void closest(Room r) {
		this.closest = r;
	}
	
	public Position openGate(Position connect, Room other) {
		if (center.getX() == connect.getX() && center.getY() > connect.getY()) {
			return new Position(center.getX(), start.getY());
		}
		if (center.getX() == connect.getX() && center.getY() < connect.getY()) {
			return new Position(center.getX(), end.getY());
		}
		if (center.getY() == connect.getY() && center.getX() < connect.getX()) {
			return new Position(end.getX(), center.getY());
		}
		if (center.getY() == connect.getY() && center.getX() > connect.getX()) {
			return new Position(start.getX(), center.getY());
		}
		if (center.getY() == connect.getY() && center.getX() == connect.getX()) {
			if (center.getX() < other.getCenter().getX()) {
				return new Position(end.getX(), center.getY());
			}
			if (center.getX() > other.getCenter().getX()) {
				return new Position(start.getX(), center.getY());
			}
			if (center.getY() < other.getCenter().getY()) {
				return new Position(center.getX(), end.getY());
			}
			if (center.getY() > other.getCenter().getY()) {
				return new Position(center.getX(), start.getY());
			}
		}
		return null;
	}
	
	
	
	/**
	public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(50, 50);

        TETile[][] randomTiles = new TETile[50][50];
        for (int x = 0; x < 50; x += 1) {
            for (int y = 0; y < 50; y += 1) {
            	randomTiles[x][y] = Tileset.NOTHING;
            }
        }
        Position p = new Position(10,10);
        Room r = new Room(randomTiles, p, 10, 20);
        r.addRoom();

        ter.renderFrame(randomTiles);
    }
    **/
}
