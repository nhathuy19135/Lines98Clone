package Lines;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TopScores extends JFrame {

	public Player[] player = new Player[10];
	public JButton[] number = new JButton[10];

	public TopScores() {

		for (int i = 0; i < 10; i++) {
			this.player[i] = new Player();
			this.player[i].name = "player" + (i + 1);
		}
		for (int i = 0; i < 10; i++) {
			number[i] = new JButton((i + 1) + ". " + this.player[i].name + "  :   " + this.player[i].scores);
			number[i].setOpaque(false);
			number[i].setContentAreaFilled(false);
			number[i].setFocusable(false);
			add(number[i]);
		}
		setTitle(" TopScores");
		setSize(200, 320);
		setLayout(new GridLayout(10, 1));
		setResizable(true);
	}

	//Sort player name
	public void sortPlayer() {
		for (int i = 0; i < 10; i++)
			for (int j = i; j > 0; j--)
				if (player[j].scores > player[j - 1].scores) {
					Player tmp = this.player[j];
					this.player[j] = this.player[j - 1];
					this.player[j - 1] = tmp;
				}
	}

	//Add 1 player to TopScores
//	public void add(Player newPlayer) throws IOException {
//		readFile();
//		if (this.player[9].scores < newPlayer.scores) {
//			this.player[9] = newPlayer;
//			sortPlayer();
//			for (int i = 0; i < 10; i++) {
//				this.number[i].setText((i + 1) + ". " + player[i].name + "  :   " + player[i].scores);
//			}
//			writeFile();//Export to file TopScores.dat
//		}
//	}
	public void add(Player newPlayer) throws IOException {
		readFile();
		boolean isSamePlayer = false;
		int samePlayerIndex = -1;

		for (int i = 0; i < 10; i++) {
			if (player[i].scores == newPlayer.scores) {
				if (player[i].name.equals(newPlayer.name)) {
					isSamePlayer = true;
					samePlayerIndex = i;
					break;
				}
			}
		}

		if (isSamePlayer) {
			// If it's the same player, update the existing player's name
			player[samePlayerIndex].name = newPlayer.name;
		} else if (player[9].scores < newPlayer.scores) {
			// If it's a new player with a higher score, add it to the leaderboard
			player[9] = newPlayer;
			sortPlayer();
		}

		for (int i = 0; i < 10; i++) {
			number[i].setText((i + 1) + ". " + player[i].name + "  :   " + player[i].scores);
		}

		writeFile(); // Export to file TopScores.dat
	}

	//Write player info to TopScores.dat
	public void writeFile() throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream("TopScores.dat"));
		for (int i = 0; i < 10; i++)
			this.player[i].writeData(out);
		out.close();
	}

	//Read player info from TopScores.dat
	public void readFile() throws IOException {
		RandomAccessFile in = new RandomAccessFile("TopScores.dat", "r");
		for (int i = 0; i < 10; i++)
			this.player[i].readData(in);
		sortPlayer();
		for (int i = 0; i < 10; i++) {
			this.number[i].setText((i + 1) + ". " + player[i].name + "  :   " + player[i].scores);
		}
	}

	//Display TopScores
	public void showTopScores() throws IOException {
		readFile();
		setVisible(true);
	}

	//reset TopScores
	public void resetTopScores() throws IOException {
		// Create an empty file object
		File file = new File("TopScores.dat");
		if (file.delete()){
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "Reset TopScores successfully!");
		} else {
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "Reset TopScores failed, close the game and try again.");
		}

		new TopScores();
		writeFile();
	}


}