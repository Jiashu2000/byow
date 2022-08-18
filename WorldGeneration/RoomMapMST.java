package byow.WorldGeneration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class RoomMapMST implements Serializable {
	private static final int maxH = 5;
	private static final int maxW = 10;
	private static final int WIDTH = 50;
	private static final int HEIGHT = 50;
	
	private TETile[][] world;
	private int roomNum;
	private int seed;
	private Random RANDOM;
	private Avatar avatar;
	ArrayList<Room> roomList;
	ArrayList<Position> centerList;
	Map<Position, Room> centerToRoom;
	Map<Position, Position> parent;
	ArrayList<RoomConnectMST> directHall;
	ArrayList<RoomConnectMST> turnHall;
	
	public RoomMapMST() {
		
	}
	
	public TETile[][] getWorld(){
		return this.world;
	}
			
	
	public RoomMapMST(TETile[][] world, int seed) {
		this.world = world;
		this.RANDOM = new Random(seed);
		this.roomNum = 20;
		this.roomList = new ArrayList<Room>();
		this.centerList = new ArrayList<Position>();
		this.centerToRoom = new HashMap<Position, Room>();
		this.parent = new HashMap<Position, Position>();
		this.directHall =  new ArrayList<RoomConnectMST>();
		this.turnHall =  new ArrayList<RoomConnectMST>();
		this.roomList = buildRoomList();
		this.parent = MST();
		this.seed = seed;
		int randCenter = byow.Core.RandomUtils.uniform(RANDOM, 0, centerList.size() - 1);
		this.avatar = new Avatar(world,this.centerList.get(randCenter));
	}
	
	private ArrayList<Room> buildRoomList() {
		
		while (roomList.size() < roomNum) {
			Position newp = randP(world[0].length-1, world.length-1);
			Room newRoom = new Room(world, newp, maxH, maxW);
			if (newRoom.checkOverlap(roomList)) {
				continue;
			} else if (newRoom.checkOutBound()) {
				continue;
			}
			else {
				roomList.add(newRoom);
				centerList.add(newRoom.getCenter());
				centerToRoom.put(newRoom.getCenter(), newRoom);
			}
		}
		return roomList;
	}
	
	public Position randP(int maxX, int maxY) {
		int newx = byow.Core.RandomUtils.uniform(RANDOM, maxX);
		int newy = byow.Core.RandomUtils.uniform(RANDOM, maxY);
		return new Position(newx, newy);
	}
	
	private Map<Position, Position> MST(){
		MST mstAlg = new MST(world,centerList);
		return mstAlg.prim();
	}
	
	private void drawRoom() {
		for (Room r : roomList) {
			r.addRoom();
		}
	}
	
	public void drawRoomMap() {
		roomList = buildRoomList();
		drawRoom();
		drawRoomConnect();
		drawAvatar();
	}
	
	public void drawRoomConnect() {
		for (Position p : centerList) {
			Room x = centerToRoom.get(p);
			if (parent.get(p) == null) {
				continue;
			}
			Room y = centerToRoom.get(parent.get(p));
			RoomConnectMST conn = new RoomConnectMST(x, y);
			if (conn.getRange() != null && conn.getRange().getLen() >= 3) {
				directHall.add(conn);
			} 
			else{
				turnHall.add(conn);
			}
			
		}
		for (RoomConnectMST conn : directHall) {
			conn.buildHall(world);
		}
		for (RoomConnectMST conn : turnHall) {
			conn.buildHall(world);
		}
	}
	
	public Avatar getAvatar() {
		return this.avatar;
	}
	
	public void drawAvatar() {
		world[avatar.getX()][avatar.getY()] = Tileset.TREE;
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

        int seednum = Integer.parseInt(args[0]);

        RoomMapMST rm = new RoomMapMST(randomTiles, 23442);
        
        rm.drawRoomMap();

        ter.renderFrame(randomTiles);
    }

    **/
    
}

