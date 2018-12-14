package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeClient {

	public static void main(String[] zero){
		PrintWriter out = null;
		BufferedReader in = null;
		Socket socket;
		try {
			
			socket = new Socket("localhost",2009);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
			out.write("hello");
			socket.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
