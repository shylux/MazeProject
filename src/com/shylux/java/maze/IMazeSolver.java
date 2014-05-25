package com.shylux.java.maze;

import java.util.List;

public interface IMazeSolver {
	public List<Node> solve(Maze2D maze, Node startNode);
}
