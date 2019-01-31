package blockchain;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * dans notre cas trois types de transactions dont :
 * 		-création d'un evt 
 * 		-participer à un evt
 * 		-gestion de clés
 *
 */
public class Transaction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String CREATION_TYPE = "creation";
	public static String PARTICIPATION_TYPE = "participation";
	public static String GESTIONCLE_TYPE = "gestion_cle";
	
	public Transaction() {}
	
	
	public Transaction(byte[] creators_public_key, long timestamp, String type,
			byte[] json) {
		this.creators_public_key = creators_public_key;
		this.timestamp = timestamp;
		this.type = type;
	}
	
	protected byte[] creators_public_key;
	protected long timestamp;
	protected String type;
	protected byte[] json ;
	
	

	@Override
	public String toString() {
		return "Transaction [creators_public_key=" + Arrays.toString(creators_public_key) + ", creators_signature="
				+ ", timestamp=" + timestamp + ", type=" + type + "]";
	}

	public byte[] getCreators_public_key() {
		return creators_public_key;
	}

	public void setCreators_public_key(byte[] creators_public_key) {
		this.creators_public_key = creators_public_key;
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
	
	public PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException
	{
	    KeyFactory factory;
		factory = KeyFactory.getInstance("EC");
		X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(this.creators_public_key);
		return factory.generatePublic(encodedKeySpec);
	}
	
	public static String getLimitsJson() {
		JsonObject jlimits = new JsonObject();
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		
		jlimits.addProperty("min", "");
		jlimits.addProperty("max", "");
		
		return jlimits.toString();
	}



	public static String getDateJson() {
		JsonObject jdate = new JsonObject();
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		Date date = Calendar.getInstance().getTime();

		// Display a date in day, month, year format
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String today = formatter.format(date);
		
		jdate.addProperty("date", today);
		jdate.addProperty("format", "dd/MM/yyyy");
		
		return jdate.toString();
	}


	public byte[] getJson() {
		return json;
	}


	public void setJson(byte[] json) {
		this.json = json;
	}
	
}
