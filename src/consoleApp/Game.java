package consoleApp;

import java.util.Scanner;

public class Game {

	public static void main(String[] args) {

		Deck gameDeck = new Deck();
		Player player1 = new Player();
		Player computer = new Player();
		Card[] board = new Card[52];

		int cutPoint = GetCutPointFromUser();

		// PrintDeck(gameDeck.getCards()); // CONTROL
		// gameDeck.CutDeck(cutPoint); // Cards are ready to play CONTROL
		// System.out.println("---"); // CONTROL
		// PrintDeck(gameDeck.getCards()); // CONTROL

		gameDeck.CutDeck(cutPoint);
		PrintDeck(gameDeck.getCards()); // CONTROL

		// Game-------------------------------
		for (int hand = 1; hand <= 6; hand++) {
			System.out.println();
			System.out.println("HAND" + hand);
			System.out.println("---------------------------");

			DealHands(gameDeck.getCards(), player1, computer, board, hand);
			PrintGameTable(board, computer, player1);
			for (int times = 1; times <= 4; times++) { // a player(player1) have 4 cards so player will play 4 times
				Scanner scan = new Scanner(System.in);
				System.out.println("Choose one card to play:");
				int cardOrder = scan.nextInt();

				player1.PlayHand(cardOrder,times);
			}
		}

		/*
		 * 
		 * / * while (!gameDeck.IsAllCardsUsed()) {
		 * 
		 * DealHands();
		 * 
		 * PlayHands(players);
		 * 
		 * }
		 * 
		 * CalculatePoints(players);
		 */

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
		System.out.println("█ █ █ █ Computer ");
		computer.PrintActiveHand();
		System.out.println();
		System.out.println("█ █ █ █ BOARD ");
		for (int i = 0; i < 4; i++) {
			System.out.print(board[i].GetCardName() + "  ");
		}
		System.out.println();
		System.out.println("█ █ █ █ YOU ");
		player1.PrintActiveHand();

	}

	/*
	 * private static void CalculatePoints(Player[] players) {
	 * 
	 * for (int i = 0; i < players.length; i++) { Player player = players[i];
	 * 
	 * int point = CalculatePlayerPoint(player.OwnedCards);
	 * 
	 * player.Point = point; } }
	 * 
	 * private static int CalculatePlayerPoint(Card[] ownedCards) {
	 * 
	 * return 0; }
	 * 
	 * private static void playHands(Player[] players) {
	 * 
	 * }
	 */

}
