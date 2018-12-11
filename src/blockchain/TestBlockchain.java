package blockchain;

import java.util.List;

public class TestBlockchain {

	public static void main(String[] args) {
		List<Block> blocks = null;
		Blockchain blockchain = new Blockchain(blocks);
		System.out.println(blockchain.getLatestBlock().getIndex());
	}

}
