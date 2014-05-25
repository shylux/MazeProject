package com.shylux.java.maze;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Maze2D {
	private ITile[][] data;
	private int width;
	private int height;
	
	public enum Direction {
		NORTH (0, -1),
		EAST (1, 0),
		SOUTH (0, 1),
		WEST (-1, 0);
		
		public final int x;
		public final int y;
		Direction(int dx, int dy) {
			x = dx;
			y = dy;
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
	
	public void readCSVData(Path csvFile) throws IOException {
		List<String> csvData = Files.readAllLines(csvFile, Charset.defaultCharset());

		String[] dimensions = csvData.get(0).split("x");
		this.height = Integer.parseInt(dimensions[1]);
		this.width = Integer.parseInt(dimensions[0]);
		this.data = new ITile[this.width][this.height];
		
		for (int y = 1; y < this.height+1; y++) {
			String csvLine = csvData.get(y);

			String[] csvCells = csvLine.split(",");
			
			for (int x = 0; x < this.width; x++) {
				String csvCell = csvCells[x];

				if (csvCell.equals(" ")) {
					this.data[x][y-1] = new PathTile();
				} else if (csvCell.equals("*")) {
					this.data[x][y-1] = new WallTile();
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
				sb.append(this.data[x][y]);
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
	public ITile getTile(int x, int y) {
		try {
			return this.data[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return new OutsideTile();
		}
	}
	
	public Node getNode(int x, int y) {
		return new Node(this, x, y);
	}
	
	public List<Node> getNeighbours(int x, int y) {
		List<Node> list = new ArrayList<Node>();
		for (Direction d: Direction.values())
			list.add(getNode(x+d.x, y+d.y));
		return list;
	}
	public List<Node> getNeighbours(Node node) {
		return getNeighbours(node.x, node.y);
	}
	public Set<Node> getExitNodes() {
		Set<Node> enodes = new HashSet<Node>();
		for (int x = 0; x < getWidth(); x++) {
			if (getNode(x, 0).isPassable())
				enodes.add(getNode(x, 0));
			if (getNode(x, getHeight()-1).isPassable())
				enodes.add(getNode(x, getHeight()-1));
		}
		for (int y = 0; y < getHeight(); y++) {
			if (getNode(0, y).isPassable())
				enodes.add(getNode(0, y));
			if (getNode(getWidth()-1, y).isPassable())
				enodes.add(getNode(getWidth()-1, y));
		}
		return enodes;
	}
}
