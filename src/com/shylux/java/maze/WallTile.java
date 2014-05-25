package com.shylux.java.maze;

public class WallTile implements ITile {

	@Override
	public boolean isPassable() {
		return false;
	}
	
	public String toString() {
		return "#";
	}

}
