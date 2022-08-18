package byow.WorldGeneration;

import java.io.Serializable;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar implements Serializable {
	private static final TETile hallwall = Tileset.WALL;
	private TETile[][] world;
	private Position avatar;
	private int x;
	private int y;
	
	public Avatar(TETile[][] world, Position avatar) {
		this.world = world;
		this.avatar = avatar;
		this.x = avatar.getX();
		this.y = avatar.getY();
	}
	
	
	public void moveAvatar(char dir) {
		Position prevPos = avatar;
		if (checkMove(dir)) {
			restorePrev(prevPos);
			setNew(avatar);
		}
	}
	
	public boolean checkMove(char dir) {
		if (dir == 'a') {
			if (checkBound(avatar.getX()-1, avatar.getY()) &&
					checkTile(avatar.getX()-1, avatar.getY())) {
				this.avatar = new Position(avatar.getX()-1, avatar.getY());
				return true;
			}
		} else if (dir == 'd') {
			if (checkBound(avatar.getX()+1, avatar.getY())&&
					checkTile(avatar.getX()+1, avatar.getY())) {
				this.avatar = new Position(avatar.getX()+1, avatar.getY());
				return true;
			}
		} else if (dir == 'w') {
			if (checkBound(avatar.getX(), avatar.getY()+1) &&
					checkTile(avatar.getX(), avatar.getY()+1)){
				this.avatar = new Position(avatar.getX(), avatar.getY()+1);	
				return true;
			}
		} else if (dir == 's') {
			if (checkBound(avatar.getX(), avatar.getY()-1) &&
					checkTile(avatar.getX(), avatar.getY()-1)) {
				this.avatar = new Position(avatar.getX(), avatar.getY()-1);
				return true;
			};
		}
		System.out.println("Invalid Move");
		return false;
	}
	
	private boolean checkBound(int x, int y) {
		if (y < 0 ||
			y >= world.length ||
			x < 0 ||
			x >= world[0].length) {
			return false;
		}
		return true;
	}
	
	private boolean checkTile(int x, int y) {
		if (world[x][y].description().equals("wall")) {
			return false;
		}
		return true;
	}
	
	private void restorePrev(Position avatar) {
		world[avatar.getX()][avatar.getY()] = Tileset.WATER;
	}
	
	private void setNew(Position avatar) {
		world[avatar.getX()][avatar.getY()] = Tileset.TREE;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
}
