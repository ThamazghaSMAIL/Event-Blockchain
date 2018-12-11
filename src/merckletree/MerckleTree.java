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

	private final Adler32 crc = new Adler32();


	private List<String> feuilles;
	private Node racine;
	private int profondeur;
	private int NbreNoeuds;


	/**
	 * Créer un arbre à partir de feuilles 
	 * @param feuilles
	 * @throws NoSuchAlgorithmException 
	 */
	public MerckleTree(List<String> feuilles) throws NoSuchAlgorithmException {
		/**
		 * ça sert à rien d'avoir un arbre d'une seule feuille
		 */
		if (feuilles.size() <= 1) {
			throw new IllegalArgumentException("Must be at least two signatures to construct a Merkle tree");
		}

		this.feuilles = feuilles;
		this.NbreNoeuds = feuilles.size();

		/**
		 * Calculs des noeuds internes jusqu'à la racine
		 */
		List<Node> parents = calcul_parents(feuilles);

		/**
		 * ajouter le nombre de noeuds internes au nbre de noeuds total
		 */
		this.NbreNoeuds += parents.size();


		this.profondeur = 1;
		while (parents.size() > 1) {
			parents = internalLevel(parents);
			profondeur++;
			NbreNoeuds += parents.size();
		}

		racine = parents.get(0);
	}

	/**
	 * ok
	 * @param feuilles
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	List<Node> calcul_parents(List<String> feuilles) throws NoSuchAlgorithmException {
		List<Node> parents = new ArrayList<Node>(feuilles.size() / 2);

		for (int i = 0; i < feuilles.size() - 1; i += 2) {
			/**
			 * construire les deux noeuds 
			 */
			Node leaf1 = construireFeuille(feuilles.get(i));
			Node leaf2 = construireFeuille(feuilles.get(i+1));

			/**
			 * construire le père des deux feuilles
			 */
			Node parent = construireNoeudInterne(leaf1, leaf2);

			parents.add(parent);
		}

		// if odd number of leafs, handle last entry
		if (feuilles.size() % 2 != 0) {
			Node feuille = construireFeuille(feuilles.get(feuilles.size() - 1));      
			Node parent = construireNoeudInterne(feuille, null);
			parents.add(parent);
		}

		return parents;
	}

	/**
	 * Constructs an internal level of the tree
	 */
	List<Node> internalLevel(List<Node> children) {
		List<Node> parents = new ArrayList<Node>(children.size() / 2);

		for (int i = 0; i < children.size() - 1; i += 2) {
			Node child1 = children.get(i);
			Node child2 = children.get(i+1);

			Node parent = construireNoeudInterne(child1, child2);
			parents.add(parent);
		}

		if (children.size() % 2 != 0) {
			Node child = children.get(children.size()-1);
			Node parent = construireNoeudInterne(child, null);
			parents.add(parent);
		}

		return parents;
	}


	/**
	 * ok
	 * @param textefeuille
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	private static Node construireFeuille(String textefeuille) throws NoSuchAlgorithmException {
		Node feuille = new Node();
		/**
		 * affecter les champs de la feuille
		 */
		feuille.type = LEAF_SIG_TYPE;

		/**
		 * dans le cas d'une feuille on a les données (texte) et la signature
		 */
		feuille.texte=textefeuille;
		feuille.sig = hashFeuille(textefeuille); //.getBytes(StandardCharsets.UTF_8);
		return feuille;
	}


	/**
	 * ok
	 * @param fils_gauche
	 * @param fils_droit
	 * @return
	 */
	private Node construireNoeudInterne(Node fils_gauche, Node fils_droit) {
		Node parent = new Node();
		parent.type = INTERNAL_SIG_TYPE;

		/**
		 * Dans le cas d'un noeud interne, on a pas de données donc texte="<null>"
		 */
		parent.texte="<null>";
		/**
		 * 
		 */
		if (fils_droit == null) {
			parent.sig = fils_gauche.sig;
		} else {
			parent.sig = hashInterne(fils_gauche.sig, fils_droit.sig);
		}

		parent.fils_gauche = fils_gauche;
		parent.fils_droit = fils_droit;
		return parent;
	}


	private static String hashFeuille(String textefeuille) throws NoSuchAlgorithmException {
		// TODO hasher selon la meth demandée
		String message = textefeuille;
		String key = "your_key";
		String algorithm = "HmacSHA512";  // OPTIONS= HmacSHA512, HmacSHA256, HmacSHA1, HmacMD5
		String hash = "";
		try {
			// 1. Get an algorithm instance.
			Mac sha256_hmac = Mac.getInstance(algorithm);
			// 2. Create secret key.
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			// 3. Assign secret key algorithm.
			sha256_hmac.init(secret_key);
			// 4. Generate hex encoded string.
			 hash = Hex.encodeHexString(sha256_hmac.doFinal(message.getBytes("UTF-8")));
			/**
			 * output for "This is my message."
			 * HmacSHA256 =gCZJBUrp45o+Z5REzMwyJrdbRj8Rvfoy33ULZ1bySXM=
			 */
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return hash;
	}

	private String hashInterne(String sig_gauche, String sig_droit) {
		// TODO hasher selon la meth demandée
		return sig_droit+sig_gauche;
	}


	public int getNumNodes() {
		return this.NbreNoeuds;
	}

	public Node getRoot() {
		return this.racine;
	}

	public int getHeight() {
		return profondeur;
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
	static class Node {
		public byte type;  // INTERNAL_SIG_TYPE or LEAF_SIG_TYPE
		public String sig; // signature du noeud
		public Node fils_gauche;
		public Node fils_droit;
		public String texte;//contient le texte du noeud si c'est une feuille, =null sinon

		@Override
		public String toString() {
			String leftType = "<null>";
			String rightType = "<null>";
			if (fils_gauche != null) {
				leftType = String.valueOf(fils_droit.type);
			}
			if (fils_droit != null) {
				rightType = String.valueOf(fils_gauche.type);
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