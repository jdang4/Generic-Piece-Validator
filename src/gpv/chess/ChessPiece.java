/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import java.util.function.BiPredicate;
import gpv.Piece;
import gpv.util.*;

/**
 * The chess piece is a piece with some special properties that are used for determining
 * whether a piece can move. It implements the Piece interface and adds properties and
 * methods that are necessary for the chess-specific behavior.
 * 
 * @version Feb 21, 2020
 */
public class ChessPiece implements Piece<ChessPieceDescriptor>
{
	private final ChessPieceDescriptor descriptor;
	private boolean hasMoved; // true if this piece has moved

	/**
	 * The only constructor for a ChessPiece instance. Requires a descriptor.
	 * 
	 * @param descriptor
	 */
	public ChessPiece(ChessPieceDescriptor descriptor)
	{
		this.descriptor = descriptor;
		hasMoved = false;
	}

	/*
	 * @see gpv.Piece#getDescriptor()
	 */
	@Override
	public ChessPieceDescriptor getDescriptor()
	{
		return descriptor;
	}

	/**
	 * @return the color
	 */
	public PlayerColor getColor()
	{
		return descriptor.getColor();
	}

	/**
	 * @return the name
	 */
	public PieceName getName()
	{
		return descriptor.getName();
	}
	
	/**
	 * This method is called to check to see if there is a piece at the destination coordinate. If so,
	 * it checks to see the color of that piece, which would determine if the piece's movement is valid
	 * 
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @param movingColor
	 * 			  signifies the moving piece's color
	 * @return whether the piece can move in its desired direction
	 * 			true -> a valid move; false -> not a valid move
	 */
	private boolean checkForPieceAtDestination(Coordinate to, Board b, PlayerColor movingColor)
	{
		if (b.getPieceAt(to) != null) 
		{
			PlayerColor destinationPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();
			
			if (movingColor.equals(destinationPieceColor))
			{
				return false;
			}

		}
		
		return true;
	}
	
	/**
	 * This method is called when needing to verify that the piece can move in all three 
	 * of the main directions (vertically, horizontally, and diagonally). Depending on the 
	 * given coordinates of the from and to variables, it will test the appropriate direction
	 * and verify if the move is valid or not.
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece can move in its desired direction
	 * 			true -> a valid move; false -> not a valid move
	 */
	private boolean checkAllThreeDirections(Coordinate from, Coordinate to, Board b)
	{ 
		int rowDistance = from.getRowDistance(to);
		int columnDistance = from.getColumnDistance(to);
		
		// piece is making a vertical move so check for that
		if (from.getColumn() == to.getColumn())
		{
			return canMoveVertically(from, to, b);
		}
		
		// piece is making a horizontal move so check for that
		else if (from.getRow() == to.getRow())
		{
			return canMoveHorizontally(from, to, b);
		}
		
		// piece is making a diagonal move so check for that
		else if (rowDistance == columnDistance)
		{
			return canMoveDiagonally(from, to, b);
		}
		
		// piece is making an invalid move for whichever piece that is calling this function
		return false;
	}
	
	/**
	 * This method is called when the pawn is trying to move one spacee forward. Calling
	 * this method will verify if the pawn's movement is a move.
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the pawn can move one space forward
	 * 			true -> a valid move; false -> not a valid move
	 */
	private boolean checkPawnMove_OneSpace(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		// verifying that the pawn is moving one space forward
		if (from.getRowDistance(to) == 1)
		{
			// making a vertical move
			if (from.getColumnDistance(to) == 0) 
			{
				
				// verifying that there are no pieces at the 
				// destination
				if (b.getPieceAt(to) == null)
				{
					return true;
				}
				
				// if there is, not a pawn move
				else
				{
					return false;
				}
			}
			
			// pawn is attempting to capture an enemy
			else if (from.getColumnDistance(to) == 1)
			{
				// checking movement for a black pawn
				if (movingPieceColor.equals(PlayerColor.BLACK))
				{
					if (from.getRow() - 1 == to.getRow())
					{
						// verifying that the captured piece is not the same color
						return checkForPieceAtDestination(to, b, movingPieceColor);
					}
				}
				
				// checking movement for a white pawn
				else
				{
					if (from.getRow() + 1 == to.getRow())
					{
						// verifying that the captured piece is not the same color
						return checkForPieceAtDestination(to, b, movingPieceColor);
					}
				}
			}
		}
		
		// conditions were not met to make a one space move
		return false;
	} 
	
	/**
	 * This method is called when the pawn is trying to move two spaces forward. Calling
	 * this method will verify if the pawn's movement is a move.
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the pawn can move two spaces forward
	 * 			true -> a valid move; false -> not a valid move
	 */ 
	private boolean checkPawnTwoSpacesForward(Coordinate from, Coordinate to, Board b)
	{
		if ( (from.getRowDistance(to) == 2) && (from.getColumnDistance(to) == 0) )
		{
			// just make sure no pieces in the way
			Coordinate oneSpaceForward = Coordinate.makeCoordinate(from.getRow() + 1, from.getColumn());
			
			if (b.getPieceAt(oneSpaceForward) == null && 
					b.getPieceAt(to) == null)
			{
				return true;
			}
		}
		
		// conditions were not met to make a one space move
		return false;
	}
	
	/**
	 * This method is called when the king is trying to attempt a castling move. Calling
	 * this method will verify if the king's movement is a valid castling move.
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the king can make a valid castling move
	 * 			true -> a valid castling move; false -> not a valid castling move
	 */ 
	private boolean checkValidCastling(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		Coordinate rookCoordinate = null; Coordinate.makeCoordinate(from.getRow(), 8);
		
		// this flag determines if king is making a right castling move
		// true -> making right castling move; false -> making left castling move
		boolean rightCastlingFlag = true;
		
		// getting the coordinate where the rook should be for a right castling move
		if (from.getColumn() + 2 == to.getColumn())
		{
			rookCoordinate = Coordinate.makeCoordinate(from.getRow(), 8);
		}
		
		// getting the coordinate where the rook should be for a left castling move
		else if (from.getColumn() - 2 == to.getColumn())
		{
			rookCoordinate = Coordinate.makeCoordinate(from.getRow(), 1);
			rightCastlingFlag = false; // detected that king is making left castling move
		}
		
		// need to verify that there is a piece at the rook coordinate
		if (b.getPieceAt(rookCoordinate) != null)
		{
			PieceName pieceType = ((ChessPieceDescriptor) b.getPieceAt(rookCoordinate).getDescriptor()).getName();
			PlayerColor pieceColor = ((ChessPieceDescriptor) b.getPieceAt(rookCoordinate).getDescriptor()).getColor();
			
			// need to verify if there is a rook of the same color at the rook coordinate
			if (pieceType.equals(PieceName.ROOK) && pieceColor.equals(movingPieceColor) )
			{
				ChessPiece rook = (ChessPiece) b.getPieceAt(rookCoordinate);
				
				// need to verify if the rook has moved or not
				if (!rook.hasMoved())
				{
					int startColumnIndex = from.getColumn();
					
					// used to increment the startColumnIndex appropriately
					// is set to 1 by default (meant for right castling)
					int incrementor = 1;
					
					BiPredicate<Integer, Integer> stillSpacesLeft;
					
					if (rightCastlingFlag)
					{
						// king will be moving to the right
						startColumnIndex = startColumnIndex + 1;
						
						// checks for spaces to the right of king
						stillSpacesLeft = (startIndex, endIndex) -> 
						{
							return startIndex < endIndex;
						};
					}
					
					// detected that the king is making a left castling move
					else
					{
						startColumnIndex = startColumnIndex - 1;
						
						incrementor = -1;
						
						// checks for spaces to the left of king
						stillSpacesLeft = (startIndex, endIndex) -> 
						{
							return startIndex > endIndex;
						};
					}
					 
					// loop until get to rook coordinate and check if no pieces in the way
					while (stillSpacesLeft.test(startColumnIndex, rookCoordinate.getColumn()))
					{
						Coordinate nextCoordinate = Coordinate.makeCoordinate(from.getRow(), startColumnIndex);
						
						// check if the next coordinate to check is occupied by a piece or not
						if (b.getPieceAt(nextCoordinate) != null)
						{
							// found a piece in between the king and rook
							return false;
						}
						
						// incrementing the startColumnIndex appropriately
						startColumnIndex = startColumnIndex + incrementor; 
					}
					
					// no pieces were in between the king and the rook so valid castling move
					return true;
				}
			}
		}
		
		// conditions were not met to make a valid castling move
		return false;
	}

	// the check test for a king's movement
	private ChessPieceValidator king = (from, to, b) -> 
	{
		// checking if trying to make a castling move
		if (from.getRowDistance(to) == 0 &&
				from.getColumnDistance(to) == 2)
		{ 
			// check for conditions of castling
			if (!hasMoved())
			{
				// valid castling conditions
				return checkValidCastling(from, to, b);
			}
			
			else
			{
				return false;
			}
		}
	 	
		// king is not making a castling move
		else if ((from.getRowDistance(to) > 1)
				|| (from.getColumnDistance(to) > 1))
		{
			return false;
		}
		
		// making a standard one move
		else  
		{ 
			return checkAllThreeDirections(from, to, b);
		}
		
	};
 
	// the check tests for a queen's movements
	private ChessPieceValidator queen = (from, to, b) -> 
	{
		return checkAllThreeDirections(from, to, b);
	};

	// the check tests for a bishop's movements
	private ChessPieceValidator bishop = (from, to, b) ->
	{
		return canMoveDiagonally(from, to, b);
	};
	
	// the check tests for a knight's movements
	private ChessPieceValidator knight = (from, to, b) ->
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		// move is a L vertically
		if (from.getRowDistance(to) == 2)
		{
			if (from.getColumnDistance(to) == 1)
			{ 
				// check if piece occupies the space and what color piece
				return checkForPieceAtDestination(to, b, movingPieceColor);
			}
		}
		// move is a L horizontally
		else if (from.getColumnDistance(to) == 2)
		{
			if (from.getRowDistance(to) == 1)
			{
				// no piece at destination spot
				return checkForPieceAtDestination(to, b, movingPieceColor);
			}
		}
		
		// not a valid knight move
		return false;
	};

	// the check tests for a rook's movements
	private ChessPieceValidator rook = (from, to, b) -> 
	{
		// rook is attempting to move vertically so check vertical move
		if (from.getColumn() == to.getColumn())
		{
			return canMoveVertically(from, to, b);
		}
		 
		// rook is attempting to move horizontally so check horizontal move
		else if (from.getRow() == to.getRow())
		{
			return canMoveHorizontally(from, to, b);
		}

		return false;
	};  

	// the check tests for a pawn's movements
	private ChessPieceValidator pawn = (from, to, b) ->
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		// checking if it has moved before
		if (!hasMoved())
		{
			// is able to move 2 spaces forward on first move
			if (from.getRowDistance(to) == 2)
			{
				// handling the movement checking for a white pawn
				if (movingPieceColor.equals(PlayerColor.WHITE))
				{
					if (from.getRow() + 2 == to.getRow())
					{
						return checkPawnTwoSpacesForward(from, to, b);
					}
				}
				
				// handling the movement checking for a black pawn
				else
				{
					if (from.getRow() - 2 == to.getRow())
					{
						return checkPawnTwoSpacesForward(from, to, b);
					}
				}
			}
			
			// the pawn chooses to move only one space forward
			else if (from.getRowDistance(to) == 1)
			{
				return checkPawnMove_OneSpace(from, to, b);
			}
		}
		
		// the pawn can only move one space forward
		else
		{
			return checkPawnMove_OneSpace(from, to, b);
		}
		 
		return false; 
	};
	
	/*
	 * @see gpv.Piece#canMove(gpv.util.Coordinate, gpv.util.Coordinate, gpv.util.Board)
	 */
	@Override
	public boolean canMove(Coordinate from, Coordinate to, Board b)
	{

		// checking if where I want to move is a valid position within the board
		if ( (!b.insideBoard(to)) || (!b.insideBoard(from)) || (from.equals(to)) 
			|| (b.getPieceAt(from) == null)) 
		{
			return false;
		}
		  
		PieceName movingPieceType = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getName();

		boolean result = false;
		
		// handling the checking for a particular piece
		switch (movingPieceType) {
			case KING:
				result = king.check(from, to, b);
				break;

			case QUEEN:
				result = queen.check(from, to, b);
				break;

			case BISHOP:
				result = bishop.check(from, to, b);
				break;

			case KNIGHT:
				result = knight.check(from, to, b);
				break;

			case ROOK:
				result = rook.check(from, to, b);
				break;

			case PAWN:
				result = pawn.check(from, to, b);
				break;

			default:
				// if allowed, I would have thrown an exception here
				// but for this assignment it just yields a false statement
				result = false; 
				break;
		}

		return result;
	}
	
	/**
	 * This method is called when the piece wants to move upwards in a vertical direction.
	 * It determines if the piece can move vertically up to the desired coordinate while
	 * following the rules of chess
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece can move vertically upwards to the desired coordinate
	 *         true -> it is a valid move for moving vertically upwards, false -> it is
	 *         not a valid move
	 */
	private boolean canMoveVertically_Up(Coordinate from, Coordinate to, Board b)
	{
		// check for pieces that may be in the way
		for (int nextRow = from.getRow() + 1; nextRow <= to.getRow(); nextRow++) 
		{
			// check if there is a piece in the way (no jumping over)
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, to.getColumn());
			
			if (nextRow == to.getRow()) 
			{
				return true;
			}
			
			else if (b.getPieceAt(nextCoordinate) != null)
			{
				break;
			}
		}
		
		// condition for the move is not a valid one by the caller piece
		return false;
	}
	
	/**
	 * see canMoveVertically_Up function. Same functionality, but checks for if moving vertically down
	 */
	private boolean canMoveVertically_Down(Coordinate from, Coordinate to, Board b)
	{		
		// check if any pieces are in the way when moving
		for (int nextRow = from.getRow() - 1; nextRow >= to.getRow(); nextRow--) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, to.getColumn());
			
			// check if there is a piece in the way (no jumping over)
			if (nextRow == to.getRow()) 
			{
				return true;
			}
			
			// found a piece in the way so invalid move
			else if (b.getPieceAt(nextCoordinate) != null)
			{
				break;
			}
		}
		
		// condition for the move is not a valid one by the caller piece
		return false;
	}

	/**
	 * This method is called when trying to verify if the particular piece can move
	 * vertically while following the rules of chess
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece can move vertically to the desired coordinate: 
	 * 			true -> it is a valid move, false -> it is not a valid move
	 */
	private boolean canMoveVertically(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		// first check the piece at the destination to see if the move
		// will be valid upon reaching the destination
		if (checkForPieceAtDestination(to, b, movingPieceColor))
		{
			// verify that it is moving vertically
			if (from.getColumn() == to.getColumn()) 
			{
				// check to see if it can move vertically upwards
				if (from.getRow() < to.getRow()) 
				{
					return canMoveVertically_Up(from, to, b);
				}

				// check to see if it can move vertically downwards
				else 
				{
					return canMoveVertically_Down(from, to, b);
				}
			}
		}
		
		// condition for the move is not a valid one by the caller piece
		return false;
	}
	
	
	/**
	 * This method is called when the piece wants to move right in a horizontal direction.
	 * It determines if the piece can move horizontally right to the desired coordinate while
	 * following the rules of chess
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece can move horizontally right to the desired coordinate
	 *         	true -> it is a valid move for moving horizontally right, false -> it is not a valid move
	 */
	private boolean canMoveHorizontally_Right(Coordinate from, Coordinate to, Board b)
	{
		// check if there are any pieces in the way when moving
		for (int nextColumn = from.getColumn() + 1; nextColumn <= to.getColumn(); nextColumn++) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(to.getRow(), nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (nextColumn == to.getColumn()) 
			{
				return true;
			}
			
			else if (b.getPieceAt(nextCoordinate) != null)
			{
				break;
			}
		} 
		
		// if there is an invalid move along the way, will come here
		return false;
	}
	
	/**
	 * see canMoveHorizontally_Right function. Same functionality, except this one checks for
	 * if moving horizontally left is a valid chess move
	 */
	private boolean canMoveHorizontally_Left(Coordinate from, Coordinate to, Board b)
	{	
		// checking to make sure no pieces are in the way when moving horizontally
		for (int nextColumn = from.getColumn() - 1; nextColumn >= to.getColumn(); nextColumn--) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(to.getRow(), nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (nextColumn == to.getColumn()) 
			{
				return true;
			}
			
			else if (b.getPieceAt(nextCoordinate) != null)
			{
				break;
			}
		}
		
		// if there is an invalid move along the way, will come here
		return false;
	}
	
	/**
	 * This method is called when trying to verify if the particular piece can move
	 * horizontally while following the rules of chess
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece can move horizontally to the desired coordinate: 
	 * 			true -> it is a valid move, false -> it is not a valid move
	 */
	private boolean canMoveHorizontally(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		// first check the piece at the destination to see if the move
		// will be valid upon reaching the destination
		if (checkForPieceAtDestination(to, b, movingPieceColor))
		{
			// verifying that it is moving horizontally
			if (from.getRow() == to.getRow()) 
			{
				// piece is attempting to move right horizontally
				if (from.getColumn() < to.getColumn()) 
				{
					return canMoveHorizontally_Right(from, to, b);
				}
				
				// piece is attempting to move left horizontally
				else 
				{
					return canMoveHorizontally_Left(from, to, b);
				}
			}
		}
		
		// is not moving horizontally
		return false;
	}

	/**
	 * This method is called when the piece wants to move up right in a diagonally direction.
	 * It is called by the canMoveDiagonally_Up function.
	 * It determines if the piece can move diagonally up right to the desired coordinate while
	 * following the rules of chess
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece can move diagonally up right to the desired coordinate
	 *         true -> it is a valid move for moving diagonally up right, false -> it is
	 *         not a valid move
	 */
	private boolean canMoveDiagonally_UpRight(Coordinate from, Coordinate to, Board b)
	{
		int nextRow = from.getRow() + 1;
		int nextColumn = from.getColumn() + 1;

		// I believe for diagonals in a square, you move vertically and horizontally the
		// same amount of times
		while (nextRow <= to.getRow() && nextColumn <= to.getColumn()) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
			{
				return true;
			}
			
			else if (b.getPieceAt(nextCoordinate) != null)
			{
				break;
			}
			
			nextRow++;
			nextColumn++;
		}
		
		// if an invalid move was dected, comes to here
		return false;
	}
	
	/**
	 * see canMoveDiagonally_UpRight function. Same functionality, except this one checks for
	 * if moving diagonally up left is a valid chess move and is called by the
	 * canMoveDiagonally_Up function
	 */
	private boolean canMoveDigonally_UpLeft(Coordinate from, Coordinate to, Board b)
	{
		int nextRow = from.getRow() + 1;
		int nextColumn = from.getColumn() - 1;

		// I believe for diagonals in a square, you move vertically and horizontally the
		// same amount of times
		while (nextRow <= to.getRow() && nextColumn >= to.getColumn()) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
			{
				return true;
			}
			
			else if (b.getPieceAt(nextCoordinate) != null)
			{
				break;
			}
			nextRow++;
			nextColumn--;
		}
		
		// if an invalid move was dected, comes to here
		return false;
	}

	/**
	 * This method is called to verify if the piece can move diagonally up. If the piece
	 * is able to move diagonally up in either the right or left directions, then the
	 * piece is able to move diagonally up to the destination coordinate sucessfully -
	 * is a valid move
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece is able to move diagonally up: 
	 * 			true -> piece can move diagonally up, false -> piece cannot move diagonally up
	 */
	private boolean canMoveDiagonally_Up(Coordinate from, Coordinate to, Board b)
	{
		// moving diagonally up right
		if (from.getColumn() < to.getColumn()) 
		{
			return canMoveDiagonally_UpRight(from, to, b);
		}
		
		// moving diagonally up left
		else 
		{
			return canMoveDigonally_UpLeft(from, to, b);
		}
		
	}
	
	/**
	 * see canMoveDiagonally_UpRight function. Same functionality, except this one checks for
	 * if moving diagonally down right is a valid chess move and is called by the
	 * canMoveDiagonally_Down function
	 */
	private boolean canMoveDiagonally_DownRight(Coordinate from, Coordinate to, Board b)
	{
		int nextRow = from.getRow() - 1;
		int nextColumn = from.getColumn() + 1;

		// I believe for diagonals in a square, you move vertically and horizontally the
		// same amount of times
		while (nextRow >= to.getRow() && nextColumn <= to.getColumn()) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
			{
				return true;
			}
			
			else if (b.getPieceAt(nextCoordinate) != null)
			{
				break;
			}
			nextRow--;
			nextColumn++;
		}
		
		// if an invalid move was dected, comes to here
		return false;
	}
 
	/**
	 * see canMoveDiagonally_UpRight function. Same functionality, except this one checks for
	 * if moving diagonally down left is a valid chess move and is called by the
	 * canMoveDiagonally_Down function
	 */
	private boolean canMoveDiagonally_DownLeft(Coordinate from, Coordinate to, Board b)
	{
		int nextRow = from.getRow() - 1;
		int nextColumn = from.getColumn() - 1;
 
		// I believe for diagonals in a square, you move vertically and horizontally the
		// same amount of times
		while (nextRow >= to.getRow() && nextColumn >= to.getColumn()) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
			{
				return true;
			}
			
			else if (b.getPieceAt(nextCoordinate) != null)
			{
				break;
			}
			nextRow--;
			nextColumn--;
		}
		
		// if an invalid move was dected, comes to here
		return false;
	}

	/**
	 * See the description in the canMoveDiagonaly_Up function. It has the same functionality
	 * but it checks to see if the piece can move diagonally down 
	 */
	private boolean canMoveDiagonally_Down(Coordinate from, Coordinate to, Board b)
	{
		if (from.getColumn() < to.getColumn()) 
		{
			return canMoveDiagonally_DownRight(from, to, b);
		}
		
		else 
		{
			return canMoveDiagonally_DownLeft(from, to, b);
		}

	}

	/**
	 * This method is called to verify if the piece can move diagonally. If the piece is
	 * able to move diagonally in either the upwards direction or the downwards direction,
	 * then the piece is able to move diagonally to the destination coordinate
	 * successfully - it is a valid move.
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece is able to move diagionally: 
	 * 			true -> piece can move diagonally, false -> piece cannot move diagonally
	 */
	private boolean canMoveDiagonally(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		// first check the piece at the destination to see if the move
		// will be valid upon reaching the destination
		if (checkForPieceAtDestination(to, b, movingPieceColor))
		{
			if (from.getColumnDistance(to) == from.getRowDistance(to))
			{
				if (from.getRow() < to.getRow()) 
				{
					return canMoveDiagonally_Up(from, to, b);
				}
				
				else
				{
					return canMoveDiagonally_Down(from, to, b);
				} 
			}
			
			else 
			{
				return false; 
			}
		}
		
		return false;
	}

	/**
	 * @return the hasMoved
	 */
	public boolean hasMoved()
	{
		return hasMoved;
	}
 
	/**
	 * Once it moves, you can't change it.
	 * 
	 * @param hasMoved
	 *            the hasMoved to set
	 */
	public void setHasMoved()
	{
		hasMoved = true;
	}
}
