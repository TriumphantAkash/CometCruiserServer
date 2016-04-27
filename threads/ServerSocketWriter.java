package threads;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import models.Client;
import server.MainThread;

public class ServerSocketWriter extends Thread{
	//BufferedReader inFromServer;
	String message;
	DataOutputStream tempOutputStream;
	BlockingQueue<String> queue;
	public ServerSocketWriter(BlockingQueue<String> bq) {
		this.queue = bq;
	}
	
	public void run(){
		try {
			while(true){
				//1) receive this mesasge from any of the SocketReaderThread
				//(May be I can use some looper or something to receive 
				/*
				 * 
				 * do stuff here*/
				String message = queue.take();
				System.out.println(message);
				//here we do the parsing
				String[] splitted = message.split(",");
				String time = splitted[1];
				String lat = splitted[2];
				String lng = splitted[4];
				
				if(time.contains("$") || lat.contains("$")	|| lng.contains("$")){
					continue;
				}
				
				if(!lat.contains(".") || (lat.length() < 7) || (!lng.contains(".")) || (lng.length() < 8) || !lat.matches("[0-9.]*") || !lng.matches("[0-9.]*")){
					continue;
				}
				
				String lat1 = lat.substring(0, 2);
				String lat2 = lat.substring(2);
				
				double actualLat = Double.parseDouble(lat1)+(Double.parseDouble(lat2)/60);
				
				String lng1 = lng.substring(0, 3);
				String lng2 = lng.substring(3);
				double actualLng = Double.parseDouble(lng1)+(Double.parseDouble(lng2)/60);
				
				String msgToClient = actualLat+",-"+actualLng;
				//for now I am gonna go ahead and just print it
				//System.out.println("received this message on server: "+message);
				
				//later I am gonna write this message to all client's outputStreams (MainServer.clientList.getOutputStream())
                //handle the data
				/* 
				 * */
				//System.out.println("the size of current client llist is: "+MainThread.clientList.size());
				//a message here from any of the ServerSocketReader thread)
				//2) write this message to outputstreams of all connected clients
				
				System.out.println("Writing this coordinates to the Android client: "+ msgToClient);
				for(Client client: MainThread.clientList){
					client.getOutputStream().writeBytes(msgToClient+"\n");
					//Thread.sleep(10);
				}			
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}