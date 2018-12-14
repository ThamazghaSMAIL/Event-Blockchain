package blockchain;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import merckletree.MerckleTree;
import merckletree.MerckleTree.MerckleTreeNode;

public class Block {

	public Block(String previous_hash, List<Transaction> transactions, String public_key) 
			throws NoSuchAlgorithmException {
		this.previous_hash = previous_hash;
		this.transactions = transactions;
		this.public_key = public_key;
		
		List<MerckleTreeNode> nodes = new ArrayList<MerckleTreeNode>();
		MerckleTreeNode node =null;
		for (Transaction t : this.transactions) {
			node = new MerckleTreeNode(t.getJson());
			nodes.add(node);
		}

		try {
			this.merckletreeroot = new MerckleTree(nodes).getRoot().sig;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.hash = hash_block();
	}



	/**
	 * contient le haché de tout le bloc courant
	 */
	private String hash ;
	/**
	 *  a partir de la liste des transactions 
	 */
	private byte[] merckletreeroot;

	private String previous_hash;

	private List<Transaction> transactions;

	private String signature;

	private String public_key;

	private int nonce;

	/**
	 * retourne beta
	 * @param previous_hash2
	 * @param string
	 * @param hash2
	 * @return
	 */
	private String str() {
		return this.previous_hash+this.transactions.toString()+this.hash;
	}

	/**
	 * retourne alpha
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	private String hash_block() throws NoSuchAlgorithmException {
		return hmacsha256(str());
	}

	/**
	 * l'utilisateur signe avec sa clé privée
	 * @param private_key
	 * @return
	 */
	private String sign(String private_key) 
			throws SignatureException, NoSuchAlgorithmException,
			InvalidKeyException, UnsupportedEncodingException {
		/**
		 * pour le moment les clés sont généré ici, plus tard ds wallet (node)
		 */
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

		keyGen.initialize(256, random);

		KeyPair pair = keyGen.generateKeyPair();
		PrivateKey priv = pair.getPrivate();
		PublicKey pub = pair.getPublic();


		/*
		 * Create a Signature object and initialize it with the private key
		 */

		Signature dsa = Signature.getInstance("SHA1withECDSA");

		dsa.initSign(priv);

		
		byte[] strByte = this.hash.getBytes("UTF-8");
		dsa.update(strByte);

		/*
		 * Now that all the data to be signed has been read in, generate a
		 * signature for it
		 */

		byte[] realSig = dsa.sign();
		String result = new BigInteger(1, realSig).toString(16);

		return result;
	}

	//TODO modifier pour le calculer
	/**
	 * generer un nmbre aléatoire inferieur a la difficulté
	 * @return
	 */
	private void findNounce() {
		this.nonce = 1;
	}


	private static String hmacsha256(String text) throws NoSuchAlgorithmException {
		String message = text;
		String algorithm = "HmacSHA256"; 
		String hash = "";
		//TODO est ce que c'est 1 pour le bloc ?
		String key = "1";
		try {
			// 1. Get an algorithm instance.
			Mac sha256_hmac = Mac.getInstance(algorithm);
			// 2. Create secret key.
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			// 3. Assign secret key algorithm.
			sha256_hmac.init(secret_key);
			// 4. Generate hex encoded string.
			hash = Hex.encodeHexString(sha256_hmac.doFinal(message.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return hash;
	}


}
