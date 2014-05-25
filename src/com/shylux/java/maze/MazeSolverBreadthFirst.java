package com.shylux.java.maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shylux.java.maze.Maze2D.Direction;

public class MazeSolverBreadthFirst implements IMazeSolver {
	Direction[][] solution;
	int[][] cost;
	
	public String toString() {
		return "Breadth-First Search";
	}

	@Override
	public List<Node> solve(Maze2D maze, Node startNode) {
		if (startNode.getTile() instanceof OutsideTile) return null;
		
		solution  = new Direction[maze.getWidth()][maze.getHeight()];
		
		cost = new int[maze.getWidth()][maze.getHeight()];
		for (int[] c: cost)
			Arrays.fill(c, Integer.MAX_VALUE);
			
		List<Node> queue = new ArrayList<Node>(maze.getExitNodes());
			
		for (Node e: queue)
			cost[e.x][e.y] = 1;
		
		while (!queue.isEmpty()) {
			Node n = queue.remove(0);
			int curCost = cost[n.x][n.y] + 1;
			for (Direction d: Direction.values()) {
				Node e = maze.getNode(n.x+d.x, n.y+d.y);
				if (!e.isPassable()) continue;
				if (e.getTile() instanceof OutsideTile) continue;
				if (curCost < cost[e.x][e.y]) {
					cost[e.x][e.y] = curCost;
					solution[e.x][e.y] = d.opposite();
					queue.add(e);
				}
			}
		}
		
		// build path
		List<Node> path = new ArrayList<Node>();
		if (solution[startNode.x][startNode.y] == null) return null;
		Node curNode = startNode;
		while (true) {
			path.add(curNode);
			Direction d = solution[curNode.x][curNode.y];
			if (d == null) break;
			curNode = maze.getNode(curNode.x+d.x, curNode.y+d.y);
		}
		return path;
	}
}
