package p2p.node.reception;

import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;

/**
 * 
 * contient les methodes de decodage et decodage en binaire lors de la reception
 *
 */
public class ReceptionConversion {
	public static String json;
	public static String signature;
	public static void operationToString(String trans) {

		String size_string = trans.substring(0,32);
		int size_decimal = getSize(size_string);			   
		System.out.println("server--- taille en decimal: "+size_decimal);

		json = getTransactionJson(trans,size_decimal);
		signature = getSignature(trans,size_decimal);
	}

	private static String getSignature(String trans, int size_decimal) {
		String sig = trans.substring(31+size_decimal);
		System.out.println("signature : "+sig);
		

		StringBuilder sb = new StringBuilder(); // Some place to store the chars

		Arrays.stream( // Create a Stream
				sig.split("(?<=\\G.{8})") // Splits the input string into 8-char-sections (Since a char has 8 bits = 1 byte)
				).forEach(s -> // Go through each 8-char-section...
				sb.append((char) Integer.parseInt(s, 2)) // ...and turn it into an int and then to a char
						);
		
		String output = sb.toString(); // Output text (t)
		System.out.println("sig en json : "+output);
		return output;
	}

	public static String getTransactionJson(String trans, int size_decimal) {
		String trans_bin = trans.substring(32,size_decimal+32);
		System.out.println("transaction en binaire : "+trans_bin + "\ntaille : "+trans_bin.length());

		StringBuilder sb = new StringBuilder(); // Some place to store the chars

		Arrays.stream( // Create a Stream
				trans_bin.split("(?<=\\G.{8})") // Splits the input string into 8-char-sections (Since a char has 8 bits = 1 byte)
				).forEach(s -> // Go through each 8-char-section...
				sb.append((char) Integer.parseInt(s, 2)) // ...and turn it into an int and then to a char
						);
		
		String output = sb.toString(); // Output text (t)
		System.out.println("transaction en json : "+output);
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

}
