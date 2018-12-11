package blockchain;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

public class Block {

	public Block(int index, long creation_time, String previous_hash, String data) {
		super();
		this.index = index;
		this.creation_time = creation_time;
		this.previous_hash = previous_hash;
		this.data = data;
		this.hash = Block.calculate_hash(this);
	}

	private String str() {
		return this.index + this.creation_time + this.previous_hash + this.data;
	}
	private static String calculate_hash(Block block) {
		// TODO Auto-generated method stub
		return block.str();
	}

	private int index;

	/**
	 * store creation time of the bloc
	 */
	private long creation_time;

	private String hash ;

	private String previous_hash;

	private String data;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(long creation_time) {
		this.creation_time = creation_time;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPrevious_hash() {
		return previous_hash;
	}

	public void setPrevious_hash(String previous_hash) {
		this.previous_hash = previous_hash;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
