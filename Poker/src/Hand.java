import java.util.ArrayList;

public class Hand {
	/**
	 * The array of cards
	 */
	private Card[] hand;
	
	/**
	 * A vertical array depicting how many cards of each value there are
	 */
	private int[] repeats;
	
	/**
	 * Denotes the suit if all the cards are from the same suit
	 */
	private char sameSuit;
	
	/**
	 * Holds the name of the player
	 */
	private String name;
	
	/**
	 * Poker hands have a constant number of 5 cards
	 */
	private final int HANDLENGTH = 5;

	/**
	 * Forms a new Hand object
	 * @param h - " " delimited list of cards
	 * @param n The player name
	 */
	public Hand(String h, String n) {
		name = n;
		repeats = new int[15];
		String[] hArray = h.split(" ");
		hand = new Card[HANDLENGTH];
		
		for (int i = 0; i < HANDLENGTH; i++) {
			hand[i] = new Card(hArray[i]);
			repeats[hand[i].getRank()]++; //increment at rank index in repeats
			if (i == 0) {
				sameSuit = hand[i].getSuit();
			} else if (sameSuit != ' ' && sameSuit != hand[i].getSuit()){
				sameSuit = ' ';
			}
		}
	}

	public char getSameSuit() {
		return sameSuit;
	}
	
	/**
	 * Prints a Hand's claim
	 * @param c A Hand's claim array
	 * @param suit A sample suit from the Hand
	 * @return A String describing the claim
	 */
	public static String printHand(ArrayList<Integer> c, char suit) {
		int claim = c.get(0);
		String ans = null;
		switch(claim) {
		case 0:
			ans = "high card: " + Card.convert(c.get(1));
			break;
		case 1:
			ans = "pair: " + Card.convert(c.get(1));
			break;
		case 2:
			ans = "two pairs: " + Card.convert(c.get(1)) + " and " + Card.convert(c.get(2));
			break;
		case 3:
			ans = "three of a kind: " + Card.convert(c.get(1));
			break;
		case 4:
			ans = "straight: " + Card.convert(c.get(5)) + " through " + Card.convert(c.get(1));
			break;
		case 5:
			ans = "flush: " + suit;
			break;
		case 6:
			ans = "full house: " + Card.convert(c.get(1)) + " over " + Card.convert(c.get(2));
			break;
		case 7:
			ans = "four of a kind: " + Card.convert(c.get(1));
			break;
		case 8:
			ans = "straight flush: " + Card.convert(c.get(5)) + " through " + Card.convert(c.get(1)) + " of " + suit;
			break;
		default:
			break;
		}
		return ans;
	}
	
	/**
	 * Determines the highest claim this hand has
	 * @return An ArrayList with the claim at index 0 and card values of decreasing "importance" for tie breakers
	 */
	public ArrayList<Integer> highestClaim() {
		/*
		 * High Card(0): Hands which do not fit any higher category are ranked by the value of their highest card. 
		 * 		If the highest cards have the same value, the hands are ranked by the next highest, and so on.
		 * 
		 * Pair(1): 2 of the 5 cards in the hand have the same value. 
		 * 		Hands which both contain a pair are ranked by the value of the cards forming the pair. 
		 * 		If these values are the same, the hands are ranked by the values of the cards not forming the pair, in decreasing order.
		 * 
		 * Two Pairs(2): The hand contains 2 different pairs. 
		 * 		Hands which both contain 2 pairs are ranked by the value of their highest pair. 
		 * 		Hands with the same highest pair are ranked by the value of their other pair. 
		 * 		If these values are the same the hands are ranked by the value of the remaining card.
		 * 
		 * Three of a Kind(3): Three of the cards in the hand have the same value. 
		 * 		Hands which both contain three of a kind are ranked by the value of the 3 cards.
		 * 
		 * Straight(4): Hand contains 5 cards with consecutive values. 
		 * 		Hands which both contain a straight are ranked by their highest card.
		 * 
		 * Flush(5): Hand contains 5 cards of the same suit. 
		 * 		Hands which are both flushes are ranked using the rules for High Card.
		 * 
		 * Full House(6): 3 cards of the same value, with the remaining 2 cards forming a pair. 
		 * 		Ranked by the value of the 3 cards.
		 * 
		 * Four of a kind(7): 4 cards with the same value. 
		 * 		Ranked by the value of the 4 cards.
		 * 
		 * Straight flush(8): 5 cards of the same suit with consecutive values. 
		 * 		Ranked by the highest card in the hand.
		 * 
		 */
		ArrayList<Integer> ans = new ArrayList<Integer>() {{add(0);}};
		int singleIndex = 1; //to keep track of where to add the less "important" cards
		int claim = 0;
		int cards = 0; //keeps track of how many cards were found
		boolean consecutive = true; //determines if all cards are consecutive 
		boolean startCard = false; //helps consecutive
		boolean pair = false; //notes that there has been at least one pair
		boolean triple = false; //notes that there is a 3 of a kind
		
		if (sameSuit != ' ') {
			claim = 5;
		}
		for (int i = 2; i < repeats.length; i++) {
			cards += repeats[i];
			if (consecutive && repeats[i] > 0) {
				startCard = true;
			}
			if (consecutive && ((startCard && repeats[i] == 0 && cards != HANDLENGTH) || repeats[i] > 1)) {
				//if we've encountered a card and there is a gap in the order OR there is a value with more than one card
				consecutive = false;
			}
			
			//NOTE: this loop goes naturally from least to greatest card value
			if (repeats[i] == 1) {
				ans.add(singleIndex, i);
			}
			if (repeats[i] == 2 && pair) {
				singleIndex = 3;
				ans.add(1,i);
				claim = 2;
			}
			if (repeats[i] == 2 && !pair) {
				pair = true;
				if (!triple) {
					singleIndex = 2;
					ans.add(1,i);
					claim = 1;
				} else {
					ans.add(singleIndex,i);
				}
			}
			if (repeats[i] == 3) {
				triple = true;
				singleIndex = 2;
				ans.add(1,i);
				claim = 3;

			}
			if (repeats[i] == 4) {
				singleIndex = 2;
				ans.add(1,i);
				claim = 7;
			}

			if (cards == HANDLENGTH) {
				//no more cards
				break;
			}
		}
		if (consecutive) {
			claim = 4;
		}
		if (consecutive && sameSuit != ' ') {
			claim = 8;
		}
		if (triple && pair) {
			claim = 6;
		}
		ans.set(0, claim);
		return ans;
	}

	/**
	 * @return The player name
	 */
	public String getName() {
		return name;
	}
}
