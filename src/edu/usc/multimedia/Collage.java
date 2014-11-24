package edu.usc.multimedia;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

class Collage {

	private final List<MyImage> images;
	private YUVFeature feature;
	private static final int THREASHOLD = 314150;
	private final YUVFeature featureSum;
	public Collage() {
		images = new ArrayList<MyImage>();
		feature = null;
		featureSum = null;
	}

	public void setFeature(YUVFeature feature) {
		this.feature = feature;
	}

	public YUVFeature getFeature() {
		return this.feature;
	}

	public int getNumberOfImages() {
		return images.size();
	}

	public void addImage(MyImage newImage) {
		images.add(newImage);
		// after addition, update the YUV feature of this collage
		feature = new YUVFeature(images.get(0).toYUVFeature());
		int n = images.size();
		for (int i = 1; i < n; i++) {
			feature.add(images.get(i).toYUVFeature());
		}
		feature.divide(n);
	}

	public List<MyImage> getImages() {
		return this.images;
	}
	/**
	 *
	 * @param image
	 * @return -1: distance between image's YUV feature and collage's YUV feature is bey`ond THREASHOLD.
	 *         otherwise: the distance between two images.
	 */
	public int evaluateImage(MyImage image) {
		int result = this.feature.compare(image.toYUVFeature());
		Util.log(result);
		if (result < THREASHOLD) {
			return result;
		}
		else return -1;
	}
}

