package byow.WorldGeneration;

import java.io.Serializable;
import java.util.Random;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class RoomConnectMST implements Serializable {
	private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    
	private Room x;
	private Room y;
	private Position xStart;
	private Position yStart;
	private Position xEnd;
	private Position yEnd;
	private Overlap range;
	private Position startGate;
	private Position endGate;
	private String direction;
	
	public RoomConnectMST(Room x,Room y) {
		this.x = x;
		this.y = y;
		this.xStart = x.getStart();
		this.xEnd = x.getEnd();
		this.yStart = y.getStart();
		this.yEnd = y.getEnd();
		this.range = checkLR();
	}
	
	private Overlap checkLR() {
		if ((xEnd.getY() > yEnd.getY()) && (xStart.getY() > yStart.getY()) && (xStart.getY() < yEnd.getY())) {
			return new Overlap("vertical", x.getStart().getY(), y.getEnd().getY());
		} else if ((yEnd.getY() > xEnd.getY()) && (yStart.getY() > xEnd.getY()) && (yStart.getY() < xEnd.getY())) {
			return new Overlap("vertical", y.getStart().getY(), x.getEnd().getY());
		} else if ((xEnd.getY() >= yEnd.getY()) && (yStart.getY() >= xStart.getY())) {
			return new Overlap("vertical", y.getStart().getY(), y.getEnd().getY());	
		} else if ((xEnd.getY() <= yEnd.getY()) && (yStart.getY() <= xStart.getY())) {
			return new Overlap("vertical", x.getStart().getY(), x.getEnd().getY());	
		} else {
			return checkUL();
		}
	}
	
	private Overlap checkUL() {
		if ((xEnd.getX() < yEnd.getX()) && (xStart.getX() < yStart.getX()) && (xEnd.getX() > yStart.getX())) {
			return new Overlap("horizontal", y.getStart().getX(), x.getEnd().getX());
		} else if ((xStart.getX() > yStart.getX()) && (yEnd.getX() < xEnd.getX()) && (yEnd.getX() >xStart.getX())) {
			return new Overlap("horizontal", x.getStart().getX(), y.getEnd().getX());
		} else if ((xEnd.getX() <= yEnd.getX()) && (yStart.getX() <= xStart.getX())) {
			return new Overlap("horizontal", x.getStart().getX(), x.getEnd().getX());	
		} else if ((xEnd.getX() >= yEnd.getX()) && (yStart.getX() >= xStart.getX())) {
			return new Overlap("horizontal", y.getStart().getX(), y.getEnd().getX());	
		} else {
			return null;
		}
	}
	
	public void buildHall(TETile[][] world) {
		if (this.range != null && this.range.getLen() >= 3) {
			getGate();
			if (range.dir == "vertical") {
				this.direction = "horizontal";
			} else {
				this.direction = "vertical";
			}
			DirectHalls hall = new DirectHalls(world, startGate, endGate, this.direction);
			hall.addHall();
		} else {
			TurnHall turn = new TurnHall(world, x, y);
			turn.buildTurn();	
		} 
	}
		
	public Overlap getRange() {
		return this.range;
	}
	
	private void getGate() {
		if (this.range.dir == "horizontal") {
			if (x.getCenter().getY() > y.getCenter().getY()) {
				this.startGate = new Position(range.getGate(),y.getEnd().getY());
				this.endGate = new Position(range.getGate(),x.getStart().getY());
			} else {
				this.startGate = new Position(range.getGate(),x.getEnd().getY());
				this.endGate = new Position(range.getGate(),y.getStart().getY());
			}	
		} else {
			if (x.getCenter().getX() > y.getCenter().getX()) {
				this.startGate = new Position(y.getEnd().getX(), range.getGate());
				this.endGate = new Position(x.getStart().getX(), range.getGate());
			} else {
				this.startGate = new Position(x.getEnd().getX(), range.getGate());
				this.endGate = new Position(y.getStart().getX(), range.getGate());
			}
		}
	}
	
	protected class Overlap implements Serializable {
		private String dir;
		private int lowerBound;
		private int upperBound;
		private int length;
		
		private Overlap(String dir, int l, int u) {
			this.dir = dir;
			this.lowerBound = l;
			this.upperBound = u;
			this.length = Math.abs(l-u) + 1;
		}
		
		public int getGate() {
			if (this.lowerBound+1 >= this.upperBound-1) {
				return this.lowerBound+1;
			}
			return byow.Core.RandomUtils.uniform(RANDOM,this.lowerBound+1, this.upperBound);
		}
		
		public int getLen() {
			return this.length;
		}
		
	}

	
}
