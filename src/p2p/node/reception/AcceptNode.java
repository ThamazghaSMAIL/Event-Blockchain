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
import blockchain.Blockchain;
import blockchain.Transaction;
import p2p.node.Node;
import p2p.node.NodeInfos;
import p2p.node.dispatch.DispatchConversion;
import p2p.node.minage.Minage;
import p2p.node.reception.verification.VerificationManager;
import p2p.protocole.Operation;

public class AcceptNode implements Runnable {

	private ServerSocket socketserver;
	private Socket socket;
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
				System.out.println("**************receiving");
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				(new Thread() {
					public void run() {
						try {
							String bin = in.readLine();

							/** Recuperer l'operation en string */
							Operation request = getOperation(bin);

							String paquet = request.getPaquet(); 
							byte[] signature = ReceptionConversion.getSignature(bin);


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
								receive_transaction(request.getRest(),signature);

							}else if (paquet.equals("ok")) {
								String flag = request.getFlag();
								int upper_nounce = request.getUpper_nounce();
								instance.setNounce(upper_nounce+1);
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
									System.out.println("--> Je demande la blocchain à "+new_node.toString());
									getBlockChain(new_node);
								}

							}else if(paquet.equals("getbc")) {
								sendBlochChain(request.getIpaddress(),request.getPort());
							} else

								if(paquet.equals("blockchain")){
									receive_blockchain(request.getRest());
									System.out.println("--> La version de la blockchain que j'ai reçue :"
											+instance.getBlockchain().toJson());
									/** lancer le processus du minage**/
									if( instance.getNounce() != 1 ) {
										Thread minage = new Thread(new Minage());
										minage.start();
									}

								}else

									if (paquet.equals("block")) {
										System.out.println("j'ai reçu un nouveau block");
										Block new_block = gson.fromJson(request.getRest(),Block.class);
										instance.getBlockchain().addBlock(new_block);
									}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static Operation getOperation(String bin) {
		String size_string = bin.substring(0,32);
		int size_decimal = ReceptionConversion.getSize(size_string);			   
		String json = ReceptionConversion.getTransactionJson(bin,size_decimal);
		String sig_bin = ReceptionConversion.getSignature_bin(bin);
		Operation request = DispatchConversion.toRequest(json);

		return request;
	}


	private void getBlockChain(NodeInfos new_node) {
		PrintWriter out = null;
		String request_get_bc = make_request_get_bc();
		Socket socket;
		try {
			socket = new Socket(new_node.getIpAdress(), new_node.getPort());
			out = new PrintWriter(socket.getOutputStream());
			out.write(request_get_bc);
			out.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private void sendBlochChain(String ipaddress, int port) {
		System.out.println("--> j'envoie ma version de la BC à "
				+ipaddress+" "+port);
		String response_send_bc =  make_response_send_bc();
		PrintWriter out = null;

		Socket socket;
		try {
			socket = new Socket(ipaddress, port);
			out = new PrintWriter(socket.getOutputStream());
			out.write(response_send_bc);
			out.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receive_blockchain(String bc_json) {
		Blockchain blockchain = Blockchain.toBlockChain(bc_json);
		if(blockchain.verify_blockchain())
			instance.setBlockchain(blockchain);
		else 
			System.out.println("blockchain not valid ! ");
	}


	private String make_response_send_bc() {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();

		Operation r = new Operation();
		r.setPaquet("blockchain");
		r.setIpaddress(instance.getMyinformations().getIpAdress());
		r.setPort(instance.getMyinformations().getPort());
		r.setVersion("1.0");
		r.setTime(1);
		//		r.setNode_minage();

		String blockchain_json = instance.getBlockchain().toJson();
		r.setRest(blockchain_json);
		String r_json = gson.toJson(r);
		byte[] signature = DispatchConversion.signer(r_json, instance.getW().getPrivateK());
		String responce_binary = DispatchConversion.toBinary(r_json, signature);
		return responce_binary;
	}

	private String make_request_get_bc() {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		Operation r = new Operation();

		r.setPaquet("getbc");
		r.setIpaddress(instance.getMyinformations().getIpAdress());
		r.setPort(instance.getMyinformations().getPort());
		r.setVersion("1.0");

		String r_json = gson.toJson(r);
		byte[] signature = DispatchConversion.signer(r_json, instance.getW().getPrivateK());
		String responce_binary = DispatchConversion.toBinary(r_json, signature);
		return responce_binary;
	}

	private void response_ok(NodeInfos nodeInfos) {
		PrintWriter out = null;
		String response_ok = make_responce_ok();
		Socket socket;
		try {
			socket = new Socket(nodeInfos.getIpAdress(), nodeInfos.getPort());

			out = new PrintWriter(socket.getOutputStream());
			out.write(response_ok);
			out.flush();
			socket.close();
		} catch (IOException e) {
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
		r.setIpaddress(instance.getMyinformations().getIpAdress());
		r.setPort(instance.getMyinformations().getPort());
		r.setVersion("1.0");
		if( instance.getContacts().size() >= instance.LIMIT) {
			r.setFlag("saturated");
			r.setContacts(gson.toJson(instance.getContacts()));
		}else {
			r.setFlag("good");
		}
		r.setUpper_nounce(instance.getUpper_nounce());
		instance.setUpper_nounce(instance.getUpper_nounce()+1);
		String r_json = gson.toJson(r);
		byte[] signature = DispatchConversion.signer(r_json, instance.getW().getPrivateK());
		String responce_binary = DispatchConversion.toBinary(r_json, signature);
		return responce_binary;
	}

	public void receive_transaction(String trans, byte[] signature) throws IOException {
		Transaction t = null ;
		try {
			System.out.println("--> je viens de recevoir une transaction" + trans);
			t = ReceptionConversion.toTransaction(trans);

			if( ! instance.getTransactions().contains(t) & VerificationManager.verify(trans,signature))
				instance.getTransactions().add(t);
			else
				System.out.println("j'ai reçu une transaction invalide");
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
	}





	private static String toJson(String s) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.toJson(s);
	}




}
