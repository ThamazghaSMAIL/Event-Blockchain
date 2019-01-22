package tests.tests_crypto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import merckletree.ConversionManager;

public class TestHmac {

	public static void test_hmac() throws IOException, NoSuchAlgorithmException {
		System.out.println("- test hmac :");
		test_valid(); //ok
		test_invalid(); //ok
		test_valid_2();   //ok
		
		test_node();
	}


	private static void test_valid_2() {
		System.out.println("\n--> valid_2");
		byte[] data = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/valid_2/data");
		try {
			String hmac_hex = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/valid_2/hmac_hex");
			String secret = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/valid_2/secret");
			/**
			 * data
			 */
			byte[] hmac_data = ConversionManager.hmac(data,secret);

			/**hex*/
			String hmac_data_hex = Tools.byteToHex(hmac_data);
			System.out.println("Calculated : "+hmac_data_hex);
			System.out.println("file       : "+hmac_hex+"\n");
			System.out.println("equal ? "+hmac_data_hex.equals(hmac_hex));
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}		
	}


	private static void test_valid(){
		byte[] data = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/valid/data");
		try {
			System.out.println("--> valid");
			String hmac_hex = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/valid/hmac_hex");
			String secret = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/valid/secret");
			/**
			 * data
			 */
			byte[] hmac_data = ConversionManager.hmac(data,secret);

			/**hex*/
			String hmac_data_hex = Tools.byteToHex(hmac_data);
			System.out.println("equal ? "+hmac_data_hex.equals(hmac_hex));
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	private static void test_invalid() {
		System.out.println("--> invalid");
		byte[] data = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/invalid/data");
		try {
			String hmac_hex = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/invalid/hmac_hex");
			String secret = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/invalid/secret");
			/**
			 * data
			 */
			byte[] hmac_data = ConversionManager.hmac(data,secret);

			/**hex*/
			String hmac_data_hex = Tools.byteToHex(hmac_data);
			System.out.println("not equal ? "+ ! hmac_data_hex.equals(hmac_hex));
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private static void test_node() {
		System.out.println("--> node");
		try {
			byte[] data_1 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/node/leaf_hex_1");
			byte[] data_2 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/node/leaf_hex_2");
			String concat_hex = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/hmac/node/concat_hex");

			byte[] concat_result = ConversionManager.concatenate(data_1, data_2);
			String concat_result_hex =new String(concat_result);
			System.out.println("Produced : "+concat_result_hex);
			System.out.println("Expected : "+concat_hex);
			
			
			System.out.println(concat_hex.equals(concat_result_hex));
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
}
