package com.shylux.java.maze;

import java.io.File;
import java.io.IOException;

import net.minecraft.src.Block;
import net.minecraft.src.EnumGameType;

import org.apache.commons.io.FileUtils;

import shylux.java.minecraft.JMWorldEdit;
import shylux.java.minecraft.WorldGeneratorNull;

public class MCPlotter {
	Maze2D mz;
	
	public MCPlotter(Maze2D maze) throws IOException {
		mz = maze;
		
		//String world = "/Users/lukas/Library/Application Support/minecraft/saves";
		String world = "C:/Users/Shylux/AppData/Roaming/.minecraft/saves";
		FileUtils.deleteDirectory(new File(world+"/world"));
		try (JMWorldEdit edit = new JMWorldEdit(world,
												40,
												new WorldGeneratorNull())) {
	
			//zoom
			int c = 10;
			for (int x = 0; x < mz.getWidth(); x++) {
				for (int z = 0; z < mz.getHeight(); z++) {
					
					for (int ix = 0; ix < c; ix++) {
						for (int iz = 0; iz < c; iz++) {
							System.out.println((x*c+ix)+":"+(z*c+iz));
							edit.world.setBlock(x*c+ix, 30, z*c+iz, Block.brick.blockID);
						}
					}
					
				}
			}
			
			edit.world.getWorldInfo().setSpawnPosition(mz.getWidth()/2, 35, mz.getHeight()/2);
			edit.world.getWorldInfo().setGameType(EnumGameType.CREATIVE);

		}
	}
}
