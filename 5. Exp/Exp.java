import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Exp {
	private static String INPUT_FILE_NAME = "task.txt";
	private static String OUTPUT_FILE_NAME = "key";

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(new FileReader(INPUT_FILE_NAME));
		String line = in.nextLine().replaceAll("\\s+","");
		in.close();

		long result = eval(parseLine(line));

		FileOutputStream outputFile = new FileOutputStream(OUTPUT_FILE_NAME);
		String key = "0t" + Long.toString(result, 13).toUpperCase();
		outputFile.write(key.getBytes());
		outputFile.close();
	}

	private static Deque<Object> parseLine(String line) {
		Deque<Object> result = new ArrayDeque<>();
		boolean isReadingNumber = false;
		int numberStartIndex = -1;
		
		for (int i = 0; i < line.length(); i++) {
			if (Character.isLetterOrDigit(line.charAt(i))) {
				if (!isReadingNumber) {
					isReadingNumber = true;
					numberStartIndex = i;
				}
			} else {
				if (isReadingNumber) {
					addNumberFromString(line, numberStartIndex, i, result);
					result.add(line.charAt(i));
					isReadingNumber = false;
				} else {
					result.add(line.charAt(i));
				}
			}
		}
		addNumberFromString(line, numberStartIndex, line.length(), result);
		return result;
	}
	
	private static void addNumberFromString(String line, int startIndex, int endIndex, Deque<Object> to) {
		String numberString = line.substring(startIndex, endIndex);
		long number;
		if (numberString.contains("0t")) {
			number = Long.parseLong(numberString.substring(2), 13);
		} else {
			number = Long.parseLong(numberString, 7);
		}
		to.add(number);
	} 

	private static long eval(Deque<Object> expression) {
		Object next;
		long arg1, arg2;
		char symbol;

		arg1 = (long) expression.remove();

		while (!expression.isEmpty()) {
			symbol = (char) expression.remove();
			if (symbol == ')') {
				return arg1;
			}
			next = expression.remove();
			if (next instanceof Character) {
				arg2 = eval(expression);
			} else {
				arg2 = (long) next;
			}
			switch(symbol) {
				case '#':
					arg1 = nand(arg1, arg2);
					break;
				case '!':
					arg1 = nor(arg1, arg2);
					break;
				case '>':
					arg1 = impl(arg1, arg2);
					break;
				case '=':
					arg1 = eq(arg1, arg2);
					break;
			}
		}

		return arg1;
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
}
