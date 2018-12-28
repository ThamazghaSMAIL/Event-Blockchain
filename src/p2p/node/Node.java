package p2p.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.JsonObject;

import blockchain.Blockchain;
import blockchain.Transaction;
import p2p.interfaces.INode;

public class Node implements INode{

	public final static int LIMIT = 10;
	/**
	 * Les adresses des noeuds que ce noeud connait préalablement
	 */
	public CopyOnWriteArrayList<NodeInfos> contacts;
	public Wallet wallet ;
	public List<Transaction> transactions;
	public Blockchain blockchain;
	public int id;

	private Node() {
		this.contacts = new CopyOnWriteArrayList<NodeInfos>();
		this.wallet = new Wallet();
		this.transactions = new ArrayList<Transaction>();
		this.contacts.add(new NodeInfos("localhost", 2009));
		this.blockchain = new Blockchain(getInstance());
	}

	/** Instance unique pré-initialisée */
	private static Node INSTANCE = new Node();

	public static Node getInstance()
	{   
		return INSTANCE;
	}

	public static void main(String[] args) {
		/**
		 * l'utilisateur saisi un seul contact pour entrer dans la blockchain, le reste se fera automatiquement
		 */
		NodeInfos ni = saisie();

		/**
		 * se mettre à l'écoute
		 */

		(new Thread(){
			public void run(){
				try {
					getInstance().listen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();


		(new Thread(){
			public void run(){
				try {
					getInstance().premier_contact(ni);
					//getInstance().getContacts().add(ni);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * create transaction
		 */
		Thread t = new Thread(new CreateTransaction(getInstance()));
		t.start();
	}

	private static NodeInfos saisie() {
		Scanner keyboard = new Scanner(System.in);

		System.out.println("**************************");
		System.out.println("enter at least a contact ");

		System.out.println("ip adress :");
		String ipadress = keyboard.nextLine();

		System.out.println("port :");
		int port = keyboard.nextInt();
		return new NodeInfos(ipadress, port);
	}


	@Override
	public void premier_contact(NodeInfos ni) {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			System.out.println("premier contact");
			socket = new Socket(ni.getIpAdress(), ni.getPort());
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			JsonObject coord = new JsonObject();
			coord.addProperty("paquet", "hello");
			coord.addProperty("ippadress", "192.168.1.44");
			coord.addProperty("port", 2009);
			String coordonees = coord.toString();
			out.write(coordonees);
			out.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("this node isn't disponible");
		}
	}



	@Override
	public void listen() {
		(new Thread(){
			public void run(){
				ServerSocket socket;
				try {
					socket = new ServerSocket(2009);
					new AcceptNode(socket,getInstance());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		System.out.println("listening...");
	}

	@Override
	public void search_freinds(List<NodeInfos> list) {
		Thread t;
		ServerSocket socket;
		try {
			socket = new ServerSocket(2009);

			for( NodeInfos n : list ) {
				t = new Thread() {
					public void run() {
						premier_contact(n);
					}
				};
				t.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	
	/**
	 * getters and setters
	 */
	public List<NodeInfos> getContacts() {
		return contacts;
	}
	public Wallet getW() {
		return wallet;
	}
	public void setW(Wallet w) {
		this.wallet = w;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Blockchain getBlockchain() {
		return blockchain;
	}

	public void setBlockchain(Blockchain blockchain) {
		this.blockchain = blockchain;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}


