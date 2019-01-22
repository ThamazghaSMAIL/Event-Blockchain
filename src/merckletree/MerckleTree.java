package merckletree;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.sun.prism.RTTexture;

import blockchain.Transaction;
/**
 * au début on génére une clé (un bloc de génése) 
 * quelqu'un entre les 30 propose un bloc, il le transmets à tout le monde et ils vot voté pour lui 
 * s'il y à 66% de ces gens qui ont voté ok, le vote est bon 
 * sinon on dit que le vote a été échoué, on attend une certain durée, puis le suivant proose son bloc 
 * 
 * si le bloc est accepté, soit on laisse la même personne pour proposer les blocs ou on change systematiquement
 * Pour modéliser le vote ce sera une opération qui est signée 
 * 
 * dan ntre cas n pourrais uste un round parce que les gens se connaissent 
 * @author thamazgha
 *
 */
public class MerckleTree {

	public static final byte LEAF_SIG_TYPE = 0x0;
	public static final byte INTERNAL_SIG_TYPE = 0x01;


	private List<MerckleTreeNode> feuilles;
	private MerckleTreeNode racine;


	/**
	 * Créer un arbre à partir de feuilles 
	 * @param feuilles
	 * @throws NoSuchAlgorithmException 
	 */
	public MerckleTree(List<MerckleTreeNode> feuilles) throws NoSuchAlgorithmException {
		this.feuilles = feuilles;
		/**
		 * Calculs des noeuds internes jusqu'à la racine
		 */
		if( feuilles.size() > 0 )
			calcul_parents(feuilles);
		else 
		{
			this.racine = new MerckleTreeNode();
			this.racine.texte = "".getBytes();
			try {
				this.racine.sig = hashEmpty(this.racine.texte);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ok
	 * @param feuilles
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public byte[] calcul_parents(List<MerckleTreeNode> feuilles){
		if(feuilles.size() == 1) {
			this.racine = feuilles.get(0);
			return this.racine.sig;
		}else {
			List<MerckleTreeNode> parents = new ArrayList<MerckleTreeNode>();
			for (int i = 0; i < feuilles.size(); i += 2) {
				/**
				 * construire les deux noeuds 
				 */
				MerckleTreeNode  leaf1 = feuilles.get(i);

				MerckleTreeNode  leaf2 = feuilles.get(i+1);

				/**
				 * construire le père des deux feuilles
				 */
				MerckleTreeNode  parent = construireNoeudInterne(leaf1, leaf2);

				parents.add(parent);
			}
			return calcul_parents(parents);
		}

	}


	//	/**
	//	 * ok
	//	 * @param textefeuille
	//	 * @return
	//	 * @throws NoSuchAlgorithmException 
	//	 */
	//	private static MerckleTreeNode  construireFeuille(byte[] textefeuille) throws NoSuchAlgorithmException {
	//		MerckleTreeNode  feuille = new MerckleTreeNode ();
	//		/**
	//		 * affecter les champs de la feuille
	//		 */
	//		feuille.type = LEAF_SIG_TYPE;
	//
	//		/**
	//		 * dans le cas d'une feuille on a les données (texte) et la signature
	//		 */
	//		//feuille.texte=textefeuille;
	//		//feuille.sig = hashFeuille(textefeuille,"0"); //.getBytes(StandardCharsets.UTF_8);
	//		return feuille;
	//	}


	/**
	 * ok
	 * @param fils_gauche
	 * @param fils_droit
	 * @return
	 */
	private MerckleTreeNode  construireNoeudInterne(MerckleTreeNode  fils_gauche, MerckleTreeNode  fils_droit) {
		MerckleTreeNode  parent = null;//= new MerckleTreeNode ();
		if (fils_droit == null) {
			parent =  new MerckleTreeNode();
			parent.sig = fils_gauche.sig;
			parent.fils_gauche = fils_gauche;
		} else {
			parent =  new MerckleTreeNode();
			parent.sig = hashInterne(fils_gauche.sig, fils_droit.sig);
			parent.fils_gauche = fils_gauche;
			parent.fils_droit = fils_droit;
		}



		parent.type = INTERNAL_SIG_TYPE;
		return parent;
	}


	private static byte[] hashFeuille( byte[] textefeuille) throws NoSuchAlgorithmException {
		String key = "\000";
		String algorithm = "HmacSHA256";  // OPTIONS= HmacSHA512, HmacSHA256, HmacSHA1, HmacMD5
		byte[] hash = null;
		try {
			// 1. Get an algorithm instance.
			Mac sha256_hmac = Mac.getInstance(algorithm);
			// 2. Create secret key.
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			// 3. Assign secret key algorithm.
			sha256_hmac.init(secret_key);
			// 4. Generate hex encoded string.
			hash = sha256_hmac.doFinal(textefeuille);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return hash;
	}

	private static byte[] hashInternalNode(String textefeuille) throws NoSuchAlgorithmException {
		String key = "\001";
		String algorithm = "HmacSHA256";  // OPTIONS= HmacSHA512, HmacSHA256, HmacSHA1, HmacMD5
		byte[] hash = null;
		try {
			// 1. Get an algorithm instance.
			Mac sha256_hmac = Mac.getInstance(algorithm);
			// 2. Create secret key.
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			// 3. Assign secret key algorithm.
			sha256_hmac.init(secret_key);
			// 4. Generate hex encoded string.
			hash = sha256_hmac.doFinal(textefeuille.getBytes());
		
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return hash;
	}
	private byte[] hashInterne( byte[] sig_gauche,  byte[] sig_droit) {
		byte[] hash = null ;
		try {
			hash = hashInternalNode(Hex.encodeHexString(concatenate(sig_gauche, sig_droit)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}

	/**
	 *
	 * @param hash1
	 * @param hash2
	 * @return concatenation des deux hachés donné en parametres
	 */
	public static byte[] concatenate(byte[] hash1, byte[] hash2) {
		if(hash2 != null){
			byte[] newHash = new byte[hash1.length + hash2.length];
			System.arraycopy(hash1, 0, newHash, 0, hash1.length);
			System.arraycopy(hash2, 0, newHash, hash1.length, hash2.length);
			return newHash;
		}else{
			return  hash1;
		}
	}

	public MerckleTreeNode getRoot() {
		return this.racine;
	}

	private static byte[] hashEmpty( byte[] textefeuille) throws NoSuchAlgorithmException {
		String key = "\002";
		String algorithm = "HmacSHA256";  // OPTIONS= HmacSHA512, HmacSHA256, HmacSHA1, HmacMD5
		byte[] hash = null;
		try {
			// 1. Get an algorithm instance.
			Mac sha256_hmac = Mac.getInstance(algorithm);
			// 2. Create secret key.
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			// 3. Assign secret key algorithm.
			sha256_hmac.init(secret_key);
			// 4. Generate hex encoded string.
			hash = sha256_hmac.doFinal(textefeuille);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	public static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}


	/* ---[ Node class ]--- */

	/**
	 * The Node class should be treated as immutable, though immutable
	 * is not enforced in the current design.
	 * 
	 * A Node knows whether it is an internal or leaf node and its signature.
	 * 
	 * Internal Nodes will have at least one child (always on the left).
	 * Leaf Nodes will have no children (left = right = null).
	 */
	public static class MerckleTreeNode {
		public byte type;  // INTERNAL_SIG_TYPE or LEAF_SIG_TYPE
		public  byte[] sig; // signature du noeud
		public MerckleTreeNode fils_gauche;
		public MerckleTreeNode fils_droit;
		public  byte[] texte;//contient le texte du noeud si c'est une feuille, =null sinon

		
		public MerckleTreeNode(byte[] texte) {
			this.texte = texte;
			try {
				this.sig = hashFeuille(this.texte);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		public MerckleTreeNode() {}

		@Override
		public String toString() {
			String leftType = "<null>";
			String rightType = "<null>";
			if (fils_gauche != null) {
				leftType = String.valueOf(fils_gauche.type);
			}
			if (fils_droit != null) {
				rightType = String.valueOf(fils_droit.type);
			}
			return String.format("MerkleTree.Node<type:%d, sig:%s, left (type): %s, right (type): %s>",
					type, sig, leftType, rightType);
		}

		//TODO vérifier le type du haché d'un noeud et régler ça
		//		private String sigAsString() {
		//			StringBuffer sb = new StringBuffer();
		//			sb.append('[');
		//			for (int i = 0; i < sig.length; i++) {
		//				sb.append(sig[i]).append(' ');
		//			}
		//			sb.insert(sb.length()-1, ']');
		//			return sb.toString();
		//		}
	}  


}