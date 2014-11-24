package edu.usc.multimedia;

public class PhotoAlbum {

	//private final String dataFolder;
	private final ClassificationEngine classificationEngine;
	private SuperCollage superCollage;

	public PhotoAlbum(String dataFolder) {
		classificationEngine = new ClassificationEngine(dataFolder);
	}

	public void start() {
		// acqure this superCollage;
		superCollage = classificationEngine.start();




	}
}
