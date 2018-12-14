package merckletree;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import blockchain.Transaction;
import merckletree.MerckleTree.MerckleTreeNode;

public class TestMerckleTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<MerckleTreeNode> sigs = new ArrayList<>();
		MerckleTreeNode t1 = new MerckleTreeNode(new String("transaction 1").getBytes());
		MerckleTreeNode t2 = new MerckleTreeNode(new String("transaction 2").getBytes());
		sigs.add(t1);
		sigs.add(t2);
		MerckleTree mt ;


		try {
			mt = new MerckleTree(sigs);
			//parent 2f268c05100965ee43aee96580b2774c69e16e2cee9bd11be450704826f2ff70
			System.out.println("root "+mt.bytesToHex(mt.getRoot().sig));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}





	}
}