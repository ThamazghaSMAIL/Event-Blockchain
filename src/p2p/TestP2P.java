package p2p;

import blockchain.Transaction;

public class TestP2P {

	public static void main(String[] args) {
		Node node1 = new Node(null);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Node node2 = new Node(null);
		Wallet w = new Wallet();
		String json = "hihi";
		Transaction transaction = new Transaction(w.getPublic_key(), 
				"creators_signature", System.currentTimeMillis(), 
				Transaction.CREATION_TYPE , json.getBytes());
		//node2.broadcast_transaction(transaction);
	}
}
