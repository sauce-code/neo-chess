package gui;

import chess.Board;
import chess.Game;
import chess.Player;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * This class provides the Gui for the chess game.
 * 
 * @author Maike Rees
 * @author Torben Kr&uuml;ger
 */
public class Gui extends Application {

	/**
	 * The current ply.
	 */
	private int ply = 1;

	/**
	 * The ply toggle group.
	 */
	private final ToggleGroup plyGroup = new ToggleGroup();

	/**
	 * A.I. checkbox.
	 */
	private final CheckBox ai = new CheckBox("Player Black A.I.");

	/**
	 * Tells whether black is controlled by an a.i. nor not.<br>
	 * {@code true}, if black is being controlled by an a.i.
	 */
	private boolean aiBlack = false;

	/**
	 * The game.
	 */
	private Game game = new Game();

	/**
	 * The GridPane that contains all elements.
	 */
	private GridPane grid = new GridPane();

	/**
	 * The Size of each image.
	 */
	private final int imgSize = 40;

	/**
	 * All the Images.
	 */
	private Image pawnB, pawnW, rookB, rookW, knightB, knightW, bishopB, bishopW, kingB, kingW, queenB, queenW;

	/**
	 * The StackPanes - one for each rectangle of the chess game.
	 */
	private StackPane panes[][];

	/**
	 * Saves one clicked Field of the chess game.
	 */
	private int clickedX = -1, clickedY = -1;

	/**
	 * The x-coordinate of the grid where the chess board starts.
	 */
	private final int startBoardX = 0;

	/**
	 * The y-coordinate of the grid where the chess board starts.
	 */
	private final int startBoardY = 1;

	/**
	 * The Label which displays the current Player.
	 */
	private Label playerLabel = new Label();

	/**
	 * The label which displays the current state.
	 */
	private Label stateLabel = new Label();

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            arguments, unused
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Neo Chess");

		initializeGrid();

		initializeImages();
		initializeStackPanes();
		drawBoard();

		putButtonsLabels();
		setCurrentPlayerText();

		primaryStage.setScene(new Scene(grid, 500, 600));
		primaryStage.show();
	}

	/**
	 * Sets the properties of the grid.
	 */
	private void initializeGrid() {
		grid.setHgap(1);
		grid.setVgap(1); // space between rows
		grid.setPadding(new Insets(10, 10, 10, 10));
		// grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
	}

	/**
	 * Puts Buttons and Labels onto the gui.
	 */
	private void putButtonsLabels() {

		// Boxes
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.CENTER);

		VBox vbBtn = new VBox(10);

		grid.add(vbBtn, 0, 10, 8, 3);

		// Undo Button
		Button buttonUndo = new Button("Undo");

		buttonUndo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (aiBlack) {
					game.undo();
				}
				game.undo();
				drawBoard();
			}
		});
		hbBtn.getChildren().add(buttonUndo);

		// Restart Game Button
		Button buttonRestart = new Button("Restart");
		buttonRestart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				game = new Game();
				drawBoard();
			}
		});
		hbBtn.getChildren().add(buttonRestart);

		// Close Button
		Button buttonClose = new Button("Close");
		buttonClose.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				((Node) (event.getSource())).getScene().getWindow().hide();
			}
		});
		hbBtn.getChildren().add(buttonClose);

		// Heading
		Label heading = new Label("Chess Game");
		HBox hbhead = new HBox(10);
		heading.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		hbhead.setAlignment(Pos.CENTER);
		hbhead.getChildren().add(heading);
		grid.add(hbhead, 0, 0, 8, 1);

		// Player Label
		HBox hbPlayer = new HBox(10);
		hbPlayer.setAlignment(Pos.CENTER);
		hbPlayer.getChildren().add(playerLabel);
		playerLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		// grid.add(hbPlayer, 0, 9, 8, 1);

		// Radio Buttons
		RadioButton rb1 = new RadioButton("ply 1");
		rb1.setToggleGroup(plyGroup);
		RadioButton rb2 = new RadioButton("ply 2");
		rb2.setToggleGroup(plyGroup);
		RadioButton rb3 = new RadioButton("ply 3");
		rb3.setToggleGroup(plyGroup);
		RadioButton rb4 = new RadioButton("ply 4");
		rb4.setToggleGroup(plyGroup);
		rb1.setSelected(true);
		plyGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
				if (newToggle == rb1) {
					ply = 1;
				} else if (newToggle == rb2) {
					ply = 2;
				} else if (newToggle == rb3) {
					ply = 3;
				} else if (newToggle == rb4) {
					ply = 4;
				}
			}
		});

		HBox rbBox = new HBox(10);
		rbBox.getChildren().add(rb1);
		rbBox.getChildren().add(rb2);
		rbBox.getChildren().add(rb3);
		rbBox.getChildren().add(rb4);
		rbBox.setAlignment(Pos.CENTER);

		vbBtn.getChildren().add(hbPlayer);
		vbBtn.getChildren().add(hbBtn);
		vbBtn.getChildren().add(rbBox);

		// AI check box
		ai.setSelected(false);
		ai.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
				aiBlack = newVal;
				if (game.getBoard().getCurrentPlayer() == Player.BLACK) {
					game.move(ply);
					drawBoard();
				}
			}
		});

		HBox aiBox = new HBox(10);
		aiBox.getChildren().add(ai);
		aiBox.setAlignment(Pos.CENTER);

		vbBtn.getChildren().add(aiBox);

		// state Label
		HBox hbState = new HBox(10);
		hbState.setAlignment(Pos.CENTER);
		hbState.getChildren().add(stateLabel);
		stateLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

		vbBtn.getChildren().add(hbState);

	}

	/**
	 * Loads the Images.
	 */
	private void initializeImages() {
		// @formatter:off
		pawnB   = new Image(getClass().getResource( "/bauerBlack.png").toExternalForm(), imgSize, imgSize, false, false);
		pawnW   = new Image(getClass().getResource( "/bauerWhite.png").toExternalForm(), imgSize, imgSize, false, false);
		rookB   = new Image(getClass().getResource( "/towerBlack.png").toExternalForm(), imgSize, imgSize, false, false);
		rookW   = new Image(getClass().getResource( "/towerWhite.png").toExternalForm(), imgSize, imgSize, false, false);
		knightB = new Image(getClass().getResource( "/horesBlack.png").toExternalForm(), imgSize, imgSize, false, false);
		knightW = new Image(getClass().getResource( "/horesWhite.png").toExternalForm(), imgSize, imgSize, false, false);
		bishopB = new Image(getClass().getResource("/bishopBlack.png").toExternalForm(), imgSize, imgSize, false, false);
		bishopW = new Image(getClass().getResource("/bishopWhite.png").toExternalForm(), imgSize, imgSize, false, false);
		kingB   = new Image(getClass().getResource(  "/kingBlack.png").toExternalForm(), imgSize, imgSize, false, false);
		kingW   = new Image(getClass().getResource(  "/kingWhite.png").toExternalForm(), imgSize, imgSize, false, false);
		queenB  = new Image(getClass().getResource(  "/dameBlack.png").toExternalForm(), imgSize, imgSize, false, false);
		queenW  = new Image(getClass().getResource(  "/dameWhite.png").toExternalForm(), imgSize, imgSize, false, false);
		// @formatter:on
	}

	/**
	 * Initializes the StackPanes for every rectangle of the chess board and
	 * draws the rectangles also sets the eventListener for clicks for every
	 * rectangle.
	 */
	private void initializeStackPanes() {
		panes = new StackPane[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				panes[i][j] = new StackPane();
				panes[i][j].setAlignment(Pos.CENTER);
				// label.setMouseTransparent(true);
				// grid.add(panes[i][j], i, (7 + startBoardY) - j);
				grid.add(panes[i][j], i, j + 1);

				Rectangle recti = new Rectangle(50, 50);
				Color color = ((i + j) % 2 == 0) ? Color.BLANCHEDALMOND : Color.INDIANRED;
				recti.setFill(color);

				// recti.addEventFilter(MouseEvent.MOUSE_PRESSED,
				// event -> System.out.println("i clicked it"));

				recti.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {

						// System.out.println("i clicked it ");

						for (Node node : grid.getChildren()) {

							// if( node instanceof Rectangle) {
							if (node.getBoundsInParent().contains(e.getSceneX(), e.getSceneY())) {
								// System.out.println(
								// "Node: " + node + " at " +
								// GridPane.getRowIndex(node) + "/"
								// + GridPane.getColumnIndex(node));
								rectangleClicked(GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
							}
							// }
						}

					}
				});

				panes[i][j].getChildren().add(recti);

			}
		}
	}

	/**
	 * Sets the text for the current player.
	 */
	private void setCurrentPlayerText() {
		String player = (game.getBoard().getCurrentPlayer() == Player.WHITE) ? "White" : "Black";
		// Label playerLabel = new Label("It's " + player + "'s turn");
		playerLabel.setText("It's " + player + "'s turn");
	}

	private void setCurrentStateText() {
		stateLabel.setText(game.getBoard().getCurrentState().toString());
	}

	/**
	 * Puts a pawn at the given position (<b>chess coordinates</b>).
	 * 
	 * @param black
	 *            true - if the pawn is black, false - if the pawn is white
	 * @param x
	 *            coordinate on the chess board of the pawn
	 * @param y
	 *            coordinate on the chess board of the pawn
	 */
	private void putPawn(boolean black, int x, int y) {
		Image image = (black) ? pawnB : pawnW;
		panes[x][y].getChildren().add(getImageView(image));
	}

	/**
	 * Puts a rook at the given position (<b>chess coordinates</b>).
	 * 
	 * @param black
	 *            true - if the rook is black, false - if the rook is white
	 * @param x
	 *            coordinate on the chess board of the rook
	 * @param y
	 *            coordinate on the chess board of the rook
	 */
	private void putRook(boolean black, int x, int y) {
		Image image = (black) ? rookB : rookW;
		panes[x][y].getChildren().add(getImageView(image));
	}

	/**
	 * Puts a knight at the given position (<b>chess coordinates</b>).
	 * 
	 * @param black
	 *            true - if the knight is black, false - if the knight is white
	 * @param x
	 *            coordinate on the chess board of the knight
	 * @param y
	 *            coordinate on the chess board of the knight
	 */
	private void putKnight(boolean black, int x, int y) {
		Image image = (black) ? knightB : knightW;
		panes[x][y].getChildren().add(getImageView(image));
	}

	/**
	 * Puts a bishop at the given position (<b>chess coordinates</b>).
	 * 
	 * @param black
	 *            true - if the bishop is black, false - if the bishop is white
	 * @param x
	 *            coordinate on the chess board of the bishop
	 * @param y
	 *            coordinate on the chess board of the bishop
	 */
	private void putBishop(boolean black, int x, int y) {
		Image image = (black) ? bishopB : bishopW;
		panes[x][y].getChildren().add(getImageView(image));
	}

	/**
	 * Puts a queen at the given position (<b>chess coordinates</b>).
	 * 
	 * @param black
	 *            true - if the queen is black, false - if the queen is white
	 * @param x
	 *            coordinate on the chess board of the queen
	 * @param y
	 *            coordinate on the chess board of the queen
	 */
	private void putQueen(boolean black, int x, int y) {
		Image image = (black) ? queenB : queenW;
		panes[x][y].getChildren().add(getImageView(image));
	}

	/**
	 * Puts a king at the given position (<b>chess coordinates</b>).
	 * 
	 * @param black
	 *            true - if the king is black, false - if the king is white
	 * @param x
	 *            coordinate on the chess board of the king
	 * @param y
	 *            coordinate on the chess board of the king
	 */
	private void putKing(boolean black, int x, int y) {
		Image image = (black) ? kingB : kingW;
		panes[x][y].getChildren().add(getImageView(image));
	}

	/**
	 * This returns an ImageView containing the transfered image and sets the
	 * setMouseTransparent-Property of the ImageView to true (so you can click
	 * it).
	 * 
	 * @param image
	 *            the image to be set
	 * @return the generated ImageView
	 */
	private ImageView getImageView(Image image) {
		ImageView view = new ImageView(image);
		view.setMouseTransparent(true);
		return view;
	}

	/**
	 * Handles what happens if a field of the grid is clicked.
	 * 
	 * @param x
	 *            the x-coordinate of the clicked field
	 * @param y
	 *            the y-coordinate of the clicked field
	 */
	private void rectangleClicked(int x, int y) {

		// transform coordinates in coordinates of chess board
		x -= startBoardX;
		y -= startBoardY;
		y = 7 - y;
		// System.out.println("I clicked " + x + "|" + y + " (chess
		// coordinates)");

		if (game.getBoard().getFigure(x, y) == null
				|| game.getBoard().getFigure(x, y).getOwner() != game.getBoard().getCurrentPlayer()) {
			// empty space clicked or enemy figure clicked

			if (clickedX != -1 && clickedY != -1) { // means a figure was
													// clicked in previous click
				// System.out.println("a figure was clicked in previous click
				// -->move!");
				// System.out.println("from: " + clickedX + "|" + clickedY + "
				// to " + x + "|" + y);

				if (game.move(clickedX, clickedY, x, y)) {

					// TODO wird nicht rechtzeitig aufgerufen
					drawBoard();

					if (aiBlack && game.getBoard().getCurrentPlayer() == Player.BLACK) {
						game.move(ply);
						drawBoard();
					}
				}
			}
			clickedX = clickedY = -1;
		} else {
			// figure clicked
			if (game.getBoard().getFigure(x, y).getOwner() == game.getBoard().getCurrentPlayer()) {
				clickedX = x;
				clickedY = y;
				// System.out.println("figure clicked and saved");
			}
		}
	}

	/**
	 * Draws the board.
	 */
	private void drawBoard() {

		deleteAllPieces();
		setCurrentPlayerText();
		setCurrentStateText();

		Board board = game.getBoard();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {

				if (board.getFigure(x, y) != null) {
					switch (board.getFigure(x, y).toString()) {

					// change coords
					case "WB":
						putBishop(false, x, 7 - y);
						break;
					case "BB":
						putBishop(true, x, 7 - y);
						break;
					case "WK":
						putKing(false, x, 7 - y);
						break;
					case "BK":
						putKing(true, x, 7 - y);
						break;
					case "WN": // knight
						putKnight(false, x, 7 - y);
						break;
					case "BN": // knight
						putKnight(true, x, 7 - y);
						break;
					case "WP":
						putPawn(false, x, 7 - y);
						break;
					case "BP":
						putPawn(true, x, 7 - y);
						break;
					case "WQ":
						putQueen(false, x, 7 - y);
						break;
					case "BQ":
						putQueen(true, x, 7 - y);
						break;
					case "WR":
						putRook(false, x, 7 - y);
						break;
					case "BR":
						putRook(true, x, 7 - y);
						break;
					}
				}
			}
		}
	}

	/**
	 * Delete the ImageView of the given Piece.
	 * 
	 * @param x
	 *            x-coordinate of the piece
	 * @param y
	 *            y-coordinate of the piece
	 */
	private void deletePiece(int x, int y) {
		for (Node n : panes[x][y].getChildren()) {
			if (n instanceof ImageView) {
				((ImageView) n).setImage(null);
			}
		}
	}

	/**
	 * Deletes all pieces.
	 */
	private void deleteAllPieces() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				deletePiece(x, y);
			}
		}
	}

}
