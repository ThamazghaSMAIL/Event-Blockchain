package p2p.node.minage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import blockchain.Block;
import p2p.node.Node;
import p2p.node.NodeInfos;
import p2p.protocole.Operation;

public class Minage implements Runnable{

	public Minage() {
	}
	public static Node instance = Node.getInstance();


	@Override
	public void run() {
		int num_node = 1;

		try {
			while (true) {
				TimeUnit.SECONDS.sleep(1000);
				System.out.println("[Minage] il est temps de miner un bloc ");
				int tour = Consensus.hmac_sha256(num_node);
				

				System.out.println("c'est au tour de "+tour + " de miner un bloc ");
				if( tour == instance.getId()) {
					miner();
				}
				num_node = (num_node+1)%Consensus.NODES_NUMBER;
				
//				TimeUnit.MINUTES.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void miner() {
		if( instance.getTransactions().size() > 0 ) {
			Block new_block = instance.getBlockchain().newBlock();
			broadcast_block(new_block);
		}else {
			System.out.println("Sorry ! there's no transaction in your list");
		}
	}


	public static void broadcast_block(Block new_block) {
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
				Operation req_block = new Operation("newblock","myipaddress",2009);
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
