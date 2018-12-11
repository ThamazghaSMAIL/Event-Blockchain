package merckletree;

import java.util.ArrayList;
import java.util.List;

public class TestMerckleTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<String> sigs = new ArrayList<>();
		sigs.add("heloo");
		sigs.add("yay");
		sigs.add("hello");
		MerckleTree mt = new MerckleTree(sigs);
		System.out.println("mt "+mt.getRoot().toString());
		System.out.println(mt.getHeight());
	}

}