package blockchain.persistance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import blockchain.Block;
import blockchain.Blockchain;
import blockchain.Transaction;
import p2p.node.Node;

public class Persistance {

	public static void WriteBlockChainToFile(Blockchain bc) {
		try {
			FileOutputStream fileOut = new FileOutputStream("blockchain.txt");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(bc);
			objectOut.close();
			System.out.println("The BlockChain was succesfully written to a file");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static Blockchain ReadBlockChainFromFile(String filepath) {
		        try {
		            FileInputStream fileIn = new FileInputStream(filepath);
		            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		            Blockchain obj = (Blockchain) objectIn.readObject();
		            System.out.println("The BlockChain has been read from the file");
		            objectIn.close();
		            return obj;
		        } catch (Exception ex) {
		            ex.printStackTrace();
		            return null;
		        }
		    }

	public static void main(String[] args) {
		try {
			Blockchain bc = new Blockchain();
			Block b = new Block(null, new ArrayList<Transaction>(), 
					Node.getInstance().getW().getPublic_key(), Node.getInstance().getNounce(),0);
			Block b1 = new Block(null, new ArrayList<Transaction>(),
					Node.getInstance().getW().getPublic_key(), Node.getInstance().getNounce(),1);

			bc.addBlock(b);
			bc.addBlock(b1);
			WriteBlockChainToFile(bc);
			
			Blockchain bcc = ReadBlockChainFromFile("blockchain.txt");
			System.out.println(bc.getBlocks().size());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
