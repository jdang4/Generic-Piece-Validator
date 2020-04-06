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
	
	@Test // 1
	void rowCoordinateOfDestinationIsNotInBoard()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(0,5), board));
	}
	
	@Test // 2
	void columnCoordinateOfDestinationIsNotInBoard()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(1,8));
		assertFalse(wb.canMove(makeCoordinate(1,8), makeCoordinate(1,9), board));
	}
	
	@Test // 3
	void pieceMovingInPlace()
	{
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(bk, makeCoordinate(1,5));
		assertFalse(bk.canMove(makeCoordinate(1,5), makeCoordinate(1,5), board));
	}
	
	@Test
	void noPieceAtSourceLocation()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wk.canMove(makeCoordinate(5,5), makeCoordinate(6,5), board));
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
		wk.setHasMoved();
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
		wk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,6), board));
	}
	
	@Test
	void kingMovesDiagonallyDownLeft_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,4), board));
	}
	
	@Test // 6
	void kingCapturesEnemy_VerticallyDown()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		board.putPieceAt(bk, makeCoordinate(1,5));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,5), board));
	}
	
	@Test // 6
	void kingCapturesEnemy_VerticallyUp()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,5));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,5), board));
	}
	
	@Test // 6
	void kingCapturesEnemy_HorizontallyRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(1,6));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,6), board));
	}
	
	@Test // 6
	void kingCapturesEnemy_HorizontallyLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(1,4));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,4), board));
	}
	
	@Test // 6
	void kingCapturesEnemy_DiagonallyUpRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,6));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,6), board));
	}
	
	@Test // 6
	void kingCapturesEnemy_DiagonallyUpLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,4));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,4), board));
	}
	
	@Test // 6
	void kingCapturesEnemy_DiagonallyDownRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		board.putPieceAt(bk, makeCoordinate(1,6));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,6), board));
	}
	
	@Test 
	void kingCapturesEnemy_DiagonallyDownLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		board.putPieceAt(bk, makeCoordinate(1,4));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,4), board));
	}
	
	@Test // 6
	void kingAttemptsToCaptureSameColor_VerticallyDown()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(2,1));
		wk.setHasMoved();
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertFalse(wk.canMove(makeCoordinate(2,1), makeCoordinate(1,1), board));
	}
	
	@Test // 6
	void kingAttemptsToCaptureSameColor_VerticallyUp()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(1,1));
		board.putPieceAt(wr, makeCoordinate(2,1));
		wr.setHasMoved();
		assertFalse(wk.canMove(makeCoordinate(1,1), makeCoordinate(2,1), board));
	}
	
	@Test // 6
	void kingAttemptsToCaptureSameColor_HorizontallyRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wb, makeCoordinate(1,6));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,6), board));
	}
	
	@Test // 6
	void kingAttemptsToCaptureSameColor_HorizontallyLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,4), board));
	}
	
	@Test
	void kingAttemptsToCaptureSameColor_DiagonallyUpRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wp, makeCoordinate(2,6));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,6), board));
	}
	
	@Test // 6
	void kingAttemptsToCaptureSameColor_DiagonallyUpLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,4), board));
	}
	
	@Test // 6
	void kingAttemptsToCaptureSameColor_DiagonallyDownRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		board.putPieceAt(wb, makeCoordinate(1,6));
		assertFalse(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,6), board));
	}
	
	@Test 
	void kingAttemptsToCaptureSameColor_DiagonallyDownLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertFalse(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,4), board));
	}
	
	@Test
	void kingMakesValidRightCastlingMove()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wr, makeCoordinate(1,8));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,7), board));
	}
	
	@Test
	void kingAttemptsRightCastling_KingHasMovedBefore()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(1,5));
		wk.setHasMoved();
		board.putPieceAt(wr, makeCoordinate(1,8));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,7), board));
	}
	
	@Test
	void kingAttemptsRightCastling_RookHasMovedBefore()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(1,5));
		wr.setHasMoved();
		board.putPieceAt(wr, makeCoordinate(1,8));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,7), board));
	}
	
	@Test 
	void kingAttemptsRightCastlingWithPiecesInBetween()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wkn, makeCoordinate(1, 7));
		board.putPieceAt(wr, makeCoordinate(1,8));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,7), board));
	}
	
	@Test
	void kingMakesValidLeftCastlingMove()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,3), board));
	}
	
	@Test
	void kingAttemptsLeftCastling_KingHasMovedBefore()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(1,5));
		wk.setHasMoved();
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,3), board));
	}
	
	@Test
	void kingAttemptsLeftCastling_RookHasMovedBefore()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(1,5));
		wr.setHasMoved();
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,3), board));
	}
	
	@Test 
	void kingAttemptsLeftCastlingWithPiecesInBetween()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wb, makeCoordinate(1, 3));
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,3), board));
	}
	
//////////////////////// Test Cases for Queen ////////////////////////////////////////////////
	
	@Test
	void queenMovesUpOneSpace_Empty()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(2,4), board));
	}
	
	@Test
	void queenMovesRightThreeSpaces_Empty()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,7), board));
	}
	
	@Test 
	void queenMovesDiagonallyUpRight_Empty()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,6), board));
	}
	
	@Test
	void queenMakesInvalidMove_Empty()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,5), board));
	}
	
	@Test
	void queenAttemptsToJumpOverPiece_VerticallyUp()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,4), board));
	}
	
	@Test
	void queenAttemptsToJumpOverPiece_VerticallyDown()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(3,4));
		wq.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wq.canMove(makeCoordinate(3,4), makeCoordinate(1,4), board));
	}
	
	@Test
	void queenAttemptsToJumpOverPiece_HorizontalRight()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wb, makeCoordinate(1,6));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,7), board));
	}
	
	@Test
	void queenAttemptsToJumpOverPiece_HorizontalLeft()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wb, makeCoordinate(1,3));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,2), board));
	}
	
	@Test
	void queenAttemptsToJumpOverPiece_DiagonallyUpRight()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,6), board));
	}
	
	@Test
	void queenAttemptsToJumpOverPiece_DiagonallyUpLeft()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wp, makeCoordinate(2,3));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,2), board));
	}
	
	@Test
	void queenAttemptsToJumpOverPiece_DiagonallyDownRight()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(3,2));
		wq.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,3));
		assertFalse(wq.canMove(makeCoordinate(3,2), makeCoordinate(1,4), board));
	}
	
	@Test
	void queenAttemptsToJumpOverPiece_DiagonallyDownLeft()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(3,6));
		wq.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wq.canMove(makeCoordinate(3,6), makeCoordinate(1,4), board));
	}
	
	@Test
	void queenCapturesEnemy_Vertically()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(bp, makeCoordinate(2,4));
		bp.setHasMoved();
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(2,4), board));
	}
	
	@Test
	void queenCapturesEnemy_Horizontally()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(bp, makeCoordinate(1,3));
		bp.setHasMoved();
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,3), board));
	}
	
	@Test
	void queenCapturesEnemy_Diagonally()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wq, makeCoordinate(3,4));
		wq.setHasMoved();
		board.putPieceAt(bp, makeCoordinate(1,2));
		bp.setHasMoved();
		assertTrue(wq.canMove(makeCoordinate(3,4), makeCoordinate(1,2), board));
	}
	
	@Test
	void queenAttemptsToCaptureSameColor_Vertically()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(2,4), board));
	}
	
	@Test
	void queenAttemptsToCaptureSameColor_Horizontally()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,5), board));
	}
	
	@Test
	void queenAttemptsToCaptureSameColor_Diagonally()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEKING);
		board.putPieceAt(wq, makeCoordinate(4,7));
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wq.canMove(makeCoordinate(4,7), makeCoordinate(2,5), board));
	}

////////////////////////Test Cases for Bishop ////////////////////////////////////////////////
	
	@Test
	void bishopMovesDiagonallyUpRight_Empty()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(1, 3));
		assertTrue(wb.canMove(makeCoordinate(1, 3), makeCoordinate(3, 5), board));
	}
	
	@Test
	void bishopMovesDiagonallyUpLeft_Empty()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(1, 6));
		assertTrue(wb.canMove(makeCoordinate(1, 6), makeCoordinate(4, 3), board));
	}
	
	@Test
	void bishopMovesDiagonallyDownRight_Empty()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(4, 3));
		wb.setHasMoved();
		assertTrue(wb.canMove(makeCoordinate(4, 3), makeCoordinate(1, 6), board));
	}
	
	@Test
	void bishopMovesDiagonallyDownLeft_Empty()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(3, 5));
		wb.setHasMoved();
		assertTrue(wb.canMove(makeCoordinate(3, 5), makeCoordinate(1, 3), board));
	}
	
	@Test
	void bishopAttemptsInvalidMove()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(1, 6));
		assertFalse(wb.canMove(makeCoordinate(1, 6), makeCoordinate(2, 6), board));
	}
	
	@Test
	void bishopAttempsToJumpOverPiece()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wb, makeCoordinate(1, 3));
		board.putPieceAt(wb, makeCoordinate(2,4));
		wb.setHasMoved();
		assertFalse(wb.canMove(makeCoordinate(1, 3), makeCoordinate(3, 5), board));
	}
	
	@Test
	void bishopCapturesEnemy()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wb, makeCoordinate(1, 6));
		board.putPieceAt(bp, makeCoordinate(4,3));
		bp.setHasMoved();
		assertTrue(wb.canMove(makeCoordinate(1, 6), makeCoordinate(4, 3), board));
	}
	
	@Test
	void bishopAttemptsToCaptureSameColor()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wb, makeCoordinate(1, 6));
		board.putPieceAt(wp, makeCoordinate(4,3));
		wp.setHasMoved();
		assertFalse(wb.canMove(makeCoordinate(1, 6), makeCoordinate(4, 3), board));
	}
	
////////////////////////Test Cases for Knight ////////////////////////////////////////////////
	
	@Test
	void knightMovesVerticalUpRight_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		assertTrue(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,3), board));
	}
	
	@Test
	void knightMovesVerticalUpLeft_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		assertTrue(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,1), board));
	}
	
	@Test
	void knightMovesVerticalDownRight_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(1,4), board));
	}
	
	@Test
	void knightMovesVerticalDownLeft_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(1,2), board));
	}
	
	@Test
	void knightMovesHorizontallyRightUp_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		assertTrue(wkn.canMove(makeCoordinate(1,2), makeCoordinate(2,4), board));
	}
	
	@Test
	void knightMovesHorizontallyRightDown_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,5), board));
	}
	
	@Test
	void knightMovesHorizontallyLeftUp_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(4,1), board));
	}
	
	@Test
	void knightMovesHorizontallyLeftDown_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,1), board));
	}
	
	@Test
	void knightAttemptsInvalidMove_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		assertFalse(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,4), board));
	}
	
	@Test
	void knightJumpsOverPiece()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		board.putPieceAt(bp, makeCoordinate(1,3));
		bp.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,1), board));
	}
	
	@Test
	void knightCapturesEnemy_VerticalLShapeMove()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		board.putPieceAt(bp, makeCoordinate(3,3));
		bp.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,3), board));
	}
	
	@Test
	void knightCapturesEnemy_HorizontalLShapeMove()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		board.putPieceAt(bp, makeCoordinate(2,1));
		bp.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,1), board));
	}
	
	@Test
	void knightAttemptsToCaptureSameColor_VerticalLShapeMove()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		board.putPieceAt(wp, makeCoordinate(3,3));
		wp.setHasMoved();
		assertFalse(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,3), board));
	}

	@Test
	void knightAttemptsToCaptureSameColor_HorizontalLShapeMove()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,1));
		assertFalse(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,1), board));
	}
	
////////////////////////Test Cases for Rook //////////////////////////////////////////////////
	
	@Test
	void rookMovesVerticallyUp_Empty()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertTrue(wr.canMove(makeCoordinate(1,1), makeCoordinate(5,1), board));
	}
	
	@Test
	void rookMovesVerticallyDown_Empty()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(5,1));
		wr.setHasMoved();
		assertTrue(wr.canMove(makeCoordinate(5,1), makeCoordinate(1,1), board));
	}
	
	@Test
	void rookMovesHorizontallyRight_Empty()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertTrue(wr.canMove(makeCoordinate(1,1), makeCoordinate(1,5), board));
	}
	
	@Test
	void rookMovesHorizontallyLeft_Empty()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1,8));
		assertTrue(wr.canMove(makeCoordinate(1,8), makeCoordinate(1,5), board));
	}
	
	@Test
	void rookAttemptsDiagonalMove()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1,8));
		assertFalse(wr.canMove(makeCoordinate(1,8), makeCoordinate(2,7), board));
	}
	
	@Test
	void rookAttemptsToJumpOverPiece()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wr, makeCoordinate(1,8));
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wr.canMove(makeCoordinate(1,8), makeCoordinate(1,2), board));
	}
	
	@Test
	void rookCaputuresEnemy_Vertically()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wr, makeCoordinate(1,1));
		board.putPieceAt(bp, makeCoordinate(5,1));
		bp.setHasMoved();
		assertTrue(wr.canMove(makeCoordinate(1,1), makeCoordinate(5,1), board));
	}
	
	@Test
	void rookCaputuresEnemy_Horizontally()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wr, makeCoordinate(1,1));
		board.putPieceAt(bp, makeCoordinate(1,5));
		bp.setHasMoved();
		assertTrue(wr.canMove(makeCoordinate(1,1), makeCoordinate(1,5), board));
	}
	
	@Test
	void rookAttemptsToCaputureSameColor_Vertically()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wr, makeCoordinate(1,1));
		board.putPieceAt(wp, makeCoordinate(5,1));
		wp.setHasMoved();
		assertFalse(wr.canMove(makeCoordinate(1,1), makeCoordinate(5,1), board));
	}
	
	@Test
	void rookAttemptsToCaputureSameColor_Horizontally()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wr, makeCoordinate(1,1));
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wr.canMove(makeCoordinate(1,1), makeCoordinate(1,5), board));
	}
////////////////////////Test Cases for Pawn //////////////////////////////////////////////////
	
	@Test
	void whitePawnMovesOneSpaceForward_Empty()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(3,5), board));
	}

	@Test
	void whitePawnMovesTwoSpaceForwardOnFirstMove_Empty()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(4,5), board));
	}
	
	@Test
	void whitePawnAttemptsTwoSpaceForwardOnNonFirstMove_Empty()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(3,5));
		wp.setHasMoved();
		assertFalse(wp.canMove(makeCoordinate(3,5), makeCoordinate(5,5), board));
	}
	
	@Test
	void whitePawnAttemptsInvalidMove_Empty()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(4,6), board));
	}
	
	@Test
	void whitePawnAttemptsCaptureEnemyPieceInFront()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(3,5));
		wp.setHasMoved();
		board.putPieceAt(bp, makeCoordinate(4,5));
		bp.setHasMoved();
		assertFalse(wp.canMove(makeCoordinate(3,5), makeCoordinate(4,5), board));
	}

	@Test
	void whitePawnCapturesEnemyPiece_DiagonallyUpRight()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(3,5));
		wp.setHasMoved();
		board.putPieceAt(bp, makeCoordinate(4,6));
		bp.setHasMoved();
		assertTrue(wp.canMove(makeCoordinate(3,5), makeCoordinate(4,6), board));
	}

	@Test
	void whitePawnCapturesEnemyPiece_DiagonallyUpLeft()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(3,5));
		wp.setHasMoved();
		board.putPieceAt(bp, makeCoordinate(4,4));
		bp.setHasMoved();
		assertTrue(wp.canMove(makeCoordinate(3,5), makeCoordinate(4,4), board));
	}
	
	@Test
	void whitePawnAttemptsToCapturesEnemyPiece_DiagonallyDownLeft()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(5,5));
		wp.setHasMoved();
		board.putPieceAt(bp, makeCoordinate(4,3));
		bp.setHasMoved();
		assertFalse(wp.canMove(makeCoordinate(5,5), makeCoordinate(4,3), board));
	}
	
	@Test
	void whitePawnAttemptsToCapturesSameColorPiece_DiagonallyUpLeft()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wp, makeCoordinate(3,4));
		wp.setHasMoved();
		board.putPieceAt(wq, makeCoordinate(4,3));
		wq.setHasMoved();
		assertFalse(wp.canMove(makeCoordinate(3,4), makeCoordinate(4,3), board));
	}

	@Test
	void blackPawnMovesOneSpaceForward_Empty()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(7,5));
		assertTrue(bp.canMove(makeCoordinate(7,5), makeCoordinate(6,5), board));
	}

	@Test
	void blackPawnMovesTwoSpaceForwardOnFirstMove_Empty()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(7,5));
		assertTrue(bp.canMove(makeCoordinate(7,5), makeCoordinate(5,5), board));
	}
	
	@Test
	void blackPawnAttemptsTwoSpaceForwardOnNonFirstMove_Empty()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(6,5));
		bp.setHasMoved();
		assertFalse(bp.canMove(makeCoordinate(6,5), makeCoordinate(4,5), board));
	}
	
	@Test
	void blackPawnAttemptsInvalidMove_Empty()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(7,5));
		assertFalse(bp.canMove(makeCoordinate(7,5), makeCoordinate(5,4), board));
	}
	
	@Test
	void blackPawnAttemptsCaptureEnemyPieceInFront()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(bp, makeCoordinate(6,5));
		bp.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(5,5));
		wp.setHasMoved();
		assertFalse(bp.canMove(makeCoordinate(6,5), makeCoordinate(5,5), board));
	}

	@Test
	void blackPawnCapturesEnemyPiece_DiagonallyUpRight()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(bp, makeCoordinate(6,5));
		bp.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(5,4));
		wp.setHasMoved();
		assertTrue(bp.canMove(makeCoordinate(6,5), makeCoordinate(5,4), board));
	}

	@Test
	void blackPawnCapturesEnemyPiece_DiagonallyUpLeft()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(bp, makeCoordinate(6,5));
		bp.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(5,6));
		wp.setHasMoved();
		assertTrue(bp.canMove(makeCoordinate(6,5), makeCoordinate(5,6), board));
	}
	
	@Test
	void blackPawnAttemptsToCapturesEnemyPiece_DiagonallyDownLeft()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(bp, makeCoordinate(5,5));
		bp.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(6,6));
		wp.setHasMoved();
		assertFalse(wp.canMove(makeCoordinate(5,5), makeCoordinate(6,6), board));
	}
	
	@Test
	void blackPawnAttemptsToCapturesSameColorPiece_DiagonallyUpLeft()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		ChessPiece bq = factory.makePiece(BLACKQUEEN);
		board.putPieceAt(bp, makeCoordinate(6,4));
		bp.setHasMoved();
		board.putPieceAt(bq, makeCoordinate(5,5));
		bq.setHasMoved();
		assertFalse(bp.canMove(makeCoordinate(6,4), makeCoordinate(5,5), board));
	}
	
}
