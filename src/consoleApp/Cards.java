package consoleApp;

import java.util.Random;

public class Cards {
//Determining and Getting Suits
	public static String GetSuit(int index) {
		String Suits[] = new String[4];
		Suits[0] = "♠"; // spades
		Suits[1] = "♣"; // clubs
		Suits[2] = "♥"; // hearts
		Suits[3] = "♦"; // diamonds

		return Suits[index];

	}

//Getting Full Card Names(suit-no)-Determining Special Cards
	public static String GetCardName(int cardValue) {

		int suitIndex = cardValue / 13;
		String suit = GetSuit(suitIndex);

		int cardNumber = (cardValue % 13) + 1;
		String specialCharacter = "" + cardNumber;

		if (cardNumber == 1 || cardNumber > 10) {
			switch (cardNumber) {
			case 1:
				specialCharacter = "A";
				break;
			case 11:
				specialCharacter = "J";
				break;
			case 12:
				specialCharacter = "Q";
				break;
			case 13:
				specialCharacter = "K";
				break;
			}
		}

		return suit + specialCharacter;
	}

// Printing and Creating Sorted Deck
	public static int[] InitializeDeck() {

		int deck[] = new int[52];

		for (int i = 0; i < 52; i++) {
			deck[i] = i;
		}

		return deck;
	}

	public static void PrintDeck(int[] deck) {
		for (int i = 0; i < deck.length; i++) {
			int cardValue = deck[i];
			String cardName = GetCardName(cardValue);
			System.out.println(cardName);
		}
	}

//Shuffle Function
	public static int[] ShuffleDeck(int[] deck, int shuffleCount) {
		Random r = new Random();

		for (int i = 0; i < shuffleCount; i++) {
			int selectedCardIndex = r.nextInt(deck.length);
			int selectedNewPosition = r.nextInt(deck.length);

			deck = SwapCards(deck, selectedCardIndex, selectedNewPosition);
		}

		return deck;
	}

//Shuffle Function -Swapping 2 cards 
	public static int[] SwapCards(int[] deck, int from, int to) {

		int firstValue = deck[from];
		int secondValue = deck[to];

		deck[from] = secondValue;
		deck[to] = firstValue;

		return deck;
	}

	// Cutting Deck Function
	public static int[] CutDeck(int[] deck, int dividerindex) {
		int newDeck[] = new int[52];
		int newDeckIndex = 0;
		for (int i = dividerindex; i < 52; i++) {
			newDeck[newDeckIndex] = deck[i];
			newDeckIndex++;
		}
		for (int i = 0; i < dividerindex; i++) {
			newDeck[newDeckIndex] = deck[i];
			newDeckIndex++;
		}
		return newDeck;
	}

//In Progress...
	public static int ChooseCard(int[] hand, int frontFacingCard, boolean isPistiPossible) {
		// - piÃ…Å¸ti var mÃ„Â± bak, varsa pisti yap
		// - ortadaki kart elimde var mÃ„Â±, varsa at
		// - front facing card varsa; J var mÃ„Â±? varsa at
		// - Elindeki J olmayan en kucuk puanlÃ„Â± kaÃ„Å¸Ã„Â±dÃ„Â± at, yoksa J at (karo
		// 10 3 puan,
		// sinek 2 2 puan, diÃ„Å¸erleri 1 puan)

		Random r = new Random();
		int selectedCardIndex = r.nextInt(hand.length);
		return hand[selectedCardIndex];
	}

	public static void main(String[] args) {

		int[] deck = InitializeDeck();

		PrintDeck(deck);

		deck = ShuffleDeck(deck, 1000);

		System.out.println("Shuffled Deck:");
		PrintDeck(deck);
//IN PROGRESS...
		deck = CutDeck(deck, 30);
		System.out.println("Cutted Deck:");
		PrintDeck(deck);

		// Get Player count from user (2 or 4)
		// Deal hand with 4 cards (one by one) per player + 4 in the middle
		//

	}
}
