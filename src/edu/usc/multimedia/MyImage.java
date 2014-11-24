package edu.usc.multimedia;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

class MyImage {

	private BufferedImage data;
	private String imageName; // with path
	private static final int WIDTH  = 352;
	private static final int HEIGHT = 288;
	private YUVFeature feature;

	public MyImage(String imageName) {
		this.imageName = imageName;
	}

	public MyImage() {
		this.imageName = null;
		this.feature = null;
	}

	/**
	 * read the image data from the file specified by filename, imageName
	 * into MyImage object, data field.
	 * @throws IOException
	 * @author longfengjia
	 */
	public void read() {
		try {
			if (this.imageName == null) {
				throw new IOException("The MyImage is not associated with any image files.");
			}
		File file = new File(this.imageName);
		InputStream is = new FileInputStream(file);
		long len = file.length();
		byte[] imageData = new byte[(int) len];
		int offset = 0;
		int numRead = 0;

		while(offset < len && (numRead = is.read(imageData, offset, imageData.length - offset)) >= 0) {
			offset += numRead;
		}
		is.close();

		data = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		int index = 0;

		for (int y = 0; y < HEIGHT; y++)
			for (int x = 0; x < WIDTH; x++) {
				int r = 0x000000ff & imageData[index];
				int g = 0x000000ff & imageData[index + WIDTH * HEIGHT];
				int b = 0x000000ff & imageData[index + 2 * WIDTH * HEIGHT];
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | ((b & 0xff));
				data.setRGB(x, y, pix);
				index++;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void display() {
			JFrame frame = new JFrame();
			JLabel label = new JLabel(new ImageIcon(data));
			frame.getContentPane().removeAll();
			frame.getContentPane().add(label, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
	}

	public String getImageName() {
		return this.imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public YUVFeature toYUVFeature() {
		if (this.feature != null) return this.feature;
		YUVFeature feature = new YUVFeature();
		Mat imageMat = new Mat(data.getHeight(), data.getWidth(), CvType.CV_8UC3);
		byte[] pixels = ((DataBufferByte)data.getRaster().getDataBuffer()).getData();
		imageMat.put(0, 0, pixels);

		Mat YUVImageMat = new Mat(data.getHeight(), data.getWidth(), CvType.CV_8UC3);
		Imgproc.cvtColor(imageMat, YUVImageMat, Imgproc.COLOR_BGR2YUV);
		// YUV channels
		List<Mat> channels = new ArrayList<Mat>();
		Core.split(YUVImageMat, channels);
		//Util.log(channels.size());
		MatOfInt   histSize = new MatOfInt(256);
		MatOfFloat histRange = new MatOfFloat(0, 256);
		for (Mat channel : channels) {
			Mat hist = new Mat();
			List<Mat> list = new ArrayList<Mat>();
			list.add(channel);
			Imgproc.calcHist(list, new MatOfInt(0), new Mat(), hist, histSize, histRange);
			feature.addHist(hist);
		}
		this.feature = feature;
		return feature;
	}

	public BufferedImage getImageData() {
		return data;
	}
}
