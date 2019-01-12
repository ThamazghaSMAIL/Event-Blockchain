package p2p.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Signature;

import p2p.node.Wallet;
import p2p.node.dispatch.DispatchConversion;

public class NodeClient {
	static Wallet w;
	public static void main(String[] zero){
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {

			w = new Wallet();

			String data = "Transaction_0";
			byte[] signature = signer(data);
			String trans = DispatchConversion.toBinary(data, signature);
			
			socket = new Socket("localhost",2009);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
			out.write(trans);
			out.flush();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] signer(String trans_json) {
		byte[] signatureBytes = null;
		try {
			byte[] data = trans_json.getBytes("UTF8");

			Signature dsa = Signature.getInstance("SHA1withECDSA");

			dsa.initSign(w.getPrivateK());

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
