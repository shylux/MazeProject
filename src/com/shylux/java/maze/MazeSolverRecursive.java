package com.shylux.java.maze;

import java.util.ArrayList;
import java.util.List;

import com.shylux.java.maze.Maze2D.Direction;

public class MazeSolverRecursive implements IMazeSolver {
	
	Maze2D mz;

	public String toString() {
		return "Recursive Search";
	}
	
	@Override
	public List<Node> solve(Maze2D maze, Node startNode) {
		mz = maze;
		if (!startNode.isPassable()) return null;
		try {
			return checkNode(new ArrayList<Node>(), startNode);
		} catch (StackOverflowError e) {
			return null;
		}
	}
	
	public List<Node> checkNode(List<Node> path, Node node) {
		List<Node> exPath = new ArrayList<Node>(path);
		exPath.add(node);
		
		if (node.isExitNode()) {
			return exPath;
		}
		
		for (Direction d: Direction.values()) {
			Node n = mz.getNode(node, d);
			if (exPath.contains(n)) continue;
			if (!node.isPassable(d)) continue;
	
			List<Node> npath = checkNode(exPath, n);
			if (npath != null) return npath;
		}

		return null;
	}

}
