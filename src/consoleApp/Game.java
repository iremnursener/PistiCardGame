package consoleApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;

public class Game {

	public static String FileName = "leaderboard.txt";

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		Deck gameDeck = new Deck();
		System.out.println("Enter your name:");
		String name = scan.next();
		Player player1 = new Player(name);
		Player computer = new Player("Computer");
		Card[] board = new Card[52];
		Player lastWinner = null;
		FileOperations operator=new FileOperations();

		int cutPoint = GetCutPointFromUser(); // gets the cut point from user

		// PrintDeck(gameDeck.getCards()); // CONTROL
		// gameDeck.CutDeck(cutPoint); // Cards are ready to play CONTROL
		// System.out.println("---"); // CONTROL
		// PrintDeck(gameDeck.getCards()); // CONTROL

		gameDeck.CutDeck(cutPoint); // cuts the deck
		// PrintDeck(gameDeck.getCards()); // CONTROL

// Game Starts-------------------------------
		for (int hand = 1; hand <= 6; hand++) { // we will play the game until all cards(52) are used so game will last
												// until 6th hand is played
			System.out.println();
			System.out.println("HAND" + hand + "⛿");

			Random rand = new Random();

			DealHands(gameDeck.getCards(), player1, computer, board, hand); // deals hands before starting the game

			for (int times = 1; times <= 4; times++) { // a player(computer and player1) have 4 cards so players will
														// play cards for 4 times

				PrintGameTable(board, computer, player1); // we show the current table(before starting the hand and
															// after playing a card)

				System.out.println();
				System.out.println("════════════════════════════════════════");
				System.out.println("Choose one card to play:");
				int selectedCardIndex = scan.nextInt();

				Card topCard = GetTopCard(board);

				Card selectedCard = player1.PlayHand(selectedCardIndex, topCard);
				Card[] pistiCards = player1.getPistiCards();
				Boolean isWinner = EvaluatePlayedCard(player1, selectedCard, board, topCard, pistiCards);
				if (isWinner == true) {
					lastWinner = player1;
				}

				topCard = GetTopCard(board);

				selectedCard = computer.PlayHand(topCard);
				pistiCards = computer.getPistiCards();
				isWinner = EvaluatePlayedCard(computer, selectedCard, board, topCard, pistiCards);
				if (isWinner == true) {
					lastWinner = computer;
				}

				player1.PrintOwnedCards(player1);
				System.out.println("Pişti:" + player1.getPistiCount());
			}
		}
//Game is finished-----------------------------------------------------------
		System.out.println("🏁Game is finished🏁");
		System.out.println("►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►");
		System.out.println("Last winner is " + lastWinner.Name);
		CheckLastBoard(board, lastWinner);

		player1.PrintOwnedCards(player1);
		System.out.println("Pişti:" + player1.getPistiCount());
		player1.PrintPistiCards(player1);
		System.out.println(); // Space
		computer.PrintOwnedCards(computer);
		System.out.println("Pişti:" + computer.getPistiCount());
		computer.PrintPistiCards(computer);

		PrintGameTable(board, computer, player1);
		System.out.println();

		int point1 = CalculatePoints(player1, player1.OwnedCards, player1.PistiCards, player1.totalPoint);
		System.out.println();
		int point2 = CalculatePoints(computer, computer.OwnedCards, computer.PistiCards, computer.totalPoint);
		Player winner = null;
		int winnerPoint = 0;
		if (point1 > point2) {
			winner = player1;
			winnerPoint = point1;
			System.out.println("THE WINNER IS  " + player1.Name);
		} else if (point1 < point2) {
			winner = computer;
			winnerPoint = point2;
			System.out.println("THE WINNER IS COMPUTER");
		} else {
			System.out.println("DEUCE");
		}

// Saving top scores to a file
		//SavePlayerToLeaderboard(winner);
		operator.SavePlayerToLeaderboard(winner);
//---------------------------------------------------------------------------
	}

	private static File GetLeaderboardFile() {
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

	private static Player[] LoadLeaderboardFromFile() {
		Player[] leaderPlayers = new Player[10];
		int playerIndex = 0;
		File leaderboardFile = GetLeaderboardFile();
		try {
			File leaderFile = new File(FileName);
			Scanner reader = new Scanner(leaderFile);
			while (reader.hasNextLine()) {
				String name = reader.nextLine();

				if (!name.equals("")) {
					int point = reader.nextInt();
					Player player = new Player(name);
					player.totalPoint = point;
					leaderPlayers[playerIndex] = player;
					playerIndex++;
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return leaderPlayers;
	}

	private static void SavePlayerToLeaderboard(Player currentPlayer) {

		Player[] playersInFile = LoadLeaderboardFromFile();
		Player[] newLeaderboard = new Player[10];
		int length = GetArrayLength(playersInFile);
		int indexToAdd = length;

		for (int i = 0; i < playersInFile.length - 1; i++) {
			if (playersInFile[i] != null && playersInFile[i].totalPoint > currentPlayer.totalPoint) {
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
			newLeaderboard[i+ 1] = playersInFile[i];
		}

		WriteToFile(newLeaderboard);
	}

	private static int GetArrayLength(Player[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				return i;
			}
		}

		return array.length;
	}

	private static void WriteToFile(Player[] newLeaderboard) {
		try {
			FileWriter fileWriter = new FileWriter(FileName);

			for (int i = 0; i < newLeaderboard.length; i++) {
				if (newLeaderboard[i] != null) {
					fileWriter.append(newLeaderboard[i].Name + "\n");
					fileWriter.append(newLeaderboard[i].totalPoint + "\n");
				}
			}

			fileWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static Boolean EvaluatePlayedCard(Player player, Card selectedCard, Card[] board, Card topCard,
			Card[] PistiCards) {
		Boolean isWinner = false;
		int boardLength = GetBoardLength(board);
		board[boardLength] = selectedCard;

		if (boardLength == 1 && selectedCard.CardNumber.equals(topCard.CardNumber)) {
			System.out.println("PİŞTİ!!");
			player.PistiCount++;
			AddCardsToPistiCards(player, PistiCards, player.PistiCount, board);
			RemoveCardsFromBoard(board);
			isWinner = true;

		}

		if (topCard == null) {
			board[0] = selectedCard;

		}

		if (topCard != null) {

			if (selectedCard.CardNumber.equals(topCard.CardNumber) || selectedCard.CardNumber.equals("J")) {
				AddCardsToOwnedCards(player, board, player.OwnedCards);
				RemoveCardsFromBoard(board);
				isWinner = true;

			}

		}
		return isWinner;
	}

	// Checks table and add last cards on the board to the last player who got cards
	// from board.
	public static void CheckLastBoard(Card[] board, Player lastWinner) {

		AddCardsToOwnedCards(lastWinner, board, lastWinner.OwnedCards);
		RemoveCardsFromBoard(board);
	}

	public static void AddCardsToOwnedCards(Player player, Card[] board, Card[] OwnedCards) {
		int boardLength = GetBoardLength(board);
		if (OwnedCards[0] == null) {
			for (int i = 0; i < boardLength; i++) {
				player.OwnedCards[i] = board[i];
			}
		} else {
			int currentOwnedCardsLength = player.getOwnedCardsLength();
			int boardIndex = 0;
			for (int i = currentOwnedCardsLength; i < currentOwnedCardsLength + boardLength; i++) {
				player.OwnedCards[i] = board[boardIndex];
				boardIndex++;
			}
		}

	}

	public static void AddCardsToPistiCards(Player player, Card[] PistiCards, int PistiCount, Card[] board) {
		if (PistiCards[0] == null) {
			for (int i = 0; i < PistiCount * 2; i++) {
				player.PistiCards[i] = board[i];
			}
		} else {
			int currentLength = player.getPistiCardsLength();
			int boardIndex = 0;
			for (int i = currentLength; i < PistiCount * 2; i++) {
				player.PistiCards[i] = board[boardIndex];
				boardIndex++;
			}
		}
	}

	public static void RemoveCardsFromBoard(Card[] board) {
		int boardLength = GetBoardLength(board);

		for (int i = 0; i < boardLength; i++) {
			board[i] = null;
		}

	}

	private static Card GetTopCard(Card[] board) { // we need to see the top card to decide which card we should play

		int boardLength = GetBoardLength(board); // we need to know the length of the board to see the last card on the
		if (boardLength != 0) {

			return board[boardLength - 1];
		}
		return null;
	}

	private static int GetBoardLength(Card[] board) { // we search for all the indexes of board.the first "i" that gives
														// us the value null is the current length of the array.

		for (int i = 0; i < board.length; i++) {
			if (board[i] == null) {
				return i;
			}
		}

		return board.length; // if there is no null value, the length of the array is equal to the value we
								// defined first.
	}

//Dealing Hands
	public static void DealHands(Card[] cards, Player player1, Player computer, Card[] board, int hand) {
		// Dealing First Hand (Including Board)
		if (hand == 1) {
			int cardindex = 0;
			for (int i = 0; i < 4; i++) {

				player1.ActiveHand[i] = cards[cardindex];
				cardindex++;
				computer.ActiveHand[i] = cards[cardindex];
				cardindex++;
				board[i] = cards[cardindex];
				cardindex++;
			}
			// Dealing Hands( 2-6 )
		} else {
			int cardindex = 4 + ((hand - 1) * 8);
			for (int i = 0; i < 4; i++) {

				player1.ActiveHand[i] = cards[cardindex];
				cardindex++;
				computer.ActiveHand[i] = cards[cardindex];
				cardindex++;

			}
		}

	}

	public static Card[] AddCardsToBoard(Card[] board, Card[] ActiveHand, int cardOrder, int times) {
		if (times == 1) {
			int lastCardIndex = 3;
			if (board[lastCardIndex].CardNumber != ActiveHand[cardOrder].CardNumber) {
				lastCardIndex++;
				board[lastCardIndex] = ActiveHand[cardOrder];

			}

		}
		return board;
	}

//Getting CutPoint From User
	public static int GetCutPointFromUser() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Where do you want me to cut the deck?");
		int cutPoint = scan.nextInt();
		return cutPoint;
	}

//Printing Shuffled Deck
	public static void PrintDeck(Card[] cards) {
		for (int i = 0; i < cards.length; i++) {
			System.out.println(cards[i].GetCardName());
		}
	}

//Printing ActiveHand
	public static void PrintGameTable(Card[] board, Player computer, Player player1) {
		System.out.println();
		System.out.print("Computer ➤  ");
		computer.PrintActiveHand();
		System.out.println();
		System.out.println("════════════════════════════════════════");
		System.out.println("❮ BOARD ❯");

		int boardLength = GetBoardLength(board);
		for (int i = boardLength - 1; i >= 0; i--) {
			if (board[i] != null) {

				if (i == boardLength - 1) {
					System.out.println("█" + board[i].GetCardName() + "  ");
					System.out.println();
				} else {
					System.out.print("█" + board[i].GetCardName() + "  ");
				}
			}
		}

		System.out.println();
		System.out.println("════════════════════════════════════════");
		System.out.print(" YOU ➤  ");
		player1.PrintActiveHand();

	}

	private static int CalculatePoints(Player player, Card[] OwnedCards, Card[] PistiCards, int totalPoint) {
		int currentOwnedCardsLength = player.getOwnedCardsLength();
		int numberOfOwnedCards = currentOwnedCardsLength;
		int pistiCardsPoint = (player.getPistiCount()) * 10;
		int specialCardsPoints = 0;
		int specialCardCounter = 0;
		for (int i = 0; i < currentOwnedCardsLength; i++) {
			if (player.OwnedCards[i].GetCardName().equals("♦10")) {
				specialCardsPoints += 3;
				specialCardCounter++;
			}
			if (player.OwnedCards[i].GetCardName().equals("♣2")) {
				specialCardsPoints += 2;
				specialCardCounter++;
			}

		}
		int normalCardsNumber = numberOfOwnedCards - specialCardCounter; // normal cards are 1 point
		player.totalPoint = pistiCardsPoint + specialCardsPoints + normalCardsNumber;

		if (numberOfOwnedCards + player.PistiCount * 2 > 26) {
			player.totalPoint += 3;
		}
		System.out.println(player.Name + "'s total point :" + player.totalPoint);
		return player.totalPoint;
	}

}
