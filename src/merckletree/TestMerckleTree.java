package merckletree;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class TestMerckleTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<String> sigs = new ArrayList<>();
		sigs.add("heloo");
		sigs.add("yay");
		sigs.add("hello");
		MerckleTree mt;
		try {
			mt = new MerckleTree(sigs);
			//System.out.println("mt "+mt.getRoot().toString());
			System.out.println(mt.getRoot().sig);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}