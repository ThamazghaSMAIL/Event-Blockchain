package p2p;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Node {
	public static void main(String[] zero){

		ServerSocket socket;
		try {
			socket = new ServerSocket(2009);
			Thread t = new Thread(new AcceptNode(socket));
			t.start();
			System.out.println("Mes employeurs sont prêts !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class AcceptNode implements Runnable {

	private ServerSocket socketserver;
	private Socket socket;
	private int nbrclient = 1;
	public Thread t1;

	public AcceptNode(ServerSocket s){
		socketserver = s;
	}

	public void run() {

		try {
			while(true){
				socket = socketserver.accept(); // Un node se connecte on l'accepte
				t1 = new Thread(){
					public void run(){
						try {
							receive_transaction(socket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				t1.start();
				System.out.println("Le client numéro "+nbrclient+ " est connecté !");
				nbrclient++;
				//socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receive_transaction(Socket s) throws IOException{
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));	
		System.out.println("receive"+in.readLine());
		s.close();
	}
}
