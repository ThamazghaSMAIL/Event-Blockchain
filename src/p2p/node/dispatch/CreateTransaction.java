package p2p.node.dispatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.bouncycastle.asn1.ocsp.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import blockchain.Transaction;
import p2p.node.Node;
import p2p.node.NodeInfos;
import p2p.node.reception.ReceptionConversion;
import p2p.protocole.Operation;

public class CreateTransaction implements Runnable{

	public CreateTransaction() {
	}


	public static Node instance = Node.getInstance();
	@Override
	public void run() {
		System.out.println("**************************\n"
				+ "Vous pouvez créer une transaction à tout moment en appuyant sur 1");
		int myint = -1;
		try {
			Scanner scanner = new Scanner(System.in);
			try {
				while (true) {
					String line = scanner.nextLine();
					if( line.equals("1")) {
						System.out.println("- Envoi de la transaction");
						String transaction1 = create_transaction();
						broadcast_transaction(transaction1);
						System.out.println("Transaction à envoyer : "+transaction1);
					}
				}
			} catch(IllegalStateException | NoSuchElementException e) {
				System.out.println("System.in was closed; exiting");
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String create_transaction() {
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

		String trans_json = toJson(t);
		return trans_json;
	}

	public void broadcast_transaction(String transaction_json) {
		PrintWriter out = null;
		Socket socket;
		try {
			System.out.println("trying to broadcast : "+instance.getContacts().size() + 
					ReceptionConversion.contactsToString(instance.getContacts()));
			List<NodeInfos> contacts = instance.getContacts();
			for( NodeInfos ni : contacts ){
				System.out.println("ni : "+ni.getIpAdress()+" "+ni.getPort());
				socket = new Socket(ni.getIpAdress(), ni.getPort());
				out = new PrintWriter(socket.getOutputStream());

				final GsonBuilder builder = new GsonBuilder();
				final Gson gson = builder.create();
				String ipadress = instance.getMyinformations().getIpAdress();
				int port = instance.getMyinformations().getPort();
				Operation op_trans = new Operation("transaction",ipadress,port);
				op_trans.setRest(transaction_json);

				String	op_trans_json = gson.toJson(op_trans);
				byte[] signature = DispatchConversion.signer(op_trans_json, instance.getW().getPrivateK());
				String trans = DispatchConversion.toBinary(op_trans_json,signature );
				System.out.println("binaire à envoyer : "+trans);
				out.write(trans);
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

	public static byte[] signer(String trans_json) {
		byte[] signatureBytes = null;
		try {
			byte[] data = trans_json.getBytes("UTF8");
			Signature dsa = Signature.getInstance("SHA1withECDSA");
			dsa.initSign(instance.getW().getPrivateK());
			dsa.update(data);

			signatureBytes = dsa.sign();

			/** verificationo optionnal */
			dsa.initVerify(instance.getW().getPublicK());
			dsa.update(data);
			System.out.println(dsa.verify(signatureBytes));

		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | SignatureException e) {
			e.printStackTrace();
		}
		return signatureBytes;
	}

}
