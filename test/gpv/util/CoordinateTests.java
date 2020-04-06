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

package gpv.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Description
 * @version Apr 6, 2020
 */
class CoordinateTests
{

	@Test
	void getTheRowDistanceOfDifferentRows()
	{
		Coordinate source = Coordinate.makeCoordinate(1, 6);
		Coordinate destination = Coordinate.makeCoordinate(4, 6);
		
		assertEquals(3, source.getRowDistance(destination));
	}
	
	@Test
	void getTheRowDistanceOfSameRow()
	{
		Coordinate source = Coordinate.makeCoordinate(1, 6);
		Coordinate destination = Coordinate.makeCoordinate(1, 6);
		
		assertEquals(0, source.getRowDistance(destination));
	}
	
	@Test
	void getTheColumnDistanceOfDifferentColumns()
	{
		Coordinate source = Coordinate.makeCoordinate(1, 4);
		Coordinate destination = Coordinate.makeCoordinate(1, 6);
		
		assertEquals(2, source.getColumnDistance(destination));
	}
	
	@Test
	void getTheColumnDistanceOfSameColumns()
	{
		Coordinate source = Coordinate.makeCoordinate(4, 6);
		Coordinate destination = Coordinate.makeCoordinate(1, 6);
		
		assertEquals(0, source.getColumnDistance(destination));
	}
	
	@Test
	void comparingSameCoordinates()
	{
		Coordinate coord1 = Coordinate.makeCoordinate(4, 6);
		Coordinate coord2 = Coordinate.makeCoordinate(4, 6);
		
		assertTrue(coord1.equals(coord2));
	}
	
	@Test
	void comparingDifferentCoordinates()
	{
		Coordinate coord1 = Coordinate.makeCoordinate(4, 6);
		Coordinate coord2 = Coordinate.makeCoordinate(5, 4);
		
		assertFalse(coord1.equals(coord2));
	}

}
