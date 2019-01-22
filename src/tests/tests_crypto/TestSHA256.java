package tests.tests_crypto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import merckletree.ConversionManager;
import merckletree.MerckleTree;
import merckletree.MerckleTree.MerckleTreeNode;

public class TestSHA256 {

	public static void test() {
		System.out.println("- test sha256  ");
		try {
			String sha256_hex = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/sha256/sha256_hex");
			String data = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/sha256/data");
			
			String data_sha256 = ConversionManager.sha256(data);
			
			System.out.println("equal ? "+sha256_hex.equals(data_sha256));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		
	}

	
}
