package gameLogic;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {

	int m;
	int n;
	Coordinate[][] board;
	
	public Board(int numberOfRows, int numberOfColumns)
	{
		this.m = numberOfRows;
		this.n = numberOfColumns;
		this.board = new Coordinate[this.m][this.n];
		
		for(int i =0;i<this.m;i+=1)
		{
			for(int j=0;j<this.n;j+=1)
			{
				this.board[i][j] = new Coordinate(i,j);
			}
		}
	}
	
	public boolean checkValidCoordinate(int i, int j)
	{
		if(i>=0 && i<this.m && j>=0 && j<this.n)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void displayBoard()
	{
		for(int i=0;i<this.m;i+=1)
		{
			for(int j=0;j<this.n;j+=1)
			{
				System.out.print(""+board[i][j].value+board[i][j].color.charAt(0)+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void setCoordinateOfBoard(int i,int j,int value)
	{
		this.board[i][j].xCoordinate = i;
		this.board[i][j].yCoordinate = j;
		this.board[i][j].value = value;
	}
	
	public int getValueAtCoordinate(int i, int j)
	{
		return this.board[i][j].value;
	}
	
	public int getCriticalMass(int i, int j)
	{
		if ((i==0 && j==0) || (i==0 && j==this.n-1) || (i==this.m-1 && j==0) || (i==this.m-1 && j==this.n-1))
		{
			return 2;
		}
		else if((i==0) || (i==this.m-1) || (j==0) || (j==this.n-1))
		{
			return 3;
		}
		else
		{
			return 4;
		}
	}
	
	public int playerCount(int playerStatus)
	{
		int count = 0;
		for(int i=0;i<this.m;i+=1)
		{
			for(int j=0;j<this.n;j+=1)
			{
				if(this.board[i][j].playerStatus==playerStatus)
				{
					count+=1;
				}
			}
		}
		return count;
	}
	
	public ArrayList<Coordinate> getListOfNeighbours(int i, int j)
	{
		ArrayList<Coordinate> allNeighbours = new ArrayList<Coordinate>();
		if(this.checkValidCoordinate(i, j+1))
		{
			allNeighbours.add(this.board[i][j+1]);
		}
		if(this.checkValidCoordinate(i, j-1))
		{
			allNeighbours.add(this.board[i][j-1]);
		}
		if(this.checkValidCoordinate(i+1, j))
		{
			allNeighbours.add(this.board[i+1][j]);
		}
		if(this.checkValidCoordinate(i-1, j))
		{
			allNeighbours.add(this.board[i-1][j]);
		}
		return allNeighbours;
	}
	
	public static int countAllActivePlayers(ArrayList<Player> allPlayers)
	{
		int count = 0;
		for(int i=0;i<allPlayers.size();i+=1)
		{
			if(allPlayers.get(i).active)
			{
				count+=1;
			}
		}
		return count;
	}
	
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		System.out.println("Enter the dimensions of the board");
		System.out.println("Enter the number of Rows");
		int m = s.nextInt();
		System.out.println("Enter the number of Columns");
		int n = s.nextInt();
		Board b = new Board(m,n);
		System.out.println("Enter total number of Players (2 to 8)");
		int numberOfPlayers = s.nextInt();
		ArrayList<Player> allPlayers = new ArrayList<Player>();
		for(int i=0;i<numberOfPlayers;i+=1)
		{
			System.out.println("Enter the colour of Player "+(i+1));
			String colour = s.next();
			Player p = new Player(i+1,colour);
			allPlayers.add(p);
		}
		
		int i =0;
		while(i<numberOfPlayers)
		{
			System.out.println("Enter the co-ordinates of Player "+(i+1));
			int x = s.nextInt();
			int y = s.nextInt();
			allPlayers.get(i).move(b, x, y);
			if(b.playerCount(i+1)>0)
			{
				allPlayers.get(i).orbCount = 1;
			}
			else
			{
				i-=1;
			}
			b.displayBoard();
			i+=1;
		}
		
		int currentPlayer = 0;
		while(countAllActivePlayers(allPlayers)!=1)
		{
			if(allPlayers.get(currentPlayer).active)
			{
				System.out.println("Enter the co-ordinates of Player "+(currentPlayer+1));
				int x = s.nextInt();
				int y = s.nextInt();
				allPlayers.get(currentPlayer).move(b, x, y);
				if(b.playerCount(currentPlayer+1)<=allPlayers.get(currentPlayer).orbCount)
				{
					currentPlayer = (currentPlayer - 1) % numberOfPlayers;
				}
				b.displayBoard();
				
				for(int j=0;j<numberOfPlayers;j+=1)
				{
					if(allPlayers.get(j).active)
					{
						allPlayers.get(j).orbCount = b.playerCount(j+1);
						if(allPlayers.get(j).orbCount==0)
						{
							allPlayers.get(j).active=false;
						}
					}
				}
			}
			currentPlayer = (currentPlayer + 1) % numberOfPlayers;
		}
		
		for(int j=0;j<allPlayers.size();j+=1)
		{
			if(allPlayers.get(j).active)
			{
				System.out.println("The Winner is Player " + allPlayers.get(j).playerNumber+" "+allPlayers.get(j).colour);
				break;
			}
		}
	}
}