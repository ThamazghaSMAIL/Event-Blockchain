package p2p.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import blockchain.Transaction;
import p2p.protocole.Request;

public class CreateTransaction implements Runnable{

	public CreateTransaction(Node instance) {
		this.instance = instance;
	}

	
	public static Node instance;
	@Override
	public void run() {
		try {
			Scanner keyboard = new Scanner(System.in);
			Transaction transaction1 = create_transaction();
			System.out.println("**************************\n"
					+ "Vous pouvez créer une transaction à tout moment en appuyant sur 1");
			int myint = -1;
			while((myint = keyboard.nextInt()) != 1) {
				System.out.println("choix pas valable");
			}
			broadcast_transaction(transaction1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Transaction create_transaction() {
		//TODO c'est quoi creators_signature ?
		//TODO changer par les données saisies par le user 
		Transaction t = new Transaction();
		t.setCreators_public_key(instance.getW().getPublic_key());
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

	public void broadcast_transaction(Transaction transaction) {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			System.out.println("trying to broadcast"+instance.getContacts().size());
			List<NodeInfos> contacts = instance.getContacts();
			for( NodeInfos ni : contacts ){
				System.out.println("ni : "+ni.getIpAdress()+" "+ni.getPort());
				socket = new Socket(ni.getIpAdress(), ni.getPort());
				out = new PrintWriter(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String trans_json = toJson(transaction);
				Request req_trans = new Request("transaction","myipaddress",2009);
				req_trans.setRest(trans_json);
				
				out.write((new Gson()).toJson(req_trans));
				System.out.println("h");
				out.flush();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static String toJson(Transaction transaction1) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.toJson(transaction1);
	}

}
