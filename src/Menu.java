/* 
 * Project 4: Battleship
 * Author: Margi Katwala (mkatwa3), Rime Brika (rbrika2), Anusha Pai (apai7)
 * Professor Troy and Wei
 * Fall 2017
 */
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
@SuppressWarnings("serial")
public class Menu extends JMenuBar{
	public Menu() {
		// initialize all menu options
		
		JMenu file=new JMenu("File");
		JMenuItem aboutMenu = new JMenuItem( "About" );
		aboutMenu.addActionListener(
				new ActionListener() {  // anonymous inner class
					// terminate application when user clicks exitItem
					public void actionPerformed( ActionEvent event )
					{
						JOptionPane.showMessageDialog( Menu.this,
								"Authors\nThis program was written by  Margi Katwala (mkatwa3), Rime Brika (rbrika2), Anusha Pai (apai7)\n"
										+ "Due November 16th, 2017\nThe 4th programming assignment for CS 342",
										"About", JOptionPane.PLAIN_MESSAGE );
					}
				}  // end anonymous inner class
				);
		file.add(aboutMenu);
		JMenuItem exitMenu = new JMenuItem( "Exit" );
		exitMenu.addActionListener(
				new ActionListener() {  // anonymous inner class
					// terminate application when user clicks exitItem
					public void actionPerformed( ActionEvent event )
					{
						System.exit( 0 );
					}
				}  // end anonymous inner class
				);
		file.add(exitMenu);
		this.add(file);

		JMenu help = new JMenu( "Help" );
		JMenuItem helpC = new JMenuItem( "Connection" );
		helpC.addActionListener(
				new ActionListener() {  // anonymous inner class
					// terminate application when user clicks exitItem
					public void actionPerformed( ActionEvent event ){
						JOptionPane.showMessageDialog( Menu.this,"Choose whether you want to be a server or a client.\n Wait for your opponent to come on (which will then establish the connection between you two)\n This will be shown when it allows you to place ship", "How to Connect", JOptionPane.PLAIN_MESSAGE);

					}
				}  // end anonymous inner class
				);
		help.add(helpC);
		JMenuItem helpR = new JMenuItem( "Rules" );
		helpR.addActionListener(
				new ActionListener() {  // anonymous inner class
					// terminate application when user clicks exitItem
					public void actionPerformed( ActionEvent event )
					{
						JOptionPane.showMessageDialog( Menu.this,"Step 1: Go to Establish Connection menu option and choose whether you want to be a server or a client.\nStep 2: Wait for your opponent to come on (which will then establish the connection between you two)\nStep 3a: If you are the server, you play first. Go to step 4\nStep 3b: If you are the client, wait for the server to finish making it's move.\nStep 4: Pick a position in the bottom grid (Ship Grid) where you think\nthe opponent has a ship there. If the opponent has a ship in that position, then your top grid will\nbe updated with an icon that looks like a red ripple(hit) or a blue ripple(miss)\nStep 5: Your turn is done. Now wait for the opponent to finish it's turn.\nStep 6: Go to step 4", "How to Play",JOptionPane.PLAIN_MESSAGE);
					}
				}  // end anonymous inner class
				);
		help.add(helpR);
		this.add(help);
		JMenu establishMenu = new JMenu( "Establish Connection" );
		JMenuItem serverOrClient= new JMenuItem("Client or Server");
		serverOrClient.addActionListener(
				new ActionListener() {  // anonymous inner class
					// terminate application when user clicks exitItem
					public void actionPerformed( ActionEvent event )
					{
						Object[] options = {"Server", "Client"};
						int sc = JOptionPane.showOptionDialog(null,
								"Server or client?",
								"SC", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[1]); // server = 0 ; client = 1

						if(sc == 0) {
							// set up server
							Game.serverMode = true;
							Game.setUpServer();
						} else {
							Game.serverMode = false;
							Game.setUpClient();
						}
					}
				}  // end anonymous inner class
				);
		establishMenu.add(serverOrClient);
		this.add(establishMenu);
		JMenu statMenu = new JMenu( "Statistics" );
		statMenu.setMnemonic( 'S' );
		JMenuItem stat1=new JMenuItem( "Statistics For Player" );
		stat1.addActionListener(
				new ActionListener() {  // anonymous inner class
					// terminate application when user clicks exitItem
					public void actionPerformed( ActionEvent event )
					{
						Board.statsForShotGrid();
					}
				}  // end anonymous inner class
				);
		statMenu.add(stat1);
		this.add(statMenu);
		setVisible( true );
	}
}
