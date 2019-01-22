package p2p.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import blockchain.Transaction;
import p2p.node.Node;
import p2p.node.NodeInfos;
import p2p.node.dispatch.CreateTransaction;
import p2p.node.dispatch.DispatchConversion;
import p2p.node.reception.ReceptionConversion;
import p2p.protocole.Operation;
public class TestP2P {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		String trans = CreateTransaction.create_transaction();
		broadcast_transaction(trans);

	}


	public static void broadcast_transaction(String transaction_json) {

		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();

		Operation op_trans = new Operation("transaction","myipaddress",2000);
		op_trans.setRest(transaction_json);

		String trans = DispatchConversion.transactionToBinary(gson.toJson(op_trans));
		System.out.println(trans);
	}
	private static String toJson(Transaction transaction1) {
		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();
		return gson.toJson(transaction1);
	}

}