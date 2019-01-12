package p2p.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import p2p.node.reception.ReceptionConversion;

public class NodeServer {


	public static void main(String[] args) {
		Socket socket;
		ServerSocket socketserver = null;

		try {
			socketserver =  new ServerSocket(2009);
			socket = socketserver.accept();

			System.out.println("receiving");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String json = in.readLine();


			System.out.println("je viens de recevoir : "+json);
			ReceptionConversion.operationToString(json);

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
