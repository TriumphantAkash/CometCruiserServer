package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class LaunchpadReader extends Thread{

	BufferedReader inFromLaunchPad;
	String data;
	BlockingQueue<String> bq = null;
	public LaunchpadReader (BufferedReader inFromLaunchPad, BlockingQueue<String> queue) {
		this.inFromLaunchPad = inFromLaunchPad;
		this.bq = queue;
		System.out.println("Launchpad reader thread is created for listening GPS coordinates..");
	}
	
	public void run(){
		try {
			while(true){
				data = inFromLaunchPad.readLine();	//a new message is arrived
				System.out.println("received coordinates..");
				if(data != null){
					System.out.println("launchpad wrote this: "+data);
					
					if((bq!=null) && (data != null)){
						if(data.startsWith("$$GPGGA,")){
							bq.put(data);
						}
					}
				}		
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
}
