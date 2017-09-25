import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Steganography {
	private static String FIRST_INPUT_FILE_NAME = "a.bmp";
	private static String SECOND_INPUT_FILE_NAME = "b.bmp";
	private static String OUTPUT_FILE_NAME = "key";

	public static void main(String[] args) throws IOException {
		BufferedImage firstImage = ImageIO.read(new File(FIRST_INPUT_FILE_NAME));
		BufferedImage secondImage = ImageIO.read(new File(SECOND_INPUT_FILE_NAME));
		int height = firstImage.getHeight();
		int width = firstImage.getWidth();
		BufferedImage resultImage = new BufferedImage(width, height, TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color firstColor = new Color(firstImage.getRGB(x, y));
				Color secondColor = new Color(secondImage.getRGB(x, y));
				int resultRed = (firstColor.getRed() % 2 + secondColor.getRed() % 2) * 127;
				int resultGreen = (firstColor.getGreen() % 2 + secondColor.getGreen() % 2) * 127;
				int resultBlue = (firstColor.getBlue() % 2 + secondColor.getBlue() % 2) * 127;
				Color resultColor = new Color(resultRed, resultGreen, resultBlue);
				resultImage.setRGB(x, y, resultColor.getRGB());
			}
		}

		File outputFile = new File(OUTPUT_FILE_NAME);
		ImageIO.write(resultImage, "bmp", outputFile);
	}
}
