package com.shylux.java.maze;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.shylux.java.maze.Maze2D.Direction;

public class MazeGeneratorDeepFirstRecursive {
	SecureRandom rand = new SecureRandom();
	Maze2D mz;
	
	public Maze2D generate(int width, int height) {
		mz = new Maze2D(width, height);
		
		mz.setAll(Maze2D.WALL_ALL);
		
		Node start = mz.getNode(rand.nextInt(width), rand.nextInt(height));
		punchHolesInWall(1);
		
		check(start);
		
		punchRandomHoles((width+height)/2);
		
		System.out.println(mz);
		
		return mz;
	}
	
	private boolean check(Node n) {
		if (!n.inMaze() || n.isVisited()) return false;

		n.setVisited(true);
		
		List<Direction> direct = Arrays.asList(Direction.values());
		Collections.shuffle(direct, rand);

		for (Direction d: direct) {
			Node m = mz.getNode(n, d);
			
			if (check(m))
				n.digWall(d);
		}
		return true;
	}
	
	private void punchHolesInWall(int numberOnEachSide) {
		int x=0, y=0;
		for (Direction d: Direction.values()) {
			for (int i = 0; i < numberOnEachSide; i++) {
				switch (d) {
				case NORTH:
					y = 0;
					x = rand.nextInt(mz.getWidth());
					break;
				case SOUTH:
					y = mz.getHeight()-1;
					x = rand.nextInt(mz.getWidth());
					break;
				case EAST:
					x = mz.getWidth()-1;
					y = rand.nextInt(mz.getHeight());
					break;
				case WEST:
					x = 0;
					y = rand.nextInt(mz.getHeight());
					break;
				}
				mz.getNode(x,y).digWall(d);				
			}
		}
	}
	
	private void punchRandomHoles(int numberOfHoles) {
		for (int i = 0; i < numberOfHoles; i++) {
			int x = rand.nextInt(mz.getWidth());
			int y = rand.nextInt(mz.getHeight());
			Direction d = Direction.values()[rand.nextInt(4)];
			mz.getNode(x,y).digWall(d);
		}
	}
}
