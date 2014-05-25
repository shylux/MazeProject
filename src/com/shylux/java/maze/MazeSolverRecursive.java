package com.shylux.java.maze;

import java.util.ArrayList;
import java.util.List;

public class MazeSolverRecursive implements IMazeSolver {

	public String toString() {
		return "Recursive Search";
	}
	
	@Override
	public List<Node> solve(Maze2D maze, Node startNode) {
		if (!startNode.isPassable()) return null;
		try {
			return checkNode(new ArrayList<Node>(), startNode);
		} catch (StackOverflowError e) {
			return null;
		}
	}
	
	public List<Node> checkNode(List<Node> path, Node node) {
		if (node.getTile() instanceof OutsideTile) {
			return path;
		}
		
		List<Node> exPath = new ArrayList<Node>(path);
		exPath.add(node);
		
		for (Node n: node.getNeighbours()) {
			if (exPath.contains(n)) continue;
			if (!n.isPassable()) continue;
	
			List<Node> npath = checkNode(exPath, n);
			if (npath != null) return npath;
		}

		return null;
	}

}
