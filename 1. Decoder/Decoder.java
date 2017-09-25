import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Decoder {
    private static String ENCRYPTED_FILE_NAME = "encrypted";
    private static String KEY_FILE_NAME = "key";
    private static String OUTPUT_FILE_NAME = "output";

    private static int KEY_SIZE_BYTES = 32;
    private static int BLOCK_SIZE_BYTES = 4;
    private static int INPUT_BUFFER_SIZE_BYTES = 65536;

    public static void main(String[] args) throws IOException {
		FileInputStream inputFile = new FileInputStream(ENCRYPTED_FILE_NAME);
		FileInputStream keyFile = new FileInputStream(KEY_FILE_NAME);
		FileOutputStream outputFile = new FileOutputStream(OUTPUT_FILE_NAME);

		byte[] key = new byte[KEY_SIZE_BYTES];
		byte[] keyPrevBlock = new byte[BLOCK_SIZE_BYTES];
		byte[] keyCurrBlock = new byte[BLOCK_SIZE_BYTES];
		int keyCurrBlockIndex = 0;
		keyFile.read(key);

		byte[] inputBuffer = new byte[INPUT_BUFFER_SIZE_BYTES];
		byte[] inputCurrBlock = new byte[BLOCK_SIZE_BYTES];
		int currInputIndex = 0;
		int bytesRead = inputFile.read(inputBuffer);

		byte[] output = new byte[INPUT_BUFFER_SIZE_BYTES];
		int currOutputIndex = 0;

		while (bytesRead > 0) {
			System.arraycopy(inputBuffer, currInputIndex, inputCurrBlock, 0, BLOCK_SIZE_BYTES);

			//step 1
			System.arraycopy(key, keyCurrBlockIndex, keyCurrBlock, 0, BLOCK_SIZE_BYTES);

			//step 2
			for (int i = 0; i < BLOCK_SIZE_BYTES; i++) {
				keyCurrBlock[i] ^= keyPrevBlock[i];
			}

			//step 3
			byte[] reversedKeyBlock = new byte[BLOCK_SIZE_BYTES];
			for (int i = 0; i < BLOCK_SIZE_BYTES; i++) {
				reversedKeyBlock[i] = keyCurrBlock[BLOCK_SIZE_BYTES - 1 - i];
			}
			ByteBuffer reversedKeyBuffer = ByteBuffer.wrap(reversedKeyBlock);
			long unsignedKeyValue = reversedKeyBuffer.getInt() & 0x00000000ffffffffL;
			unsignedKeyValue = unsignedKeyValue * 134775813 & 0x00000000ffffffffL;
			unsignedKeyValue += 1 & 0x00000000ffffffffL;

			//step 4
			keyCurrBlock = ByteBuffer.allocate(BLOCK_SIZE_BYTES).putInt((int) unsignedKeyValue).array();
			for (int i = 0; i < BLOCK_SIZE_BYTES / 2; i++) {
				byte swap = keyCurrBlock[i];
				keyCurrBlock[i] = keyCurrBlock[BLOCK_SIZE_BYTES - 1 - i];
				keyCurrBlock[BLOCK_SIZE_BYTES - 1 - i] = swap;
			}
			System.arraycopy(keyCurrBlock, 0, keyPrevBlock, 0, BLOCK_SIZE_BYTES);

			//step 5 {
			for (int i = 0; i < BLOCK_SIZE_BYTES; i++) {
				inputCurrBlock[i] ^= keyCurrBlock[i];
			}

			//output
			int bytes = BLOCK_SIZE_BYTES;
			if (bytesRead - currInputIndex < BLOCK_SIZE_BYTES) {
				bytes = bytesRead % BLOCK_SIZE_BYTES;
			}
			for (int i = 0; i < bytes; i++) {
				output[currOutputIndex++] = inputCurrBlock[i];
				if (currOutputIndex >= INPUT_BUFFER_SIZE_BYTES) {
					currOutputIndex = 0;
					outputFile.write(output);
				}
			}

			//next iteration
			keyCurrBlockIndex = (keyCurrBlockIndex == KEY_SIZE_BYTES - BLOCK_SIZE_BYTES) ?
					0 : keyCurrBlockIndex + BLOCK_SIZE_BYTES;
			currInputIndex += BLOCK_SIZE_BYTES;
			if (currInputIndex >= bytesRead) {
				currInputIndex = 0;
				bytesRead = inputFile.read(inputBuffer);
			}
		}
		if (currOutputIndex > 0) {
			outputFile.write(Arrays.copyOfRange(output, 0, currOutputIndex));
		}
		inputFile.close();
		outputFile.close();
		keyFile.close();
	}
}
