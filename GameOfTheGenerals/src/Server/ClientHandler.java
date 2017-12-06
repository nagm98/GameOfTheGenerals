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
				int bn=Integer.parseInt(input.nextLine());
				
				try {
					arrFromClient = (int[][]) inFromClient.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				turn=setup.getPlaced(bn);
				output.println(turn);
				setup.PlacePiece(bn,arrFromClient);
				try {
					outToClient.writeObject(setup.getBoard(bn));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(request.equals("2")) {
				int bn=Integer.parseInt(input.nextLine());
				rop=setup.getRop(bn);
				rnp=setup.getRnp(bn);
				output.println(rop);
				output.println(rnp);
				np = input.nextLine();
				op = input.nextLine();
				
				int npnum=Integer.parseInt(np);
				int opnum=Integer.parseInt(op);
				
				String ed = input.nextLine();
				String w = input.nextLine();
				int end=Integer.parseInt(ed);
				int win=Integer.parseInt(w);
				setup.setEnd(bn,end);
				setup.setWin(bn,win);
				setup.move(bn,npnum,opnum);
				
				
				
				output.println(Integer.toString(setup.getEnd(bn)));
				output.println(Integer.toString(setup.getWin(bn)));
				
			}
			else if(request.equals("3")) {
				int bn=Integer.parseInt(input.nextLine());
				turn=setup.getPlaced(bn);
				output.println(turn);
				
			}
			else if(request.equals("4")) {
				int bn=Integer.parseInt(input.nextLine());
				setup.first(bn);
				output.println(" ");
				
			}
			
			
			
			else if(request.equals("6")) {
				String un = input.nextLine();
				output.println(setup.getPlayer(un));
				output.println(setup.getPlayer1());
				output.println(setup.getPlayer2());
				output.println(setup.getPlayer3());
				output.println(setup.getPlayer4());
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