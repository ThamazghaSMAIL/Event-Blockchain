package tests.tests_crypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.codec.binary.Hex;

import merckletree.MerckleTree;
import merckletree.MerckleTree.MerckleTreeNode;

public class Tools {
	public static byte[] getBytesFromFile(String path) {
		File file;
		file = new File(path);

		byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(file.toPath());
		} catch (IOException e) {e.printStackTrace();}

		return fileContent;
	}

	public static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		try {
			while((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				//      stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}
	public static String byteToHex(byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}

	

}
