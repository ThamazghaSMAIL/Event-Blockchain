package p2p.interfaces;

import java.util.List;

import blockchain.Transaction;
import p2p.NodeInfos;

public interface INode {
	public void premier_contact(NodeInfos ni);
	public void broadcast_transaction(Transaction transaction, List<NodeInfos> contacts);
	public void search_freinds();
	public void listen();
	public void minage();
}
