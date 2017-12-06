package Server;

class Board
{
	private int[][][] numResources=new int[2][8][9];
	private int[] placed= {0,0};
	private int player = -1;
	
	private int[] rnp= {-1,-1},rop= {-1,-1},end= {0,0},win= {0,0};
	private String[] playerNames = new String[4];
	
	public Board(int[][] startLevel){
		
		numResources[0] = startLevel;
		System.out.println("adsf");
		numResources[1] = startLevel;
		
	}
	public int[][] getBoard(int bn){
		return numResources[bn];
	}
	public int getEnd(int bn){
		return end[bn];
	}
	public int getWin(int bn){
		return win[bn];
	}
	public int getPlaced(int bn){
		return placed[bn];
	}
	public int getRop(int bn){
		return rop[bn];
	}
	public int getRnp(int bn){
		return rnp[bn];
	}
	public synchronized int[][] PlacePiece(int bn,int[][] stuff){
		placed[bn]++;
		System.out.print(placed[bn]);
		if(placed[bn]%2==1) {
			numResources[bn]=stuff;
		}else if(placed[bn]%2==0) {
			for(int i=0;i<3;i++) {
				for(int j=0;j<9;j++) {
					if(stuff[7-i][8-j]!=0) {
						numResources[bn][i][j]=stuff[7-i][8-j]+15;
					}
				}
			}
		}
		try{
			while (placed[bn]%2 == 1)
				wait();
		
			notifyAll();
			
		}
		catch (InterruptedException interruptEx){
			System.out.println(interruptEx);
		}
		return numResources[bn];
	}
	public synchronized int[][] move(int bn, int np, int op){
		placed[bn]++;
		notifyAll();
		rnp[bn]=np;
		rop[bn]=op;
		if(placed[bn]%2==1) {
			
			while (placed[bn]%2 == 1) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			
			while (placed[bn]%2 == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		numResources[bn][np/9][np%9]=numResources[bn][op/9][op%9];
		numResources[bn][op/9][op%9]=0;
		return numResources[bn];
	}
	public synchronized void setEnd(int bn,int ed) {
		end[bn]=ed;
	}
	public synchronized void setWin(int bn,int w) {
		win[bn]=w;
	}
	public synchronized int getPlayer(String pn) {
		player++;
		notifyAll();
		playerNames[player]=pn;
		return  player;
	}
	public synchronized String getPlayer1() {
		return  playerNames[0];
	}
	public synchronized String getPlayer2() {
		while (player<1) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return  playerNames[1];
	}
	public synchronized String getPlayer3() {
		while (player<2) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return  playerNames[2];
	}
	public synchronized String getPlayer4() {
		while (player<3) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return  playerNames[3];
	}
	public synchronized void first(int bn) {
		placed[bn]++;
		if(placed[bn]%2==1) {
			
			while (placed[bn]%2 == 1) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			
			while (placed[bn]%2 == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
}