package p2p.node;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Wallet {

	public Wallet() {

		KeyPairGenerator keyGen;
		KeyPair pair;
		try {
			keyGen = KeyPairGenerator.getInstance("EC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			keyGen.initialize(256, random);
			pair = keyGen.generateKeyPair();
			this.private_key = pair.getPrivate().getEncoded();
			System.out.println(pair.getPublic().getFormat());
			this.public_key = pair.getPublic().getEncoded();
			System.err.println(this.public_key.getClass().getCanonicalName());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	protected byte[] private_key;
	protected byte[] public_key;
	
	
	public byte[] getPrivate_key() {
		return private_key;
	}

	public byte[] getPublic_key() {
		return public_key;
	}
}
