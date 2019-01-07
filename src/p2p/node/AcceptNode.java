package p2p.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import blockchain.Block;
import blockchain.Transaction;
import p2p.protocole.Request;

public class AcceptNode implements Runnable {

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
				final GsonBuilder builder = new GsonBuilder();
				final Gson gson = builder.create();
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
								System.out.println("new contact"+instance.getContacts().get(0));
								/**
								 * lui répondre "ok"
								 */

								PrintWriter out = new PrintWriter(socket.getOutputStream());
								out.write(make_responce());
							}else if (paquet.equals("transaction")) {
								receive_transaction(request.getRest());

							}else if (paquet.equals("ok")) {
								System.out.println("okay i'll search another freinds");
								List<NodeInfos> list = gson.fromJson(request.getContacts(), 
										new TypeToken<List<NodeInfos>>(){}.getType());
								instance.search_freinds(list);
							}else if (paquet.equals("ok")) {
								System.out.println("j'ai reçu un nouveau block");
								Block new_block = gson.fromJson(request.getRest(),Block.class);
								instance.getBlockchain().addBlock(new_block);
								Minage.broadcast_block(new_block);
							}
							/**
							 * enregistrer le contact
							 */
							addContact(request.getIpaddress(), request.getPort());

						} catch (IOException e) {
							e.printStackTrace();
						}
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

	private void addContact(String ipaddress, int port) {
		NodeInfos ni = new NodeInfos(ipaddress, port);
		if( ! instance.getContacts().contains(ni))
			instance.getContacts().add(ni);
	}

	private String make_responce() {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		Request r = new Request();

		r.setPaquet("ok");
		//TODO replace with ip adress of machine getip..
		r.setIpaddress("192.168.1.44");
		r.setPort(2009);
		r.setVersion("1.0");
		if( instance.getContacts().size() >= instance.LIMIT) {
			r.setFlag("saturated");
			r.setContacts(gson.toJson(instance.getContacts()));
		}else {
			r.setFlag("good");
		}

		return gson.toJson(r);

	}

	public void receive_transaction(String trans) throws IOException {
		Transaction t = null ;
		try {
			System.out.println("je viens de recevoir une transaction" + trans);
			t = toTransaction(trans);
			if( ! instance.getTransactions().contains(t) )
				instance.getTransactions().add(t);


			System.out.println("creator_sig : " + t.getPublicKey());
			System.err.println(((ECPublicKey) t.getPublicKey()).getW().getAffineX());
		} catch (JsonSyntaxException  | NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
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
