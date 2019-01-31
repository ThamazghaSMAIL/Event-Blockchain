package blockchain;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import blockchain.persistance.Persistance;
import p2p.node.Node;
import p2p.protocole.Operation;

public class Blockchain implements Serializable{

	private static final long serialVersionUID = 1L;

	public Blockchain() {
		this.blocks = new ArrayList<Block>();
	}

	public List<Block> blocks;
	public static Node instance = Node.getInstance();

	public Block getLatestBlock() {
		System.out.println();
		return this.blocks.get(this.blocks.size()-1);
	}

	public Block newBlock() {
		Block new_block = null;
		try {
			String previous_hash = this.getLatestBlock().getHash();
			int level = this.getLatestBlock().getLevel() + 1 ;
			new_block = new Block(previous_hash, 
					instance.getTransactions() ,
					instance.getW().getPublic_key(), 
					instance.getNounce(),level);

			this.blocks.add(new_block);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new_block;
	}

	public void addBlock(Block block) {
		if(block != null && ! this.blocks.contains(block)) {
			if( block.verify_block(this.getLatestBlock() )) {
				this.blocks.add(block);
			}else {
				System.out.println("[block] bloc invalide");
			}
		}else {
			System.out.println("[block] j'ai deja ce bloc");
		}

		Persistance.WriteBlockChainToFile(this);
	}

	public String toJson(){
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		String r_json = gson.toJson(this);
		return r_json;
	}

	public static void main(String[] args) {
		Blockchain bc = new Blockchain();

		try {
			List<Transaction> transactions = new ArrayList<Transaction>();
			Transaction t = new Transaction( Node.getInstance().getW().getPublic_key(),
					System.currentTimeMillis(),
					Transaction.CREATION_TYPE,
					"json".getBytes());

			transactions.add(t);
			bc.addBlock(new Block(null, transactions, Node.getInstance().getW().getPublic_key(), Node.getInstance().getNounce(),0));

			System.out.println(bc.toJson());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public boolean genesisBlockValide(Block block){
		if(block.getLevel() != 0 || block.getPrevious_hash() != null ) {
			return false;
		}
		return true;
	}


	public boolean newBlockValide(Block new_block, Block previous_block) {
		if(new_block == null || new_block.getPrevious_hash() == null){
			return false;
		}

		if( ! new_block.getPrevious_hash().equals(previous_block.getHash())) {
			return false;
		}

		return true;
	}
	
	public boolean verify_blockchain() {
		List<Block> blocks =this.getBlocks();
		if( genesisBlockValide(this.getBlocks().get(0)))
			for( int i = 0; i< blocks.size() ; i++ ) {
				if( blocks.get(i).getPrevious_hash() != null )
					if( blocks.get(i).getPrevious_hash().equals(blocks.get(i-1).getHash()))
						return false;
			}
		return true;
	}

	public static Blockchain toBlockChain(String bc_json) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.fromJson(bc_json, Blockchain.class);
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

}
