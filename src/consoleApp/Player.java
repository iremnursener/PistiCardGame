package consoleApp;

public class Player {
	public String Name;
	public Card[] ActiveHand;
	public Card[] OwnedCards;
	public int Point;

	public Player() {
		this.ActiveHand = InitiliazeActiveHand();
	}

	public Card[] getOwnedCards() {
		return this.OwnedCards;
	}

	public Card[] getActiveHand() {
		return this.ActiveHand;
	}

	//Initializing ActiveHand
	public Card[] InitiliazeActiveHand() {
		this.ActiveHand = new Card[52];
		for (int i = 0; i < 52; i++) {

			this.ActiveHand[i] = new Card(i);
		}
		return this.ActiveHand;
	}

}
