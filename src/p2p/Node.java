package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import blockchain.Transaction;

public class Node {
	/**
	 * Les adresses des noeuds que ce noeud connait préalablement
	 */
	List<NodeInfos> contacts;

	public Node(List<NodeInfos> contacts) {
		this.contacts = contacts;
		// this.contacts.add(new NodeInfos("192.168.1.54", 2009));
	}

	public static void main(String[] args) {
		//Wallet w = new Wallet();
		try {
//			String jsonStr = "{\"creators_public_key\":{\"w\":{\"x\":78649421698933646191900224114266286575873751204767990094939210844518474925562,\"y\":63083437692842313511151106572703147327134487824414857947347528395597583446748},\"params\":{\"name\":\"secp256r1 [NIST P-256, X9.62 prime256v1]\",\"oid\":\"1.2.840.10045.3.1.7\",\"encoded\":[6,8,42,-122,72,-50,61,3,1,7],\"curve\":{\"field\":{\"p\":115792089210356248762697446949407573530086143415290314195533631308867097853951},\"a\":115792089210356248762697446949407573530086143415290314195533631308867097853948,\"b\":41058363725152142129326129780047268409114441015993725554835256314039467401291},\"g\":{\"x\":48439561293906451759052585252797914202762949526041747995844080717082404635286,\"y\":36134250956749795798585127919587881956611106672985015071877198253568414405109},\"n\":115792089210356248762697446949407573529996955224135760342422259061068512044369,\"h\":1},\"algid\":{\"algid\":{\"encoding\":[42,-122,72,-50,61,2,1],\"componentLen\":-1},\"algParams\":{\"provider\":{\"AlgorithmParameters.EC\":\"sun.security.ec.ECParameters\",\"KeyAgreement.ECDH SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Signature.SHA256withECDSA ImplementedIn\":\"Software\",\"Provider.id name\":\"SunEC\",\"Signature.NONEwithECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Signature.SHA224withECDSA ImplementedIn\":\"Software\",\"Signature.SHA1withECDSA\":\"sun.security.ec.ECDSASignature$SHA1\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.1\":\"SHA1withECDSA\",\"Signature.SHA256withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Signature.SHA224withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"KeyPairGenerator.EC KeySize\":\"256\",\"KeyFactory.EC ImplementedIn\":\"Software\",\"Provider.id version\":\"1.8\",\"AlgorithmParameters.EC KeySize\":\"256\",\"Signature.NONEwithECDSA\":\"sun.security.ec.ECDSASignature$Raw\",\"Signature.SHA512withECDSA ImplementedIn\":\"Software\",\"Alg.Alias.KeyFactory.EllipticCurve\":\"EC\",\"Alg.Alias.KeyPairGenerator.EllipticCurve\":\"EC\",\"Signature.SHA256withECDSA\":\"sun.security.ec.ECDSASignature$SHA256\",\"Signature.SHA512withECDSA\":\"sun.security.ec.ECDSASignature$SHA512\",\"Signature.SHA1withECDSA KeySize\":\"256\",\"Signature.SHA1withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Signature.SHA384withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Alg.Alias.AlgorithmParameters.EllipticCurve\":\"EC\",\"Alg.Alias.AlgorithmParameters.1.2.840.10045.2.1\":\"EC\",\"Alg.Alias.Signature.1.2.840.10045.4.1\":\"SHA1withECDSA\",\"Signature.SHA224withECDSA\":\"sun.security.ec.ECDSASignature$SHA224\",\"Signature.SHA384withECDSA ImplementedIn\":\"Software\",\"AlgorithmParameters.EC ImplementedIn\":\"Software\",\"Provider.id info\":\"Sun Elliptic Curve provider (EC, ECDSA, ECDH)\",\"Signature.SHA512withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"KeyPairGenerator.EC\":\"sun.security.ec.ECKeyPairGenerator\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.3.4\":\"SHA512withECDSA\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.3.3\":\"SHA384withECDSA\",\"KeyAgreement.ECDH\":\"sun.security.ec.ECDHKeyAgreement\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.3.2\":\"SHA256withECDSA\",\"Alg.Alias.Signature.1.2.840.10045.4.3.4\":\"SHA512withECDSA\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.3.1\":\"SHA224withECDSA\",\"Signature.SHA384withECDSA\":\"sun.security.ec.ECDSASignature$SHA384\",\"Alg.Alias.Signature.1.2.840.10045.4.3.3\":\"SHA384withECDSA\",\"Alg.Alias.Signature.1.2.840.10045.4.3.2\":\"SHA256withECDSA\",\"Alg.Alias.Signature.1.2.840.10045.4.3.1\":\"SHA224withECDSA\",\"Signature.SHA1withECDSA ImplementedIn\":\"Software\",\"Signature.NONEwithECDSA ImplementedIn\":\"Software\",\"Provider.id className\":\"sun.security.ec.SunEC\",\"AlgorithmParameters.EC SupportedCurves\":\"[secp112r1,1.3.132.0.6]|[secp112r2,1.3.132.0.7]|[secp128r1,1.3.132.0.28]|[secp128r2,1.3.132.0.29]|[secp160k1,1.3.132.0.9]|[secp160r1,1.3.132.0.8]|[secp160r2,1.3.132.0.30]|[secp192k1,1.3.132.0.31]|[secp192r1,NIST P-192,X9.62 prime192v1,1.2.840.10045.3.1.1]|[secp224k1,1.3.132.0.32]|[secp224r1,NIST P-224,1.3.132.0.33]|[secp256k1,1.3.132.0.10]|[secp256r1,NIST P-256,X9.62 prime256v1,1.2.840.10045.3.1.7]|[secp384r1,NIST P-384,1.3.132.0.34]|[secp521r1,NIST P-521,1.3.132.0.35]|[X9.62 prime192v2,1.2.840.10045.3.1.2]|[X9.62 prime192v3,1.2.840.10045.3.1.3]|[X9.62 prime239v1,1.2.840.10045.3.1.4]|[X9.62 prime239v2,1.2.840.10045.3.1.5]|[X9.62 prime239v3,1.2.840.10045.3.1.6]|[sect113r1,1.3.132.0.4]|[sect113r2,1.3.132.0.5]|[sect131r1,1.3.132.0.22]|[sect131r2,1.3.132.0.23]|[sect163k1,NIST K-163,1.3.132.0.1]|[sect163r1,1.3.132.0.2]|[sect163r2,NIST B-163,1.3.132.0.15]|[sect193r1,1.3.132.0.24]|[sect193r2,1.3.132.0.25]|[sect233k1,NIST K-233,1.3.132.0.26]|[sect233r1,NIST B-233,1.3.132.0.27]|[sect239k1,1.3.132.0.3]|[sect283k1,NIST K-283,1.3.132.0.16]|[sect283r1,NIST B-283,1.3.132.0.17]|[sect409k1,NIST K-409,1.3.132.0.36]|[sect409r1,NIST B-409,1.3.132.0.37]|[sect571k1,NIST K-571,1.3.132.0.38]|[sect571r1,NIST B-571,1.3.132.0.39]|[X9.62 c2tnb191v1,1.2.840.10045.3.0.5]|[X9.62 c2tnb191v2,1.2.840.10045.3.0.6]|[X9.62 c2tnb191v3,1.2.840.10045.3.0.7]|[X9.62 c2tnb239v1,1.2.840.10045.3.0.11]|[X9.62 c2tnb239v2,1.2.840.10045.3.0.12]|[X9.62 c2tnb239v3,1.2.840.10045.3.0.13]|[X9.62 c2tnb359v1,1.2.840.10045.3.0.18]|[X9.62 c2tnb431r1,1.2.840.10045.3.0.20]|[brainpoolP160r1,1.3.36.3.3.2.8.1.1.1]|[brainpoolP192r1,1.3.36.3.3.2.8.1.1.3]|[brainpoolP224r1,1.3.36.3.3.2.8.1.1.5]|[brainpoolP256r1,1.3.36.3.3.2.8.1.1.7]|[brainpoolP320r1,1.3.36.3.3.2.8.1.1.9]|[brainpoolP384r1,1.3.36.3.3.2.8.1.1.11]|[brainpoolP512r1,1.3.36.3.3.2.8.1.1.13]\",\"KeyPairGenerator.EC ImplementedIn\":\"Software\",\"KeyAgreement.ECDH ImplementedIn\":\"Software\",\"KeyFactory.EC\":\"sun.security.ec.ECKeyFactory\"},\"paramSpi\":{\"namedCurve\":{\"name\":\"secp256r1 [NIST P-256, X9.62 prime256v1]\",\"oid\":\"1.2.840.10045.3.1.7\",\"encoded\":[6,8,42,-122,72,-50,61,3,1,7],\"curve\":{\"field\":{\"p\":115792089210356248762697446949407573530086143415290314195533631308867097853951},\"a\":115792089210356248762697446949407573530086143415290314195533631308867097853948,\"b\":41058363725152142129326129780047268409114441015993725554835256314039467401291},\"g\":{\"x\":48439561293906451759052585252797914202762949526041747995844080717082404635286,\"y\":36134250956749795798585127919587881956611106672985015071877198253568414405109},\"n\":115792089210356248762697446949407573529996955224135760342422259061068512044369,\"h\":1}},\"algorithm\":\"EC\",\"initialized\":true},\"constructedFromDer\":false},\"key\":[4,-83,-31,-2,-61,35,14,31,54,-126,16,51,-66,-100,117,38,-17,84,119,-89,-83,20,-99,27,1,15,-27,19,24,99,21,69,-6,-117,119,-11,-34,-9,-51,40,-7,-42,51,-13,67,-82,-21,116,-89,39,126,15,-29,-16,64,-121,62,105,-18,37,92,-82,67,114,-36],\"unusedBits\":0},\"creators_signature\":\"creators_signature\",\"timestamp\":1545760544296,\"type\":\"creation\",\"json\":[104,105,104,105]}\n";
//			final GsonBuilder builder = new GsonBuilder();
//			final Gson gson = builder.create();
//			Transaction t = gson.fromJson(jsonStr, Transaction.class);
//			System.out.println(t.toString());
		
			
		} catch (Exception e  ) {
			e.printStackTrace();
		}

		 List<NodeInfos> contacts = new ArrayList<NodeInfos>();
		 Node n = new Node(contacts);
		
		 Scanner keyboard = new Scanner(System.in);
		
		 System.out.println("**************************");
		 System.out.println("enter at least a contact\n ip adress : ");
		 String ipadress = keyboard.nextLine();
		
		 System.out.println("port :");
		 int port = keyboard.nextInt();
		 n.getContacts().add(new NodeInfos(ipadress, port));
		
		 System.out.println("***************************");
		 /**
		 * se mettre à l'écoute
		 */
		 (new Thread(){
		 public void run(){
		 ServerSocket socket;
		
		 try {
		 socket = new ServerSocket(2009);
		 Thread server = new Thread(new AcceptNode(socket));
		 server.start();
		 System.out.println(" Le serveur est prêt !\n");
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
		 }
		 }).start();
		
		
		 /**
		 * create transaction
		 */
		 Wallet w = new Wallet();
		 String json ="hihi";
		 //TODO c'est quoi creators_signature ?
		 Transaction transaction1 = new Transaction(w.getPublic_key(),
		 "creators_signature", System.currentTimeMillis(),
		 Transaction.CREATION_TYPE , json.getBytes());
		
		 //Scanner keyboard = new Scanner(System.in);
		 System.out.println("**************************\n"
		 + "1- Créer une transaction");
		 System.out.println("enter an integer");
		 int myint = keyboard.nextInt();
		
		
		 if( myint == 1 ) {
		 broadcast_transaction(transaction1,contacts);
		 }else {
		 System.out.println("choix pas valable");
		 }
	}

	public static void broadcast_transaction(Transaction transaction, List<NodeInfos> contacts) {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			NodeInfos ni = contacts.get(0);
			socket = new Socket(ni.getIpAdress(), ni.getPort());
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			Wallet w = new Wallet();
			String json = "hihi";

			String trans_json = toJson(transaction);

			out.write(trans_json);
			out.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String toJson(Transaction transaction1) {
		Gson gson = new Gson();
		return gson.toJson(transaction1);
	}

	public List<NodeInfos> getContacts() {
		return contacts;
	}

	public void setContacts(List<NodeInfos> contacts) {
		this.contacts = contacts;
	}
}

class AcceptNode implements Runnable {

	private ServerSocket socketserver;
	private Socket socket;
	private int nbrclient = 1;
	public Thread t1;

	public AcceptNode(ServerSocket s) {
		socketserver = s;
	}

	public void run() {

		try {
			while (true) {
				socket = socketserver.accept(); // Un node se connecte on l'accepte
				t1 = new Thread() {
					public void run() {
						try {
							receive_transaction(socket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				t1.start();
				System.out.println("Le client numéro " + nbrclient + " est connecté !");
				nbrclient++;
				// socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receive_transaction(Socket s) throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String json = in.readLine();
		System.out.println("je viens de recevoir une transaction" + json);

		String jsonStr = "{\"creators_public_key\":{\"w\":{\"x\":78649421698933646191900224114266286575873751204767990094939210844518474925562,\"y\":63083437692842313511151106572703147327134487824414857947347528395597583446748},\"params\":{\"name\":\"secp256r1 [NIST P-256, X9.62 prime256v1]\",\"oid\":\"1.2.840.10045.3.1.7\",\"encoded\":[6,8,42,-122,72,-50,61,3,1,7],\"curve\":{\"field\":{\"p\":115792089210356248762697446949407573530086143415290314195533631308867097853951},\"a\":115792089210356248762697446949407573530086143415290314195533631308867097853948,\"b\":41058363725152142129326129780047268409114441015993725554835256314039467401291},\"g\":{\"x\":48439561293906451759052585252797914202762949526041747995844080717082404635286,\"y\":36134250956749795798585127919587881956611106672985015071877198253568414405109},\"n\":115792089210356248762697446949407573529996955224135760342422259061068512044369,\"h\":1},\"algid\":{\"algid\":{\"encoding\":[42,-122,72,-50,61,2,1],\"componentLen\":-1},\"algParams\":{\"provider\":{\"AlgorithmParameters.EC\":\"sun.security.ec.ECParameters\",\"KeyAgreement.ECDH SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Signature.SHA256withECDSA ImplementedIn\":\"Software\",\"Provider.id name\":\"SunEC\",\"Signature.NONEwithECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Signature.SHA224withECDSA ImplementedIn\":\"Software\",\"Signature.SHA1withECDSA\":\"sun.security.ec.ECDSASignature$SHA1\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.1\":\"SHA1withECDSA\",\"Signature.SHA256withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Signature.SHA224withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"KeyPairGenerator.EC KeySize\":\"256\",\"KeyFactory.EC ImplementedIn\":\"Software\",\"Provider.id version\":\"1.8\",\"AlgorithmParameters.EC KeySize\":\"256\",\"Signature.NONEwithECDSA\":\"sun.security.ec.ECDSASignature$Raw\",\"Signature.SHA512withECDSA ImplementedIn\":\"Software\",\"Alg.Alias.KeyFactory.EllipticCurve\":\"EC\",\"Alg.Alias.KeyPairGenerator.EllipticCurve\":\"EC\",\"Signature.SHA256withECDSA\":\"sun.security.ec.ECDSASignature$SHA256\",\"Signature.SHA512withECDSA\":\"sun.security.ec.ECDSASignature$SHA512\",\"Signature.SHA1withECDSA KeySize\":\"256\",\"Signature.SHA1withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Signature.SHA384withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"Alg.Alias.AlgorithmParameters.EllipticCurve\":\"EC\",\"Alg.Alias.AlgorithmParameters.1.2.840.10045.2.1\":\"EC\",\"Alg.Alias.Signature.1.2.840.10045.4.1\":\"SHA1withECDSA\",\"Signature.SHA224withECDSA\":\"sun.security.ec.ECDSASignature$SHA224\",\"Signature.SHA384withECDSA ImplementedIn\":\"Software\",\"AlgorithmParameters.EC ImplementedIn\":\"Software\",\"Provider.id info\":\"Sun Elliptic Curve provider (EC, ECDSA, ECDH)\",\"Signature.SHA512withECDSA SupportedKeyClasses\":\"java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey\",\"KeyPairGenerator.EC\":\"sun.security.ec.ECKeyPairGenerator\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.3.4\":\"SHA512withECDSA\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.3.3\":\"SHA384withECDSA\",\"KeyAgreement.ECDH\":\"sun.security.ec.ECDHKeyAgreement\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.3.2\":\"SHA256withECDSA\",\"Alg.Alias.Signature.1.2.840.10045.4.3.4\":\"SHA512withECDSA\",\"Alg.Alias.Signature.OID.1.2.840.10045.4.3.1\":\"SHA224withECDSA\",\"Signature.SHA384withECDSA\":\"sun.security.ec.ECDSASignature$SHA384\",\"Alg.Alias.Signature.1.2.840.10045.4.3.3\":\"SHA384withECDSA\",\"Alg.Alias.Signature.1.2.840.10045.4.3.2\":\"SHA256withECDSA\",\"Alg.Alias.Signature.1.2.840.10045.4.3.1\":\"SHA224withECDSA\",\"Signature.SHA1withECDSA ImplementedIn\":\"Software\",\"Signature.NONEwithECDSA ImplementedIn\":\"Software\",\"Provider.id className\":\"sun.security.ec.SunEC\",\"AlgorithmParameters.EC SupportedCurves\":\"[secp112r1,1.3.132.0.6]|[secp112r2,1.3.132.0.7]|[secp128r1,1.3.132.0.28]|[secp128r2,1.3.132.0.29]|[secp160k1,1.3.132.0.9]|[secp160r1,1.3.132.0.8]|[secp160r2,1.3.132.0.30]|[secp192k1,1.3.132.0.31]|[secp192r1,NIST P-192,X9.62 prime192v1,1.2.840.10045.3.1.1]|[secp224k1,1.3.132.0.32]|[secp224r1,NIST P-224,1.3.132.0.33]|[secp256k1,1.3.132.0.10]|[secp256r1,NIST P-256,X9.62 prime256v1,1.2.840.10045.3.1.7]|[secp384r1,NIST P-384,1.3.132.0.34]|[secp521r1,NIST P-521,1.3.132.0.35]|[X9.62 prime192v2,1.2.840.10045.3.1.2]|[X9.62 prime192v3,1.2.840.10045.3.1.3]|[X9.62 prime239v1,1.2.840.10045.3.1.4]|[X9.62 prime239v2,1.2.840.10045.3.1.5]|[X9.62 prime239v3,1.2.840.10045.3.1.6]|[sect113r1,1.3.132.0.4]|[sect113r2,1.3.132.0.5]|[sect131r1,1.3.132.0.22]|[sect131r2,1.3.132.0.23]|[sect163k1,NIST K-163,1.3.132.0.1]|[sect163r1,1.3.132.0.2]|[sect163r2,NIST B-163,1.3.132.0.15]|[sect193r1,1.3.132.0.24]|[sect193r2,1.3.132.0.25]|[sect233k1,NIST K-233,1.3.132.0.26]|[sect233r1,NIST B-233,1.3.132.0.27]|[sect239k1,1.3.132.0.3]|[sect283k1,NIST K-283,1.3.132.0.16]|[sect283r1,NIST B-283,1.3.132.0.17]|[sect409k1,NIST K-409,1.3.132.0.36]|[sect409r1,NIST B-409,1.3.132.0.37]|[sect571k1,NIST K-571,1.3.132.0.38]|[sect571r1,NIST B-571,1.3.132.0.39]|[X9.62 c2tnb191v1,1.2.840.10045.3.0.5]|[X9.62 c2tnb191v2,1.2.840.10045.3.0.6]|[X9.62 c2tnb191v3,1.2.840.10045.3.0.7]|[X9.62 c2tnb239v1,1.2.840.10045.3.0.11]|[X9.62 c2tnb239v2,1.2.840.10045.3.0.12]|[X9.62 c2tnb239v3,1.2.840.10045.3.0.13]|[X9.62 c2tnb359v1,1.2.840.10045.3.0.18]|[X9.62 c2tnb431r1,1.2.840.10045.3.0.20]|[brainpoolP160r1,1.3.36.3.3.2.8.1.1.1]|[brainpoolP192r1,1.3.36.3.3.2.8.1.1.3]|[brainpoolP224r1,1.3.36.3.3.2.8.1.1.5]|[brainpoolP256r1,1.3.36.3.3.2.8.1.1.7]|[brainpoolP320r1,1.3.36.3.3.2.8.1.1.9]|[brainpoolP384r1,1.3.36.3.3.2.8.1.1.11]|[brainpoolP512r1,1.3.36.3.3.2.8.1.1.13]\",\"KeyPairGenerator.EC ImplementedIn\":\"Software\",\"KeyAgreement.ECDH ImplementedIn\":\"Software\",\"KeyFactory.EC\":\"sun.security.ec.ECKeyFactory\"},\"paramSpi\":{\"namedCurve\":{\"name\":\"secp256r1 [NIST P-256, X9.62 prime256v1]\",\"oid\":\"1.2.840.10045.3.1.7\",\"encoded\":[6,8,42,-122,72,-50,61,3,1,7],\"curve\":{\"field\":{\"p\":115792089210356248762697446949407573530086143415290314195533631308867097853951},\"a\":115792089210356248762697446949407573530086143415290314195533631308867097853948,\"b\":41058363725152142129326129780047268409114441015993725554835256314039467401291},\"g\":{\"x\":48439561293906451759052585252797914202762949526041747995844080717082404635286,\"y\":36134250956749795798585127919587881956611106672985015071877198253568414405109},\"n\":115792089210356248762697446949407573529996955224135760342422259061068512044369,\"h\":1}},\"algorithm\":\"EC\",\"initialized\":true},\"constructedFromDer\":false},\"key\":[4,-83,-31,-2,-61,35,14,31,54,-126,16,51,-66,-100,117,38,-17,84,119,-89,-83,20,-99,27,1,15,-27,19,24,99,21,69,-6,-117,119,-11,-34,-9,-51,40,-7,-42,51,-13,67,-82,-21,116,-89,39,126,15,-29,-16,64,-121,62,105,-18,37,92,-82,67,114,-36],\"unusedBits\":0},\"creators_signature\":\"creators_signature\",\"timestamp\":1545760544296,\"type\":\"creation\",\"json\":[104,105,104,105]}\n";
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();

		Transaction t = gson.fromJson(jsonStr, Transaction.class);
		// Transaction t = new Transaction();

		// Transaction t = toTransaction(json);
		System.out.println("creator_sig : " + t.getCreators_signature());
		s.close();
	}

	private Transaction toTransaction(String json) {
		System.out.println("receive : " + json);
		Gson gson = new Gson();
		return gson.fromJson(json, Transaction.class);
	}

	public void client() {
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			// TODO changer en contacts.get(0)
			socket = new Socket("192.168.1.54", 2009);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			Wallet w = new Wallet();
			String json = "hihi";
			// TODO c'est quoi creators_signature ?
			Transaction transaction1 = new Transaction(w.getPublic_key(), "creators_signature",
					System.currentTimeMillis(), Transaction.CREATION_TYPE, json.getBytes());

			String trans_json = toJson(transaction1);

			out.write(trans_json);
			out.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String toJson(Transaction transaction1) {
		Gson gson = new Gson();
		return gson.toJson(transaction1);
	}

}
