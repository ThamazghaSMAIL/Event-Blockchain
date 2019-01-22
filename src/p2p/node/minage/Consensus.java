package p2p.node.minage;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import merckletree.ConversionManager;

public class Consensus {

	public final static int NODES_NUMBER = 30;

	public static int hmac_sha256(int num_node) {
		final HashCode hashed = Hashing.sha256().hashInt(num_node);
		System.out.println("hashed "+hashed);
        return hashed.asInt()%NODES_NUMBER;
	}
}
