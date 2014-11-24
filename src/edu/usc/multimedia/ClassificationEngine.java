package edu.usc.multimedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassificationEngine {

	private final String dataFolder;

	private final SuperCollage superCollage;

	private final List<MyImage> imageList = new ArrayList<MyImage>();

	// whenever the difference between image hist and any collage feature hist
	// is more than this THREASHOLD, create a new collage.
	private static final int THREASHOLD = 1000;

	public ClassificationEngine(String dataFolder) {
		this.dataFolder = dataFolder;
		this.superCollage = new SuperCollage();
	}

	/**
	 * Start to classify all the provided images
	 * and create a SuperCollage hierarchical structure.
	 * @return the created SuperCollage
	 */
	public SuperCollage start() {

		readAllImages();

		classifyImages();
		//Collage collage = new Collage();
		//collage.addImage(imageList.get(0));

		//collage.evaluateImage(imageList.get(0));
		//Util.log(imageList.get(0).toYUVFeature().compare(imageList.get(25).toYUVFeature()));
		return superCollage;
	}

	/**
	 * classify all the images in the imageList into a number of collages (image set)
	 */
	private void classifyImages() {

		List<Collage> collageList = new ArrayList<Collage>();

		for (MyImage image : imageList) {
			YUVFeature imageFeature = image.toYUVFeature();
			Collage matchedCollage = null;
			int difference = Integer.MAX_VALUE;
			for (Collage collage : collageList) {
				int result = collage.evaluateImage(image);
				if (result == -1) continue; // this image cannot be classified to this collage
				if (result < difference) {
					difference = result;
					matchedCollage = collage;
				}
			}
			if (matchedCollage == null) { // create a new collage
				Collage newCollage = new Collage();
				newCollage.addImage(image);
				collageList.add(newCollage);
			}
			else {
				matchedCollage.addImage(image);

			}
		}
		Util.log(collageList.size());
		for (int i = 0; i < collageList.size(); i++)
			Util.log(collageList.get(i).getNumberOfImages());
		for (MyImage image : collageList.get(3).getImages()) {
			image.display();
		}
		this.superCollage.setCollages(collageList);
	}



	private void readAllImages() {
		File dataDir = new File(dataFolder);
		for (File fileEntry : dataDir.listFiles()) {
			String imageName = fileEntry.getAbsolutePath();
			//log(imageName);
			MyImage image = new MyImage(imageName);
			image.read();
			imageList.add(image);
		}

		// imageList already has all the images in the folder
	}
}
