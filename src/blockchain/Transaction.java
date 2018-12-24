package blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;

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
	
	public Transaction() {}
	
	
	public Transaction(PublicKey creators_public_key, String creators_signature, long timestamp, String type,
			byte[] json) {
		this.creators_public_key = creators_public_key;
		this.creators_signature = creators_signature;
		this.timestamp = timestamp;
		this.type = type;
		this.json = json;
	}
	
	
	protected PublicKey creators_public_key;
	protected String creators_signature;
	protected long timestamp;
	protected String type;
	protected byte[] json;
	
	@Override
	public String toString() {
		return this.creators_public_key.toString()+this.creators_signature+this.timestamp+this.type+this.json;
	}

	public byte[] getJson() {
		return this.json;
	}

	public void setJson(byte[] json) {
		this.json = json;
	}

	public static String getCREATION_TYPE() {
		return CREATION_TYPE;
	}

	public static void setCREATION_TYPE(String cREATION_TYPE) {
		CREATION_TYPE = cREATION_TYPE;
	}

	public static String getPARTICIPATION_TYPE() {
		return PARTICIPATION_TYPE;
	}

	public static void setPARTICIPATION_TYPE(String pARTICIPATION_TYPE) {
		PARTICIPATION_TYPE = pARTICIPATION_TYPE;
	}

	public static String getGESTIONCLE_TYPE() {
		return GESTIONCLE_TYPE;
	}

	public static void setGESTIONCLE_TYPE(String gESTIONCLE_TYPE) {
		GESTIONCLE_TYPE = gESTIONCLE_TYPE;
	}

	public PublicKey getCreators_public_key() {
		return creators_public_key;
	}

	public void setCreators_public_key(PublicKey creators_public_key) {
		this.creators_public_key = creators_public_key;
	}

	public String getCreators_signature() {
		return creators_signature;
	}

	public void setCreators_signature(String creators_signature) {
		this.creators_signature = creators_signature;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
