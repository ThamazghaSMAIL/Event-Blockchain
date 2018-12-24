package p2p;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.sun.corba.se.impl.ior.GenericTaggedComponent;

import blockchain.Transaction;

public class Node {
	/**
	 * Les adresses des noeuds que ce noeud connait préalablement 
	 */
	List<NodeInfos> contacts;
	public Node(List<NodeInfos> contacts) {
		this.contacts = contacts;
		//this.contacts.add(new NodeInfos("192.168.1.54", 2009));
	}
	

	public static void main(String[] args){
		List<NodeInfos> contacts = new ArrayList<NodeInfos>();
		Node n = new Node(contacts);
		
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("**************************");
		System.out.println("enter at least a contact\n ip adress : ");
		String ipadress = keyboard.nextLine();
		
		System.out.println("port :");
		int port = keyboard.nextInt();
		n.getContacts().add(new NodeInfos(ipadress, port));
		
		System.out.println("***************************");
		/**
		 * se mettre à l'écoute
		 */
		(new Thread(){
			public void run(){
				ServerSocket socket;

				try {
					socket = new ServerSocket(2009);
					Thread server = new Thread(new AcceptNode(socket));
					server.start();
					System.out.println(" Le serveur est prêt !\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
		/**
		 * create transaction
		 */
		Wallet w = new Wallet();
		String json ="hihi";
		//TODO c'est quoi creators_signature ?
		Transaction transaction1 = new Transaction(w.getPublic_key(), 
				"creators_signature", System.currentTimeMillis(), 
				Transaction.CREATION_TYPE , json.getBytes());

		//Scanner keyboard = new Scanner(System.in);
		System.out.println("**************************\n"
				+ "1- Créer une transaction");
		System.out.println("enter an integer");
		int myint = keyboard.nextInt();


		if( myint == 1 ) {
			broadcast_transaction(transaction1,contacts);
		}else {
			System.out.println("choix pas valable");
		}
	}

	public static void broadcast_transaction(Transaction transaction, List<NodeInfos> contacts) {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			NodeInfos ni = contacts.get(0);
			socket = new Socket(ni.getIpAdress(),ni.getPort());
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	

			Wallet w = new Wallet();
			String json ="hihi";
			
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


	public List<NodeInfos> getContacts() {
		return contacts;
	}


	public void setContacts(List<NodeInfos> contacts) {
		this.contacts = contacts;
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
		Transaction t = toTransaction(in.readLine());
		System.out.println("creator_sig : "+t.getCreators_signature());
		s.close();
	}


	private Transaction toTransaction(String json) {
		System.out.println("receive : "+json);
		Gson gson = new Gson();
		return gson.fromJson(json, Transaction.class);
	}

	public void client() {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			//TODO changer en contacts.get(0)
			socket = new Socket("192.168.1.54",2009);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	

			Wallet w = new Wallet();
			String json ="hihi";
			//TODO c'est quoi creators_signature ?
			Transaction transaction1 = new Transaction(w.getPublic_key(), 
					"creators_signature", System.currentTimeMillis(), 
					Transaction.CREATION_TYPE , json.getBytes());

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
