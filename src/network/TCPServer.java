package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class TCPServer implements Runnable{
	Logger log = Logger.getLogger(TCPServer.class);
	@Override
	public void run(){
		BasicConfigurator.configure();
		try{
			//ICMP default packet size is 56 bytes
			byte[] packet = new byte[56];
			ServerSocket sSocket = new ServerSocket(8008);
			Socket socket = sSocket.accept();
			log.debug("tcp server ready!");
			BufferedReader read = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			DataOutputStream write = new DataOutputStream(socket.getOutputStream());
			read.read();
			write.write(packet);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
