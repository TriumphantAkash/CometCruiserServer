package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import threads.LaunchpadReader;
import threads.ServerSocketWriter;
import models.Client;

public class MainThread {

	//an ArrayList of Clients
		public static ArrayList<Client> clientList;
		private static ServerSocket welcomeSocket;
		private static Client curr_client;
		private static BlockingQueue<String> queue = null;
		
		public static void main(String argv[]) throws IOException {
			try {
				welcomeSocket = new ServerSocket(6970);
			    clientList = new ArrayList<Client>();
			    queue = new ArrayBlockingQueue<String>(1000);
			    //for now let's assume there will be a single socket writer thread that will
			    //write to all the connected clinet's output streams
			    //Consumer of BlockingQueue
			    ServerSocketWriter serverWriteThread = new ServerSocketWriter(queue);
			    serverWriteThread.start();
			    System.out.println("Comet Cruiser server is up and running...");
			    
				while(true){             
					Socket connectionSocket = welcomeSocket.accept();
					System.out.println("some device got connected");
					
					//control comes here whenever a new client is connected to the server
					connectionSocket.getLocalAddress();
					connectionSocket.getLocalPort();
					
					//create a new client object and input this into client list
					curr_client = new Client();
					curr_client.setId(clientList.size()+1);
					
					BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
					curr_client.setInputStream(inFromClient);
					
					DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					curr_client.setOutputStream(outToClient);
					String str = inFromClient.readLine();
					if(str.contains("launch")){//assuming (only) first message from the launchpad is the string "launchpad"
						System.out.println("Launchpad is connected now....");
						LaunchpadReader launchpadReaderThread = new LaunchpadReader(curr_client.getInputStream(), queue);
						launchpadReaderThread.start();
					}else {	//this is an Android client
						clientList.add(curr_client);
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				welcomeSocket.close();
			}
		}
}
