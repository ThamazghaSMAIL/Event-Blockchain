package p2p.node.reception;

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
import p2p.node.Node;
import p2p.node.NodeInfos;
import p2p.node.dispatch.DispatchConversion;
import p2p.node.minage.Minage;
import p2p.protocole.Operation;

public class AcceptNode implements Runnable {

	private ServerSocket socketserver;
	private Socket socket;
	private int nbrclient = 1;
	public Thread t1;
	public static Node instance = Node.getInstance();

	public AcceptNode(ServerSocket s) {
		socketserver = s;
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
							System.out.println("**************receiving");
							BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String bin = in.readLine();
							System.out.println("binaire reçu : "+bin);

							/** Recuperer l'operation en string */
							ReceptionConversion.operationToString(bin); 
							String json = ReceptionConversion.json;
							String signature = ReceptionConversion.signature;

							Operation request = toRequest(json);
							String paquet = request.getPaquet(); 

							if(paquet.equals("hello")) {
								/**
								 * lui répondre "ok"
								 */
								response_ok(new NodeInfos(request.getIpaddress(), request.getPort()));
								
								NodeInfos new_node = new NodeInfos(request.getIpaddress(), request.getPort());
								System.out.println("--> Je rajoute ce noeud à mes contacts : "+new_node.toString());
								addContact(request.getIpaddress(), request.getPort());
								System.out.println("--> contacts :[ "+ReceptionConversion.contactsToString(instance.getContacts()));
							}else if (paquet.equals("transaction")) {
								receive_transaction(request.getRest());

							}else if (paquet.equals("ok")) {
								String flag = request.getFlag();
								if(flag.equals("saturated")) {
									System.out.println("okay i'll search another freinds");
									List<NodeInfos> list = gson.fromJson(request.getContacts(), 
											new TypeToken<List<NodeInfos>>(){}.getType());
									instance.search_freinds(list);
								}else if(flag.equals("good")) {
									NodeInfos new_node = new NodeInfos(request.getIpaddress(), request.getPort());
									System.out.println("--> Je rajoute ce noeud à mes contacts : "+new_node.toString());
									addContact(request.getIpaddress(), request.getPort());
									System.out.println("--> contacts :[ "+ReceptionConversion.contactsToString(instance.getContacts()));
								}

							}else if (paquet.equals("block")) {
								System.out.println("j'ai reçu un nouveau block");
								Block new_block = gson.fromJson(request.getRest(),Block.class);
								instance.getBlockchain().addBlock(new_block);
								Minage.broadcast_block(new_block);
							}
							/**
							 * enregistrer le contact
							 */
							//addContact(request.getIpaddress(), request.getPort());

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

	private void response_ok(NodeInfos nodeInfos) {
		PrintWriter out = null;
		System.out.println("**********");
		String response_ok = make_responce_ok();
		System.out.println("- Reponse à 'hello' "+response_ok);
		
		Socket socket;
		try {
			socket = new Socket(nodeInfos.getIpAdress(), nodeInfos.getPort());

			out = new PrintWriter(socket.getOutputStream());
			out.write(response_ok);
			out.flush();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private void addContact(String ipaddress, int port) {
		NodeInfos ni = new NodeInfos(ipaddress, port);
		if( ! instance.getContacts().contains(ni))
			instance.getContacts().add(ni);
	}

	private String make_responce_ok() {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		Operation r = new Operation();



		r.setPaquet("ok");
		r.setIpaddress("localhost");
		r.setPort(2009);
		r.setVersion("1.0");
		if( instance.getContacts().size() >= instance.LIMIT) {
			r.setFlag("saturated");
			r.setContacts(gson.toJson(instance.getContacts()));
		}else {
			r.setFlag("good");
		}
		String r_json = gson.toJson(r);
		byte[] signature = DispatchConversion.signer(r_json, instance.getW().getPrivateK());
		String responce_binary = DispatchConversion.toBinary(r_json, signature);
		return responce_binary;
	}

	public void receive_transaction(String trans) throws IOException {
		Transaction t = null ;
		try {
			System.out.println("--> je viens de recevoir une transaction" + trans);
			t = toTransaction(trans);
			if( ! instance.getTransactions().contains(t) )
				instance.getTransactions().add(t);


			System.out.println("public key : " + t.getPublicKey());
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

	private Operation toRequest(String json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(json, Operation.class);
	}

	public NodeInfos toNodeInfos(String json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(json, NodeInfos.class);
	}


}
