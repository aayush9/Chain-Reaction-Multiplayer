package Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import application.mainApp;
import javafx.application.Platform;

class Connection implements Runnable{
	
	Socket conn;
	public DataOutputStream dos;
	public DataInputStream dis;
	int errors=0;
	int idx;
	Network network;
	public Connection(Socket _conn, Network _network, int _idx) {
		this.conn=_conn;
		this.network=_network;
		this.idx=_idx;
		try {
			dos = new DataOutputStream(conn.getOutputStream());
			dis = new DataInputStream(conn.getInputStream());
			System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			recieve();
		}
	}
	private void  recieve() {
		if(errors<10) {
			try {
				String res = dis.readUTF();
				System.out.println(res);
				network.recievefromAll(res,this.idx);
			} catch (IOException e) {
				//e.printStackTrace();
				++errors;
			}
		}
	}
	
	public void send(String s) {
		try {
			dos.writeUTF(s);
			dos.flush();
		System.out.println("Sent.");
		} catch (IOException e) {
			System.out.println("Data not sent...");
		}
	}
}