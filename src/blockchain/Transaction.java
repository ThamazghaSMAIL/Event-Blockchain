package blockchain;

/**
 * dans notre cas trois types de transactions dont :
 * 		-création d'un evt 
 * 		-participer à un evt
 * 		-gestion de clés
 *
 */
public class Transaction {

	public static String CREATION_TYPE = "creation";
	public static String PARTICIPATION_TYPE = "participation";
	public static String GESTIONCLE_TYPE = "gestion cle";
	
	public Transaction(String creators_public_key, String creators_signature, long timestamp, String type,
			byte[] json) {
		this.creators_public_key = creators_public_key;
		this.creators_signature = creators_signature;
		this.timestamp = timestamp;
		this.type = type;
		this.json = json;
	}
	//TODO change to PubKey
	protected String creators_public_key;
	protected String creators_signature;
	protected long timestamp;
	protected String type;
	protected byte[] json;
	
	@Override
	public String toString() {
		return this.creators_public_key+this.creators_signature+this.timestamp+this.type+this.json;
	}

	public byte[] getJson() {
		return this.json;
	}

	public void setJson(byte[] json) {
		this.json = json;
	}
}
