package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGeneration.RoomMapMST;

public class Engine {
    TERenderer ter = new TERenderer();
    
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;


    public static void interactWithKeyboard() {
    	keyboardInput.main(null);
    }
    
 
    public static TETile[][] interactWithInputString(String input) {
    	InputSource inputSource = new StringInputDevice(input);
    	if (Character.toUpperCase(inputSource.getNextKey()) == 'n') {
    		String seeds = "";
    		while (inputSource.possibleNextInput()) {
    	            char c = inputSource.getNextKey();
    	            if (Character.toUpperCase(c) == 's') {
    	                break;
    	            }
    	            seeds += c;
    	        }
    		System.out.println("string of seed" + seeds);
    		
    		int seed  = Integer.parseInt(seeds);
    		System.out.println("int of seed" + seed);

    	    TETile[][] world = new TETile[WIDTH][HEIGHT];
    	     for (int x = 0; x < WIDTH; x += 1) {
    	    	 for (int y = 0; y < HEIGHT; y += 1) {
    	    		 world[x][y] = Tileset.NOTHING;
    	    		 }
    	        }
    	     
    	     System.out.println("init room done");
    	     RoomMapMST myWorld = new RoomMapMST(world, seed);
    	      
    	     myWorld.drawRoomMap();
    	     System.out.println("draw room done");
    	     
    	     TERenderer ter = new TERenderer();
    	     ter.initialize(50, 50);
    	     ter.renderFrame(world);

    	     return world;
    	}
    	
        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }
    
    public static void main(String[] args) {
    	interactWithKeyboard();
    	
    }
}
