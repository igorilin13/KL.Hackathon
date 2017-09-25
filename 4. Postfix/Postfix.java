import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Postfix {
    private static String INPUT_FILE_NAME = "task.txt";
    private static String OUTPUT_FILE_NAME = "key";

    private static String OPERATION_SYMBOLS = "#!>=";

    public static void main(String[] args) throws IOException {
		Scanner inputScanner = new Scanner(new FileReader(INPUT_FILE_NAME));
		String line;
		long result;

		Deque<Long> st = new ArrayDeque<>();

		while (inputScanner.hasNext()) {
			line = inputScanner.nextLine();
			if (isOperation(line)) {
				long arg2 = st.pop();
				long arg1 = st.pop();
				switch(line) {
					case "#":
						result = nand(arg1, arg2);
						st.push(result);
						break;
					case "!":
						result = nor(arg1, arg2);
						st.push(result);
						break;
					case ">":
						result = impl(arg1, arg2);
						st.push(result);
						break;
					case "=":
						result = eq(arg1, arg2);
						st.push(result);
						break;
					default:
						throw new IllegalStateException("Unknown operation symbol");
				}
			} else {
				long number = Long.parseLong(line, 7);
				st.push(number);
			}
		}

		result = st.pop();
		FileOutputStream outputFile = new FileOutputStream(OUTPUT_FILE_NAME);
		String key = Long.toString(result, 7);
		outputFile.write(key.getBytes());
		outputFile.close();
    }

	private static long nand(long l1, long l2) {
		return ~(l1 & l2);
	}

	private static long nor(long l1, long l2) {
		return ~(l1 | l2);
	}

	private static long impl(long l1, long l2) {
		return ~l1 | l2;
	}

	private static long eq(long l1, long l2) {
		return (l1 & l2) | (~l1 & ~l2);
	}

	private static boolean isOperation(String s) {
		for (int i = 0; i < OPERATION_SYMBOLS.length(); i++) {
			if (s.charAt(0) == OPERATION_SYMBOLS.charAt(i)) {
				return true;
			}
		}
		return false;
	}
}
