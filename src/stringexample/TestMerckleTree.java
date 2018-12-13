package stringexample;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import blockchain.Transaction;

public class TestMerckleTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<String> sigs = new ArrayList<>();
		sigs.add("transaction 1");
		sigs.add("transaction 2");
		MerckleTree mt;
		try {
			mt = new MerckleTree(sigs);
			
			System.out.println(mt.getRoot().sig);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}