import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Image {
    private static String INPUT_FILE_NAME = "task.bmp";
    private static String OUTPUT_FILE_NAME = "key";

    public static void main(String[] args) throws IOException {
        BufferedImage inputImage = ImageIO.read(new File(INPUT_FILE_NAME));
        byte[] result = new byte[32];
        int currResultIndex = 0;

		for (int y = 0; y < inputImage.getHeight(); y++) {
			for (int x = 0; x < inputImage.getWidth(); x++) {
				Color color = new Color(inputImage.getRGB(x, y));
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				if (red == green && green == blue) {
					result[currResultIndex++] = (byte) color.getRed();
					if (currResultIndex >= 32) {
						FileOutputStream outputFile = new FileOutputStream(OUTPUT_FILE_NAME);
						outputFile.write(result);
						outputFile.close();
						return;
					}
				}
			}
		}
    }
}
