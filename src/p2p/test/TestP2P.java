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

		String emmeteur="110000010001000000001000100000011110101100011011011111101000100000011000011001011111001000110101110000001011010110010111011110101010010100000100011101000000101100001110011100111011010010101111001110011110101011100111111101000011110000001111010100111110001010111110010001110000000110010000000010001000000111010101010000111100111111001011010100100010001000111001000011001010001110111011011010101101110010111010101011001011000110011111011010110100011000010011111011111100101010010101100010101111001100000110100001011101100100011101111000001110000111001010111101";
		String ss =     "110000010001000000001000100000011110101100011011011111101000100000011000011001011111001000110101110000001011010110010111011110101010010100000100011101000000101100001110011100111011010010101111001110011110101011100111111101000011110000001111010100111110001010111110010001110000000110010000000010001000000111010101010000111100111111001011010100100010001000111001000011001010001110111011011010101101110010111010101011001011000110011111011010110100011000010011111011111100101010010101100010101111001100000110100001011101100100011101111000001110000111001010111101";
		System.out.println(emmeteur.length()+" "+ss.length()+"\n"+emmeteur.equals(ss));
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