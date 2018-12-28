package p2p.interfaces;

import java.util.List;

import blockchain.Transaction;
import p2p.node.NodeInfos;

public interface INode {
	public void premier_contact(NodeInfos ni);
	public void listen();
	void search_freinds(List<NodeInfos> list);
}
