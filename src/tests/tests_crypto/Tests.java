package tests.tests_crypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import merckletree.ConversionManager;
import merckletree.MerckleTree;
import merckletree.MerckleTree.MerckleTreeNode;


public class Tests {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
//		TestHex.test_hex();//ok
//		TestSHA256.test(); //ok
		
//		
//		TestHmac.test_hmac(); //ok
//		
		TestMerckle.test_merckle(); //nope
//		TestSECP256R1.test(); //nope
		
	}


	

}
