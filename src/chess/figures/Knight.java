package chess.figures;

import chess.Figure;
import chess.Board;
import chess.Player;

public class Knight extends Figure {

	/**
	 * The base value.
	 */
	private static final int VALUE = 320;

	/**
	 * The evaluation matrix.
	 */
	private final static int[][] EVAL = {
			// @formatter:off
			{ -50, -40, -30, -30, -30, -30, -40, -50 },
			{ -40, -20,   5,   0,   5,   0, -20, -40 },
			{ -30,   0,  10,  15,  15,  10,   0, -30 },
			{ -30,   5,  15,  20,  20,  15,   0, -30 },
			{ -30,   5,  15,  20,  20,  15,   0, -30 },
			{ -30,   0,  10,  15,  15,  10,   0, -30 },
			{ -40, -20,   5,   0,   5,   0, -20, -40 },
			{ -50, -40, -30, -30, -30, -30, -40, -50 }
			// @formatter:on
	};

	public Knight(Player owner, Board game, int x, int y) {
		super(owner, game, x, y);
	}

	@Override
	public String toString() {
		return super.toString() + 'N';
	}

	@Override
	protected boolean isSquareReachable(int toX, int toY) {

		if (!super.isSquareReachable(toX, toY)) {
			return false;
		}

		int absDistX = Math.abs(toX - x);
		int absDistY = Math.abs(toY - y);
		return (absDistX == 2 && absDistY == 1) || (absDistX == 1 && absDistY == 2);
	}

	@Override
	protected Figure clone(Board board) {
		return new Knight(this.owner, board, this.x, this.y);
	}

	@Override
	public int getValue() {
		int value = Knight.VALUE;
		switch (owner) {
		case WHITE:
			value += EVAL[x][y];
			break;
		case BLACK:
			value += EVAL[x][7 - y];
			break;
		default:
			throw new IllegalArgumentException();
		}
		return value;
	}

}
