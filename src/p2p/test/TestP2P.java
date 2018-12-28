package p2p.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import blockchain.Transaction;
import p2p.node.Node;
import p2p.node.NodeInfos;
import p2p.node.Wallet;

public class TestP2P {

	public static void main(String[] args) {

		InetAddress localhost;
		try {
			localhost = InetAddress.getLocalHost();
			System.out.println("System IP Address : " + (localhost.getHostAddress()).trim()); 
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} 




		Date date = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String today = formatter.format(date);
		System.out.println("Today : " + today);
		System.out.println("format : "+formatter);
		Node node1 = Node.getInstance();

		(new Thread() {
			public void run() {
				try {
					node1.getContacts().add(new NodeInfos("127.0.0.1", 8080));
					System.out.println("in thread ");
					System.out.println("yaas : "+node1.getContacts().get(1).getIpAdress());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		System.out.println("that's work\nAdress : "+node1.getContacts().get(0).getIpAdress()+" port : "
				+node1.getContacts().get(0).getPort());



		Wallet w = new Wallet();
		String json = "hihi";
		Transaction transaction = new Transaction(w.getPublic_key(), 
				"creators_signature", System.currentTimeMillis(), 
				Transaction.CREATION_TYPE , json.getBytes());
		//node2.broadcast_transaction(transaction);
	}
}
