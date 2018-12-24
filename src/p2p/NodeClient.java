package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import blockchain.Transaction;

public class NodeClient {

	public static void main(String[] zero){
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			socket = new Socket("192.168.1.54",2009);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
			
			Wallet w = new Wallet();
			String json ="hihi";
			//TODO c'est quoi creators_signature ?
			Transaction transaction1 = new Transaction(w.getPublic_key(), 
					"creators_signature", System.currentTimeMillis(), 
					Transaction.CREATION_TYPE , json.getBytes());
			
//			ObjectMapper mapper = new ObjectMapper();
//			String jsonInString = mapper.writeValueAsString(transaction1);
			
			String trans_json = toJson(transaction1);
	        
			out.write(trans_json);
			out.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String toJson(Transaction transaction1) {
		Gson gson = new Gson();
		return gson.toJson(transaction1);
	}
}
