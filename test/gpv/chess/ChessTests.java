/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import static gpv.chess.ChessPieceDescriptor.*;
import static gpv.util.Coordinate.makeCoordinate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import gpv.util.*;

/**
 * Description
 * @version Apr 3, 2020
 */
class ChessTests
{

	private static ChessPieceFactory factory = null;
	private Board board;
	
	@BeforeAll
	public static void setupBeforeTests()
	{
		factory = new ChessPieceFactory();
	}
	
	@BeforeEach
	public void setupTest()
	{
		board = new Board(8, 8);
	}
	
	@Test
	void makePiece()
	{
		ChessPiece pawn = factory.makePiece(WHITEPAWN);
		assertNotNull(pawn);
	}
	
	/**
	 * This type of test loops through each value in the Enum and
	 * one by one feeds it as an argument to the test method.
	 * It's worth looking at the different types of parameterized
	 * tests in JUnit: 
	 * https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests
	 * @param d the Enum value
	 */
	@ParameterizedTest
	@EnumSource(ChessPieceDescriptor.class)
	void makeOneOfEach(ChessPieceDescriptor d)
	{
		ChessPiece p = factory.makePiece(d);
		assertNotNull(p);
		assertEquals(d.getColor(), p.getColor());
		assertEquals(d.getName(), p.getName());
	}

	@Test
	void placeOnePiece()
	{
		ChessPiece p = factory.makePiece(BLACKPAWN);
		board.putPieceAt(p, makeCoordinate(2, 2));
		assertEquals(p, board.getPieceAt(makeCoordinate(2, 2)));
	}

	@Test
	void placeTwoPieces()
	{
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));
		assertEquals(bn, board.getPieceAt(makeCoordinate(3, 5)));
		assertEquals(wb, board.getPieceAt(makeCoordinate(2, 6)));
	}
	
	@Test
	void checkForPieceHasMoved()
	{
		ChessPiece bq = factory.makePiece(BLACKQUEEN);
		assertFalse(bq.hasMoved());
		bq.setHasMoved();
		assertTrue(bq.hasMoved());
	}
	
	/*
	@Test
	void thisShouldFailOnDelivery()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2, 5), board));
	}
	*/
	@Test // 1
	void rowCoordinateOfDestinationNotInBoard()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(0,5), board));
	}
	
	@Test // 2
	void columnCoordinateOfDestinationNotinBoard()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,8));
		assertFalse(wk.canMove(makeCoordinate(1,8), makeCoordinate(1,9), board));
	}
	
	@Test // 3
	void pieceMovingToSourceLocation()
	{
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(bk, makeCoordinate(1,5));
		assertFalse(bk.canMove(makeCoordinate(1,5), makeCoordinate(1,5), board));
	}
	
	@Test
	void testPawn1()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(3,5), board));
	}
	
	@Test
	void testPawn2()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(4,5), board));
	}
	
	@Test
	void testPawn3()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		wp.setHasMoved();
		System.out.println(wp.hasMoved());
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(4,5), board));
	}
	
	@Test
	void testPawn4()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(3,5));
		board.putPieceAt(bp, makeCoordinate(4,5));
		assertFalse(wp.canMove(makeCoordinate(3,5), makeCoordinate(4,5), board));
	}
	
	@Test
	void testPawn5()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(3,5));
		board.putPieceAt(bp, makeCoordinate(4,6));
		assertTrue(wp.canMove(makeCoordinate(3,5), makeCoordinate(4,6), board));
	}
	
	@Test
	void testPawn6()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(3,5));
		board.putPieceAt(bp, makeCoordinate(4,4));
		assertTrue(wp.canMove(makeCoordinate(3,5), makeCoordinate(4,4), board));
	}
	
///////////////////////////// Test Cases for King Piece ////////////////////////////////////////
	@Test // 4
	void kingMovesUpOneSpace_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,5), board));
	}
	
	@Test // 4
	void kingMovesTwoSpaces()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(3,5), board));
	}
	
	@Test // 5
	void kingMovesVerticallyDown_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,5), board));
	}
	
	@Test // 5
	void kingMovesHorizontallyRight_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,6), board));
	}
	
	@Test // 5
	void kingMovesHorizontallyLeft_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,4), board));
	}
	
	@Test
	void kingMovesDiagonallyUpRight_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,6), board));
	}
	
	@Test
	void kingMovesDiagonallyUpLeft_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,4), board));
	}
	
	@Test
	void kingMovesDiagonallyDownRight_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,6), board));
	}
	
	@Test
	void kingMovesDiagonallyDownLeft_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,4), board));
	}
	
	@Test // 6
	void kingMovesVerticallyDown_OpponentAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		board.putPieceAt(bk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,5), board));
	}
	
	@Test // 6
	void kingMovesVerticallyUp_OpponentAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,5), board));
	}
	
	@Test // 6
	void kingMovesHorizontallyRight_OpponentAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(1,6));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,6), board));
	}
	
	@Test // 6
	void kingMovesHorizontallyLeft_OpponentAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(1,4));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,4), board));
	}
	
	@Test // 6
	void kingMovesDiagonallyUpRight_OpponentAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,6));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,6), board));
	}
	
	@Test // 6
	void kingMovesDiagonallyUpLeft_OpponentAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,4));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,4), board));
	}
	
	@Test // 6
	void kingMovesDiagonallyDownRight_OpponentAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		board.putPieceAt(bk, makeCoordinate(1,6));
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,6), board));
	}
	
	@Test // 6
	void kingMovesDiagonallyDownLeft_OpponentAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		board.putPieceAt(bk, makeCoordinate(1,4));
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,4), board));
	}
	
	@Test // 6
	void kingMovesVerticallyDown_SameColorAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wk, makeCoordinate(2,5));
		board.putPieceAt(wb, makeCoordinate(1,5));
		assertFalse(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,5), board));
	}
	
	@Test // 6
	void kingMovesVerticallyUp_SameColorAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wb, makeCoordinate(2,5));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,5), board));
	}
	
	@Test // 6
	void kingMovesHorizontallyRight_SameColorAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wq, makeCoordinate(1,6));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,6), board));
	}
	
	@Test // 6
	void kingMovesHorizontallyLeft_SameColorAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,4), board));
	}
	
	@Test // 6 (REMOVE)
	void kingMovesDiagonallyUpRight_SameColorAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wq, makeCoordinate(2,6));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,6), board));
	}
	
	@Test // 6
	void kingMovesDiagonallyUpLeft_SameColorAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wq, makeCoordinate(2,4));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,4), board));
	}
	
	@Test // 6
	void kingMovesDiagonallyDownRight_SameColorAtDestination()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(2,5));
		board.putPieceAt(wq, makeCoordinate(1,6));
		assertFalse(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,6), board));
	}
	
	
	

}
