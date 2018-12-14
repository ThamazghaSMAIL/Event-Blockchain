package p2p;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
			this.private_key = pair.getPrivate();
			this.public_key = pair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	PrivateKey private_key;
	PublicKey public_key;
}
