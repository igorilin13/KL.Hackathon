import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Base7 {
    private static String INPUT_FILE_NAME = "task.txt";
    private static String OUTPUT_FILE_NAME = "key";

    public static void main(String[] args) throws IOException {
        Scanner inputScanner = new Scanner(new FileReader(INPUT_FILE_NAME));
        String line;
        long sum = 0;

        while (inputScanner.hasNext()) {
            line = inputScanner.nextLine();
            sum += Long.parseLong(line, 7);
        }
        String result = Long.toString(sum, 7);
        inputScanner.close();

        FileOutputStream outputFile = new FileOutputStream(OUTPUT_FILE_NAME);
        outputFile.write(result.getBytes());
        outputFile.close();
    }
}
