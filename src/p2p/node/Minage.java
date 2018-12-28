package p2p.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;

import blockchain.Block;
import blockchain.Transaction;
import p2p.protocole.Request;

public class Minage implements Runnable{

	public Minage(Node instance) {
		super();
		this.instance = instance;
	}
	public Node instance;


	@Override
	public void run() {
		//TODO remplacer par l'id u node choisi pour le minage
		//TODO initialiser id
		int tour = 0;
		if( tour == instance.getId())
			miner();
	}


	private void miner() {
		if( instance.getTransactions().size() > 0 ) {
			Block new_block = instance.getBlockchain().newBlock();
			broadcast_block(new_block);
		}
	}


	private void broadcast_block(Block new_block) {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			System.out.println("trying to broadcast "+instance.getContacts().size()+"nodes");
			List<NodeInfos> contacts = instance.getContacts();
			for( NodeInfos ni : contacts ){
				System.out.println("ni : "+ni.getIpAdress()+" "+ni.getPort());
				socket = new Socket(ni.getIpAdress(), ni.getPort());
				out = new PrintWriter(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String block_json = Block.toJson(new_block);
				Request req_block = new Request("newblock","myipaddress",2009);
				req_block.setRest(block_json);
				out.write((new Gson()).toJson(req_block));
				out.flush();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
