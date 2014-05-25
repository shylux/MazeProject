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
				Maze2D model = new Maze2D(Paths.get("./mazeData2.csv"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
