package com.shylux.java.maze;

import java.io.IOException;
import java.nio.file.Paths;

public class Launcher {

	public static void main(String[] args) {
		boolean fxGui = true;
		
		if (fxGui) {
			MazeGui.launch();
		} else {
			try {
				MazeGeneratorDeepFirstRecursive generator = new MazeGeneratorDeepFirstRecursive();
				Maze2D maze = generator.generate(8,8);
				new MCPlotter(maze);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
