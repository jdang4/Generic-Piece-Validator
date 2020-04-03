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
		if (!insideBoard(to, b)) {

			return false;
		}
		
		// check that i am actually moving (from != to)
		if (from.equals(to))
		{
			return false;
		}
		
		PieceName movingPieceType = ((ChessPieceDescriptor) b.getPieceAt(from).getDescriptor()).getName();
		
		boolean result = false;
		
		switch(movingPieceType)
		{
			case KING :
				// move for king
				break;
				
			case QUEEN :
				// move for queen
				break;
				
			case BISHOP :
				// move for bishop
				break;
				
			case KNIGHT :
				// move for knight
				break;
				
			case ROOK :
				// move for rook
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
