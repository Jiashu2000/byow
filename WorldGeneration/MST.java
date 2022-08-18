package byow.WorldGeneration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import byow.TileEngine.TETile;

public class MST implements Serializable {
	private ArrayList<Position> centerList;
	private Map<Position, Position> parent;
	private Map<Position, Double> distTo;
	private Map<Position, Boolean> marked;
	private DoubleMapPQ<Position> fringe;
	private TETile[][] world;
	private Position startCenter;
	private Map<Position, Map<Position, Double>> edges;
	
	public MST(TETile[][] world, ArrayList<Position> centerList) {
		 System.out.println("---------------");
		 System.out.println("MST CALLED");
		
		this.world = world;
		this.startCenter = centerList.get(0);
		this.centerList = centerList;
		
		this.parent = new HashMap<Position, Position>();
		this.distTo = new HashMap<Position, Double>();
		this.marked = new HashMap<Position, Boolean>();
		this.fringe = new DoubleMapPQ<Position>();
		this.edges =  buildEdges();
		
		for (Position i : centerList) {
			distTo.put(i, 1.0 *(world.length * world[0].length));
			fringe.add(i, 1.0 *(world.length * world[0].length));
			marked.put(i, false);
			parent.put(i, null);
		}
		distTo.replace(this.startCenter, 0.0);
		fringe.changePriority(startCenter, 0.0);
		/**
		parent.put(startCenter, startCenter);
		**/
	}
	
	private Map<Position, Map<Position, Double>> buildEdges() {
		Map<Position, Map<Position, Double>> edges = new HashMap<Position, Map<Position, Double>>();
		for (Position p : centerList) {
			edges.put(p,  edgeList(p));
		}
		return edges;
	}
	
	private Map<Position, Double> edgeList(Position p){
		Map<Position, Double> edgeList = new HashMap<Position, Double>();
		for (Position i : centerList) {
			if (i != p) {
				edgeList.put(i, calDist(i, p));
			}
		}
		return edgeList;
	}
	
	private double calDist(Position r, Position w) {
		return Math.sqrt((Math.pow(r.getX() - w.getX(),2) +
		Math.pow(r.getY() - w.getY(),2)));
	}
	
	
	public Map<Position, Position> prim() {
		System.out.println("prim() called");
		while (this.fringe.size() > 0) {
			Position smallest = fringe.removeSmallest();
			System.out.println("smallest x: " + smallest.getX());
        	System.out.println("smallest y: " + smallest.getY());
			Map<Position, Double> edgesFromSmall = edges.get(smallest);
			for (Position p: edgesFromSmall.keySet()) {
				System.out.println("orig dist " + distTo.get(p));
				if (!marked.get(p)) {
					double potentialDist = edgesFromSmall.get(p);
					System.out.println("potential dist " + potentialDist);
					if (distTo.get(p) > potentialDist) {
						distTo.replace(p, potentialDist);
						fringe.changePriority(p, potentialDist);
						parent.replace(p, smallest);
						System.out.println("x: " + p.getX());
			        	System.out.println("y: " + p.getY());
						System.out.println("parent x: " + smallest.getX());
			        	System.out.println("parent y: " + smallest.getY());
			        	System.out.println("-----------");
					}
				}		
			}
			marked.put(smallest, true);
		}
		
		return parent;
	}
	

}
