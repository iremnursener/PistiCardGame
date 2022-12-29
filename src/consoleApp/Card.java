package consoleApp;

public class Card {
	private String Suit;
	private String CardNumber;
	private int CardId;

	public Card(int cardId) {
		this.CardId = cardId;
		this.Suit = getSuit(cardId);
		this.CardNumber = getCardNumber(cardId);
	}

	
	public String getCardNumber() {
		return this.CardNumber;
	}
	
	public String GetCardName() {
		return this.Suit + this.CardNumber;

	}
	
	private String getSuit(int cardId) {
		String Suits[] = new String[4];
		int suitIndex = cardId / 13;
		Suits[0] = "♠"; // spades
		Suits[1] = "♣"; // clubs
		Suits[2] = "♥"; // hearts
		Suits[3] = "♦"; // diamonds

		return Suits[suitIndex];

	}

	private String getCardNumber(int cardId) {
		int cardNumber = (cardId % 13) + 1;
		String cardNumberText = "" + cardNumber;

		if (cardNumber == 1 || cardNumber > 10) {
			switch (cardNumber) {
			case 1: 
				cardNumberText = "A";
				break;
			case 11:
				cardNumberText = "J";
				break;
			case 12:
				cardNumberText = "Q";
				break;
			case 13:
				cardNumberText = "K";
				break;
			}
		}
		return cardNumberText;
	}
	
	
}
