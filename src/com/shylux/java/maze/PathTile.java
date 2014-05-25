package com.shylux.java.maze;

public class PathTile implements ITile {

	@Override
	public boolean isPassable() {
		return true;
	}
	
	public String toString() {
		return ".";
	}

}
