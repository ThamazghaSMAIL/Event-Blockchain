package blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import p2p.node.Node;

public class Blockchain {

	public Blockchain(Node inst) {
		this.blocks = new ArrayList<Block>();
		instance = inst;
	}

	public List<Block> blocks;
	public static Node instance ;

	public Block getLatestBlock() {
		return this.blocks.get(this.blocks.size()-1);
	}

	public Block newBlock() {
		Block new_block = null;
		try {
			Block latestblock = this.getLatestBlock();
			String previous_hash = "";
			new_block = new Block(previous_hash, instance.getTransactions() ,instance.getW().getPublic_key());
			this.blocks.add(new_block);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new_block;
	}

	public void addBlock(Block block) {
		if(block != null) {
			this.blocks.add(block);
		}
	}

//	public boolean genesisBlockValide(Block block){
//		if(block.getIndex() != 0 || block.getPrevious_hash() != null ) {
//			return false;
//		}
//		return true;
//	}
//
//
//	public boolean newBlockValide(Block new_block, Block previous_block) {
//		if(new_block == null || new_block.getPrevious_hash() == null){
//			return false;
//		}
//
//		if( ! new_block.getPrevious_hash().equals(previous_block.getHash())) {
//			return false;
//		}
//
//		return true;
//	}

}
