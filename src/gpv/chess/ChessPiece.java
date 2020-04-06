/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

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
	 * This method is called when trying to determine if the move from the source
	 * coordinate to the destination coordinate is a valid move. It determines if the
	 * given coordinate is inside the board.
	 * 
	 * @param coord
	 *            - the position to check if it is inside the board
	 * @param b
	 *            - signfies the board that the coordinate is referring to
	 * @return whether the coordinate is a valid coordinate within the board: true -> a
	 *         valid coordinate, false -> not a valid coordinate
	 */
	private boolean insideBoard(Coordinate coord, Board b)
	{
		if (coord.getRow() > 0 && coord.getRow() <= b.nRows) {
			if (coord.getColumn() > 0 && coord.getColumn() <= b.nRows) {
				return true;
			}

		}

		return false;
	}
	
	private boolean checkAllThreeDirections(Coordinate from, Coordinate to, Board b)
	{
		int rowDistance = Math.abs(to.getRow() - from.getRow());
		int columnDistance = Math.abs(to.getColumn() - from.getColumn());
		
		if (from.getColumn() == to.getColumn())
		{
			return canMoveVertically(from, to, b);
		}
		 
		else if (from.getRow() == to.getRow())
		{
			return canMoveHorizontally(from, to, b);
		}
		
		else if (rowDistance == columnDistance)
		{
			return canMoveDiagonally(from, to, b);
		}
		
		return false;
	}
	
	/*
	 * TODO write the lambda functions of the movement of these pieces
	 */
	
	private boolean checkValidCastling(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		// doing the right castling
		if (from.getColumn() + 2 == to.getColumn())
		{
			Coordinate rookCoordinate = Coordinate.makeCoordinate(from.getRow(), 8);
			
			if (b.getPieceAt(rookCoordinate) != null)
			{
				PieceName pieceType = ((ChessPieceDescriptor) b.getPieceAt(rookCoordinate).getDescriptor()).getName();
				PlayerColor pieceColor = ((ChessPieceDescriptor) b.getPieceAt(rookCoordinate).getDescriptor()).getColor();
				
				if (pieceType.equals(PieceName.ROOK) && pieceColor.equals(movingPieceColor))
				{
					if (!((ChessPiece)b.getPieceAt(rookCoordinate)).hasMoved())
					{
						// loop until get to rook coordinate to check if no pieces in the way
						int startColumn = from.getColumn() + 1;
						
						while (startColumn <= to.getColumn())
						{
							Coordinate nextCoordinate = Coordinate.makeCoordinate(from.getRow(), startColumn);
							
							if (b.getPieceAt(nextCoordinate) != null)
							{
								return false;
							}
							
							startColumn++; 
						}
						
						// if all empty in between
						return true; 
					}
				}
			} 
		}
		
		// doing the left castling
		else if (from.getColumn() - 2 == to.getColumn())
		{
			Coordinate rookCoordinate = Coordinate.makeCoordinate(from.getRow(), 1);
			
			if (b.getPieceAt(rookCoordinate) != null)
			{
				PieceName pieceType = ((ChessPieceDescriptor) b.getPieceAt(rookCoordinate).getDescriptor()).getName();
				PlayerColor pieceColor = ((ChessPieceDescriptor) b.getPieceAt(rookCoordinate).getDescriptor()).getColor();
				
				if (pieceType.equals(PieceName.ROOK) && pieceColor.equals(movingPieceColor))
				{
					if (!((ChessPiece)b.getPieceAt(rookCoordinate)).hasMoved())
					{ 
						// loop until get to rook coordinate to check if no pieces in the way
						int startColumn = from.getColumn() - 1;
						
						while (startColumn >= to.getColumn())
						{ 
							Coordinate nextCoordinate = Coordinate.makeCoordinate(from.getRow(), startColumn);
							
							if (b.getPieceAt(nextCoordinate) != null)
							{
								return false;
							}
							
							startColumn--;
						}
						// if all empty in between
						return true;
					}
				}
			}
		}
		
		return false;
	}

	// can move one space at a time in any direction
	private ChessPieceValidator king = (from, to, b) -> {
		
		// checking if trying to make a castling move
		if (from.getRow() == to.getRow() &&
				Math.abs(to.getColumn() - from.getColumn()) == 2)
		{
			if (!hasMoved())
			{
				return checkValidCastling(from, to, b);
			}
			
			else
			{
				return false;
			}
			
		}
		
		// cannot make a castling move
		else if ((Math.abs(to.getRow() - from.getRow()) > 1)
				|| (Math.abs(to.getColumn() - from.getColumn()) > 1)) 
		{
			return false;
		}
		
		else 
		{ 
			return checkAllThreeDirections(from, to, b);
		}
		
	};
 
	// can move in any direction as far as it can without jumping over any pieces
	private ChessPieceValidator queen = (from, to, b) -> 
	{
		return checkAllThreeDirections(from, to, b);
	};

	// can move diagonally as far as they want, but can only move diagonally
	private ChessPieceValidator bishop = (from, to, b) ->
	{
		return canMoveDiagonally(from, to, b);
	 
	};

	// moves in an L shape (2 in one direction, 1 in the 90 degree direction) and can jump
	// over pieces
	private ChessPieceValidator knight = (from, to, b) ->
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		// L vertically
		if (Math.abs(to.getRow() - from.getRow()) == 2)
		{
			if (Math.abs(to.getColumn() - from.getColumn()) == 1)
			{
				// check if piece occupies the space and what color piece
				if (b.getPieceAt(to) == null)
				{
					return true;
				}
				
				else 
				{
					PlayerColor destinationPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to)
							.getDescriptor()).getColor();
					
					if (!movingPieceColor.equals(destinationPieceColor)) 
					{
						return true;
					}
				}
			}
		}
		// L horizontally
		else if (Math.abs(to.getColumn() - from.getColumn()) == 2)
		{
			if (Math.abs(to.getRow() - from.getRow()) == 1)
			{
				// no piece at destination spot
				if (b.getPieceAt(to) == null)
				{
					return true;
				} 
				
				// there is a piece at the destination
				else 
				{
					PlayerColor destinationPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();
					
					if (!movingPieceColor.equals(destinationPieceColor)) 
					{
						return true;
					}
				}
			}
		}
		
		return false;
	};

	private ChessPieceValidator rook = (from, to, b) -> 
	{
		if (from.getColumn() == to.getColumn())
		{
			return canMoveVertically(from, to, b);
		}
		
		else if (from.getRow() == to.getRow())
		{
			return canMoveHorizontally(from, to, b);
		}

		return false;
	}; 

	private boolean checkPawnMove_OneSpace(Coordinate from, Coordinate to, Board b, boolean isBlackPiece)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		if (isBlackPiece)
		{
			if (from.getRow() - 1 == to.getRow()) 
			{
				// if making only one move forward
				if (from.getColumn() == to.getColumn())
				{
					// make sure no piece in front
					if (b.getPieceAt(to) == null)
					{
						return true;
					}
				}
				
				// see if the pawn can make a capture move
				else if (Math.abs(to.getColumn() - from.getColumn()) == 1)
				{
					if (b.getPieceAt(to) != null)
					{
						PlayerColor destinationPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();
						
						if (!movingPieceColor.equals(destinationPieceColor))
						{
							return true;
						}
					} 
				}
			}
			
			return false;
		}
		
		else 
		{
			if (from.getRow() + 1 == to.getRow()) 
			{
				// if making a one move forward
				if (from.getColumn() == to.getColumn())
				{
					// make sure no piece in front
					if (b.getPieceAt(to) == null)
					{
						return true;
					}
				}
				
				// see if the pawn can make a capture move
				else if (Math.abs(to.getColumn() - from.getColumn()) == 1)
				{
					if (b.getPieceAt(to) != null)
					{
						PlayerColor destinationPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();
						
						if (!movingPieceColor.equals(destinationPieceColor))
						{
							return true;
						}
					} 
				}
			}
			
			return false;
		}
		 
		
	}
	
	/*
	 * On the pawn's first move, can move 2 squares forward 
	 * After first move, can only
	 * move 1 square forward (use hasMoved()) Cannot move backwards 
	 * Can only capture one
	 * square diagonally
	 */
	private ChessPieceValidator pawn = (from, to, b) ->
	{
		//PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		// not first move situation
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();
		
		if (movingPieceColor.equals(PlayerColor.WHITE))
		{
			if (!hasMoved())
			{
				if (from.getRow() + 2 == to.getRow()) 
				{
					if (from.getColumn() == to.getColumn())
					{
						// just make sure no pieces in the way
						Coordinate oneSpaceForward = Coordinate.makeCoordinate(from.getRow() + 1, from.getColumn());
						
						if (b.getPieceAt(oneSpaceForward) == null && 
								b.getPieceAt(to) == null)
						{
							return true;
						}
					}
					
					// pawn wants to move two spaces (non-vertically) so return false (down below)
				}
				
				// pawn wants to move only one space so checks for the one space movement
				else 
				{
					// treat the same as a one move forward
					return checkPawnMove_OneSpace(from, to, b, false);
				}
			}
			
			// the pawn has moved before, so it can only move one space
			else
			{
				return checkPawnMove_OneSpace(from, to, b, false);
			}
			
			return false;
		}
		
		// it is a black pawn
		else 
		{
			if (!hasMoved())
			{
				if (from.getRow() - 2 == to.getRow()) 
				{
					if (from.getColumn() == to.getColumn())
					{
						// just make sure no pieces in the way
						Coordinate oneSpaceForward = Coordinate.makeCoordinate(from.getRow() - 1, from.getColumn());
						
						// checking the two spaces forward if they are empty
						// if yes -> true, if no -> false
						if (b.getPieceAt(oneSpaceForward) == null && 
								b.getPieceAt(to) == null)
						{
							return true;
						}
					}
					
					// pawn wants to move two spaces (non-vertically) so return false (down below)
				}
				
				// pawn wants to move only one space so checks for the one space movement
				else 
				{
					// treat the same as a one move forward
					return checkPawnMove_OneSpace(from, to, b, true);
				}
			}
			
			// the pawn has moved before, so it can only move one space
			else
			{
				return checkPawnMove_OneSpace(from, to, b, true);
			}
			
			return false;
		}
		
	};
	
	/*
	 * @see gpv.Piece#canMove(gpv.util.Coordinate, gpv.util.Coordinate, gpv.util.Board)
	 * IMPLEMENT HERE
	 */
	@Override
	public boolean canMove(Coordinate from, Coordinate to, Board b)
	{

		// checking if where I want to move is a valid position within the board
		if ((!insideBoard(to, b)) || (from.equals(to)) 
			|| (b.getPieceAt(from) == null)) 
		{
			return false;
		}
		

		PieceName movingPieceType = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getName();

		boolean result = false;

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
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();

		for (int nextRow = from.getRow() + 1; nextRow <= to.getRow(); nextRow++) {
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow,
					to.getColumn());
			// check if there is a piece in the way (no jumping over)
			if (b.getPieceAt(nextCoordinate) == null) {
				// got to the desired location and there is no piece at the coordinate
				if (nextRow == to.getRow()) {
					return true;
				}

			}
			// there is a piece in the way
			else {
				// check if next move will be the desired location and see if I can
				// remove an opponent piece
				if (nextRow == to.getRow()) 
				{
					PlayerColor nextPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();

					// found an oponent piece at the desired location
					if (!movingPieceColor.equals(nextPieceColor)) 
					{
						return true;
					}
				}
				
				else
				{
					return false;
				}
			}
		}

		return false;
	}

	/**
	 * This method is called when the piece wants to move downwards in a vertical
	 * direction. It determines if the piece can move vertically down to the desired
	 * coordinate while following the rules of chess
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece can move vertically downwards to the desired coordinate
	 *         true -> it is a valid move for moving vertically downwords, false -> it is
	 *         not a valid move
	 */
	private boolean canMoveVertically_Down(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();

		for (int nextRow = from.getRow() - 1; nextRow >= to.getRow(); nextRow--) {
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, to.getColumn());
			
			// check if there is a piece in the way (no jumping over)
			if (b.getPieceAt(nextCoordinate) == null) {
				// got to the desired location and there is no piece at the coordinate
				if (nextRow == to.getRow()) {
					return true;
				}
			}

			// there is a piece in the way
			else {
				// check if next move will be the desired location and see if I can
				// remove an opponent piece
				if (nextRow == to.getRow()) {
					PlayerColor nextPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();

					// found an oponent piece at the desired location
					if (!movingPieceColor.equals(nextPieceColor)) {
						return true;
					}
				}
				
				else
				{
					return false;
				}
			}
		}

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
	 * @return whether the piece can move vertically to the desired coordinate: true -> it
	 *         is a valid move, false -> it is not a valid move
	 */
	private boolean canMoveVertically(Coordinate from, Coordinate to, Board b)
	{
		if (from.getColumn() == to.getColumn()) {
			// check to see if it can move vertically upwards
			if (from.getRow() < to.getRow()) {
				return canMoveVertically_Up(from, to, b);
			}

			// check to see if it can move vertically downwards
			else {
				return canMoveVertically_Down(from, to, b);
			}
		}

		return false;
	}

	private boolean canMoveHorizontally_Right(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from)
				.getDescriptor()).getColor();

		for (int nextColumn = from.getColumn() + 1; nextColumn <= to.getColumn(); nextColumn++) {
			Coordinate nextCoordinate = Coordinate.makeCoordinate(to.getRow(), nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (b.getPieceAt(nextCoordinate) == null) {
				// got to the desired location and there is no piece at the coordinate
				if (nextColumn == to.getColumn()) {
					return true;
				}
			}

			// there is a piece in the way
			else {
				// check if next move will be the desired location and see if I can
				// remove an opponent piece
				if (nextColumn == to.getColumn()) {
					PlayerColor nextPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();

					// found an oponent piece at the desired location
					if (!movingPieceColor.equals(nextPieceColor)) {
						return true;
					}

				}
				
				else
				{
					return false;
				}
			}
		}

		return false;
	}

	private boolean canMoveHorizontally_Left(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from)
				.getDescriptor()).getColor();

		for (int nextColumn = from.getColumn() - 1; nextColumn >= to
				.getColumn(); nextColumn--) {
			Coordinate nextCoordinate = Coordinate.makeCoordinate(to.getRow(),
					nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (b.getPieceAt(nextCoordinate) == null) {
				// got to the desired location and there is no piece at the coordinate
				if (nextColumn == to.getColumn()) {
					return true;
				}
			}

			// there is a piece in the way
			else {
				// check if next move will be the desired location and see if I can
				// remove an opponent piece
				if (nextColumn == to.getColumn()) {
					PlayerColor nextPieceColor = ((ChessPieceDescriptor) b
							.getPieceAt(to).getDescriptor()).getColor();

					// found an oponent piece at the desired location
					if (!movingPieceColor.equals(nextPieceColor)) {
						return true;
					}

				}
				
				else
				{
					return false;
				}
			}
		}

		return false;
	}

	private boolean canMoveHorizontally(Coordinate from, Coordinate to, Board b)
	{
		if (from.getRow() == to.getRow()) 
		{
			// piece is attempting to move right horizontally
			if (from.getColumn() < to.getColumn()) 
			{
				return canMoveHorizontally_Right(from, to, b);
			}
			
			// piece is attempting to move left horizontally
			else {
				return canMoveHorizontally_Left(from, to, b);
			}
		}

		return false;
	}

	private boolean canMoveDiagonally_UpRight(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();

		int nextRow = from.getRow() + 1;
		int nextColumn = from.getColumn() + 1;

		// I believe for diagonals in a square, you move vertically and horizontally the
		// same amount of times
		while (nextRow <= to.getRow() && nextColumn <= to.getColumn()) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (b.getPieceAt(nextCoordinate) == null) 
			{
				// got to the desired location and there is no piece at the coordinate
				if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
				{
					return true;
				}
			}

			// there is a piece in the way
			else 
			{
				// check if next move will be the desired location and see if I can
				// remove an opponent piece
				if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
				{
					PlayerColor nextPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();

					// found an oponent piece at the desired location
					if (!movingPieceColor.equals(nextPieceColor)) 
					{
						return true;
					}
				}
				
				else
				{
					return false;
				}
			}
			nextRow++;
			nextColumn++;
		}
		
		return false;
	}

	private boolean canMoveDigonally_UpLeft(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();

		int nextRow = from.getRow() + 1;
		int nextColumn = from.getColumn() - 1;

		// I believe for diagonals in a square, you move vertically and horizontally the
		// same amount of times
		while (nextRow <= to.getRow() && nextColumn >= to.getColumn()) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (b.getPieceAt(nextCoordinate) == null) 
			{
				// got to the desired location and there is no piece at the coordinate
				if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
				{
					return true;
				}
			}

			// there is a piece in the way
			else 
			{
				// check if next move will be the desired location and see if I can
				// remove an opponent piece
				if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
				{
					PlayerColor nextPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();

					// found an oponent piece at the desired location
					if (!movingPieceColor.equals(nextPieceColor)) 
					{
						return true;
					}
				}
				
				else
				{
					return false; 
				}
			}
			nextRow++;
			nextColumn--;
		}
		
		return false;
	}

	private boolean canMoveDiagonally_Up(Coordinate from, Coordinate to, Board b)
	{
		if (from.getColumn() < to.getColumn()) 
		{
			return canMoveDiagonally_UpRight(from, to, b);
		}
		
		else 
		{
			return canMoveDigonally_UpLeft(from, to, b);
		}
		
	}

	private boolean canMoveDiagonally_DownRight(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();

		int nextRow = from.getRow() - 1;
		int nextColumn = from.getColumn() + 1;

		// I believe for diagonals in a square, you move vertically and horizontally the
		// same amount of times
		while (nextRow >= to.getRow() && nextColumn <= to.getColumn()) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (b.getPieceAt(nextCoordinate) == null) 
			{
				// got to the desired location and there is no piece at the coordinate
				if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
				{
					return true;
				}
			}

			// there is a piece in the way
			else 
			{
				// check if next move will be the desired location and see if I can
				// remove an opponent piece
				if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
				{
					PlayerColor nextPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();

					// found an oponent piece at the desired location
					if (!movingPieceColor.equals(nextPieceColor)) 
					{
						return true;
					}
				}
				
				else
				{
					return false;
				}
			}
			nextRow--;
			nextColumn++;
		}
		
		return false;
	}

	private boolean canMoveDiagonally_DownLeft(Coordinate from, Coordinate to, Board b)
	{
		PlayerColor movingPieceColor = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getColor();

		int nextRow = from.getRow() - 1;
		int nextColumn = from.getColumn() - 1;
 
		// I believe for diagonals in a square, you move vertically and horizontally the
		// same amount of times
		while (nextRow >= to.getRow() && nextColumn >= to.getColumn()) 
		{
			Coordinate nextCoordinate = Coordinate.makeCoordinate(nextRow, nextColumn);

			// check if there is a piece in the way (no jumping over)
			if (b.getPieceAt(nextCoordinate) == null) 
			{
				// got to the desired location and there is no piece at the coordinate
				if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
				{
					return true;
				}
			}

			// there is a piece in the way
			else 
			{
				// check if next move will be the desired location and see if I can
				// remove an opponent piece
				if (nextRow == to.getRow() && nextColumn == to.getColumn()) 
				{
					PlayerColor nextPieceColor = ((ChessPieceDescriptor) b.getPieceAt(to).getDescriptor()).getColor();

					// found an oponent piece at the desired location
					if (!movingPieceColor.equals(nextPieceColor)) 
					{
						return true;
					}
				}
				
				else
				{
					return false;
				}
				
			}
			nextRow--;
			nextColumn--;
		}
		
		return false;
	}

	/**
	 * This method is called to verify if the piece can move diagonally down. If the piece
	 * is able to move diagonally down in either the right or left directions, then the
	 * piece is able to move diagonally down to the destination coordinate sucessfully -
	 * is a valid move
	 * 
	 * @param from
	 *            the source coordinate that the piece is moving from
	 * @param to
	 *            the destination coordinate that the piece is moving to
	 * @param b
	 *            signfies the board that the piece is moving on
	 * @return whether the piece is able to move diagonally down: true -> piece can move
	 *         diagonally down, false -> piece cannot move diagonally down
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
	 * @return whether the piece is able to move diagionally: true -> piece can move
	 *         diagonally, false -> piece cannot move diagonally
	 */
	private boolean canMoveDiagonally(Coordinate from, Coordinate to, Board b)
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
