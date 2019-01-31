package blockchain;

import java.io.Serializable;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import merckletree.MerckleTree;
import merckletree.MerckleTree.MerckleTreeNode;
import p2p.node.Node;

public class Block implements Serializable{

	private static final long serialVersionUID = 1L;

	public Block(String previous_hash, List<Transaction> transactions, byte[] public_key, int nounce, int level) 
			throws NoSuchAlgorithmException {
		this.previous_hash = previous_hash;
		this.transactions = transactions;
		this.public_key = public_key;
		this.nounce = nounce;
		this.timestamp = (int) System.currentTimeMillis();
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

	private byte[] public_key;

	private int nounce;

	//TODO 
	private int timestamp ; 

	private int level;

	/**
	 * retourne beta
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

	public static String toJson(Block b) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.toJson(b);
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public byte[] getMerckletreeroot() {
		return merckletreeroot;
	}

	public void setMerckletreeroot(byte[] merckletreeroot) {
		this.merckletreeroot = merckletreeroot;
	}

	public String getPrevious_hash() {
		return previous_hash;
	}

	public void setPrevious_hash(String previous_hash) {
		this.previous_hash = previous_hash;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public byte[] getPublic_key() {
		return public_key;
	}

	public void setPublic_key(byte[] public_key) {
		this.public_key = public_key;
	}

	public int getNounce() {
		return nounce;
	}

	public void setNounce(int nounce) {
		this.nounce = nounce;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}


	public boolean verifyBlockTime() {
		if( this.timestamp < (int) System.currentTimeMillis()+2 )
			return true;
		return false;
	}
	
	public boolean verifyPrevious(Block previous) {
		if( ! this.getPrevious_hash().equals(previous.getHash()))
			return false;
		return true;
	}

	public boolean verify_block(Block previous) {
		if( verifyBlockTime() ) {
			if( ! verifyPrevious(previous) ) {
				System.out.println("bloc non valide *previous*");
				return false;
			}else
				return true;
		}else {
			System.out.println("bloc non valide *timstamp*");
			return false;
		}
	}


}
