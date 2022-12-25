package consoleApp;

import java.util.Random;
import java.util.Scanner;

public class Deck {
	private Card[] cards;

	public Deck() {
		this.cards = InitializeDeck();
		ShuffleDeck(cards, 10000);
	}

	public Card[] getCards() {
		return this.cards;
	}

	private Card[] InitializeDeck() {
		this.cards = new Card[52];

		for (int i = 0; i < 52; i++) {
			this.cards[i] = new Card(i);
		}

		return this.cards;
	}

	public void CutDeck(int cutPoint) {
		Card[] newCards = new Card[cards.length];
		int newDeckIndex = 0;
		for (int i = cutPoint; i < 52; i++) {
			newCards[newDeckIndex] = cards[i];
			newDeckIndex++;
		}
		for (int i = 0; i < cutPoint; i++) {
			newCards[newDeckIndex] = cards[i];
			newDeckIndex++;
		}
		this.cards = newCards;
	}

	private static Card[] SwapCards(Card[] cards, int from, int to) {
		Card tempCard = cards[to];
		cards[to] = cards[from];
		cards[from] = tempCard;

		return cards;
	}

	private Card[] ShuffleDeck(Card[] cards, int shuffleCount) {
		Random r = new Random();

		for (int i = 0; i < shuffleCount; i++) {
			int selectedCardIndex = r.nextInt(cards.length);
			int selectedNewPosition = r.nextInt(cards.length);

			cards = SwapCards(cards, selectedCardIndex, selectedNewPosition);
		}
		return cards;
	}

	public boolean IsAllCardsUsed() {
		
		return false;
	}

}
