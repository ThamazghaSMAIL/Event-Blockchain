package tests.tests_crypto;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;

import p2p.node.dispatch.DispatchConversion;

public class TestSECP256R1 {

	public static void test() {
		System.out.println("--> TestSECP256R1 :");
		byte[] secret_key = null;
		try {
			String data = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/SECP256R1/data");
			secret_key = Tools.getBytesFromFile("/home/thamazgha/TPDEV/tests/test_crypto/SECP256R1/secret_key");
			String valid_signature_hex = Tools.readFile("/home/thamazgha/TPDEV/tests/test_crypto/SECP256R1/valid_signature_hex");

			PrivateKey privateKey = getPrivKeyFromCurve(secret_key);
			System.out.println("privateKey : "+privateKey.getAlgorithm()
			+" "+privateKey.getFormat()+" "+privateKey.getEncoded());

			System.out.println("Expected : "+valid_signature_hex);
			System.out.println("Produced : "+DispatchConversion.signer(data, privateKey));

			//verifySignature(secret_key);
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}




	}

	private static PrivateKey getPrivateKeyFromByte(byte[] secret_key) {
		PrivateKey privateKey = null;
		try {
			KeyFactory kf = KeyFactory.getInstance("EC");
			privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(secret_key));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	/**
	 * celia solution
	 */
	/**
	 * Get private Key java object from priv Byte
	 * @param privKey
	 * @return
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static PrivateKey getPrivKeyFromCurve(byte[] privKey) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
		KeyFactory kf = KeyFactory.getInstance("ECDSA", new BouncyCastleProvider());
		ECNamedCurveSpec params = new ECNamedCurveSpec("secp256r1", spec.getCurve(), spec.getG(), spec.getN());
		ECPrivateKeySpec priKey = new ECPrivateKeySpec(new BigInteger(privKey), params);
		return kf.generatePrivate(priKey);
	}
	/**
	 * will solution
	 * @param privatekey
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static void verifySignature(byte[] privatekey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
		KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
		ECNamedCurveSpec params = new ECNamedCurveSpec("secp256r1 ", spec.getCurve(), spec.getG(), spec.getN());
		ECPoint point = ECPointUtil.decodePoint(params.getCurve(), privatekey);
		ECPublicKeySpec privateKeySpec = new ECPublicKeySpec(point, params);
		PublicKey privateKey = keyFactory.generatePublic(privateKeySpec);
		Signature signature = Signature.getInstance("SHA256withECDSA");
		signature.initVerify(privateKey);
	}
}
