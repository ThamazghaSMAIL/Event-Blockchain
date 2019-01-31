package p2p.node.reception.verification;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;

import blockchain.Transaction;
import p2p.node.reception.ReceptionConversion;

public class VerificationManager {
	protected final String EXPECTED_SIGNATURE_ALGORITHM = "SHA1withECDSA";


	public static boolean verify(String trans, byte[] signature) {
		Transaction t = ReceptionConversion.toTransaction(trans);
		PublicKey publicKey;

		try {
			publicKey = t.getPublicKey();
			System.out.println("public key : " + publicKey);
			System.err.println(((ECPublicKey) publicKey).getW().getAffineX());

			if( verif_metier(t) && verify_signature(signature,trans,publicKey))
				return true;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean verify_signature(byte[] sig, String transaction_json,PublicKey publicKey) {
		boolean result = false;
		try {

			Signature dsa = Signature.getInstance("SHA1withECDSA");
			dsa.initVerify(publicKey);
			dsa.update(transaction_json.getBytes());
			result = dsa.verify(sig);
		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
		}		 
		System.out.println("yaaaaaaaaaaaaas "+result);
		return result;
	}

	private static boolean verif_metier(Transaction t) {
		return true;
	}

	public static byte[] signer(String trans_json , PrivateKey privatekey) {
		byte[] signatureBytes = null;
		try {
			byte[] data = trans_json.getBytes("UTF8");

			Signature dsa = Signature.getInstance("SHA1withECDSA");

			dsa.initSign(privatekey);

			//String str = trans_json;
			//byte[] strByte = trans_json.getBytes("UTF-8");
			dsa.update(data);

			/*
			 * Now that all the data to be signed has been read in, generate a
			 * signature for it
			 */

			byte[] realSig = dsa.sign();
			signatureBytes = realSig;

		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
		}
		return signatureBytes;
	}

	public static void main(String[] args) {
		KeyPairGenerator keyGen;
		KeyPair pair;
		try {
			keyGen = KeyPairGenerator.getInstance("EC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			keyGen.initialize(256, random);

			pair = keyGen.generateKeyPair();


			PrivateKey privateK = pair.getPrivate();
			PublicKey publicK = pair.getPublic();


			byte[] sig = signer("{\"creators_public_key\":[48,89,48,19,6,7,42,-122,72,-50,61,2,1,6,8,42,-122,72,-50,61,3,1,7,3,66,0,4,4,-119,-106,27,-55,-44,9,-57,-83,120,29,-65,16,-2,82,16,-71,-79,13,-45,125,65,-73,-59,-66,-120,-63,71,67,32,-115,110,6,71,81,-9,-108,-25,60,-38,76,60,-19,101,124,47,-66,32,-106,-61,-113,-24,78,-83,-111,7,51,-46,51,88,-8,50,-77,-78],\"timestamp\":1548604170204,\"type\":\"creation\",\"json\":[123,34,110,97,109,101,34,58,34,110,97,109,101,34,44,34,100,101,115,99,114,105,112,116,105,111,110,34,58,34,100,101,115,99,114,105,112,116,105,111,110,34,44,34,100,97,116,101,34,58,34,123,92,34,100,97,116,101,92,34,58,92,34,50,55,47,48,49,47,50,48,49,57,92,34,44,92,34,102,111,114,109,97,116,92,34,58,92,34,100,100,47,77,77,47,121,121,121,121,92,34,125,34,44,34,108,111,99,97,116,105,111,110,34,58,34,108,111,99,97,116,105,111,110,34,44,34,100,101,98,117,116,34,58,34,34,44,34,102,105,110,34,58,34,34,125]}",privateK);

			System.out.println(verify_signature(sig,"{\"creators_public_key\":[48,89,48,19,6,7,42,-122,72,-50,61,2,1,6,8,42,-122,72,-50,61,3,1,7,3,66,0,4,4,-119,-106,27,-55,-44,9,-57,-83,120,29,-65,16,-2,82,16,-71,-79,13,-45,125,65,-73,-59,-66,-120,-63,71,67,32,-115,110,6,71,81,-9,-108,-25,60,-38,76,60,-19,101,124,47,-66,32,-106,-61,-113,-24,78,-83,-111,7,51,-46,51,88,-8,50,-77,-78],\"timestamp\":1548604170204,\"type\":\"creation\",\"json\":[123,34,110,97,109,101,34,58,34,110,97,109,101,34,44,34,100,101,115,99,114,105,112,116,105,111,110,34,58,34,100,101,115,99,114,105,112,116,105,111,110,34,44,34,100,97,116,101,34,58,34,123,92,34,100,97,116,101,92,34,58,92,34,50,55,47,48,49,47,50,48,49,57,92,34,44,92,34,102,111,114,109,97,116,92,34,58,92,34,100,100,47,77,77,47,121,121,121,121,92,34,125,34,44,34,108,111,99,97,116,105,111,110,34,58,34,108,111,99,97,116,105,111,110,34,44,34,100,101,98,117,116,34,58,34,34,44,34,102,105,110,34,58,34,34,125]}", publicK));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
