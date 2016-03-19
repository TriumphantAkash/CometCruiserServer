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
	}
	
	public void run(){
		try {
			while(true){
				data = inFromLaunchPad.readLine();	//a new message is arrived
				//now broadcast this message i.e
				//hand this message to ServerSocketWriter thread
				/*
				 * .
				 * (do stuff here)
				 */
				if((bq!=null) && (data != null)){
					bq.put(data);
				}
				 /* .
				 * ..
				 * */
				/*pass this message to SocketWriterThread, and he will write it to all the 
				connected clinet's output streams
				(may be I can use some Looper or something)*/			
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
}
