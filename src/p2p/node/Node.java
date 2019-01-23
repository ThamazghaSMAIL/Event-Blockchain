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

import com.google.common.hash.HashCode;
import com.google.gson.JsonObject;

import blockchain.Blockchain;
import blockchain.Transaction;
import p2p.interfaces.INode;
import p2p.node.dispatch.CreateTransaction;
import p2p.node.dispatch.DispatchConversion;
import p2p.node.minage.Minage;
import p2p.node.reception.AcceptNode;
import p2p.node.reception.ReceptionConversion;
import p2p.protocole.Operation;

public class Node implements INode{

	public final static int LIMIT = 10;
	/**
	 * Les adresses des noeuds que ce noeud connait préalablement
	 */
	public CopyOnWriteArrayList<NodeInfos> contacts;
	public Wallet wallet ;
	public List<Transaction> transactions;
	public Blockchain blockchain;
	public String id;
	public int nounce;
	public NodeInfos myinformations;

	private Node() {
		this.contacts = new CopyOnWriteArrayList<NodeInfos>();
		this.wallet = new Wallet();
		this.transactions = new ArrayList<Transaction>();
		this.blockchain = new Blockchain();
		//TODO mettre un id pour le noeud
	}

	/** Instance unique pré-initialisée */
	private static volatile Node INSTANCE ;
	public synchronized static Node getInstance()
	{   
		Node result = INSTANCE;
		if (result == null) {
			INSTANCE = result = new Node();
		}
		return result;
	}

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);

		System.out.println("id :");
		String id = keyboard.nextLine();
		getInstance().setId(id);

		System.out.println("your ipadress :");
		String my_ipadress = keyboard.nextLine();

		System.out.println("port :");
		int my_port = keyboard.nextInt();

		getInstance().setMyinformations(new NodeInfos(my_ipadress, my_port));

		/**
		 * l'utilisateur saisi un seul contact pour entrer dans la blockchain, le reste se fera automatiquement
		 */
		List<NodeInfos> nodes = saisie();

		if( nodes.size() > 0 )
			for(NodeInfos ni : nodes ) {

				(new Thread(){
					public void run(){
						try {
							getInstance().premier_contact(ni);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}



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





		//		/** Minage **/
		//		(new Thread(new Minage())).start();
		//
		//		
		/** create transaction */
		Thread t = new Thread(new CreateTransaction());
		t.start();
	}

	private static List<NodeInfos> saisie() {
		List<NodeInfos> first_freinds = new ArrayList<NodeInfos>();
		Scanner keyboard = new Scanner(System.in);

		System.out.println("************************");
		System.out.println("have you any freinds ? (y/n)");

		String rep = keyboard.nextLine();


		String response = null;
		if( rep.equals("y"))
			do {
				System.out.println("ip adress :");
				String ipadress = keyboard.nextLine();

				System.out.println("port :");
				int port = keyboard.nextInt();
				
				
				System.out.println("fini ? tap y or n?");
				System.out.println(keyboard.nextLine());
				response = keyboard.nextLine();
				
				System.out.println("response "+response);
				first_freinds.add(new NodeInfos(ipadress, port));
			}while (response.equals("n"));

		return first_freinds;
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

			/**
			 * construire le json à envoyer
			 */
			Operation coord = new Operation();
			coord.setPaquet("hello");
			coord.setIpaddress("localhost");
			coord.setPort(INSTANCE.getMyinformations().getPort());

			/**
			 * recuperer la signature de l'opération
			 */
			String data = DispatchConversion.operationToJson(coord);
			byte[] signature = DispatchConversion.signer(data,getInstance().getW().getPrivateK());

			/**
			 * construire l'operation : taille + json + signature, le tout en binaire
			 */
			String result = DispatchConversion.toBinary(data, signature);


			//			System.out.println("**************verification ");
			//			ReceptionConversion.operationToString(result);
			out.write(result);
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
					socket = new ServerSocket(INSTANCE.getMyinformations().getPort());
					AcceptNode an = new AcceptNode(socket);
					Thread thread1 = new Thread(an);
					thread1.start();
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
			socket = new ServerSocket(INSTANCE.getMyinformations().getPort());

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNounce() {
		return nounce;
	}

	public void setNounce(int nounce) {
		this.nounce = nounce;
	}

	public NodeInfos getMyinformations() {
		return myinformations;
	}

	public void setMyinformations(NodeInfos myinformations) {
		this.myinformations = myinformations;
	}

}


