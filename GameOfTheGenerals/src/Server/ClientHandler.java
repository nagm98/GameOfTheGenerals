package Server;

import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler extends Thread{
	private Socket client;
	private Board setup;
	private Scanner input;
	private ObjectInputStream inFromClient;
	private ObjectOutputStream outToClient;
	private PrintWriter output;
	public ClientHandler(Socket socket, Board board){
		client = socket;
		setup = board;
	
			//Create input and output streams
			//on the socket…
			try {
				
				input = new Scanner(client.getInputStream());
				output = new PrintWriter(client.getOutputStream(),true);
				inFromClient = new ObjectInputStream(client.getInputStream());
				outToClient = new ObjectOutputStream(client.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		
	}
	public void run(){
		String request = "",np="",op="";
		int rop,rnp;
		do{
			int[][] arrFromClient = null;
			int turn;
			request = input.nextLine();
			if(request.equals("1")) {
				try {
					arrFromClient = (int[][]) inFromClient.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				turn=setup.getPlaced();
				output.println(turn);
				setup.PlacePiece(arrFromClient);
				try {
					outToClient.writeObject(setup.getBoard());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(request.equals("2")) {
				
				rop=setup.getRop();
				rnp=setup.getRnp();
				output.println(rop);
				output.println(rnp);
				np = input.nextLine();
				op = input.nextLine();
				
				int npnum=Integer.parseInt(np);
				int opnum=Integer.parseInt(op);
				
				setup.move(npnum,opnum);
				output.println("1");
				
			}
			else if(request.equals("3")) {
				turn=setup.getPlaced();
				output.println(turn);
				
			}
			else if(request.equals("4")) {
				setup.first();
				output.println(" ");
				
			}
			
			
		}while (!request.equals("0"));
		try{
			System.out.println("Closing down connection…");
	
			client.close();
		}catch(IOException ioEx){
			System.out.println("Unable to close connection to client!");
		}
	}
 } 

/////////////////////////////////
//////////////////////////

/*if (request.equals("1")){
int a=item.takeOne();//If none available,
//wait until resource(s)
//available (and thread is
//at front of thread queue).
output.println("Request granted.");
output.println(a+"Resources left.");
}
else {
output.println("**Invalid**");
output.println("Choose new value");
}
*/