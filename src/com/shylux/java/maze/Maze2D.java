package com.shylux.java.maze;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * One maze coordinate contains the following information:
 * 1: Has a wall on the north side.
 * 2: Has a wall on the east side.
 * 4: Has a wall on the south side.
 * 8: Has a wall on the west side.
 * 16: Is passable when set to 1.
 * 32: Is visited when set to 1.
 * @author Shylux
 *
 */
public class Maze2D {
	public static final int WALL_NORTH = 1,
			   				WALL_EAST = 2,
							WALL_SOUTH = 4,
							WALL_WEST = 8,
							IS_PASSABLE = 16,
							VISITED = 32,
			   
							WALL_ALL = WALL_WEST + WALL_SOUTH + WALL_EAST + WALL_NORTH;
		
	
	private int[][] data;
	private int width;
	private int height;
	
	public enum Direction {
		NORTH (0, -1, WALL_NORTH),
		EAST (1, 0, WALL_EAST),
		SOUTH (0, 1, WALL_SOUTH),
		WEST (-1, 0, WALL_WEST);
		
		public final int x;
		public final int y;
		public final int bitmask;
		Direction(int dx, int dy, int dbitmask) {
			x = dx;
			y = dy;
			bitmask = dbitmask;
		}
		public Direction opposite() {
			switch (this) {
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
			}
			return null;
		}
	}
	
	public Maze2D(Path mazeDataFile) throws IOException {
		readCSVData(mazeDataFile);
		System.out.println(this);
	}
	public Maze2D(int width, int height) {
		this.width = width;
		this.height = height;
		data = new int[width][height];
	}
	
	public void setAll(int mask) {
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++)
				data[x][y] |= mask;
		}
	}
	public void clearAll(int mask) {
		for (int x = 1; x < this.width; x++) {
			for (int y = 1; y < this.height; y++)
				data[x][y] &= ~mask;
		}
	}
	public int checkNode(Node n, int mask) {
		return data[n.x][n.y] & mask;
	}
	
	public void readCSVData(Path csvFile) throws IOException {
		List<String> csvData = Files.readAllLines(csvFile, Charset.defaultCharset());

		String[] dimensions = csvData.get(0).split("x");
		this.height = Integer.parseInt(dimensions[1]);
		this.width = Integer.parseInt(dimensions[0]);
		this.data = new int[this.width][this.height];
		
		for (int y = 1; y < this.height+1; y++) {
			String csvLine = csvData.get(y);

			String[] csvCells = csvLine.split(",");
			
			for (int x = 0; x < this.width; x++) {
				String csvCell = csvCells[x];
				Node n = getNode(x, y-1);
				if (csvCell.equals(" ")) {
				} else if (csvCell.equals("*")) {
					data[n.x][n.y] |= WALL_ALL;
				} else {
					throw new IOException("Unexcpected symbol while parsing: "+csvCell);
				}
			}
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++)
				sb.append(getNode(x,y).getTile());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public ITile getTile(Node n) {
		if (out(n)) return new OutsideTile();
		if (n.check(WALL_ALL) == WALL_ALL)
			return new WallTile();
		else
			return new PathTile();
	}
	
	public boolean out(Node n) {
		return (n.x < 0 || n.x >= width || n.y < 0 || n.y >= height);
	}
	
	public Node getNode(int x, int y) {
		return new Node(this, x, y);
	}
	public Node getNode(Node n, Direction d) {
		return getNode(n.x+d.x, n.y+d.y);
	}

	// special nodes
	public List<Node> getNeighbours(Node n) {
		List<Node> list = new ArrayList<Node>();
		for (Direction d: Direction.values())
			list.add(getNode(n.x+d.x, n.y+d.y));
		return list;
	}
	public Set<Node> getExitNodes() {
		Set<Node> enodes = new HashSet<Node>();
		for (int x = 0; x < getWidth(); x++) {
			//north
			if (getNode(x, -1).isPassable(Direction.SOUTH))
				enodes.add(getNode(x, 0));
			//south
			if (getNode(x, getHeight()).isPassable(Direction.NORTH))
				enodes.add(getNode(x, getHeight()-1));
		}
		for (int y = 0; y < getHeight(); y++) {
			//west
			if (getNode(-1, y).isPassable(Direction.EAST))
				enodes.add(getNode(0, y));
			//east
			if (getNode(getWidth(), y).isPassable(Direction.WEST))
				enodes.add(getNode(getWidth()-1, y));
		}
		return enodes;
	}
	public boolean isExitNode(Node n) {
		for (Direction d: Direction.values()) {
			if (out(getNode(n, d)) && n.isPassable(d)) return true;
		}
		return false;
	}

	// Passable
	public boolean isPassable(Node n, Direction d) {
		if (!out(n))
			if ((data[n.x][n.y] & d.bitmask) > 0) return false;
		Node m = getNode(n, d);
		if (!out(m))
			return ((data[m.x][m.y] & d.opposite().bitmask) == 0);
		return true;
	}
	
	// Visited
	public void setVisited(Node n, boolean visited) {
		if (out(n)) return;
		if (visited)
			data[n.x][n.y] |= VISITED;
		else
			data[n.x][n.y] &= ~VISITED;
	}
	public boolean isVisited(Node n) {
		if (out(n)) return false;
		return ((data[n.x][n.y] & VISITED) > 0);
	}
	
	// Wall
	public void setWall(Node n, Direction d, boolean on) {
		if (out(n)) return;
		if (on) {
			data[n.x][n.y] |= d.bitmask;
		} else {
			data[n.x][n.y] &= ~d.bitmask;
		}
	}
	public void digWall(Node n, Direction d) {
		if (!out(n))
			data[n.x][n.y] &= ~d.bitmask;
		Node n2 = getNode(n, d);
		if (!out(n2))
			data[n2.x][n2.y] &= ~d.opposite().bitmask;
	}
	public boolean hasWall(Node n, Direction d) {
		if (out(n)) return false;
		return ((data[n.x][n.y] & d.bitmask) > 0);
	}

}
