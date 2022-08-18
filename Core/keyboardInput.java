package byow.Core;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import byow.SaveDemo.Editor;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGeneration.RoomMapMST;
import edu.princeton.cs.introcs.StdDraw;

public class keyboardInput {
	private static final int WIDTH = 50;
	private static final int HEIGHT = 50;
	private static WorldGeneration w;
	
	public static void main(String[] args) {
		initialize();
		while (true) {
			w.HUD();
			if (StdDraw.hasNextKeyTyped()) {
				char c = StdDraw.nextKeyTyped();
				if (c == 'q') {
					saveWorld(w);
					System.exit(0);
				} else {
					w.moveAvatar(c);
				}
			}
		}
	}
	
	public static void initialize() {
    	StdDraw.setCanvasSize(WIDTH*16, HEIGHT*16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        
        drawFrame("JIASHU'S WORLD", WIDTH*1/2, HEIGHT*2/3, 30);
        drawFrame("Please press N to start a new game", WIDTH/2, 25, 20);
        drawFrame("Please press L to load previous game", WIDTH/2, 20, 20);
        drawFrame("Please press Q to end the game", WIDTH/2, 15, 20);
        StdDraw.show();
        StdDraw.pause(2500);

        boolean validInput = false;
        while (!validInput) {
        	char key = StdDraw.nextKeyTyped();
        	if (key == 'n') {
        		System.out.println("new game");
        		validInput = true;
        		w = newGame();
        	} else if (key == 'l') {
        		w = loadWorld();
        		System.out.println("load success");
        		validInput = true;
        	} else if (key == 'q') {
        		validInput = true;
        		System.exit(0);
        	} else {
        		System.out.println("Wrong Input! Enter Again");
        	}
        }
	}
	
	private static WorldGeneration newGame() {
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        drawFrame("Please input a positive integer as the seed.", WIDTH/2, 25, 20);
        drawFrame("Press s when you finish", WIDTH/2, 20, 20);
        StdDraw.show();
        int seed =  solicitNCharsInput();
        WorldGeneration w = new WorldGeneration(seed);
        return w;
	}
	
	private static void drawFrame(String s, int widthPos, int heightPos, int fontSize) {
        Font bigFont = new Font("Monaco", Font.BOLD, fontSize);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(widthPos, heightPos, s);
	}

	
	private static int solicitNCharsInput() {
    	StdDraw.pause(2500);
        String input = "";
        while (StdDraw.hasNextKeyTyped()) {
            char c = StdDraw.nextKeyTyped();
            if (c == 's') {
            	break;
            }
            input += String.valueOf(c);
        }
        return Integer.parseInt(input);
    }
	
	private static WorldGeneration loadWorld() {
		File f = new File("./saveData");
		if (f.exists()) {
			try {
				System.out.println("file found");
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (WorldGeneration) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
		}
		return new WorldGeneration();
	}
	
	private static void saveWorld(WorldGeneration w) {
		File f = new File("./saveData");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
	}
	
   
	
	
}
