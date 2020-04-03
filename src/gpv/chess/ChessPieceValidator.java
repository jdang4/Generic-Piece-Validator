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

import gpv.util.*;

/**
 * Description
 * @version Apr 3, 2020
 */
@FunctionalInterface
public interface ChessPieceValidator
{
	boolean check(Coordinate from, Coordinate to, Board b);
}
