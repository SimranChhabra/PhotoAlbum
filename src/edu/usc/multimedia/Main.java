package edu.usc.multimedia;

import org.opencv.core.Core;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
		//	System.out.println("Error: one argument indicating the data folder is needed.");
			return;
		}
		System.out.println("data folder:" + args[0]);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new PhotoAlbum(args[0]).start();
	}

}
