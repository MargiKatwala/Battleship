/* 
 * Project 4: Battleship
 * Author: Margi Katwala (mkatwa3), Rime Brika (rbrika2), Anusha Pai (apai7)
 * Professor Troy and Wei
 * Fall 2017
 * Board: in charge of initializing the board and communicated with opponent via sockets (server and client)
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;
import javax.swing.JSplitPane;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Board extends JFrame implements Serializable{

	public Container container;
	private JPanel shipGrid;
	private JPanel shotGrid;
	private JPanel interactiveShipGrid;
	private JPanel interactiveHMGrid;
	public JLabel status;
	public ArrayList<PuzzlePiece[]> shipGridList;
	private static ArrayList<PuzzlePiece[]> shotGridList;
	private ArrayList<PuzzlePiece> interactiveShipList;
	private ArrayList<PuzzlePiece> interactiveHMList;
	private String mode;
	boolean placeShipMode=true;
	private PuzzlePiece clickedPiece = null;
	private int horizVertOption = 0;
	boolean allowUserToPlaceShip = false;
	private int shipNum = -1;
	private JSplitPane splitPane2;
	private String[] letter={"","A","B","C","D","E","F","G","H","I","J"};
	public static int attempts=0;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public ArrayList<PuzzlePiece[]> opponentShipList;
	public PuzzlePiece puzzlePiece; // input from opponent
	private boolean connectionEstablished = false;

	public Board(String name) {
		shipGridList = new ArrayList<PuzzlePiece[]>();
		shotGridList = new ArrayList<PuzzlePiece[]>();

		PuzzlePiece[] tmp = null;
		for(int i = 0; i < 11; i++) {
			tmp = new PuzzlePiece[11];
			shipGridList.add(tmp);
		}

		for(int i = 0; i < 11; i++) {
			tmp = new PuzzlePiece[11];
			shotGridList.add(tmp);
		}

		interactiveShipList = new ArrayList<PuzzlePiece>();
		interactiveHMList = new ArrayList<PuzzlePiece>();
		setTitle(name); // initially name = "board"
		mode=name;
		initialSetUp();
	}
	public void connectionEstablished() {
		connectionEstablished = true;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public boolean placeShips() {
		while(!finishedPlacingShips()) {} // loop until all ships have been placed
		return true;
	}

	public void setMode(String name){
		mode=name;
		setTitle(name);
	}
	public void initialSetUp() {
		String imageName = "image_";
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container = getContentPane();
		container.setLayout(new BorderLayout());

		// contains all ships and opponents hits/misses
		shipGrid = new JPanel();
		shipGrid.setLayout(new GridLayout(11,11));

		// contains your hits/misses
		shotGrid = new JPanel();
		shotGrid.setLayout(new GridLayout(11,11));

		PuzzlePiece pieceShip = null;
		PuzzlePiece pieceShot = null;
		for(int row = 0; row < 11; row++) {
			for(int col = 0; col < 11; col++) {
				if(row==0 && col>=1){
					pieceShip = new PuzzlePiece(letter[col], row, col);
					pieceShot = new PuzzlePiece(letter[col], row, col);

				}
				else if(col==0 && row>=1){
					pieceShip = new PuzzlePiece((""+row), row, col);
					pieceShot = new PuzzlePiece((""+row), row, col);

				}
				else{
					imageName = "batt100.gif";
					pieceShip = new PuzzlePiece(row, col);
					pieceShip.setIcon(new ImageIcon(imageName));
					pieceShip.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							PuzzlePiece piece=((PuzzlePiece)e.getSource());
							if(!connectionEstablished) {
								return;
							}
							if(placeShipMode){
								if(placeShips(piece)==true) {

									if(finishedPlacingShips()==true) {
										removeInteractiveShipGrid();
									}
								}
							}
							else if (!placeShipMode){
								interact(piece);
							}


						}

					});

					pieceShot = new PuzzlePiece(row, col);
					pieceShot.setIcon(new ImageIcon(imageName));

				}
				shipGridList.get(row)[col] = pieceShip;
				shipGrid.add(pieceShip);

				shotGridList.get(row)[col] = pieceShot;
				shotGrid.add(pieceShot);

			}
		}

		PuzzlePiece piece = null;
		interactiveShipGrid = new JPanel();
		interactiveShipGrid.setLayout(new GridLayout(1,5));
		String[] shipNames = {"Aircraft Carrier [size: 5]", "Battleship [size: 4]",
				"Destroyer [size: 3]", "Submarine [size: 3]", "Patrol Boat [size: 2]"};

		for(int row = 0; row < 5; row++) {
			piece = new PuzzlePiece(shipNames[row], row, 0);
			piece.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(!connectionEstablished) {
						return;
					}

					// prompt user if they want to place it horizontally or vertically
					Object[] options = {"Vertical", "Horizontal"};
					horizVertOption = JOptionPane.showOptionDialog(null,
							"Would you like the ship to be placed horizontally or vertically?",
							"Direction", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[1]); // vertical = 0 ; horizontal = 1
					allowUserToPlaceShip = true;
					clickedPiece = (PuzzlePiece)e.getSource();

					// get number of positions the ship needs
					if(clickedPiece.getRow() == 0) {
						shipNum = Constants.SHIP_1;
					}
					else if(clickedPiece.getRow() == 1) {
						shipNum = Constants.SHIP_2;
					}
					else if(clickedPiece.getRow() == 2) {
						shipNum = Constants.SHIP_3;
					}
					else if(clickedPiece.getRow() == 3) {
						shipNum = Constants.SHIP_4;
					}
					else
						shipNum = Constants.SHIP_5;
				}
			});
			interactiveShipList.add(piece);
			interactiveShipGrid.add(piece);
		}

		interactiveHMGrid = new JPanel();
		interactiveHMGrid.setLayout(new GridLayout(1,2));
		for(int row = 0; row < 2; row++) {
			piece = new PuzzlePiece(row, 0);
			//TODO piece.setIcon("imagename.jpg")

			interactiveHMList.add(piece);
			interactiveHMGrid.add(piece);
		}

		status= new JLabel("Establish the connection by becoming the server or client");

		Menu menuBar = new Menu();

		splitPane2 = new JSplitPane();
		splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane2.setBottomComponent(shipGrid);
		splitPane2.setTopComponent(shotGrid);

		container.add(splitPane2,BorderLayout.NORTH);
		container.add(interactiveShipGrid,BorderLayout.SOUTH);

		container.add(status,BorderLayout.CENTER);
		setJMenuBar(menuBar);
		pack();
		setSize(750 , 750);
		setVisible(true);
	}

	// reset all global vars
	void resetGlobalVarForShips(){
		clickedPiece = null;
		horizVertOption = 0;
		allowUserToPlaceShip = false;
		shipNum = -1;
	};

	// check if a ship with size shipNum can be placed horizontally from position row,col
	public boolean canPlaceShipHereHorizontal(PuzzlePiece piece, int row, int col, int shipNum) {
		if((piece.getCol()+shipNum) > 11) {
			return false;
		}

		for(int i = col; i < col+shipNum; i++) {
			if(shipGridList.get(row)[i].isShipPlaced()) {
				return false;
			}
		}

		return true;
	}

	// return true if the given ship can be placed at given position; else return false
	public boolean canPlaceShipHereVertial(PuzzlePiece piece, int row, int col, int shipNum) {
		if((piece.getRow()+shipNum) > 11) {
			return false;
		}

		for(int i = row; i < row+shipNum; i++) {
			if(shipGridList.get(i)[col].isShipPlaced()) {
				return false;
			}
		}

		return true;
	}

	// check if all ships have been placed
	public boolean finishedPlacingShips() {
		for(int i = 0; i < interactiveShipList.size(); i++) {
			if(!interactiveShipList.get(i).isShipPlaced()) {
				return false;
			}
		}
		return true;
	}

	// user finished adding ships to grid, don't need interactive ship grid anymore
	public void removeInteractiveShipGrid() {
		//container.remove(interactiveShipGrid);
		interactiveShipGrid.setVisible(false);
		//        container.revalidate();
		//        container.repaint();
		placeShipMode=false;
		if(mode.equals("Server")) {
			//loops until client to send list
			ArrayList<PuzzlePiece[]>list=shipGridList;
			sendList();
			setLabel(Constants.YOUR_TURN);

			getShipListFromOpponent();



		}
		if(mode.equals("Client")) {

			//send list to server
			sendList();
			setLabel(Constants.NOT_YOUR_TURN);

			getShipListFromOpponent();
		}


	}
	public boolean placeShips(PuzzlePiece piece){
		if(allowUserToPlaceShip && shipNum >= 2) {
			// check if user can place a ship at this specific spot
			int col = (piece).getCol();
			int row = (piece).getRow();
			String shipName = "horiz_1.gif";
			if(horizVertOption == 1) { // horizontal
				shipName = "horiz_";
				if(canPlaceShipHereHorizontal(piece,row,col,shipNum)){
					for(int i = 1; i < shipNum; i++) {
						shipName = shipName + i + ".gif";
						//then place ship to the right
						shipGridList.get(row)[col+(i-1)].setIcon(new ImageIcon(shipName));
						shipGridList.get(row)[col+(i-1)].setShipPlaced(true);
						shipName = "horiz_";
					}
					// place last part of ship
					shipGridList.get(row)[col+(shipNum-1)].setIcon(new ImageIcon("horiz_5.gif"));
					shipGridList.get(row)[col+(shipNum-1)].setShipPlaced(true);
					clickedPiece.setEnabled(false);
					clickedPiece.setShipPlaced(true);
				} else {
					JOptionPane.showMessageDialog(null, "Invalid placement of ship");
				}

			} else if(horizVertOption == 0) { // vertical
				if(canPlaceShipHereVertial(piece,row,col,shipNum)){
					//then place ship down
					shipName = "verti_";
					//((PuzzlePiece)e.getSource()).setIcon(new ImageIcon("verti_1.gif"));
					for(int i = 1; i < shipNum; i++) {
						shipName = shipName + i + ".gif";
						//then place ship to the right
						shipGridList.get(row+(i-1))[col].setIcon(new ImageIcon(shipName));
						shipGridList.get(row+(i-1))[col].setShipPlaced(true);
						shipName = "verti_";
						clickedPiece.setEnabled(false);
						clickedPiece.setShipPlaced(true);
					}
					shipGridList.get(row+(shipNum-1))[col].setIcon(new ImageIcon("verti_5.gif"));
					shipGridList.get(row+(shipNum-1))[col].setShipPlaced(true);
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid placement of ship");
				}
			}

		}
		resetGlobalVarForShips();
		return true;
	}

	public void setLabel(String s) {
		status.setText(s);
		super.update(this.getGraphics());
	}

	public void placeHitatGivenPostion(int row, int col) {
		shotGridList.get(row)[col].setIcon(new ImageIcon("hit.gif"));
		shotGridList.get(row)[col].setHit(true);
		container.validate();
		container.repaint();
	}

	public void placeMissatGivenPostion(int row, int col) {
		shotGridList.get(row)[col].setIcon(new ImageIcon("miss.gif"));
		shotGridList.get(row)[col].setHit(false);
		container.validate();
		container.repaint();
	}


	//Interaction with the Opponent
	public void interact(PuzzlePiece piece) {
		try {
			if(opponentShipList==null) {
				return;
			}

			// check if ship
			if(isShipAtGivenPosition(piece, opponentShipList)) {
				setLabel("It was a hit!");
				// change icon color of shotGrid
				placeHitatGivenPostion(piece.getRow(), piece.getCol());
				piece.setHit(true);
			}
			else {
				setLabel("Sorry. That was a miss.");
				placeMissatGivenPostion(piece.getRow(), piece.getCol());
				piece.setHit(false);

			}
			setLabel(Constants.NOT_YOUR_TURN);

			container.revalidate();
			container.repaint();


			// write board
			out.writeObject(piece);
			out.flush();

			if(gameOver()  ) {}

			if(isOpponentDone()) { return; }

			if(loopForRead()) { } // begins its own loop to get input to change own ship gri

			//System.out.println("board disabled");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}


	// check if the opponnet has won or not
	public boolean isOpponentDone() {
		int hit=0;
		PuzzlePiece piece;
		for(int r=0;r<11;r++){
			for(int c=0;c<11;c++){
				if(shipGridList.get(r)[c].isHit()){
					hit++;
				}
			}
		}
		if(hit == 17) {
			setLabel("You lost. Game over.");
			return true;
		}
		return false;
	}

	// gets the ship list from opponent using input stream
	public void getShipListFromOpponent() {
		opponentShipList=null;
		while(opponentShipList == null) {
			try {
				opponentShipList= (ArrayList<PuzzlePiece[]>)in.readObject();
			}catch(Exception ex){
				//skip initial read object
			}
		}
	}

	// wait for input from opponent
	public boolean loopForRead() {
		try {
			puzzlePiece= (PuzzlePiece)in.readObject();
			setLabel(Constants.YOUR_TURN);


			if(puzzlePiece.getText().equals("Done")) {
				Object[] options = {"Yes", "No"};
				int sc = JOptionPane.showOptionDialog(null,
						"You lost. Would you like to play again?",
						"SC", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]); // Yes = 0 ; No = 1
				if(sc == 1) {
					try{
						out.flush();
						out.close();
					}
					catch(Exception ex){}
					Game.closeSockets();
					System.exit(1);
				} else {
					set();
				}

			} else if(puzzlePiece.getText().equals("Exit")) {

				try{
					out.flush();
				}
				catch(Exception ex){}
				//in.flush();
				//Game.closeSockets();
				System.exit(1);
			}


			if(puzzlePiece.isHit()) {
				//    shotGridList.get(puzzlePiece.getRow())[puzzlePiece.getCol()].setIcon(new ImageIcon("hit.gif"));
				shipGridList.get(puzzlePiece.getRow())[puzzlePiece.getCol()].setIcon(new ImageIcon("batt100.gif"));
				shipGridList.get(puzzlePiece.getRow())[puzzlePiece.getCol()].setHit(true);

			}
			else if(!puzzlePiece.isHit()){
				//    shotGridList.get(puzzlePiece.getRow())[puzzlePiece.getCol()].setIcon(new ImageIcon("miss.gif"));
				shipGridList.get(puzzlePiece.getRow())[puzzlePiece.getCol()].setIcon(new ImageIcon("batt100.gif"));
				shipGridList.get(puzzlePiece.getRow())[puzzlePiece.getCol()].setHit(false);

			}
		}catch(Exception ex){

		}

		return true;
	}



	// check if ship is at given position. return true if it is, else return false.
	private boolean isShipAtGivenPosition(PuzzlePiece piece, ArrayList<PuzzlePiece[]> list) {
		int row = piece.getRow();
		int col = piece.getCol();

		if(list.get(row)[col].isShipPlaced()) {
			return true;
		}
		return false;
	}

	// send ship grid list through stream to opponent
	public void sendList() {
		try {
			out.flush();
			out.reset();

			out.writeObject(shipGridList);
			out.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// return true if user has won; else, return false.
	public boolean gameOver(){
		int hit=0;
		PuzzlePiece piece;
		for(int r=0;r<11;r++){
			for(int c=0;c<11;c++){
				if(shotGridList.get(r)[c].isHit()){
					hit++;
				}
			}
		}
		if(hit==17){
			setLabel("Congrats! You won.");
			Object[] options = {"Yes", "No"};
			int sc = JOptionPane.showOptionDialog(null,
					"Congratulations! You won! Would you like to play again?",
					"SC", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]); // Yes = 0 ; No = 1
			if(sc == 1) {
				PuzzlePiece exitGame =new PuzzlePiece("Exit",0,0);

				try{
					out.flush();
				}
				catch(Exception ex){}
				//in.flush();
				// Game.closeSockets();
				System.exit(1);
			} else {
				PuzzlePiece newGame=new PuzzlePiece("Done",0,0);
				//getContentPane().removeAll();
				try{
					out.writeObject(newGame);
					//out.close();
				}
				catch(Exception ex){}
				// restart game*/

				set();
			}
			return true;
		}
		return false;

	} 

	// reset the board if the user wants to play another game.
	public void set(){

		for(int row =0 ; row < 11 ; row++)
		{
			for(int col =0 ; col < 11 ; col++)
			{
				if(row==0 && col>=1){}
				else if (col==0 && row>=1){}
				else{
					shipGridList.get(row)[col].setIcon(new ImageIcon("batt100.gif"));
					shipGridList.get(row)[col].setShipPlaced(false);
					shipGridList.get(row)[col].setHit(false);
					shotGridList.get(row)[col].setIcon(new ImageIcon("batt100.gif"));
					shotGridList.get(row)[col].setShipPlaced(false);
					shotGridList.get(row)[col].setHit(false);                }
			}
		}
		for(int i=0;i<5;i++){
			interactiveShipList.get(i).setShipPlaced(false);
			interactiveShipList.get(i).setEnabled(true);
		}
		interactiveShipGrid.show(true);
		container.revalidate();
		container.repaint();
		status.setVisible(true);
		placeShipMode = true;


	}

	// gets tstats for shot grid
	public static void statsForShotGrid(){
		int shotNeedtoFinish=0;
		double percentH=0;
		double percentM=0;
		int hit=0;
		PuzzlePiece piece;
		for(int r=0;r<11;r++){
			for(int c=0;c<11;c++){
				if(shotGridList.get(r)[c].isHit()){
					hit++;
				}
			}
		}

		int miss=attempts-hit;

		shotNeedtoFinish=17-hit;
		percentH=((double)hit/attempts);

		percentM=((double)miss/attempts);

		JOptionPane.showMessageDialog(null, "Statistics For Player\nShots needed to finish "+
				shotNeedtoFinish+"%\nPercent Hits "+percentH+" \n%Percent Miss "+percentM+"\nTotal Attempts"+attempts);
	}
}
