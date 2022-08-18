package byow.WorldGeneration;

import java.io.Serializable;
import java.util.Random;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class TurnHall implements Serializable{
	private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    
	private TETile[][] world;
	private Room x;
	private Room y;
	private Position connect;
	private Position xGate;
	private Position yGate;
	private String dir;
	private TETile hallwall = Tileset.WALL;
	private TETile hallfloor = Tileset.WATER;
	
	public TurnHall(TETile[][] world, Room x, Room y) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.connect = new Position(x.getCenter().getX(),y.getCenter().getY());
	}
	
	public void buildTurn() {
		if (x.getCenter().getX() > y.getCenter().getX()) {
			 if (x.getCenter().getY() > y.getCenter().getY()) {
				 Position xGate = new Position(x.getCenter().getX(), x.getStart().getY());
				 Position yGate = new Position(y.getEnd().getX(), y.getCenter().getY());
				 buildTurnHall(yGate, "dl", xGate, "ur");

			 } else {
				 Position xGate = new Position(x.getCenter().getX(), x.getEnd().getY());
				 Position yGate = new Position(y.getEnd().getX(), y.getCenter().getY());
				 buildTurnHall(yGate, "ul", xGate, "dr");

			 }
		 } else {
			 if (x.getCenter().getY() > y.getCenter().getY()) {
				 Position xGate = new Position(x.getCenter().getX(), x.getStart().getY());
				 Position yGate = new Position(y.getStart().getX(), y.getCenter().getY());
				 buildTurnHall(yGate, "dr", xGate, "ul");

			 } else {
				 Position xGate = new Position(x.getCenter().getX(), x.getEnd().getY());
				 Position yGate = new Position(y.getStart().getX(), y.getCenter().getY());
				 buildTurnHall(yGate, "ur", xGate, "dl");
			 }
		 }
	}
	
	private void buildTurnHall(Position yGate, String dirY, Position xGate, String dirX) {
		verticalTurnHall verHall = new verticalTurnHall(xGate,dirX, this.connect);
		HorizontalTurnHall horHall = new HorizontalTurnHall(yGate,dirY, this.connect);
		verHall.buildVerticalHall();
		horHall.buildHorizontalHall();
		
	}
	
	private class verticalTurnHall{
		private int height;
		private int width;
		private String dir;
		private Position xPos;
		private Position connect;
		
		public verticalTurnHall(Position xGate, String dirX, Position connect) {
			this.xPos = xGate;
			this.width = 3;
			this.connect = connect;
			this.height = Math.abs(connect.getY() - xPos.getY()) + 1;
			this.dir = dirX;
		}
		
		public void buildVerticalHall() {
			if (this.dir == "ul") {
				for (int col = 2; col >= 0; col--) {
					Position colStart = new Position(xPos.getX() + 1 - col, connect.getY());
					addVerticalCol(col, colStart);
				}
			} else if (this.dir == "dl") {
				for (int col = 2; col >= 0; col--) {
					Position colStart = new Position(xPos.getX() + 1 - col, xPos.getY());
					addVerticalCol(col, colStart);
				}
			} else if (this.dir == "ur")  {
				for (int col = 0; col <= 2; col++) {
					Position colStart = new Position(xPos.getX() -1 + col, connect.getY());
					addVerticalCol(col, colStart);
				}
			} else {
				for (int col = 0; col <= 2; col++) {
					Position colStart = new Position(xPos.getX() -1 + col, xPos.getY());
					addVerticalCol(col, colStart);
				}
			}
		}

		/** 
		 * 
		 * @param colNum : short:0 ; middle:1 ; long:2 ;
		 * @param p
		 */
		private void addVerticalCol(int colNum, Position p) {
			if (colNum == 0) {
				for (int i = 0; i < this.height - 1; i++) {
					int xCoord = p.getX();
					int yCoord = p.getY() + i;
					if (checkBound(xCoord, yCoord) && checkFloor(xCoord, yCoord)) {
						world[xCoord][yCoord] = hallwall;
					}
				}
			} else if (colNum == 1) {
				for (int i = 0; i < this.height; i++) {
					int xCoord = p.getX();
					int yCoord = p.getY() + i;
					if (checkBound(xCoord, yCoord)) {
						world[xCoord][yCoord] = hallfloor;
					}
				}
			} else {
				for (int i = 0; i < this.height; i++) {
					int xCoord = p.getX();
					int yCoord = p.getY() + i;
					if (checkBound(xCoord, yCoord) && checkFloor(xCoord, yCoord)) {
						world[xCoord][yCoord] = hallwall;
					}
				}
			}
		}
	}
	
	private boolean checkBound(int xCoord, int yCoord) {
		if (xCoord < 0 ||
			xCoord >= world[0].length ||	
			yCoord >= world.length ||
			yCoord < 0){
			return false;
		}
		return true;
	}
	
	private boolean checkFloor(int x, int y) {
		if (world[x][y] != hallfloor) {
			return true;
		}
		return false;
	}

	
	private class HorizontalTurnHall{
		private int height;
		private int width;
		private String dir;
		private Position yPos;
		private Position connect;
		
		public HorizontalTurnHall(Position yGate, String dirY, Position connect) {
			this.yPos = yGate;
			this.height = 3;
			this.connect = connect;
			this.width = Math.abs(connect.getX() - yPos.getX()) + 1;
			this.dir = dirY;
		}
		
		public void buildHorizontalHall() {
			if (this.dir == "ul") {
				for (int row = 2; row >= 0; row--) {
					Position rowStart = new Position(yPos.getX(), yPos.getY() - 1 + row);
					addHorizontalRow(row, rowStart);
				}
			} else if (this.dir == "ur") {
				for (int row = 2; row >= 0; row--) {
					Position rowStart = new Position(connect.getX(), yPos.getY() - 1 + row);
					addHorizontalRow(row, rowStart);
				}
			} else if (this.dir == "dl") {
				for (int row = 0; row <= 2; row++) {
					Position rowStart = new Position(yPos.getX(), yPos.getY() + 1 - row);
					addHorizontalRow(row, rowStart);
				}
			} else {
				for (int row = 0; row <= 2; row++) {
					Position rowStart = new Position(connect.getX() + 1 - row, yPos.getY() + 1 - row);
					addHorizontalRow(row, rowStart);
				}
			}
		}
			
		/** 
		 * 
		 * @param rowNum : short:0 ; middle:1 ; long:2 ;
		 * @param p
		 */
		private void addHorizontalRow(int rowNum, Position p) {
			if (rowNum == 0) {
				for (int i = 0; i < this.width - 1; i++) {
					int xCoord = p.getX() + i;
					int yCoord = p.getY();
					if (checkBound(xCoord, yCoord) && checkFloor(xCoord, yCoord)) {
						world[xCoord][yCoord] = hallwall;
					}
				}
			} else if (rowNum == 1) {
				for (int i = 0; i < this.width; i++) {
					int xCoord = p.getX() + i;
					int yCoord = p.getY();
					if (checkBound(xCoord, yCoord)) {
						world[xCoord][yCoord] = hallfloor;
					}
				}
			} else {
				for (int i = 0; i < this.width; i++) {
					int xCoord = p.getX() + i;
					int yCoord = p.getY();
					if (checkBound(xCoord, yCoord) && checkFloor(xCoord, yCoord)) {
						world[xCoord][yCoord] = hallwall;
					}
				}
			}
		}
	}
	



}
