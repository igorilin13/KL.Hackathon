import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Base13 {
	private static String INPUT_FILE_NAME = "task.txt";
	private static String OUTPUT_FILE_NAME = "key";

	public static void main(String[] args) throws IOException {
		Scanner inputScanner = new Scanner(new FileReader(INPUT_FILE_NAME));
		String line;
		long sum = 0;

		while (inputScanner.hasNext()) {
			line = inputScanner.nextLine();
			if (line.startsWith("0t")) {
				sum += Long.parseLong(line.substring(2), 13);
			} else {
				sum += Long.parseLong(line, 7);
			}
		}
		inputScanner.close();
		String result = "0t" + Long.toString(sum, 13).toUpperCase();

		FileOutputStream outputFile = new FileOutputStream(OUTPUT_FILE_NAME);
		outputFile.write(result.getBytes());
		outputFile.close();
	}
}
