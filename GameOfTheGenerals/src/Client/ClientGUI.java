package Client;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import Client.ClientGUI;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.awt.Color;


public class ClientGUI {
	//saves values into the variables mostly for manipulation
	private int setCount=0,ctr=0;
	private static int ppc=0, a,b;
	private static int ppr=0, snp, sop,end=0,win=0;
	private int done=0;
	
	//Contains the one for the thread manipulation
	private static int turn=0,NetCount = -1;
	private static boolean waiter=true;
	
	//this is the networking variables
	private static InetAddress host;
	private static int PORT;
	private Socket socket = null;
	private static ObjectOutputStream outToServer;
	private static ObjectInputStream inFromServer;
	private static PrintWriter networkOutput;
	private static Scanner networkInput;
	
	//Gui Variables
	private static JButton[][] list;
	private JFrame frmGameOfThe;
	private JFrame frame;
	private JTextField txtEnterHostName;
	private final JButton Connect = new JButton("Connect");
	private static ClientGUI window;
	private JTextField textField;
	private JLabel lblPortNumber;
	private JButton setPiece;
	private static JTextPane txtpnThe;
	
	//Board storage variables
	private static int[][] boardLook= new int[8][9];
	private static String[] pieces= {"5star.png","4star.png","3star.png","2star.png","1star.png","colonel.png","ltcolonel.png","major.png","captain.png","1stleut.png", "2ndleut.png", "sergent.png", "private.png","spy.png","flag.png","Enemy.png"};
	private static String[] piecesName= {"5 Star General","4 Star General","3 Star General","2 Star General","1 Star General","Colonel"," Lt Colonel","Major","Captain","1st Leutenant", "2nd Leutenant", "Sergent", "Private","Spy","Flag"};
	private static int[] numb= {1,1,1,1,1,1,1,1,1,1,1,1,6,2,1};
	
	
	public static void reloadBoard() {
		//this part reloads the board and inputs the information into the  
		for (int i = 0; i < boardLook.length; i++) {
			for(int j =0;j<boardLook[0].length; j++) {
				if(turn==1) {
					if(boardLook[i][j]-15>0)
						list[7-i][8-j].setIcon(new ImageIcon(ClientGUI.class.getResource("/img/"+pieces[boardLook[i][j]-16])));
					else if(boardLook[i][j]-15<=0 && boardLook[i][j]-15>-15)
						list[7-i][8-j].setIcon(new ImageIcon(ClientGUI.class.getResource("/img/"+pieces[15])));
					else
						list[7-i][8-j].setIcon(null);
				}else if(turn==0) {
					if(boardLook[i][j]<=15 && boardLook[i][j]>0 )
						list[i][j].setIcon(new ImageIcon(ClientGUI.class.getResource("/img/"+pieces[boardLook[i][j]-1])));
					else if(boardLook[i][j]>15)
						list[i][j].setIcon(new ImageIcon(ClientGUI.class.getResource("/img/"+pieces[15])));
					else
						list[i][j].setIcon(null);
				}
			}
		}
		for(int i1=0; i1<8; i1++) { //clears the board of green tiles
			for(int j1=0; j1<9; j1++) {
				list[i1][j1].setBackground(new Color(139, 69, 19));
			}
		} 
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////
	//this is the main program this will first initialize the buttons ang give them actions
	//next it will crate a loop in which the networking will work every time a button is pressed
	//the networking is what receives inputs and also sends outputs
	////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		//this part sets all the values in the board to 0
		for (int i = 0; i < 8; i++) {
	    	for(int j =0;j<9; j++) {
	    		boardLook[i][j]=0;
	    	}
	    }
		
		//this initializes the GUI and starts the windows
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//initializer
					window = new ClientGUI();
					
					//starts the window to connect to the network
					window.frmGameOfThe.setVisible(true);
					
					//keeps the second window closed
					window.frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//this is the loop until the end of the game
		while(true) {
			//this waits until a button is pressed
			if(end==1) {
				networkOutput.println(5);
				if(win==1) {
					txtpnThe.setText("You Win");
				}
				else {
					txtpnThe.setText("You Lose");
				}
				break;
			}
			if(NetCount==1){
				networkOutput.println(2);
				//old position of the enemy
				int oop= Integer.parseInt(networkInput.nextLine());
				//Gets the new position of the enemy
				int onp = Integer.parseInt(networkInput.nextLine());
				System.out.print(onp+" "+oop+"\n");
				if(oop!=-1||onp!=-1) {
					if((boardLook[onp/9][onp%9]-15==boardLook[oop/9][oop%9]&&boardLook[onp/9][onp%9]-15>0)||(boardLook[onp/9][onp%9]==boardLook[oop/9][oop%9]-15 && boardLook[oop/9][oop%9]-15>0)) {
						boardLook[onp/9][onp%9]=0;
						boardLook[oop/9][oop%9]=0;
					}
					else {
						boardLook[onp/9][onp%9]=boardLook[oop/9][oop%9];
						boardLook[oop/9][oop%9]=0;
					}
					reloadBoard();
				}
				txtpnThe.setText("Your Turn");
				
			}
			while(waiter==true) {
				//this just uses try catch to be able to use sleep which waits every one second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for(int i1=0; i1<8; i1++) { 
				for(int j1=0; j1<9; j1++) {
					list[i1][j1].setEnabled(false);
					list[i1][j1].setBackground(new Color(139, 69, 19));
					txtpnThe.setText("Waiting for opponent");
				}
			}
			//this is for when the pieces are being laid out
			if(NetCount==0) {
				//this signals that the board i being set up
				if(end==1) {
					networkOutput.println(5);
					if(win==1) {
						txtpnThe.setText("You Win");
					}
					else {
						txtpnThe.setText("You Lose");
					}
					break;
				}
				networkOutput.println("1");
				
				//this part is the one that sends the array which contains the positions in the board
				try {
					outToServer.writeObject(boardLook);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//this saves which side of the board the user has for the program
				//0 for the lower half and one for the upper half
				turn = Integer.parseInt(networkInput.nextLine());
				System.out.print(turn);
				//this part gets the whole boards data
				try {
					boardLook = (int[][])inFromServer.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//places the proper icons to the board GUI
				reloadBoard();
				//tells that it is not to the game part
				NetCount++;
				if(turn==1) {
					networkOutput.println("4");
					networkInput.nextLine();
				}
			}
			//this is for when the player is already moving
			else if(NetCount==1){
				System.out.print("q");
				//tells the next movement
				if(end==1) {
					if(win==1) {
						txtpnThe.setText("You Win");
					}
					else {
						txtpnThe.setText("You Lose");
					}
				}
				//sends the new position
				networkOutput.println(snp);
				//sends the old position
				networkOutput.println(sop);
				//this gets the old position of the enemy 
				networkOutput.println(Integer.toString(end));
				
				end=Integer.parseInt(networkInput.nextLine());
			}
			//allows the waiting till a button is pressed
			waiter=true;
			
			if(end==0) {
				for(int i1=0; i1<8; i1++) { 
					for(int j1=0; j1<9; j1++) {
						list[i1][j1].setEnabled(true);
					}
				}
			}
			
			
			
		}
		/////////////////////////////////////////////////////////////////////////////
	}
	
	public ClientGUI() {
		//initialize is the first window 
		initialize();
		//the game window
		initialize2();
	}
	
	private void boardSetting() {
		list[a][b].setIcon(new ImageIcon(ClientGUI.class.getResource("/img/"+pieces[setCount])));//sets the piece
		boardLook[a][b]=setCount+1;
		ctr++;//this counts the number of pieces of a piece has been placed
		if(setCount<14) {
			if(ctr<numb[setCount]) {
				txtpnThe.setText("Place the "+piecesName[setCount] );
			}else{
				txtpnThe.setText("Place the "+piecesName[setCount+1]);
			}
		}
		if(ctr==numb[setCount]) {
			setCount++;
			ctr=0;
		}
	}
	
	private void initialize() {
		frmGameOfThe = new JFrame();
		frmGameOfThe.setBackground(Color.LIGHT_GRAY);
		frmGameOfThe.setTitle("Game of the Generals - Connect to Server");
		frmGameOfThe.setIconImage(Toolkit.getDefaultToolkit().getImage(ClientGUI.class.getResource("/img/5star.png")));
		frmGameOfThe.setBounds(100, 100, 541, 347);
		frmGameOfThe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frmGameOfThe.getContentPane().setLayout(null);
		
		txtEnterHostName = new JTextField();
		txtEnterHostName.setHorizontalAlignment(SwingConstants.CENTER);
		txtEnterHostName.setText("Enter Host Name");
		txtEnterHostName.setBounds(72, 143, 339, 34);
		frmGameOfThe.getContentPane().add(txtEnterHostName);
		txtEnterHostName.setColumns(10);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText("1234");
		textField.setBounds(148, 190, 191, 30);
		frmGameOfThe.getContentPane().add(textField);
		textField.setColumns(10);
		
		
		Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					host=InetAddress.getByName(txtEnterHostName.getText());
					PORT= Integer.parseInt(textField.getText());
					socket = new Socket(host,PORT);
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					//this sets up the networking and allows the sending of data
					networkInput = new Scanner(socket.getInputStream());
					networkOutput = new PrintWriter(socket.getOutputStream(),true);
					outToServer = new ObjectOutputStream(socket.getOutputStream());
					inFromServer = new ObjectInputStream(socket.getInputStream());
					networkOutput.println("3");
					Integer.parseInt(networkInput.nextLine());
					window.frame.setVisible(true);
					window.frmGameOfThe.setVisible(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		Connect.setBounds(204, 231, 89, 23);
		frmGameOfThe.getContentPane().add(Connect);
		
		
		lblPortNumber = new JLabel("PORT NUMBER:");
		lblPortNumber.setBounds(72, 196, 78, 14);
		frmGameOfThe.getContentPane().add(lblPortNumber);
	}
	
	private void move() {
		if(turn==1) {
			boardLook[7-a][8-b]=boardLook[7-ppr][8-ppc];
			boardLook[7-ppr][8-ppc]=0;
			if(boardLook[7-a][8-b]==30 && a==7) {
				if(7-b>=0) {
					if(9-b<9){
						if(boardLook[6-a][8-b]==0 && boardLook[8-a][8-b]==0) {
							end=1;
							win=1;
						}
					}
					else {
						if(boardLook[6-a][8-b]==0) {
							end=1;
							win=1;
						}
					}
				}else {
					if(boardLook[8-a][8-b]==0) {
						end=1;
						win=1;
					}
				}
			}
		}
		else{
			boardLook[a][b]=boardLook[ppr][ppc];
			boardLook[ppr][ppc]=0;
			
			if(boardLook[a][b]==15 && a==7) {
				if(b-1>=0) {
					if(b+1<9){
						if(boardLook[a-1][b]==0 && boardLook[a+1][b]==0) {
							end=1;
							win=1;
						}
					}
					else {
						if(boardLook[a-1][b]==0) {
							end=1;
							win=1;
						}
					}
				}else {
					if(boardLook[a+1][b]==0) {
						end=1;
						win=1;
					}
				}
			}
		}
		
		
		if(turn==1) {
			snp=((7-a)*9)+(8-b);
			sop=((7-ppr)*9)+(8-ppc);
		}
		else {
			snp=a*9+b;
			sop=ppr*9+ppc;
		}
	}
	
	
	private void chal() {
		snp=-1;
		sop=-1; //new position, old position 
		if(turn==1) {
			if(boardLook[7-a][8-b]<13) {//looks if the piece being challenged is an officer
				//if the piece is a spy or if it is out ranked the challenger wins and takes place of challenged
				if(boardLook[7-ppr][8-ppc]==29||boardLook[7-a][8-b]>boardLook[7-ppr][8-ppc]-15){
					boardLook[7-a][8-b]=boardLook[7-ppr][8-ppc];
					boardLook[7-ppr][8-ppc]=0;
					snp=((7-a)*9)+(8-b);
					sop=((7-ppr)*9)+(8-ppc);
				}
				//if equal ranks then both would disappear
				else if(boardLook[7-a][8-b]==boardLook[7-ppr][8-ppc]-15){
					boardLook[7-a][8-b]=0;
					boardLook[7-ppr][8-ppc]=0;
					snp=((7-a)*9)+(8-b);
					sop=((7-ppr)*9)+(8-ppc);
				}
				//if those do not hold then the piece loses
				else {
					boardLook[7-ppr][8-ppc]=0;
					sop=((7-ppr)*9)+(8-ppc);
					snp=sop;
				}
			}else if(boardLook[7-a][8-b]==13) {//if the challenged is a pawn
				if(boardLook[7-ppr][8-ppc]<28) {
					boardLook[7-a][8-b]=boardLook[7-ppr][8-ppc];
					boardLook[7-ppr][8-ppc]=0;
					snp=((7-a)*9)+(8-b);
					sop=((7-ppr)*9)+(8-ppc);
				}else if(boardLook[7-a][8-b]==boardLook[7-ppr][8-ppc]-15){
					boardLook[7-a][8-b]=0;
					boardLook[7-ppr][8-ppc]=0;
					snp=((7-a)*9)+(8-b);
					sop=((7-ppr)*9)+(8-ppc);
				}else {
					boardLook[7-ppr][8-ppc]=0;
					sop=((7-ppr)*9)+(8-ppc);
					snp=sop;
				}
			}else if(boardLook[7-a][8-b]==14) {//if the challenged is a spy
				if(boardLook[7-ppr][8-ppc]==28) {
					boardLook[7-a][8-b]=boardLook[7-ppr][8-ppc];
					boardLook[7-ppr][8-ppc]=0;
					snp=((7-a)*9)+(8-b);
					sop=((7-ppr)*9)+(8-ppc);
				}else if(boardLook[7-a][8-b]==boardLook[7-ppr][8-ppc]-15){
					boardLook[7-a][8-b]=0;
					boardLook[7-ppr][8-ppc]=0;
					snp=((7-a)*9)+(8-b);
					sop=((7-ppr)*9)+(8-ppc);
				}else {
					boardLook[7-ppr][8-ppc]=0;
					sop=((7-ppr)*9)+(8-ppc);
					snp=sop;
				}
			}else if(boardLook[7-a][8-b]==15){//if the challenged is a flag
				boardLook[7-a][8-b]=boardLook[7-ppr][8-ppc];
				boardLook[7-ppr][8-ppc]=0;
				snp=((7-a)*9)+(8-b);
				sop=((7-ppr)*9)+(8-ppc);
				end=1;
				win=1;
				
			}
		}
		else{
			if(boardLook[a][b]<28) {
				if(boardLook[ppr][ppc]==14||boardLook[a][b]-15>boardLook[ppr][ppc]){
					boardLook[a][b]=boardLook[ppr][ppc];
					boardLook[ppr][ppc]=0;
					snp=a*9+b;
					sop=ppr*9+ppc;
				}else if(boardLook[a][b]-15==boardLook[ppr][ppc]){
					boardLook[a][b]=0;
					boardLook[ppr][ppc]=0;
					snp=a*9+b;
					sop=ppr*9+ppc;
				}
				else {
					boardLook[ppr][ppc]=0;
					sop=ppr*9+ppc;
					snp=sop;
				}
			}else if(boardLook[a][b]==28) {
				if(boardLook[ppr][ppc]<13) {
					boardLook[a][b]=boardLook[ppr][ppc];
					boardLook[ppr][ppc]=0;
					snp=a*9+b;
					sop=ppr*9+ppc;
				}else if(boardLook[a][b]-15==boardLook[ppr][ppc]){
					boardLook[a][b]=0;
					boardLook[ppr][ppc]=0;
					snp=a*9+b;
					sop=ppr*9+ppc;
				}else {
					boardLook[ppr][ppc]=0;
					sop=ppr*9+ppc;
					snp=sop;
				}
			}else if(boardLook[a][b]==29) {
				if(boardLook[ppr][ppc]==13) {
					boardLook[a][b]=boardLook[ppr][ppc];
					boardLook[ppr][ppc]=0;
					snp=a*9+b;
					sop=ppr*9+ppc;
				}else if(boardLook[a][b]-15==boardLook[ppr][ppc]){
					boardLook[a][b]=0;
					boardLook[ppr][ppc]=0;
					snp=a*9+b;
					sop=ppr*9+ppc;
				}else {
					boardLook[ppr][ppc]=0;
					sop=ppr*9+ppc;
					snp=sop;
				}
			}else if(boardLook[a][b]==30) {
				boardLook[7-a][8-b]=boardLook[7-ppr][8-ppc];
				boardLook[7-ppr][8-ppc]=0;
				snp=a*9+b;
				sop=ppr*9+ppc;
				end=1;
				win=1;
			}
		}
	}
	
	private void markMove() {
		
		if(list[a][b].getIcon()!=null && ((turn==1 && boardLook[7-a][8-b]>15)||(turn==0 && boardLook[a][b]<16))) {//this is if a piece is on the tile
			ppr=a;
			ppc=b;
			if(a<7) {
				if(list[a+1][b].getIcon()==null) {//go down
					list[a+1][b].setBackground(new Color(0, 255, 0));
				}else {
					if(turn==1) {
						if(boardLook[7-a-1][8-b]-15<=0 && boardLook[7-a-1][8-b]-15>-15)
							list[a+1][b].setBackground(new Color(255, 0, 0));
					}
					else {
						if(boardLook[a+1][b]>15)
							list[a+1][b].setBackground(new Color(255, 0, 0));
					}
				}
			}
			if(a>0) {
				if(list[a-1][b].getIcon()==null) {//go up
					list[a-1][b].setBackground(new Color(0, 255, 0));
				}else {
					if(turn==1) {
						if(boardLook[7-a+1][8-b]-15<=0 && boardLook[7-a+1][8-b]-15>-15)
							list[a-1][b].setBackground(new Color(255, 0, 0));
					}
					else {
						if(boardLook[a-1][b]>15)
							list[a-1][b].setBackground(new Color(255, 0, 0));
					}
				}
			}
			if(b<8) {
				if(list[a][b+1].getIcon()==null) {//go right
					list[a][b+1].setBackground(new Color(0, 255, 0));
				}
				else {
					if(turn==1) {
						if(boardLook[7-a][8-b-1]-15<=0 && boardLook[7-a-1][8-b]-15>-15)
							list[a][b+1].setBackground(new Color(255, 0, 0));
					}
					else {
						if(boardLook[a][b+1]>15)
							list[a][b+1].setBackground(new Color(255, 0, 0));
					}
				}
			}
			if(b>0){
				if(list[a][b-1].getIcon()==null) {//go left 
					list[a][b-1].setBackground(new Color(0, 255, 0));
				}
				else {
					if(turn==1) {
						if(boardLook[7-a][8-b+1]-15<=0 && boardLook[7-a-1][8-b]-15>-15)
							list[a][b-1].setBackground(new Color(255, 0, 0));
					}
					else {
						if(boardLook[a][b-1]>15)
							list[a][b-1].setBackground(new Color(255, 0, 0));
					}
				}
			}
		}
	}

	private void mnm(int i,int j) {
		list[i][j].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg1) {
			    a=i;
			    b=j;
			    
				if(a>4 && setCount<15) {//this part makes sure that the pieces are in the right tiles and the list array does not exceed
					if(list[a][b].getIcon()==null) {//this part makes sure nothing is placed on the tile
						boardSetting();
					}
				}
				if(setCount>14 && setCount<17) {
					setPiece.setEnabled(true);
					setPiece.setText("Game Start");
					txtpnThe.setText("GAME START");
					
				}
				/////////////////////////////////////////////////////////////////////////////////////
				//this part is the marking of the possible movement the up down left right 
				//////////////////////////////////////////////////////////////////////////////////////
				
				if(setCount==17) {
					
					Color green = new Color(0, 255, 0);
					Color red = new Color(255, 0, 0);
					/////////////////////////////////////////////////////////////
					//this is for the movement
					////////////////////////////////////////////////////////////
					
					
					if(list[a][b].getBackground().equals(green)) {
						move();
						reloadBoard();
						waiter=false;
					}
					//////////////////////////////////////////////////////////////
					//attack
					//////////////////////////////////////////////////////////////
					
					if(list[a][b].getBackground().equals(red)) {
						chal();
						reloadBoard();
						waiter=false;
					}
					//////////////////////////////////////////////////////////////
					//marks the possible movements
					/////////////////////////////////////////////////////////////
					else {
						
						for(int i1=0; i1<8; i1++) { 
							for(int j1=0; j1<9; j1++) {
								list[i1][j1].setBackground(new Color(139, 69, 19));
							}
						}
						//this marks that tiles red if an enemy green if a free space
						markMove();
						
					}
				}
				 ////////////////////////////////////////////////////
				
				/////////////////////////////////////////////////////
				
			}
		});
	}

	private void initialize2() {
		//This is the initiation of where the other components go into
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Stencil", Font.PLAIN, 18));
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 914, 635);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//this creates the buttons for the board
		JButton grid1_1 = new JButton("");
		grid1_1.setIcon(null);
		grid1_1.setBackground(new Color(139, 69, 19));	
		grid1_1.setBounds(60, 55, 60, 60);
		frame.getContentPane().add(grid1_1);
		
		JButton grid1_2 = new JButton("");
		grid1_2.setIcon(null);
		grid1_2.setBackground(new Color(139, 69, 19));
		grid1_2.setBounds(119, 55, 60, 60);
		frame.getContentPane().add(grid1_2);
			
		
		JButton grid1_3 = new JButton("");
		grid1_3.setIcon(null);
		grid1_3.setBackground(new Color(139, 69, 19));
		grid1_3.setBounds(178, 55, 60, 60);
		frame.getContentPane().add(grid1_3);
		
		JButton grid1_4 = new JButton("");
		grid1_4.setIcon(null);
		grid1_4.setBackground(new Color(139, 69, 19));
		grid1_4.setBounds(237, 55, 60, 60);
		frame.getContentPane().add(grid1_4);
		
		JButton grid1_5 = new JButton("");
		grid1_5.setIcon(null);
		grid1_5.setBackground(new Color(139, 69, 19));
		grid1_5.setBounds(296, 55, 60, 60);
		frame.getContentPane().add(grid1_5);
		
		JButton grid1_6 = new JButton("");
		grid1_6.setIcon(null);
		grid1_6.setBackground(new Color(139, 69, 19));
		grid1_6.setBounds(355, 55, 60, 60);
		frame.getContentPane().add(grid1_6);
		
		JButton grid1_7 = new JButton("");
		grid1_7.setIcon(null);
		grid1_7.setBackground(new Color(139, 69, 19));
		grid1_7.setBounds(414, 55, 60, 60);
		frame.getContentPane().add(grid1_7);
		
		JButton grid1_8 = new JButton("");
		grid1_8.setIcon(null);
		grid1_8.setBackground(new Color(139, 69, 19));
		grid1_8.setBounds(473, 55, 60, 60);
		frame.getContentPane().add(grid1_8);
		
		JButton grid1_9 = new JButton("");
		grid1_9.setIcon(null);
		grid1_9.setBackground(new Color(139, 69, 19));
		grid1_9.setBounds(531, 55, 60, 60);
		frame.getContentPane().add(grid1_9);
		
		JButton grid2_1 = new JButton("");
		grid2_1.setIcon(null);
		grid2_1.setBackground(new Color(139, 69, 19));
		grid2_1.setBounds(60, 115, 60, 60);
		frame.getContentPane().add(grid2_1);
		
		JButton grid2_2 = new JButton("");
		grid2_2.setIcon(null);
		grid2_2.setBackground(new Color(139, 69, 19));
		grid2_2.setBounds(119, 115, 60, 60);
		frame.getContentPane().add(grid2_2);
		
		JButton grid2_3 = new JButton("");
		grid2_3.setIcon(null);
		grid2_3.setBackground(new Color(139, 69, 19));
		grid2_3.setBounds(178, 115, 60, 60);
		frame.getContentPane().add(grid2_3);
		
		JButton grid2_4 = new JButton("");
		grid2_4.setIcon(null);
		grid2_4.setBackground(new Color(139, 69, 19));
		grid2_4.setBounds(237, 115, 60, 60);
		frame.getContentPane().add(grid2_4);
		
		JButton grid2_5 = new JButton("");
		grid2_5.setIcon(null);
		grid2_5.setBackground(new Color(139, 69, 19));
		grid2_5.setBounds(296, 115, 60, 60);
		frame.getContentPane().add(grid2_5);
		
		JButton grid2_6 = new JButton("");
		grid2_6.setIcon(null);
		grid2_6.setBackground(new Color(139, 69, 19));
		grid2_6.setBounds(355, 115, 60, 60);
		frame.getContentPane().add(grid2_6);
		
		JButton grid2_7 = new JButton("");
		grid2_7.setIcon(null);
		grid2_7.setBackground(new Color(139, 69, 19));
		grid2_7.setBounds(414, 115, 60, 60);
		frame.getContentPane().add(grid2_7);
		
		JButton grid2_8 = new JButton("");
		grid2_8.setIcon(null);
		grid2_8.setBackground(new Color(139, 69, 19));
		grid2_8.setBounds(473, 115, 60, 60);
		frame.getContentPane().add(grid2_8);
		
		JButton grid2_9 = new JButton("");
		grid2_9.setIcon(null);
		grid2_9.setBackground(new Color(139, 69, 19));
		grid2_9.setBounds(531, 115, 60, 60);
		frame.getContentPane().add(grid2_9);
		
		JButton grid3_1 = new JButton("");
		grid3_1.setIcon(null);
		grid3_1.setBackground(new Color(139, 69, 19));
		grid3_1.setBounds(60, 173, 60, 60);
		frame.getContentPane().add(grid3_1);
		
		JButton grid3_2 = new JButton("");
		grid3_2.setIcon(null);
		grid3_2.setBackground(new Color(139, 69, 19));
		grid3_2.setBounds(119, 173, 60, 60);
		frame.getContentPane().add(grid3_2);
		
		JButton grid3_3 = new JButton("");
		grid3_3.setIcon(null);
		grid3_3.setBackground(new Color(139, 69, 19));
		grid3_3.setBounds(178, 173, 60, 60);
		frame.getContentPane().add(grid3_3);
		
		JButton grid3_4 = new JButton("");
		grid3_4.setIcon(null);
		grid3_4.setBackground(new Color(139, 69, 19));
		grid3_4.setBounds(237, 173, 60, 60);
		frame.getContentPane().add(grid3_4);
		
		JButton grid3_5 = new JButton("");
		grid3_5.setIcon(null);
		grid3_5.setBackground(new Color(139, 69, 19));
		grid3_5.setBounds(296, 173, 60, 60);
		frame.getContentPane().add(grid3_5);
		
		JButton grid3_6 = new JButton("");
		grid3_6.setIcon(null);
		grid3_6.setBackground(new Color(139, 69, 19));
		grid3_6.setBounds(355, 173, 60, 60);
		frame.getContentPane().add(grid3_6);
		
		JButton grid3_7 = new JButton("");
		grid3_7.setIcon(null);
		grid3_7.setBackground(new Color(139, 69, 19));
		grid3_7.setBounds(414, 173, 60, 60);
		frame.getContentPane().add(grid3_7);
		
		JButton grid3_8 = new JButton("");
		grid3_8.setIcon(null);
		grid3_8.setBackground(new Color(139, 69, 19));
		grid3_8.setBounds(473, 173, 60, 60);
		frame.getContentPane().add(grid3_8);
		
		JButton grid3_9 = new JButton("");
		grid3_9.setIcon(null);
		grid3_9.setBackground(new Color(139, 69, 19));
		grid3_9.setBounds(531, 173, 60, 60);
		frame.getContentPane().add(grid3_9);
		
		JButton grid4_1 = new JButton("");
		grid4_1.setIcon(null);
		grid4_1.setBackground(new Color(139, 69, 19));
		grid4_1.setBounds(60, 233, 60, 60);
		frame.getContentPane().add(grid4_1);
		
		JButton grid4_2 = new JButton("");
		grid4_2.setIcon(null);
		grid4_2.setBackground(new Color(139, 69, 19));
		grid4_2.setBounds(119, 233, 60, 60);
		frame.getContentPane().add(grid4_2);
		
		JButton grid4_3 = new JButton("");
		grid4_3.setIcon(null);
		grid4_3.setBackground(new Color(139, 69, 19));
		grid4_3.setBounds(178, 233, 60, 60);
		frame.getContentPane().add(grid4_3);
		
		JButton grid4_4 = new JButton("");
		grid4_4.setIcon(null);
		grid4_4.setBackground(new Color(139, 69, 19));
		grid4_4.setBounds(237, 233, 60, 60);
		frame.getContentPane().add(grid4_4);
		
		JButton grid4_5 = new JButton("");
		grid4_5.setIcon(null);
		grid4_5.setBackground(new Color(139, 69, 19));
		grid4_5.setBounds(296, 233, 60, 60);
		frame.getContentPane().add(grid4_5);
		
		JButton grid4_6 = new JButton("");
		grid4_6.setIcon(null);
		grid4_6.setBackground(new Color(139, 69, 19));
		grid4_6.setBounds(355, 233, 60, 60);
		frame.getContentPane().add(grid4_6);
		
		JButton grid4_7 = new JButton("");
		grid4_7.setIcon(null);
		grid4_7.setBackground(new Color(139, 69, 19));
		grid4_7.setBounds(414, 233, 60, 60);
		frame.getContentPane().add(grid4_7);
		
		JButton grid4_8 = new JButton("");
		grid4_8.setIcon(null);
		grid4_8.setBackground(new Color(139, 69, 19));
		grid4_8.setBounds(473, 233, 60, 60);
		frame.getContentPane().add(grid4_8);
		
		JButton grid4_9 = new JButton("");
		grid4_9.setIcon(null);
		grid4_9.setBackground(new Color(139, 69, 19));
		grid4_9.setBounds(531, 233, 60, 60);
		frame.getContentPane().add(grid4_9);
		
		JButton grid5_1 = new JButton("");
		grid5_1.setIcon(null);
		grid5_1.setBackground(new Color(139, 69, 19));
		grid5_1.setBounds(60, 293, 60, 60);
		frame.getContentPane().add(grid5_1);
		
		JButton grid5_2 = new JButton("");
		grid5_2.setIcon(null);
		grid5_2.setBackground(new Color(139, 69, 19));
		grid5_2.setBounds(119, 293, 60, 60);
		frame.getContentPane().add(grid5_2);
		
		JButton grid5_3 = new JButton("");
		grid5_3.setIcon(null);
		grid5_3.setBackground(new Color(139, 69, 19));
		grid5_3.setBounds(178, 293, 60, 60);
		frame.getContentPane().add(grid5_3);
		
		JButton grid5_4 = new JButton("");
		grid5_4.setIcon(null);
		grid5_4.setBackground(new Color(139, 69, 19));
		grid5_4.setBounds(237, 293, 60, 60);
		frame.getContentPane().add(grid5_4);
		
		JButton grid5_5 = new JButton("");
		grid5_5.setIcon(null);
		grid5_5.setBackground(new Color(139, 69, 19));
		grid5_5.setBounds(296, 293, 60, 60);
		frame.getContentPane().add(grid5_5);
		
		JButton grid5_6 = new JButton("");
		grid5_6.setIcon(null);
		grid5_6.setBackground(new Color(139, 69, 19));
		grid5_6.setBounds(355, 293, 60, 60);
		frame.getContentPane().add(grid5_6);
		
		JButton grid5_7 = new JButton("");
		grid5_7.setIcon(null);
		grid5_7.setBackground(new Color(139, 69, 19));
		grid5_7.setBounds(414, 293, 60, 60);
		frame.getContentPane().add(grid5_7);
		
		JButton grid5_8 = new JButton("");
		grid5_8.setIcon(null);
		grid5_8.setBackground(new Color(139, 69, 19));
		grid5_8.setBounds(473, 293, 60, 60);
		frame.getContentPane().add(grid5_8);
		
		JButton grid5_9 = new JButton("");
		grid5_9.setIcon(null);
		grid5_9.setBackground(new Color(139, 69, 19));
		grid5_9.setBounds(531, 293, 60, 60);
		frame.getContentPane().add(grid5_9);
		
		JButton grid6_1 = new JButton("");
		grid6_1.setIcon(null);
		grid6_1.setBackground(new Color(139, 69, 19));
		grid6_1.setBounds(60, 353, 60, 60);
		frame.getContentPane().add(grid6_1);
		
		JButton grid6_2 = new JButton("");
		grid6_2.setIcon(null);
		grid6_2.setBackground(new Color(139, 69, 19));
		grid6_2.setBounds(119, 353, 60, 60);
		frame.getContentPane().add(grid6_2);
		
		JButton grid6_3 = new JButton("");
		grid6_3.setIcon(null);
		grid6_3.setBackground(new Color(139, 69, 19));
		grid6_3.setBounds(178, 353, 60, 60);
		frame.getContentPane().add(grid6_3);
		
		JButton grid6_4 = new JButton("");
		grid6_4.setIcon(null);
		grid6_4.setBackground(new Color(139, 69, 19));
		grid6_4.setBounds(237, 353, 60, 60);
		frame.getContentPane().add(grid6_4);
		
		JButton grid6_5 = new JButton("");
		grid6_5.setIcon(null);
		grid6_5.setBackground(new Color(139, 69, 19));
		grid6_5.setBounds(296, 353, 60, 60);
		frame.getContentPane().add(grid6_5);
		
		JButton grid6_6 = new JButton("");
		grid6_6.setIcon(null);
		grid6_6.setBackground(new Color(139, 69, 19));
		grid6_6.setBounds(355, 353, 60, 60);
		frame.getContentPane().add(grid6_6);
		
		JButton grid6_7 = new JButton("");
		grid6_7.setIcon(null);
		grid6_7.setBackground(new Color(139, 69, 19));
		grid6_7.setBounds(414, 353, 60, 60);
		frame.getContentPane().add(grid6_7);
		
		JButton grid6_8 = new JButton("");
		grid6_8.setIcon(null);
		grid6_8.setBackground(new Color(139, 69, 19));
		grid6_8.setBounds(473, 353, 60, 60);
		frame.getContentPane().add(grid6_8);
		
		JButton grid6_9 = new JButton("");
		grid6_9.setIcon(null);
		grid6_9.setBackground(new Color(139, 69, 19));
		grid6_9.setBounds(531, 353, 60, 60);
		frame.getContentPane().add(grid6_9);
		
		JButton grid7_1 = new JButton("");
		grid7_1.setIcon(null);
		grid7_1.setBackground(new Color(139, 69, 19));
		grid7_1.setBounds(60, 411, 60, 60);
		frame.getContentPane().add(grid7_1);
		
		JButton grid7_2 = new JButton("");
		grid7_2.setIcon(null);
		grid7_2.setBackground(new Color(139, 69, 19));
		grid7_2.setBounds(119, 411, 60, 60);
		frame.getContentPane().add(grid7_2);
		
		JButton grid7_3 = new JButton("");
		grid7_3.setIcon(null);
		grid7_3.setBackground(new Color(139, 69, 19));
		grid7_3.setBounds(178, 411, 60, 60);
		frame.getContentPane().add(grid7_3);
		
		JButton grid7_4 = new JButton("");
		grid7_4.setIcon(null);
		grid7_4.setBackground(new Color(139, 69, 19));
		grid7_4.setBounds(237, 411, 60, 60);
		frame.getContentPane().add(grid7_4);
		
		JButton grid7_5 = new JButton("");
		grid7_5.setIcon(null);
		grid7_5.setBackground(new Color(139, 69, 19));
		grid7_5.setBounds(296, 411, 60, 60);
		frame.getContentPane().add(grid7_5);
		
		JButton grid7_6 = new JButton("");
		grid7_6.setIcon(null);
		grid7_6.setBackground(new Color(139, 69, 19));
		grid7_6.setBounds(355, 411, 60, 60);
		frame.getContentPane().add(grid7_6);
		
		JButton grid7_7 = new JButton("");
		grid7_7.setIcon(null);
		grid7_7.setBackground(new Color(139, 69, 19));
		grid7_7.setBounds(414, 411, 60, 60);
		frame.getContentPane().add(grid7_7);
		
		JButton grid7_8 = new JButton("");
		grid7_8.setIcon(null);
		grid7_8.setBackground(new Color(139, 69, 19));
		grid7_8.setBounds(473, 411, 60, 60);
		frame.getContentPane().add(grid7_8);
		
		JButton grid7_9 = new JButton("");
		grid7_9.setIcon(null);
		grid7_9.setBackground(new Color(139, 69, 19));
		grid7_9.setBounds(531, 411, 60, 60);
		frame.getContentPane().add(grid7_9);
		
		JButton grid8_1 = new JButton("");
		grid8_1.setIcon(null);
		grid8_1.setBackground(new Color(139, 69, 19));
		grid8_1.setBounds(60, 471, 60, 60);
		frame.getContentPane().add(grid8_1);
		
		JButton grid8_2 = new JButton("");
		grid8_2.setIcon(null);
		grid8_2.setBackground(new Color(139, 69, 19));
		grid8_2.setBounds(119, 471, 60, 60);
		frame.getContentPane().add(grid8_2);
		
		JButton grid8_3 = new JButton("");
		grid8_3.setIcon(null);
		grid8_3.setBackground(new Color(139, 69, 19));
		grid8_3.setBounds(178, 471, 60, 60);
		frame.getContentPane().add(grid8_3);
		
		JButton grid8_4 = new JButton("");
		grid8_4.setIcon(null);
		grid8_4.setBackground(new Color(139, 69, 19));
		grid8_4.setBounds(237, 471, 60, 60);
		frame.getContentPane().add(grid8_4);
		
		JButton grid8_5 = new JButton("");
		grid8_5.setIcon(null);
		grid8_5.setBackground(new Color(139, 69, 19));
		grid8_5.setBounds(296, 471, 60, 60);
		frame.getContentPane().add(grid8_5);
		
		JButton grid8_6 = new JButton("");
		grid8_6.setIcon(null);
		grid8_6.setBackground(new Color(139, 69, 19));
		grid8_6.setBounds(355, 471, 60, 60);
		frame.getContentPane().add(grid8_6);
		
		JButton grid8_7 = new JButton("");
		grid8_7.setIcon(null);
		grid8_7.setBackground(new Color(139, 69, 19));
		grid8_7.setBounds(414, 471, 60, 60);
		frame.getContentPane().add(grid8_7);
		
		JButton grid8_8 = new JButton("");
		grid8_8.setIcon(null);
		grid8_8.setBackground(new Color(139, 69, 19));
		grid8_8.setBounds(473, 471, 60, 60);
		frame.getContentPane().add(grid8_8);
		
		JButton grid8_9 = new JButton("");
		grid8_9.setIcon(null);
		grid8_9.setBackground(new Color(139, 69, 19));
		grid8_9.setBounds(531, 471, 60, 60);
		frame.getContentPane().add(grid8_9);
		
		//this places the buttons in an array
		list = new JButton[8][9];
		list[0][0]=grid1_1;
		list[0][1]=grid1_2;
		list[0][2]=grid1_3;
		list[0][3]=grid1_4;
		list[0][4]=grid1_5;
		list[0][5]=grid1_6;
		list[0][6]=grid1_7;
		list[0][7]=grid1_8;
		list[0][8]=grid1_9;
		list[1][0]=grid2_1;
		list[1][1]=grid2_2;
		list[1][2]=grid2_3;
		list[1][3]=grid2_4;
		list[1][4]=grid2_5;
		list[1][5]=grid2_6;
		list[1][6]=grid2_7;
		list[1][7]=grid2_8;
		list[1][8]=grid2_9;
		list[2][0]=grid3_1;
		list[2][1]=grid3_2;
		list[2][2]=grid3_3;
		list[2][3]=grid3_4;
		list[2][4]=grid3_5;
		list[2][5]=grid3_6;
		list[2][6]=grid3_7;
		list[2][7]=grid3_8;
		list[2][8]=grid3_9;
		list[3][0]=grid4_1;
		list[3][1]=grid4_2;
		list[3][2]=grid4_3;
		list[3][3]=grid4_4;
		list[3][4]=grid4_5;
		list[3][5]=grid4_6;
		list[3][6]=grid4_7;
		list[3][7]=grid4_8;
		list[3][8]=grid4_9;
		list[4][0]=grid5_1;
		list[4][1]=grid5_2;
		list[4][2]=grid5_3;
		list[4][3]=grid5_4;
		list[4][4]=grid5_5;
		list[4][5]=grid5_6;
		list[4][6]=grid5_7;
		list[4][7]=grid5_8;
		list[4][8]=grid5_9;
		list[5][0]=grid6_1;
		list[5][1]=grid6_2;
		list[5][2]=grid6_3;
		list[5][3]=grid6_4;
		list[5][4]=grid6_5;
		list[5][5]=grid6_6;
		list[5][6]=grid6_7;
		list[5][7]=grid6_8;
		list[5][8]=grid6_9;
		list[6][0]=grid7_1;
		list[6][1]=grid7_2;
		list[6][2]=grid7_3;
		list[6][3]=grid7_4;
		list[6][4]=grid7_5;
		list[6][5]=grid7_6;
		list[6][6]=grid7_7;
		list[6][7]=grid7_8;
		list[6][8]=grid7_9;
		list[7][0]=grid8_1;
		list[7][1]=grid8_2;
		list[7][2]=grid8_3;
		list[7][3]=grid8_4;
		list[7][4]=grid8_5;
		list[7][5]=grid8_6;
		list[7][6]=grid8_7;
		list[7][7]=grid8_8;
		list[7][8]=grid8_9;
		
		
		//this button is to set the pieces and start the game
		setPiece = new JButton("set");
		setPiece.setFont(new Font("Stencil", Font.PLAIN, 20));
		setPiece.setBackground(new Color(50, 205, 50));
		setPiece.setBounds(701, 528, 152, 43);
		frame.getContentPane().add(setPiece);
		
		
		//this is what prints out the instructions and what is happening in-game
		txtpnThe = new JTextPane();
		txtpnThe.setEditable(false);
		txtpnThe.setText("Click Set \n and \n Place Your Pieces \n First to Place is the 5 Star General \n The pieces can only be placed in the bottom 3 rows");
		txtpnThe.setBounds(668, 86, 180, 203);
		frame.getContentPane().add(txtpnThe);
		
		
		///////////////////////////////////////
		//This prints the labels for the board
		//////////////////////////////////////
		JLabel lblA = new JLabel("A");
		lblA.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblA.setBounds(79, 21, 31, 33);
		frame.getContentPane().add(lblA);
		
		JLabel lblB = new JLabel("B");
		lblB.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblB.setBounds(138, 21, 31, 33);
		frame.getContentPane().add(lblB);
		
		JLabel lblD = new JLabel("D");
		lblD.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblD.setBounds(257, 21, 31, 33);
		frame.getContentPane().add(lblD);
		
		JLabel lblC = new JLabel("C");
		lblC.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblC.setBounds(198, 21, 31, 33);
		frame.getContentPane().add(lblC);
		
		JLabel lblF = new JLabel("F");
		lblF.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblF.setBounds(377, 21, 31, 33);
		frame.getContentPane().add(lblF);
		
		JLabel lblE = new JLabel("E");
		lblE.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblE.setBounds(313, 21, 31, 33);
		frame.getContentPane().add(lblE);
		
		JLabel lblH = new JLabel("H");
		lblH.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblH.setBounds(488, 21, 31, 33);
		frame.getContentPane().add(lblH);
		
		JLabel lblG = new JLabel("G");
		lblG.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblG.setBounds(431, 21, 31, 33);
		frame.getContentPane().add(lblG);
		
		JLabel lblI = new JLabel("I");
		lblI.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblI.setBounds(553, 21, 31, 33);
		frame.getContentPane().add(lblI);
		
		JLabel label = new JLabel("1");
		label.setFont(new Font("Tahoma", Font.BOLD, 25));
		label.setBounds(24, 66, 31, 33);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("2");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 25));
		label_1.setBounds(24, 126, 31, 33);
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("3");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 25));
		label_2.setBounds(24, 188, 31, 33);
		frame.getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("4");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 25));
		label_3.setBounds(24, 248, 31, 33);
		frame.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("5");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 25));
		label_4.setBounds(24, 308, 31, 33);
		frame.getContentPane().add(label_4);
		
		JLabel label_5 = new JLabel("6");
		label_5.setFont(new Font("Tahoma", Font.BOLD, 25));
		label_5.setBounds(24, 368, 31, 33);
		frame.getContentPane().add(label_5);
		
		JLabel label_6 = new JLabel("7");
		label_6.setFont(new Font("Tahoma", Font.BOLD, 25));
		label_6.setBounds(24, 424, 31, 33);
		frame.getContentPane().add(label_6);
		
		JLabel label_7 = new JLabel("8");
		label_7.setFont(new Font("Tahoma", Font.BOLD, 25));
		label_7.setBounds(24, 484, 31, 33);
		frame.getContentPane().add(label_7);
		
		JLabel lblGame = new JLabel("Game");
		lblGame.setForeground(new Color(255, 0, 0));
		lblGame.setFont(new Font("Stencil", Font.PLAIN, 20));
		lblGame.setBackground(new Color(160, 82, 45));
		lblGame.setBounds(722, 66, 82, 20);
		frame.getContentPane().add(lblGame);
		
		JButton btnResign = new JButton("RESIGN");
		btnResign.setBackground(Color.RED);
		btnResign.setFont(new Font("Stencil", Font.PLAIN, 14));
		btnResign.setBounds(722, 333, 89, 23);
		frame.getContentPane().add(btnResign);
		
		
		/////////////////////////////////////////////////////////////////////////
		//Quiting the game
		////////////////////////////////////////////////////////////////////////
		btnResign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(done==0) {
					try {
						networkOutput = new PrintWriter(socket.getOutputStream(),true);
						networkOutput.println("0");
					} catch (IOException e) {
						e.printStackTrace();
					} 
					btnResign.setText("Quit");
					done=1;
				}else {
					try{
						System.out.println("\nClosing connection…");
						socket.close();
					}catch(IOException ioEx){
						System.out.println("Unable to disconnect!");
						System.exit(1);
					}
				
				}
				
				
			}
		});
		
		/////////////////////////////////////////////////////////////////////////////////////
		//Game setting the action listener for the game to start
		/////////////////////////////////////////////////////////////////////////////////////
		setPiece.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//this one looks if the game is already started
				if(setCount>14) {
					setCount++;
				}else {//if the board is to be set
					setPiece.setEnabled(false);
				}
				if(setCount==16) {//once the board has been set go on to the game
					NetCount++;
					waiter=false;
					setPiece.setEnabled(false);
					
					setCount++;
				}
				if(setCount==0) {//so there will only be one action listener
					for(int i=0; i<8; i++) {
						for(int j=0; j<9; j++) {
							mnm(i,j);
						}
					}
				}
				
			}
		});
		
		//////////////////////////////////////////////////
	}

}