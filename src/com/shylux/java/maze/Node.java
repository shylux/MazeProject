package com.shylux.java.maze;

import java.util.List;

public class Node implements ITile {
	Maze2D ctx;
	
	int x;
	int y;
	
	public Node(Maze2D context, int x, int y) {
		this.ctx = context;
		this.x = x;
		this.y = y;
	}
	
	public ITile getTile() {
		return ctx.getTile(x, y);
	}
	
	public boolean isPassable() {
		return getTile().isPassable();
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node other = (Node) obj;
			return (x == other.x && y == other.y);
		}
		return false;
	}
	
	public List<Node> getNeighbours() {
		return ctx.getNeighbours(this);
	}

}
