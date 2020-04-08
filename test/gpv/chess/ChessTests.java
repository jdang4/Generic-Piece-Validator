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
import static gpv.util.SquareInitializer.makeSquareInitializer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import gpv.util.*;

/**
 * This is test class that is mainly used to test my code 
 * and verify that it works correctly as I expect it to.
 * It runs on JUnit 5
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
	
	@Test
	void rowCoordinateOfDestinationIsNotInBoard()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(0,5), board));
	}
	
	@Test 
	void columnCoordinateOfDestinationIsNotInBoard()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(1,8));
		assertFalse(wb.canMove(makeCoordinate(1,8), makeCoordinate(1,9), board));
	}
	
	@Test
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
	
	@Test // 7
	void kingMovesUpOneSpace_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,5), board));
	}
	
	@Test // 8
	void kingMovesTwoSpaces()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(3,5), board));
	}
	
	@Test // 9
	void kingMovesVerticallyDown_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,5), board));
	}
	
	@Test // 10
	void kingMovesHorizontallyRight_Empty()
	{
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(bk, makeCoordinate(8,5));
		assertTrue(bk.canMove(makeCoordinate(8,5), makeCoordinate(8,6), board));
	}
	
	@Test // 11
	void kingMovesHorizontallyLeft_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,4), board));
	}
	
	@Test // 12
	void kingMovesDiagonallyUpRight_Empty()
	{
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(bk, makeCoordinate(8,5));
		assertTrue(bk.canMove(makeCoordinate(8,5), makeCoordinate(7,4), board));
	}
	
	@Test // 13
	void kingMovesDiagonallyUpLeft_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,4), board));
	}
	
	@Test // 14
	void kingMovesDiagonallyDownRight_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,6), board));
	}
	
	@Test // 15
	void kingMovesDiagonallyDownLeft_Empty()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,4), board));
	}
	
	@Test // 16
	void kingAttemptsInvalidMove_Empty()
	{
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(bk, makeCoordinate(7,5));
		bk.setHasMoved();
		assertFalse(bk.canMove(makeCoordinate(7,5), makeCoordinate(8,7), board));
	}
	
	@Test // 17
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
	
	@Test // 18
	void kingCapturesEnemy_VerticallyUp()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,5));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,5), board));
	}
	
	@Test // 19
	void kingCapturesEnemy_HorizontallyRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(1,6));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,6), board));
	}
	
	@Test // 20
	void kingCapturesEnemy_HorizontallyLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(1,4));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,4), board));
	}
	
	@Test // 21
	void kingCapturesEnemy_DiagonallyUpRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,6));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,6), board));
	}
	
	@Test // 22
	void kingCapturesEnemy_DiagonallyUpLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(bk, makeCoordinate(2,4));
		bk.setHasMoved();
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,4), board));
	}
	
	@Test // 23
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
	
	@Test // 24
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
	
	@Test // 25
	void kingAttemptsToCaptureSameColor_VerticallyDown()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(2,1));
		wk.setHasMoved();
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertFalse(wk.canMove(makeCoordinate(2,1), makeCoordinate(1,1), board));
	}
	
	@Test // 26
	void kingAttemptsToCaptureSameColor_VerticallyUp()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wk, makeCoordinate(1,1));
		board.putPieceAt(wr, makeCoordinate(2,1));
		wr.setHasMoved();
		assertFalse(wk.canMove(makeCoordinate(1,1), makeCoordinate(2,1), board));
	}
	
	@Test // 27
	void kingAttemptsToCaptureSameColor_HorizontallyRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wb, makeCoordinate(1,6));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,6), board));
	}
	
	@Test // 28
	void kingAttemptsToCaptureSameColor_HorizontallyLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(1,4), board));
	}
	
	@Test // 29
	void kingAttemptsToCaptureSameColor_DiagonallyUpRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wp, makeCoordinate(2,6));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,6), board));
	}
	
	@Test // 30
	void kingAttemptsToCaptureSameColor_DiagonallyUpLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wk, makeCoordinate(1,5));
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wk.canMove(makeCoordinate(1,5), makeCoordinate(2,4), board));
	}
	
	@Test // 31
	void kingAttemptsToCaptureSameColor_DiagonallyDownRight()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		board.putPieceAt(wb, makeCoordinate(1,6));
		assertFalse(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,6), board));
	}
	
	@Test // 32
	void kingAttemptsToCaptureSameColor_DiagonallyDownLeft()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wk, makeCoordinate(2,5));
		wk.setHasMoved();
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertFalse(wk.canMove(makeCoordinate(2,5), makeCoordinate(1,4), board));
	}
	
//////////////////////// Test Cases for Queen ////////////////////////////////////////////////
	
	@Test // 33
	void queenMovesUpOneSpace_Empty()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(2,4), board));
	}
	
	@Test // 34
	void queenMovesRightThreeSpaces_Empty()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,7), board));
	}
	
	@Test //35
	void queenMovesDiagonallyUpRight_Empty()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,6), board));
	}
	
	@Test //36
	void queenMakesInvalidMove_Empty()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,5), board));
	}
	
	@Test //37
	void queenAttemptsToJumpOverPiece_VerticallyUp()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,4), board));
	}
	
	@Test //38
	void queenAttemptsToJumpOverPiece_VerticallyDown()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(3,4));
		wq.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wq.canMove(makeCoordinate(3,4), makeCoordinate(1,4), board));
	}
	
	@Test //39
	void queenAttemptsToJumpOverPiece_HorizontalRight()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wb, makeCoordinate(1,6));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,7), board));
	}
	
	@Test //40
	void queenAttemptsToJumpOverPiece_HorizontalLeft()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wb, makeCoordinate(1,3));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,2), board));
	}
	
	@Test //41
	void queenAttemptsToJumpOverPiece_DiagonallyUpRight()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,6), board));
	}
	
	@Test //42
	void queenAttemptsToJumpOverPiece_DiagonallyUpLeft()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wp, makeCoordinate(2,3));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(3,2), board));
	}
	
	@Test //43
	void queenAttemptsToJumpOverPiece_DiagonallyDownRight()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(3,2));
		wq.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,3));
		assertFalse(wq.canMove(makeCoordinate(3,2), makeCoordinate(1,4), board));
	}
	
	@Test //44
	void queenAttemptsToJumpOverPiece_DiagonallyDownLeft()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(3,6));
		wq.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wq.canMove(makeCoordinate(3,6), makeCoordinate(1,4), board));
	}
	
	@Test //45
	void queenCapturesEnemy_Vertically()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(bp, makeCoordinate(2,4));
		bp.setHasMoved();
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(2,4), board));
	}
	
	@Test //46
	void queenCapturesEnemy_Horizontally()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(bk, makeCoordinate(1,3));
		bk.setHasMoved();
		assertTrue(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,3), board));
	}
	
	@Test //47
	void queenCapturesEnemy_Diagonally()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(wq, makeCoordinate(3,4));
		wq.setHasMoved();
		board.putPieceAt(bk, makeCoordinate(1,2));
		bk.setHasMoved();
		assertTrue(wq.canMove(makeCoordinate(3,4), makeCoordinate(1,2), board));
	}
	
	@Test //48
	void queenAttemptsToCaptureSameColor_Vertically()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(2,4), board));
	}
	
	@Test //49
	void queenAttemptsToCaptureSameColor_Horizontally()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wq, makeCoordinate(1,4));
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wq.canMove(makeCoordinate(1,4), makeCoordinate(1,5), board));
	}
	
	@Test //50
	void queenAttemptsToCaptureSameColor_Diagonally()
	{
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece wp = factory.makePiece(WHITEKING);
		board.putPieceAt(wq, makeCoordinate(4,7));
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wq.canMove(makeCoordinate(4,7), makeCoordinate(2,5), board));
	}

////////////////////////Test Cases for Bishop ////////////////////////////////////////////////
	
	@Test //51
	void bishopMovesDiagonallyUpRight_Empty()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(1, 3));
		assertTrue(wb.canMove(makeCoordinate(1, 3), makeCoordinate(3, 5), board));
	}
	
	@Test //52
	void bishopMovesDiagonallyUpLeft_Empty()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(1, 6));
		assertTrue(wb.canMove(makeCoordinate(1, 6), makeCoordinate(4, 3), board));
	}
	
	@Test //53
	void bishopMovesDiagonallyDownRight_Empty()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(4, 3));
		wb.setHasMoved();
		assertTrue(wb.canMove(makeCoordinate(4, 3), makeCoordinate(1, 6), board));
	}
	
	@Test //54
	void bishopMovesDiagonallyDownLeft_Empty()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(3, 5));
		wb.setHasMoved();
		assertTrue(wb.canMove(makeCoordinate(3, 5), makeCoordinate(1, 3), board));
	}
	
	@Test //55
	void bishopAttemptsInvalidMove()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(1, 6));
		assertFalse(wb.canMove(makeCoordinate(1, 6), makeCoordinate(2, 6), board));
	}
	
	@Test //56
	void bishopAttempsToJumpOverPiece()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wb, makeCoordinate(1, 3));
		board.putPieceAt(wp, makeCoordinate(2,4));
		wb.setHasMoved();
		assertFalse(wb.canMove(makeCoordinate(1, 3), makeCoordinate(3, 5), board));
	}
	
	@Test //57
	void bishopCapturesEnemy()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wb, makeCoordinate(1, 6));
		board.putPieceAt(bp, makeCoordinate(4,3));
		bp.setHasMoved();
		assertTrue(wb.canMove(makeCoordinate(1, 6), makeCoordinate(4, 3), board));
	}
	
	@Test //58
	void bishopAttemptsToCaptureSameColor()
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wb, makeCoordinate(1, 6));
		board.putPieceAt(wp, makeCoordinate(4,3));
		wp.setHasMoved();
		assertFalse(wb.canMove(makeCoordinate(1, 6), makeCoordinate(4, 3), board));
	}
	
////////////////////////Test Cases for Rook //////////////////////////////////////////////////
	
	@Test //59
	void rookMovesVerticallyUp_Empty()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertTrue(wr.canMove(makeCoordinate(1,1), makeCoordinate(5,1), board));
	}

	@Test //60
	void rookMovesVerticallyDown_Empty()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(5,1));
		wr.setHasMoved();
		assertTrue(wr.canMove(makeCoordinate(5,1), makeCoordinate(1,1), board));
	}

	@Test //61
	void rookMovesHorizontallyRight_Empty()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertTrue(wr.canMove(makeCoordinate(1,1), makeCoordinate(1,5), board));
	}

	@Test //62
	void rookMovesHorizontallyLeft_Empty()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1,8));
		assertTrue(wr.canMove(makeCoordinate(1,8), makeCoordinate(1,5), board));
	}

	@Test //63
	void rookAttemptsDiagonalMove()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(1,8));
		assertFalse(wr.canMove(makeCoordinate(1,8), makeCoordinate(2,7), board));
	}

	@Test //64
	void rookAttemptsToJumpOverPiece()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wr, makeCoordinate(1,8));
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wr.canMove(makeCoordinate(1,8), makeCoordinate(1,2), board));
	}

	@Test //65
	void rookCaputuresEnemy_Vertically()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wr, makeCoordinate(1,1));
		board.putPieceAt(bp, makeCoordinate(5,1));
		bp.setHasMoved();
		assertTrue(wr.canMove(makeCoordinate(1,1), makeCoordinate(5,1), board));
	}

	@Test //66
	void rookCaputuresEnemy_Horizontally()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wr, makeCoordinate(2,1));
		board.putPieceAt(bp, makeCoordinate(2,5));
		bp.setHasMoved();
		assertTrue(wr.canMove(makeCoordinate(2,1), makeCoordinate(2,5), board));
	}

	@Test //67
	void rookAttemptsToCaputureSameColor_Vertically()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wr, makeCoordinate(1,1));
		board.putPieceAt(wp, makeCoordinate(5,1));
		wp.setHasMoved();
		assertFalse(wr.canMove(makeCoordinate(1,1), makeCoordinate(5,1), board));
	}

	@Test //68
	void rookAttemptsToCaputureSameColor_Horizontally()
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wr, makeCoordinate(1,1));
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertFalse(wr.canMove(makeCoordinate(1,1), makeCoordinate(1,5), board));
	}
	
////////////////////////Test Cases for Knight ////////////////////////////////////////////////
	
	@Test //69
	void knightMovesVerticalUpRight_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		assertTrue(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,3), board));
	}
	
	@Test //70
	void knightMovesVerticalUpLeft_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		assertTrue(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,1), board));
	}
	
	@Test //71
	void knightMovesVerticalDownRight_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(1,4), board));
	}
	
	@Test //72
	void knightMovesVerticalDownLeft_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(1,2), board));
	}
	
	@Test //73
	void knightMovesHorizontallyRightUp_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		assertTrue(wkn.canMove(makeCoordinate(1,2), makeCoordinate(2,4), board));
	}
	
	@Test //74
	void knightMovesHorizontallyRightDown_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,5), board));
	}
	
	@Test //75
	void knightMovesHorizontallyLeftUp_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(4,1), board));
	}
	
	@Test //76
	void knightMovesHorizontallyLeftDown_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,1), board));
	}
	
	@Test //77
	void knightAttemptsInvalidMove_Empty()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		assertFalse(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,4), board));
	}
	
	@Test //78
	void knightJumpsOverPiece()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece bb = factory.makePiece(BLACKBISHOP);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		board.putPieceAt(bb, makeCoordinate(1,3));
		bb.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,1), board));
	}
	
	@Test //79
	void knightCapturesEnemy_VerticalLShapeMove()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		board.putPieceAt(bp, makeCoordinate(3,3));
		bp.setHasMoved();
		assertTrue(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,3), board));
	}
	
	@Test //80
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
	
	@Test //81
	void knightAttemptsToCaptureSameColor_VerticalLShapeMove()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wkn, makeCoordinate(1,2));
		board.putPieceAt(wp, makeCoordinate(3,3));
		wp.setHasMoved();
		assertFalse(wkn.canMove(makeCoordinate(1,2), makeCoordinate(3,3), board));
	}

	@Test //82
	void knightAttemptsToCaptureSameColor_HorizontalLShapeMove()
	{
		ChessPiece wkn = factory.makePiece(WHITEKNIGHT);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wkn, makeCoordinate(3,3));
		wkn.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,1));
		assertFalse(wkn.canMove(makeCoordinate(3,3), makeCoordinate(2,1), board));
	}
	
////////////////////////Test Cases for Pawn //////////////////////////////////////////////////
	
	@Test //83
	void whitePawnMovesOneSpaceForward_Empty()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(3,5), board));
	}
	
	@Test //84
	void whitePawnAttemptsInvalidMove_Empty()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(4,6), board));
	}

	@Test //85
	void whitePawnMovesTwoSpaceForwardOnFirstMove_Empty()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(4,5), board));
	}
	
	@Test //86
	void whitePawnAttemptsTwoSpaceForwardOnNonFirstMove_Empty()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(3,5));
		wp.setHasMoved();
		assertFalse(wp.canMove(makeCoordinate(3,5), makeCoordinate(5,5), board));
	}
	
	@Test //87
	void blackPawnMovesOneSpaceForward_Empty()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(7,5));
		assertTrue(bp.canMove(makeCoordinate(7,5), makeCoordinate(6,5), board));
	}
	
	@Test //88
	void blackPawnAttemptsInvalidMove_Empty()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(7,5));
		assertFalse(bp.canMove(makeCoordinate(7,5), makeCoordinate(5,4), board));
	}
	
	@Test //89
	void blackPawnMovesTwoSpaceForwardOnFirstMove_Empty()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(7,5));
		assertTrue(bp.canMove(makeCoordinate(7,5), makeCoordinate(5,5), board));
	}
	
	@Test //90
	void blackPawnAttemptsTwoSpaceForwardOnNonFirstMove_Empty()
	{
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(bp, makeCoordinate(6,5));
		bp.setHasMoved();
		assertFalse(bp.canMove(makeCoordinate(6,5), makeCoordinate(4,5), board));
	}
	
	@Test //91
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

	@Test //92
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

	@Test //93
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
	
	@Test //94
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
	
	@Test //95
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

	@Test //96
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

	@Test //97
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
	
	@Test //98
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
	
	@Test //99
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
	
	@Test //100
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

////////////////////////Test Cases for Castling //////////////////////////////////////////////////
	
	/**
     * When castling, the initializers are the king first, rook second,
     * and others follow, if they are in play.
     */
    @ParameterizedTest //101 - 111
    @MethodSource("castlingTestProvider")
    void castlingTest(List<SquareInitializer> initializers, boolean kingMoved,
        boolean rookMoved, Coordinate to, boolean expected)
    {
        board.reset(initializers);
        Coordinate kingCoord = initializers.get(0).getSquare();
        Coordinate rookCoord = initializers.get(1).getSquare();
        ChessPiece king = (ChessPiece)board.getPieceAt(kingCoord);
        if (kingMoved) 
        {
            ((ChessPiece)board.getPieceAt(kingCoord)).setHasMoved();
        }
        if (rookMoved) 
        {
            ((ChessPiece)board.getPieceAt(rookCoord)).setHasMoved();
        }
        assertEquals(expected, king.canMove(kingCoord, to, board));
    }
    
    // the arguments for the castlingTest
	static Stream<Arguments> castlingTestProvider()
    {
        return Stream.of(
        		
        		// making a right castling for a white king
        		Arguments.of(
        				makeInitializers(WHITEKING, 1, 5, WHITEROOK, 1, 8),
        				false, false, makeCoordinate(1, 7), true),
        		
        		// making a left castling for a white king
        		Arguments.of(
        				makeInitializers(WHITEKING, 1, 5, WHITEROOK, 1, 1),
        				false, false, makeCoordinate(1, 3), true),
        		
        		// making a right castling for a black king
        		Arguments.of(
        				makeInitializers(BLACKKING, 8, 5, BLACKROOK, 8, 8),
        				false, false, makeCoordinate(8, 7), true),
        		
        		// making a left castling for a black king
        		Arguments.of(
        				makeInitializers(BLACKKING, 8, 5, BLACKROOK, 8, 1),
        				false, false, makeCoordinate(8, 3), true),
        		
        		// king moves more than 2 columns
        		Arguments.of(
        				makeInitializers(BLACKKING, 8, 5, BLACKROOK, 8, 1),
        				false, false, makeCoordinate(8, 2), false),
        		
        		// king tries to move 2 spaces forward
        		Arguments.of(
        				makeInitializers(BLACKKING, 8, 5, BLACKROOK, 8, 1),
        				false, false, makeCoordinate(6, 3), false),
        		
        		// king has moved
        		Arguments.of(
        				makeInitializers(WHITEKING, 1, 5, WHITEROOK, 1, 8),
        				true, false, makeCoordinate(1, 7), false),
        		
        		// rook has moved
        		Arguments.of(
        				makeInitializers(WHITEKING, 1, 5, WHITEROOK, 1, 8),
        				false, true, makeCoordinate(1, 7), false),
        		
        		// king makes just a standard king move, not a castling move
        		Arguments.of(
        				makeInitializers(WHITEKING, 1, 5, WHITEROOK, 1, 8),
        				false, true, makeCoordinate(1, 6), true),
        		
        		// an enemy piece is in the way
        		Arguments.of(
        				makeInitializers(WHITEKING, 1, 5, WHITEROOK, 1, 1,
        						BLACKKNIGHT, 1, 2),
        				false, false, makeCoordinate(1, 3), false),
        		
        		// a same color piece is in the way
        		Arguments.of(
        				makeInitializers(WHITEKING, 1, 5, WHITEROOK, 1, 8,
        						WHITEBISHOP, 1, 6),
        				false, false, makeCoordinate(1, 7), false)
        );
    }

////////////////////////Test Cases for Black Color Pieces //////////////////////////////////////////////////
	
	/**
	 * This type of test gets its arguments via dependency injection from moveTestProvider();
	 * @param initializers set up the board
	 * @param from move from this coordinate
	 * @param to move to this coordinate
	 * @param expected the expected result calling canMove() on the piece at "from"
	 */
    @ParameterizedTest // 112 - 118
    @MethodSource("moveTestProvider")
    void testingBlackColorPieces(List<SquareInitializer> initializers, Coordinate from,
            Coordinate to, boolean expected)
    {
        board.reset(initializers);
        ChessPiece p = (ChessPiece)board.getPieceAt(from);
        assertNotNull(p);
        assertEquals(expected, p.canMove(from, to, board));
    }

	// provides the test cases to test for the black color pieces
    static Stream<Arguments> moveTestProvider()
    {
    	return Stream.of(
    			Arguments.of(
    					makeInitializers(BLACKKING, 8, 5, BLACKPAWN, 7, 5),
    					makeCoordinate(8, 5), makeCoordinate(7, 5), false),
    			Arguments.of(
    					makeInitializers(BLACKKING, 8, 5),
    					makeCoordinate(8, 5), makeCoordinate(7, 6), true),
    			Arguments.of(
    					makeInitializers(BLACKQUEEN, 8, 4, BLACKPAWN, 7, 3),
    					makeCoordinate(8, 4), makeCoordinate(5, 1), false),
    			Arguments.of(
    					makeInitializers(BLACKBISHOP, 8, 3),
    					makeCoordinate(8, 3), makeCoordinate(3, 8), true),
    			Arguments.of(
    					makeInitializers(BLACKBISHOP, 8, 3, BLACKPAWN, 6, 5), 
    					makeCoordinate(8, 3), makeCoordinate(3, 8), false),
    			Arguments.of(
    					makeInitializers(BLACKKNIGHT, 8, 2, BLACKPAWN, 7, 2), 
    					makeCoordinate(8, 2), makeCoordinate(5, 3), false),
    			Arguments.of(
    					makeInitializers(BLACKKNIGHT, 8, 2, BLACKPAWN, 7, 2), 
    					makeCoordinate(8, 2), makeCoordinate(6, 3), true)
    			);
    }
	
      
    private static List<SquareInitializer> makeInitializers(Object... params)
    {
    	List<SquareInitializer> initializers = new ArrayList<SquareInitializer>();
    	int ix = 0;
    	while (ix < params.length) 
    	{
    		ChessPiece p = factory.makePiece((ChessPieceDescriptor) params[ix++]);
    		Coordinate c = makeCoordinate((int)params[ix++], (int)params[ix++]);
    		initializers.add((SquareInitializer)makeSquareInitializer(p, c));
    	}

    	return initializers;
    }
	
}
