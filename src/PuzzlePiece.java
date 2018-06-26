/*
 * Project 4: Battleship
 * Author: Margi Katwala (mkatwa3), Rime Brika (rbrika2), Anusha Pai (apai7)
 * Professor Troy and Wei
 * Fall 2017
 * PuzzlePiece: holder for row and column of board, hit, and ships
 */

import javax.swing.JButton;

@SuppressWarnings("serial")
public class PuzzlePiece extends JButton {

	private boolean isShipPlaced;
	private int row, col; // of the board
	private boolean isHit;

	// default constructor
	public PuzzlePiece(int row, int col) {
		super("");
		isShipPlaced = false;
		this.row = row;
		this.col = col;
		this.isHit = false;
	}
	
	public boolean isHit() {
		return isHit;
	}
	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}
	public PuzzlePiece(String s, int row, int col) {
		setText(s);
		isShipPlaced = false;
		this.row = row;
		this.col = col;
	}

	public boolean isShipPlaced() {
		return isShipPlaced;
	}

	public void setShipPlaced(boolean isShipPlaced) {
		this.isShipPlaced = isShipPlaced;
	}
	public Object get(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

}
