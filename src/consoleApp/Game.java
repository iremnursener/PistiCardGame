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
		Boolean isWinner = null;
		Card topCard = null;
		Card selectedCard = null;
		Card[] pistiCards = null;

		FileOperations operator = new FileOperations();

		getCutPointFromUser(gameDeck); // checks the entered number is valid or not for the cut

		// int cutPoint = GetCutPointFromUser(); // gets the cut point from user

		// PrintDeck(gameDeck.getCards()); // CONTROL
		// gameDeck.CutDeck(cutPoint); // Cards are ready to play CONTROL
		// System.out.println("---"); // CONTROL
		// PrintDeck(gameDeck.getCards()); // CONTROL

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

				int a = 0;
				for (int i = 0; i < a + 1; i++) {

					try {
						int selectedCardIndex = scan.nextInt();

						topCard = GetTopCard(board);

						selectedCard = player1.PlayHand(selectedCardIndex, topCard);
						pistiCards = player1.getPistiCards();
						isWinner = EvaluatePlayedCard(player1, selectedCard, board, topCard, pistiCards);

					} catch (Exception e) {
						System.out.println("Something went wrong.Please enter a number between 1-"
								+ player1.getActiveHandLength());
						a++;
					}
				}

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
		System.out.println("Last winner is " + lastWinner.getName());
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

		int point1 = CalculatePoints(player1, player1.getOwnedCards(), player1.getPistiCards(), player1.getTotalPoint());
		System.out.println();
		int point2 = CalculatePoints(computer, computer.getOwnedCards(), computer.getPistiCards(), computer.getTotalPoint());
		Player winner = null;
		int winnerPoint = 0;
		if (point1 > point2) {
			winner = player1;
			winnerPoint = point1;
			System.out.println("THE WINNER IS  " + player1.getName());
		} else if (point1 < point2) {
			winner = computer;
			winnerPoint = point2;
			System.out.println("THE WINNER IS COMPUTER");
		} else {
			System.out.println("DEUCE");
		}

// Saving top scores to a file
		operator.SavePlayerToLeaderboard(winner);
//---------------------------------------------------------------------------
	}

	public static void getCutPointFromUser(Deck gameDeck) {
		int a = 0;
		for (int i = 0; i < a + 1; i++) {
			try {

				int cutPoint = GetCutPointFromUser();
				gameDeck.CutDeck(cutPoint);
			} catch (Exception e) {
				System.out.println("Something went wrong.Enter a number between 1-52");
				a++;
			}
		}

	}

	private static Boolean EvaluatePlayedCard(Player player, Card selectedCard, Card[] board, Card topCard,
			Card[] PistiCards) {
		Boolean isWinner = false;
		int boardLength = GetBoardLength(board);
		board[boardLength] = selectedCard;

		if (boardLength == 1 && selectedCard.getCardNumber().equals(topCard.getCardNumber())) {
			System.out.println("PİŞTİ!!");
			player.increasePistiCount();
			AddCardsToPistiCards(player, PistiCards, player.getPistiCount(), board);
			RemoveCardsFromBoard(board);
			isWinner = true;

		}

		if (topCard == null) {
			board[0] = selectedCard;

		}

		if (topCard != null) {

			if (selectedCard.getCardNumber().equals(topCard.getCardNumber()) || selectedCard.getCardNumber().equals("J")) {
				AddCardsToOwnedCards(player, board, player.getOwnedCards());
				RemoveCardsFromBoard(board);
				isWinner = true;

			}

		}
		return isWinner;
	}

	// Checks table and add last cards on the board to the last player who got cards
	// from board.
	public static void CheckLastBoard(Card[] board, Player lastWinner) {

		AddCardsToOwnedCards(lastWinner, board, lastWinner.getOwnedCards());
		RemoveCardsFromBoard(board);
	}

	public static void AddCardsToOwnedCards(Player player, Card[] board, Card[] OwnedCards) {
		int boardLength = GetBoardLength(board);
		if (OwnedCards[0] == null) {
			for (int i = 0; i < boardLength; i++) {
				player.getOwnedCards()[i] = board[i];
			}
		} else {
			int currentOwnedCardsLength = player.getOwnedCardsLength();
			int boardIndex = 0;
			for (int i = currentOwnedCardsLength; i < currentOwnedCardsLength + boardLength; i++) {
				player.getOwnedCards()[i] = board[boardIndex];
				boardIndex++;
			}
		}

	}

	public static void AddCardsToPistiCards(Player player, Card[] PistiCards, int PistiCount, Card[] board) {
		if (PistiCards[0] == null) {
			for (int i = 0; i < PistiCount * 2; i++) {
				player.getPistiCards()[i] = board[i];
			}
		} else {
			int currentLength = player.getPistiCardsLength();
			int boardIndex = 0;
			for (int i = currentLength; i < PistiCount * 2; i++) {
				player.getPistiCards()[i] = board[boardIndex];
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

				player1.getActiveHand()[i] = cards[cardindex];
				cardindex++;
				computer.getActiveHand()[i] = cards[cardindex];
				cardindex++;
				board[i] = cards[cardindex];
				cardindex++;
			}
			// Dealing Hands( 2-6 )
		} else {
			int cardindex = 4 + ((hand - 1) * 8);
			for (int i = 0; i < 4; i++) {

				player1.getActiveHand()[i] = cards[cardindex];
				cardindex++;
				computer.getActiveHand()[i] = cards[cardindex];
				cardindex++;

			}
		}

	}

	public static Card[] AddCardsToBoard(Card[] board, Card[] ActiveHand, int cardOrder, int times) {
		if (times == 1) {
			int lastCardIndex = 3;
			if (board[lastCardIndex].getCardNumber() != ActiveHand[cardOrder].getCardNumber()) {
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
			if (player.getOwnedCards()[i].GetCardName().equals("♦10")) {
				specialCardsPoints += 3;
				specialCardCounter++;
			}
			if (player.getOwnedCards()[i].GetCardName().equals("♣2")) {
				specialCardsPoints += 2;
				specialCardCounter++;
			}

		}
		int normalCardsNumber = numberOfOwnedCards - specialCardCounter; // normal cards are 1 point
		player.setTotalPoint(pistiCardsPoint + specialCardsPoints + normalCardsNumber);

		if (numberOfOwnedCards + player.getPistiCount() * 2 > 26) {
			player.setTotalPoint(player.getTotalPoint() + 3);
		}
		System.out.println(player.getName() + "'s total point :" + player.getTotalPoint());
		return player.getTotalPoint();
	}

}
