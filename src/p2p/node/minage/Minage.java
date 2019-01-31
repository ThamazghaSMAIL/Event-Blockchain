package p2p.node.minage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import blockchain.Block;
import p2p.node.Node;
import p2p.node.NodeInfos;
import p2p.node.dispatch.DispatchConversion;
import p2p.protocole.Operation;

public class Minage implements Runnable{

	public Minage() {
		this.time = 60;
		
	}
	public static Node instance = Node.getInstance();
	public int time;
	public int tour ;
	
	@Override
	public void run() {
		int num_node = 1;
		try {
			while (true) {
				tour = getTour();
				TimeUnit.SECONDS.sleep(this.time);
				System.out.println("[Minage] il est temps de miner un bloc ");
				System.out.println("c'est au tour de "+tour + " de miner un bloc ");
				if( tour == instance.getNounce() ) {
					miner();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private void miner() {
		Block new_block;
		new_block = instance.getBlockchain().newBlock();
		broadcast_block(new_block);
	}


	public static void broadcast_block(Block new_block) {
		PrintWriter out = null;
		
		try {
			Socket socket;
			/** Créer l'operation **/
			final GsonBuilder builder = new GsonBuilder();
			final Gson gson = builder.create();
			
			String block_json = Block.toJson(new_block);
			Operation op_block = new Operation("block",
					instance.getMyinformations().getIpAdress(),
					instance.getMyinformations().getPort());
			op_block.setRest(block_json);
			
			String	op_block_json = gson.toJson(op_block);
			System.out.println("je viens de créer un bloc : "+op_block_json);
			byte[] signature = DispatchConversion.signer(op_block_json, instance.getW().getPrivateK());
			
			String bin = DispatchConversion.toBinary(op_block_json, signature);
			
			List<NodeInfos> contacts = instance.getContacts();
			for( NodeInfos ni : contacts ){
				/** Réseau **/
				socket = new Socket(ni.getIpAdress(), ni.getPort());
				out = new PrintWriter(socket.getOutputStream());
				
				out.write(bin);
				out.flush();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getTour() {
		Block latest = instance.getBlockchain().getLatestBlock();
		int latest_tour = latest.getNounce();
		int current_tour = Consensus.calcul_tour(latest_tour);
		return current_tour ;
	}


	public synchronized int getTime() {
		return time;
	}

	public synchronized void setTime(int time) {
		this.time = time;
	}
}
