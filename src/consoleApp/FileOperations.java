package consoleApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileOperations {
	private static String FileName;

	public FileOperations() {
		this.FileName = "leaderboard.txt";
	}

	public void SavePlayerToLeaderboard(Player currentPlayer) {

		Player[] playersInFile = LoadLeaderboardFromFile();
		Player[] newLeaderboard = new Player[10];
		int length = GetArrayLength(playersInFile);
		int indexToAdd = length;

		for (int i = 0; i < playersInFile.length - 1; i++) {
			if (playersInFile[i] != null && playersInFile[i].getTotalPoint() > currentPlayer.getTotalPoint()) {
				indexToAdd = i + 1;
			}
		}

		if (indexToAdd > playersInFile.length - 1) {
			return;
		}

		for (int i = 0; i < indexToAdd; i++) {
			newLeaderboard[i] = playersInFile[i];
		}

		newLeaderboard[indexToAdd] = currentPlayer;

		for (int i = indexToAdd; i < playersInFile.length - 1; i++) {
			newLeaderboard[i + 1] = playersInFile[i];
		}

		WriteToFile(newLeaderboard);
	}
//checks if the file already exists or not
	private File GetLeaderboardFile() {
		try {
			File file = new File(FileName);
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");
			}

			return file;
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return null;
		}
	}
//reads file
	private Player[] LoadLeaderboardFromFile() {
		Player[] leaderPlayers = new Player[10];
		int playerIndex = 0;
		try {
			File leaderboardFile = GetLeaderboardFile();
			Scanner reader = new Scanner(leaderboardFile);
			while (reader.hasNextLine()) {
				String name = reader.nextLine();

				if (!name.equals("")) {
					int point = reader.nextInt();
					Player player = new Player(name);
					player.setTotalPoint(point);
					leaderPlayers[playerIndex] = player;
					playerIndex++;
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred while executing file operations.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return leaderPlayers;
	}

	private int GetArrayLength(Player[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				return i;
			}
		}

		return array.length;
	}
//writes last leaderboard to the file
	private void WriteToFile(Player[] newLeaderboard) {
		try {
			FileWriter fileWriter = new FileWriter(FileName);

			for (int i = 0; i < newLeaderboard.length; i++) {
				if (newLeaderboard[i] != null) {
					fileWriter.append(newLeaderboard[i].getName() + "\n");
					fileWriter.append(newLeaderboard[i].getTotalPoint() + "\n");
				}
			}

			fileWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}
