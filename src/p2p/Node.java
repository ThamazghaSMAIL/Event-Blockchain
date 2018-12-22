package p2p;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;

import blockchain.Transaction;

public class Node {
	public Node(List<String> contacts) {
		this.contacts = contacts;
	}
	/**
	 * Les adresses des noeuds que ce noeud connait préalablement 
	 */
	//TODO contient l'@ ip + le port ?
	List<String> contacts;

	public static void main(String[] args){

		ServerSocket socket;
		try {
			socket = new ServerSocket(2009);
			Thread server = new Thread(new AcceptNode(socket));
			server.start();
			System.out.println(" Le serveur est prêt !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void broadcast_transaction(Transaction transaction) {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			socket = new Socket("localhost",2009);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	

			Wallet w = new Wallet();
			String json ="hihi";
			//TODO c'est quoi creators_signature ?
			//			Transaction transaction1 = new Transaction(w.getPublic_key(), 
			//					"creators_signature", System.currentTimeMillis(), 
			//					Transaction.CREATION_TYPE , json.getBytes());

			//			ObjectMapper mapper = new ObjectMapper();
			//			String jsonInString = mapper.writeValueAsString(transaction1);

			String trans_json = toJson(transaction);

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

class AcceptNode implements Runnable {

	private ServerSocket socketserver;
	private Socket socket;
	private int nbrclient = 1;
	public Thread t1;

	public AcceptNode(ServerSocket s){
		socketserver = s;
	}

	public void run() {

		try {
			while(true){
				socket = socketserver.accept(); // Un node se connecte on l'accepte
				t1 = new Thread(){
					public void run(){
						try {
							receive_transaction(socket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				t1.start();
				System.out.println("Le client numéro "+nbrclient+ " est connecté !");
				nbrclient++;
				//socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receive_transaction(Socket s) throws IOException{
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));	
		System.out.println("receive : "+in.readLine());
		s.close();
	}
}
