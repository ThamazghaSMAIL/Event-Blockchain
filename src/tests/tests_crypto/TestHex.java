package tests.tests_crypto;

import java.io.IOException;

public class TestHex {
	public static void test_hex() throws IOException {
		test_0();
	}

	private static void test_0() {
		
		try {
			System.out.println("- test_hex");
			/** test_0*/
			String hex0;
			byte[] data0 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/hex/test_0/data");
			hex0 = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hex/test_0/hex");
			System.out.println("test_0 : " +hex0.equals(Tools.byteToHex(data0)));

			/** test_1*/
			byte[] file1 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/hex/test_1/data");
			String result_file1 = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hex/test_1/hex");
			
			byte[] result_file1_byte = result_file1.getBytes();
			System.out.println("test_1 : " +result_file1.equals(Tools.byteToHex(file1)));


			/** test_2*/
			byte[] file2 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/hex/test_2/data");
			String result_file2 = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hex/test_2/hex");
			
			System.out.println("test_2 : " +result_file2.equals(Tools.byteToHex(file2)));	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
