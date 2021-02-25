
public class Card {
	/**
	 * Single character denoting suit
	 */
	private char suit;
	
	/**
	 * Numeric value denoting rank
	 */
	private int rank;

	/**
	 * Forms a new card object.
	 * @param c The card - suit after value (e.g. "AS", "3H", "JC", etc.)
	 */
	public Card(String c) {
		suit = c.charAt(c.length()-1);
		String r = c.substring(0, c.length()-1);
		rank = Card.convert(r);
	}

	public char getSuit() {
		return suit;
	}

	public int getRank() {
		return rank;
	}
	
	/**
	 * Prints the card in its original form
	 */
	public String toString() {
		return Card.convert(rank)+suit;
	}

	/**
	 * Converts string rank form to numeric value
	 * Assumes validity: 
	 * 		int - between 2 and 14
	 * 		String - "2"-"10","J","Q","K","A"
	 * @param r String form of rank
	 * @return Numeric form of rank
	 */
	public static int convert(String r) {
		int ans;
		if (r.length() > 1 && r.equals("10")) {
			return 10;
		}
		r = r.toUpperCase();
		switch(r) {
		case "J":
			return 11;
		case "Q":
			return 12;
		case "K":
			return 13;
		case "A":
			return 14;
		default:
			ans = r.charAt(0) - '0';
		}
		return ans;
	}
	
	/**
	 * Converts numeric value form to String rank
	 * Assumes validity: 
	 * 		int - between 2 and 14
	 * 		String - "2"-"10","J","Q","K","A"
	 * @param r Numeric form of rank
	 * @return String form of rank
	 */
	public static String convert(int r) {
		switch(r) {
		case 10:
			return "10";
		case 11:
			return "J";
		case 12:
			return "Q";
		case 13:
			return "K";
		case 14:
			return "A";
		default:
			return "" + (char) (r + '0');
		}
	}
	
	/**
	 * Compares this card with another card
	 * @param o The other card
	 * @return  -1 if this < o
	 * 			 1 if this > o
	 * 			 0 if this = o
	 */
	public int compareTo(Card o) {
		if (o.getRank() > this.getRank()) {
			return -1;
		} else if (o.getRank() < this.getRank()) {
			return 1;
		} else {
			return 0;
		}
	}
}
