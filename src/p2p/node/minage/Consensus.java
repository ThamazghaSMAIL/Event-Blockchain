package p2p.node.minage;

public class Consensus {

	public final static int NODES_NUMBER = 4;
	public static int calcul_tour(int latest_tour){
		return (latest_tour%NODES_NUMBER) + 1 ;
	}
	
}
