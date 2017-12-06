package Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.JLabel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;



public class ServerGUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private static ServerSocket serverSocket = null;
	private static Socket client = null;
	private static int PORT = 1234;
	private static InetAddress ip;
    private static String hostname;
    private static Board item;
    private static ServerGUI window;
    static int a=0;
	/**
	 * Launch the application.
	 */
    
    static Integer any = new Integer(0);
    
	public static void main(String[] args) {
		
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        } catch (UnknownHostException e) {
 
            e.printStackTrace();
        }
        
        
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new ServerGUI();
					window.frame.setVisible(true);
				
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			
			}
			
		});
		
		
		try{
			serverSocket = new ServerSocket(PORT);
		}
		catch (IOException e){
			System.out.println("\nUnable to set up port!");
			//System.exit(1);
		}

		int[][] arr= new int[8][9];
		for (int i = 0; i < 8; i++) {
			for(int j =0;j<9; j++) {
	    		arr[i][j]=0;
	    	}
	    }
		item = new Board(arr);
		
		a=1;
		do{ 
			try {
				
				client = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.print("\nNew client accepted.\n");
			ClientHandler handler = new ClientHandler(client,item);
			handler.start();
		}while (true);
		
	}

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 438, 368);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblHostName = new JLabel("Host Name:");
		lblHostName.setBounds(10, 54, 101, 29);
		frame.getContentPane().add(lblHostName);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setText(hostname);
		textField.setBounds(96, 58, 319, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setText(ip.toString());
		textField_1.setBounds(96, 94, 319, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(10, 97, 72, 14);
		frame.getContentPane().add(lblIpAddress);
		
		textField_2 = new JTextField();
		textField_2.setText("1234");
		textField_2.setEditable(false);
		textField_2.setBounds(96, 138, 112, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		
		JLabel lblSetPort = new JLabel("Port Number:");
		lblSetPort.setBounds(10, 141, 80, 14);
		frame.getContentPane().add(lblSetPort);
		
		
	}
}
