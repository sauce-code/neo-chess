package chess;

/**
 * The Players.
 * 
 * @author Maike Rees
 * @author Torben Kr&uuml;ger
 */
public enum Player {

	/**
	 * White.
	 */
	WHITE,

	/**
	 * Black.
	 */
	BLACK;

	@Override
	public String toString() {
		switch (this) {
		case WHITE:
			return "W";
		case BLACK:
			return "B";
		default:
			throw new IllegalArgumentException();
		}
	}

}
