package merckletree;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.google.common.hash.Hashing;

public class ConversionManager {

	private static byte[] hashInternalNode(String textefeuille) throws NoSuchAlgorithmException {
		String key = "1";
		String algorithm = "HmacSHA256";  // OPTIONS= HmacSHA512, HmacSHA256, HmacSHA1, HmacMD5
		byte[] hash = null;
		try {
			// 1. Get an algorithm instance.
			Mac sha256_hmac = Mac.getInstance(algorithm);
			// 2. Create secret key.
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			// 3. Assign secret key algorithm.
			sha256_hmac.init(secret_key);
			// 4. Generate hex encoded string.
			hash = sha256_hmac.doFinal(textefeuille.getBytes());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return hash;
	}
	public static byte[] hashInterne( byte[] sig_gauche,  byte[] sig_droit) {
		byte[] hash = null ;
		try {
			hash = hashInternalNode(Hex.encodeHexString(concatenate(sig_gauche, sig_droit)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}

	/**
	 *
	 * @param hash1
	 * @param hash2
	 * @return concatenation des deux hachés donné en parametres
	 */
	public static byte[] concatenate(byte[] hash1, byte[] hash2) {
		if(hash2 != null){
			byte[] newHash = new byte[hash1.length + hash2.length];
			System.arraycopy(hash1, 0, newHash, 0, hash1.length);
			System.arraycopy(hash2, 0, newHash, hash1.length, hash2.length);
			return newHash;
		}else{
			return  hash1;
		}
	}

	public static String stringToHex(String s) {
		String result = null;
		try {
			result = Hex.encodeHexString(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static byte[] hmac( byte[] textefeuille, String secret) throws NoSuchAlgorithmException {
		String key = secret;
		String algorithm = "HmacSHA256";  // OPTIONS= HmacSHA512, HmacSHA256, HmacSHA1, HmacMD5
		byte[] hash = null;
		try {
			// 1. Get an algorithm instance.
			Mac sha256_hmac = Mac.getInstance(algorithm);
			// 2. Create secret key.
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			// 3. Assign secret key algorithm.
			sha256_hmac.init(secret_key);
			// 4. Generate hex encoded string.
			hash = sha256_hmac.doFinal(textefeuille);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return hash;
	}

	public static String sha256(String input) {
		final String hashed = Hashing.sha256()
				.hashString(input, StandardCharsets.UTF_8)
				.toString();
		return hashed;
	}

}
