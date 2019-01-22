package tests.tests_crypto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import merckletree.MerckleTree;
import merckletree.MerckleTree.MerckleTreeNode;

public class TestMerckle {

	public static void test_merckle() throws IOException, NoSuchAlgorithmException {
		System.out.println("- test merckletree : ");
		/** empty */

		//TestMerckle.test_empty(); //ok

		/** une seule feuille*/
		test_leaf_0();

		test_leaf_1();
		
		test_leaf_2();

		
		/** nodes */
		test_node_1();

	}

	public static void test_empty() {
		String root_hex = null;

		try {
			root_hex = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/empty/root_hex");
			List<MerckleTreeNode> feuilles = new ArrayList<MerckleTreeNode>();

			MerckleTree merckleTree_empty = new MerckleTree(feuilles);
			System.out.println("--> empty ");
			String root = merckleTree_empty.bytesToHex(merckleTree_empty.getRoot().sig);
			
			
			System.out.println("Produced :"+root);
			System.out.println("Expected :"+root_hex);
			System.out.println("equal ? :"+root_hex.equals(root));
		} catch (IOException | NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
	}

	public static void test_leaf_0() {
		System.out.println("\n--> leaf_0 ");
		List<MerckleTreeNode> feuilles = new ArrayList<MerckleTreeNode>();

		try {
			byte[] data_00 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/leaf_0/data_00");
			String root_hex_oneleaf = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/leaf_0/root_hex");
			feuilles.add(new MerckleTreeNode(data_00));
			

			MerckleTree mt_oneleaf = new MerckleTree(feuilles);
			String root = mt_oneleaf.bytesToHex(mt_oneleaf.getRoot().sig);
			System.out.println("Produced :"+root);

			System.out.println("Expected :"+root_hex_oneleaf);	
			System.out.println("equal ? :"+root_hex_oneleaf.equals(root));	
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}
	private static void test_leaf_1() {
		System.out.println("\n--> leaf_1 ");
		List<MerckleTreeNode> feuilles = new ArrayList<MerckleTreeNode>();

		try {
			byte[] data_00 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/leaf_1/data_00");
			String root_hex_oneleaf = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/leaf_1/root_hex");
			feuilles.add(new MerckleTreeNode(data_00));

			MerckleTree mt_oneleaf = new MerckleTree(feuilles);
			String root = mt_oneleaf.bytesToHex(mt_oneleaf.getRoot().sig);

			System.out.println("Produced :"+root);
			System.out.println("Expected :"+root_hex_oneleaf);
			System.out.println("equal ? :"+root_hex_oneleaf.equals(root));	
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}
	private static void test_leaf_2() {
		System.out.println("\n--> leaf_2 ");
		List<MerckleTreeNode> feuilles = new ArrayList<MerckleTreeNode>();

		try {
			byte[] data_00 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/leaf_2/data_00");
			String root_hex_oneleaf = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/leaf_2/root_hex");
			feuilles.add(new MerckleTreeNode(data_00));

			MerckleTree mt_oneleaf = new MerckleTree(feuilles);
			String root = mt_oneleaf.bytesToHex(mt_oneleaf.getRoot().sig);

			System.out.println("Produced :"+root_hex_oneleaf);	
			System.out.println("Expected :"+root);	
			System.out.println("equal ? :"+root_hex_oneleaf.equals(root));	
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private static void test_node_1() {
		
		System.out.println("\n--> node_0 ");
		List<MerckleTreeNode> feuilles = new ArrayList<MerckleTreeNode>();

		try {
			String root_hex_oneleaf = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/node_0/root_hex");
			
			byte[] data_00 = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/node_0/data_00");
			byte[] data_01 =Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/merkle/node_0/data_01");
			
			feuilles.add(new MerckleTreeNode(data_00));
			feuilles.add(new MerckleTreeNode(data_01));
			
			MerckleTree mt_node_0 = new MerckleTree(feuilles);
			String root = mt_node_0.bytesToHex(mt_node_0.getRoot().sig);
			System.out.println("Produced : "+root);

			System.out.println("Expected : "+root_hex_oneleaf);	
			System.out.println("equal ? :"+root_hex_oneleaf.equals(root));	
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
