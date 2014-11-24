package edu.usc.multimedia;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 * A data structure to store the histogram of a certain image
 * @author longfengjia
 */
class YUVFeature {
	public List<Mat> featureHistList;

	public YUVFeature(){
		featureHistList = new ArrayList<Mat>();
	}

	public YUVFeature(YUVFeature other) {
		featureHistList = new ArrayList<Mat>();
		for (Mat mat : other.featureHistList) {
			featureHistList.add(mat.clone());
		}
	}

	public void addHist(Mat hist) {
		featureHistList.add(hist);
	}

	/**
	 * Calculate the distance between this feature and that feature.
	 * The distance is defined as the summation of absolute difference of each element in all mat in the feature
	 * @param other
	 * @return distance
	 */
	public int compare(YUVFeature other) {
		int result = 0;
		for (int i = 0; i < 3; i++) {
			Mat feature1 = featureHistList.get(i);
			Mat feature2 = other.featureHistList.get(i);
			for (int x = 0; x < feature1.rows(); x++)
				for (int y = 0; y < feature1.cols(); y++) {
					result += Math.abs(feature1.get(x, y)[0] - feature2.get(x, y)[0]);
				}
		}
		return result;
	}

	public void add(YUVFeature other) {
		for (int i = 0; i < featureHistList.size(); i++) {
			Mat feature1 = featureHistList.get(i);
			Mat feature2 = other.featureHistList.get(i);
			Core.add(feature1, feature2, feature1);
		}
	}

	public void divide(int n) {
		for (int i = 0; i < featureHistList.size(); i++) {
			Mat hist = featureHistList.get(i);
			for (int x = 0; x < hist.rows(); x++)
				for (int y = 0; y < hist.cols(); y++)
					hist.put(x, y, hist.get(x, y)[0] / n);
		}
	}
}
