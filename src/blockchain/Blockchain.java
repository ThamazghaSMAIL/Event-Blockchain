package blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

	
//	public Blockchain() {
//		this.blocks = new ArrayList<Block>();
//		
//		
//		List<Transaction> trans = new ArrayList<Transaction>();
//		Block genesis_block = new Block(null,trans,null);
//		this.blocks.add(genesis_block);
//	}
//
//	List<Block> blocks;
//	
//	public Block getLatestBlock() {
//		return this.blocks.get(this.blocks.size()-1);
//	}
//	
//	public Block newBlock(String data) {
//		Block latestblock = this.getLatestBlock();
//		return new Block(latestblock.getIndex()+1,System.currentTimeMillis(),
//				latestblock.getHash(),data);
//	}
//	
//	public void addBlock(Block block) {
//		if(block != null) {
//			this.blocks.add(block);
//		}
//	}
//	
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
