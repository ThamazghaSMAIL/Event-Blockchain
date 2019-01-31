package p2p.node.dispatch;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import p2p.node.NodeInfos;
import p2p.protocole.Operation;
/**
 * 
 * contient les methodes de codage en binaire lors de l'envoi
 *
 */
public class DispatchConversion {

	public static String toBinary(String trans_json, byte[] signature) {
		String result = "";
		String trans_binary = transactionToBinary(trans_json);
		result = result + sizeToBinaire(trans_binary.length());
		result = result + trans_binary;
		result = result + signatureToBinary(signature);
		return result;
	}

	public static String sizeToBinaire(int size) {
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
		BigInteger bi = new BigInteger(sig);
		String result = bi.toString(2);
		return result;
	}
	public static byte[] signer(String trans_json , PrivateKey privatekey) {
		byte[] signatureBytes = null;
		try {
			byte[] data = trans_json.getBytes("UTF8");

			Signature dsa = Signature.getInstance("SHA1withECDSA");

			dsa.initSign(privatekey);

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
	
	public static String operationToJson(Operation o) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.toJson(o);
	}
	
	public static Operation toRequest(String json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(json, Operation.class);
	}

	public NodeInfos toNodeInfos(String json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(json, NodeInfos.class);
	}
	
	
}
