/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import gpv.Piece;
import gpv.util.*;

/**
 * The chess piece is a piece with some special properties that are used for
 * determining whether a piece can move. It implements the Piece interface
 * and adds properties and methods that are necessary for the chess-specific
 * behavior.
 * @version Feb 21, 2020
 */
public class ChessPiece implements Piece<ChessPieceDescriptor>
{
	private final ChessPieceDescriptor descriptor;
	private boolean hasMoved;	// true if this piece has moved
	
	/**
	 * The only constructor for a ChessPiece instance. Requires a descriptor.
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
	 * 
	 * Description
	 * @param coord
	 * @param b
	 * @return
	 */
	private boolean insideBoard(Coordinate coord, Board b)
	{
		if (coord.getRow() > 0 && coord.getRow() <= b.nRows) 
		{
			if (coord.getColumn() > 0 && coord.getColumn() <= b.nRows)
			{
				return true;
			}
			
		}
		
		return false;
	}
	
	/*
	 * TODO
	 * write the lambda functions of the movement of these pieces
	 */
	
	// can move one space at a time in any direction
	private ChessPieceValidator king = (from, to, b) ->
	{
		if ( (Math.abs(to.getRow() - from.getRow()) > 1) ||
				(Math.abs(to.getColumn() - from.getColumn()) > 1) )
		{
			return false;
		}
		
		boolean verticalMove = canMoveVertically(from, to, b);
		boolean horizontalMove = canMoveHorizontally(from, to, b);
		boolean diagonalMove = canMoveDiagonally(from, to, b);
		
		return (verticalMove || horizontalMove || diagonalMove);
	};
	
	// can move in any direction as far as it can without jumping over any pieces
	private ChessPieceValidator queen = (from, to, b) ->
	{
		boolean verticalMove = canMoveVertically(from, to, b);
		boolean horizontalMove = canMoveHorizontally(from, to, b);
		boolean diagonalMove = canMoveDiagonally(from, to, b);
		
		return (verticalMove || horizontalMove || diagonalMove);
	};
	
	private ChessPieceValidator bishop;
	
	private ChessPieceValidator knight;
	
	private ChessPieceValidator rook = (from, to, b) ->
	{
		boolean verticalMove = canMoveVertically(from, to, b);
		boolean horizontalMove = canMoveHorizontally(from, to, b);
		
		return (verticalMove || horizontalMove);
	};
	
	private ChessPieceValidator pawn;
	
	
	private boolean canMoveVertically_Up(Coordinate from, Coordinate to, Board b)
	{
		return false;
	}
	
	private boolean canMoveVertically_Down(Coordinate from, Coordinate to, Board b)
	{
		return false;
	}
	
	private boolean canMoveVertically(Coordinate from, Coordinate to, Board b)
	{
		return (canMoveVertically_Up(from, to, b) || canMoveVertically_Down(from, to, b));
	}
	
	private boolean canMoveHorizontally_Right(Coordinate from, Coordinate to, Board b)
	{
		return false;
	}
	
	private boolean canMoveHorizontally_Left(Coordinate from, Coordinate to, Board b)
	{
		return false;
	}
	
	private boolean canMoveHorizontally(Coordinate from, Coordinate to, Board b)
	{
		return (canMoveHorizontally_Right(from, to, b) || canMoveHorizontally_Left(from, to, b));
	}
	
	private boolean canMoveDiagonally_UpRight(Coordinate from, Coordinate to, Board b)
	{
		return false;
	}
	
	private boolean canMoveDigonally_UpLeft(Coordinate from, Coordinate to, Board b)
	{
		return false;
	}
	
	private boolean canMoveDiagonally_Up(Coordinate from, Coordinate to, Board b)
	{
		return (canMoveDiagonally_UpRight(from, to, b) || canMoveDigonally_UpLeft(from, to, b));
	}
	
	private boolean canMoveDiagonally_DownRight(Coordinate from, Coordinate to, Board b)
	{
		return false;
	}
	
	private boolean canMoveDiagonally_DownLeft(Coordinate from, Coordinate to, Board b)
	{
		return false;
	}
	
	private boolean canMoveDiagonally_Down(Coordinate from, Coordinate to, Board b)
	{
		return (canMoveDiagonally_DownRight(from, to, b) || canMoveDiagonally_DownLeft(from, to, b));
	}
	
	private boolean canMoveDiagonally(Coordinate from, Coordinate to, Board b) 
	{
		return (canMoveDiagonally_Up(from, to, b) || canMoveDiagonally_Down(from, to, b));
	}
	
	
	/*
	 * @see gpv.Piece#canMove(gpv.util.Coordinate, gpv.util.Coordinate, gpv.util.Board)
	 * IMPLEMENT HERE
	 */
	@Override
	public boolean canMove(Coordinate from, Coordinate to, Board b)
	{
		// TODO Auto-generated method stub
		// propably need to verify the moving location up, down, left, right, horizontals
		
		/*
		 * TODO
		 * check if the from and to coordinates are valid (DONE)
		 * check the piece (know from the descriptor) (DONE)
		 * depending on the piece, check if the move is valid
		 * 		not valid if another piece on the same team
		 * 		not valid if that certain piece cannot make that movement
		 */
		
		// checking if where I want to move is a valid position within the board
		if ( (!insideBoard(to, b)) || (from.equals(to)) ) 
		{
			return false;
		}
		
		PieceName movingPieceType = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getName();
		
		boolean result = false;
		
		switch(movingPieceType)
		{
			case KING :
				result = king.check(from, to, b);
				break;
				
			case QUEEN :
				result = queen.check(from, to, b);
				break;
				
			case BISHOP :
				// move for bishop
				break;
				
			case KNIGHT :
				// move for knight
				break;
				
			case ROOK :
				result = rook.check(from, to, b);
				break;
				
			case PAWN :
				// move for pawn
				break;
				
			default :
				// idk how it would get to here 
				break;
		}
		
		
		return result;
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
	 * @param hasMoved the hasMoved to set
	 */
	public void setHasMoved()
	{
		hasMoved = true;
	}
}
