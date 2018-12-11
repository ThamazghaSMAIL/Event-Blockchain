package merckletree;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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