package Server;

class Board
{
	private int[][] numResources;
	private int placed= 0,rnp=-1,rop=-1;
	public Board(int[][] startLevel){
		numResources = startLevel;
	}
	public int[][] getBoard(){
		return numResources;
	}
	public int getPlaced(){
		return placed;
	}
	public int getRop(){
		return rop;
	}
	public int getRnp(){
		return rnp;
	}
	public synchronized int[][] PlacePiece(int[][] stuff){
		placed++;
		if(placed%2==1) {
			numResources=stuff;
		}else if(placed%2==0) {
			for(int i=0;i<3;i++) {
				for(int j=0;j<9;j++) {
					if(stuff[7-i][8-j]!=0) {
						numResources[i][j]=stuff[7-i][8-j]+15;
					}
				}
			}
		}
		try{
			while (placed%2 == 1)
				wait();
		
			notifyAll();
			
		}
		catch (InterruptedException interruptEx){
			System.out.println(interruptEx);
		}
		return numResources;
	}
	public synchronized int[][] move(int np, int op){
		placed++;
		notifyAll();
		rnp=np;
		rop=op;
		if(placed%2==1) {
			
			while (placed%2 == 1) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			
			while (placed%2 == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		numResources[np/9][np%9]=numResources[op/9][op%9];
		numResources[op/9][op%9]=0;
		return numResources;
	}
	public synchronized void first() {
		placed++;
		if(placed%2==1) {
			
			while (placed%2 == 1) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			
			while (placed%2 == 0) {
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