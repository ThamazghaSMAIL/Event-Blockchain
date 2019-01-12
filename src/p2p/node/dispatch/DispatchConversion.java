package p2p.node.dispatch;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.Signature;
/**
 * 
 * contient les methodes de codage en binaire lors de l'envoi
 *
 */
public class DispatchConversion {

	public static String toBinary(String trans_json, byte[] signature) {
		String result = "";
		System.out.println("- Transaction avant encodage : "+trans_json);
		String trans_binary = transactionToBinary(trans_json);
		result = result + sizeToBinaire(trans_binary.length());
		result = result + trans_binary;
		result = result + signatureToBinary(signature);
		System.out.println("- Taille + Transaction + Signature après encodage : "+result);
		return result;
	}



	public static String sizeToBinaire(int size) {
		System.out.println("- Taille en decimal de la transaction: "+size);
		String s =  Integer.toBinaryString(size);
		return bourage(s);
	}

	private static String bourage(String s) {
		String s_bourage = s;
		while( s_bourage.length() < 32 ) {
			s_bourage = "0" + s_bourage ;
		}
		return s_bourage;
	}

	public static String transactionToBinary(String trans) {
		byte[] infoBin = null;
		String result = "";
		try {
			infoBin = trans.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String bin = "" ;
		for (byte b : infoBin) {
			bin = Integer.toBinaryString(b); 
			while ( bin.length() < 8 ) {
				bin = "0" + bin;
			}
				

			result = result + bin;
		}
		return result;
	}

	public static String signatureToBinary(byte[] sig) {
		String result = "";
		String bin = "" ;
		for (byte b : sig) {
			bin = Integer.toBinaryString(b);
			while ( bin.length() < 8 ) {
				bin = "0" + bin;
			}
			result = result + bin;
		}
		return result;
	}
	public static byte[] signer(String trans_json,PrivateKey privatekey) {
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
	
}
