package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import blockchain.Transaction;
import p2p.interfaces.INode;
import p2p.protocole.Request;

public class Node implements INode{

	public final static int LIMIT = 10;
	/**
	 * Les adresses des noeuds que ce noeud connait préalablement
	 */


	public CopyOnWriteArrayList<NodeInfos> contacts;
	public Wallet wallet ;
	public List<Transaction> transactions;


	private Node() {
		this.contacts = new CopyOnWriteArrayList<NodeInfos>();
		this.wallet = new Wallet();
		this.transactions = new ArrayList<Transaction>();
		this.contacts.add(new NodeInfos("192.168.1.54", 2009));
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
		Scanner keyboard = new Scanner(System.in);

		System.out.println("**************************");
		System.out.println("enter at least a contact ");

		System.out.println("ip adress :");
		String ipadress = keyboard.nextLine();

		System.out.println("port :");
		int port = keyboard.nextInt();

		/**
		 * se mettre à l'écoute
		 */
		getInstance().listen();

		NodeInfos ni = new NodeInfos(ipadress, port);
		getInstance().premier_contact(ni);


		getInstance().getContacts().add(ni);



		System.out.println("***************************\n");



		/**
		 * create transaction
		 */
		try {
			Transaction transaction1 = create_transaction();

			System.out.println("**************************\n"
					+ "1- Créer une transaction");
			System.out.println("enter an integer");
			int myint = keyboard.nextInt();


			if( myint == 1 ) {
				getInstance().broadcast_transaction(transaction1,getInstance().getContacts());
			}else {
				System.out.println("choix pas valable");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					(new Thread(new AcceptNode(socket,getInstance()))).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		System.out.println("listening...");
	}



	private static Transaction create_transaction() {
		//TODO c'est quoi creators_signature ?
		//TODO changer par les données saisies par le user 
		Transaction t = new Transaction();
		t.setCreators_public_key(getInstance().getW().getPublic_key());
		t.setCreators_signature("creators_signature");	
		t.setTimestamp(System.currentTimeMillis());

		t.setType(Transaction.CREATION_TYPE);

		JsonObject j = new JsonObject();
		if( t.getType().equals(Transaction.CREATION_TYPE)) {
			j.addProperty("name", "name");
			j.addProperty("description", "description");
			j.addProperty("date", Transaction.getDateJson());

		}else if (t.getType().equals(Transaction.PARTICIPATION_TYPE)) {
			//TODO c'est quoi event-hash 
			j.addProperty("event-hash", "event-hash");
			j.addProperty("location", "location");
			j.addProperty("limits", Transaction.getLimitsJson());
		}


		t.setJson(j.toString().getBytes());

		return t;
	}

	@Override
	public void broadcast_transaction(Transaction transaction, List<NodeInfos> contacts) {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			System.out.println("trying to broadcast");

			for(NodeInfos ni : contacts ){
				socket = new Socket(ni.getIpAdress(), ni.getPort());
				out = new PrintWriter(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//TODO mettre les champs qu'il faut dans json
				//System.out.println(((ECPublicKey)transaction.getPublicKey()).getW().getAffineX().bitLength());
				String trans_json = toJson(transaction);
				out.write(trans_json);
				out.flush();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String toJson(Transaction transaction1) {
		Gson gson = new Gson();
		return gson.toJson(transaction1);
	}


	@Override
	public void search_freinds() {
		List<NodeInfos> contacts = getInstance().getContacts();
		Thread t;
		ServerSocket socket;
		try {
			socket = new ServerSocket(2009);

			for( NodeInfos n : contacts ) {
				t = new Thread() {
					public void run() {

					}
				};
				t.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void minage() {
		// TODO Auto-generated method stub
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


}

class AcceptNode implements Runnable {

	private ServerSocket socketserver;
	private Socket socket;
	private int nbrclient = 1;
	public Thread t1;
	public Node instance ;

	public AcceptNode(ServerSocket s, Node instance) {
		socketserver = s;
		this.instance = instance;
	}

	public void run() {
		try {
			while (true) {
				socket = socketserver.accept(); // Un node se connecte on l'accepte
				t1 = new Thread() {
					public void run() {
						try {
							System.out.println("receiving");
							BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String json = in.readLine();
							System.out.println(json);
							Request request = toRequest(json);
							String paquet = request.getPaquet(); 

							if(paquet.equals("hello")) {
								System.out.println("hello");
								/**
								 * enregistrer le contact
								 */
								instance.getContacts().add(new NodeInfos(request.getIpaddress(), request.getPort()));
								System.out.println("new contact"+instance.getContacts().get(0));
								/**
								 * lui répondre "ok"
								 */

								PrintWriter out = new PrintWriter(socket.getOutputStream());
								out.write(make_responce());
							}else if (paquet.equals("transaction")) {
								//TODO add transaction dans broadcast transaction 
								Transaction t = receive_transaction(socket);
								instance.getTransactions().add(t);
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					private String make_responce() {
						final GsonBuilder builder = new GsonBuilder();
						final Gson gson = builder.create();
						Request r = new Request();

						r.setPaquet("ok");
						r.setIpaddress("192.168.1.44");
						r.setPort(2009);
						r.setVersion("1.0");
						if( instance.getContacts().size() >= instance.LIMIT) {
							r.setFlag("saturated");
						}else {
							r.setFlag("good");
						}

						return gson.toJson(r);

					}
				};
				t1.start();
				System.out.println("Le node numéro " + nbrclient + " est connecté !");
				nbrclient++;
				// socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Transaction receive_transaction(Socket s) throws IOException {
		Transaction t = null ;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String json = in.readLine();
			System.out.println("je viens de recevoir une transaction" + json);

			t = toTransaction(json);

			System.out.println("creator_sig : " + t.getPublicKey());
			System.err.println(((ECPublicKey) t.getPublicKey()).getW().getAffineX());
			s.close();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return t;
	}

	private Transaction toTransaction(String json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(json, Transaction.class);
	}

	private static String toJson(String s) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.toJson(s);
	}

	private Request toRequest(String json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(json, Request.class);
	}

	public NodeInfos toNodeInfos(String json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(json, NodeInfos.class);
	}

}
