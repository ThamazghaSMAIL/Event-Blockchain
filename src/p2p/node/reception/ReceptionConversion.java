package p2p.node.reception;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import blockchain.Transaction;
import p2p.node.NodeInfos;

/**
 * 
 * contient les methodes de decodage et decodage en binaire lors de la reception
 *
 */
public class ReceptionConversion {


	public static byte[] getSignature(String trans) {

		try {
			String size_string = trans.substring(0,32);
			int size_decimal = ReceptionConversion.getSize(size_string);	
			
			String sig = trans.substring(32+size_decimal);
			if(sig.contains("\"")) sig = sig.replaceAll("\"","");
			StringBuilder sb = new StringBuilder(); // Some place to store the chars
			byte[] array = new BigInteger(sig,2).toByteArray();
			return array;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;





		/*
		 * last version


		Arrays.stream( // Create a Stream
				sig.split("(?<=\\G.{8})") // Splits the input string into 8-char-sections (Since a char has 8 bits = 1 byte)
				).forEach(s -> // Go through each 8-char-section...
				sb.append((char) Integer.parseInt(s, 2)) // ...and turn it into an int and then to a char
						);

		String output = sb.toString(); // Output text (t)
		return output.getBytes();

		/*
//		StringBuilder sb = new StringBuilder(); // Some place to store the chars
//
//		Arrays.stream( // Create a Stream
//				sig.split("(?<=\\G.{8})") // Splits the input string into 8-char-sections (Since a char has 8 bits = 1 byte)
//				).forEach(s -> // Go through each 8-char-section...
//				sb.append((char) Integer.parseInt(s, 2)) // ...and turn it into an int and then to a char
//						);
//		String output = sb.toString(); // Output text (t)
//		System.out.println("- Signature en json : "+output);
//		return output.getBytes();
		 */
	}

	public static String getTransactionJson(String trans, int size_decimal) {
		String trans_bin = trans.substring(32,size_decimal+32);

		StringBuilder sb = new StringBuilder(); // Some place to store the chars

		Arrays.stream( // Create a Stream
				trans_bin.split("(?<=\\G.{8})") // Splits the input string into 8-char-sections (Since a char has 8 bits = 1 byte)
				).forEach(s -> // Go through each 8-char-section...
				sb.append((char) Integer.parseInt(s, 2)) // ...and turn it into an int and then to a char
						);

		String output = sb.toString(); // Output text (t)
		System.out.println("- Transaction recue en json : "+output);
		return output;
	}

	public static int getSize(String size_string) {
		int i = 0;
		while( size_string.charAt(i) == '0') {
			size_string = size_string.substring(1);
			i++;
		}
		return Integer.parseInt(size_string, 2);
	}



	/** verification */
	public boolean verify(PublicKey publicKey, byte[] signed, String message) throws Exception {
		Signature signature = Signature.getInstance("SHA1withECDSA");
		signature.initVerify(publicKey);

		signature.update(message.getBytes());

		return signature.verify(signed);
	}

	public static String contactsToString(List<NodeInfos> list) {
		System.out.println("size : "+list.size());
		if( list != null ) {
			String s = "";
			for (NodeInfos nodeInfos : list) {
				s = s+nodeInfos.toString() + ",";
			}
			return s + " ]";
		} 
		return "null ]";
	}

	public static Transaction toTransaction(String json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(json, Transaction.class);
	}

	public static String getSignature_bin(String trans) {
		// TODO Auto-generated method stub
		String size_string = trans.substring(0,32);
		int size_decimal = ReceptionConversion.getSize(size_string);	
		
		String sig = trans.substring(32+size_decimal);
		return sig;
	}
}
