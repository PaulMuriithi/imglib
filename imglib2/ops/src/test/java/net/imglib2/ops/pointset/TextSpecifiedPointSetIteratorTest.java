/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package net.imglib2.ops.pointset;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * @author Barry DeZonia
 */
public class TextSpecifiedPointSetIteratorTest {

	@Test
	public void test() {
		PointSet ps = new TextSpecifiedPointSet("x=[1..4],y=[1..4], x > 2, y <= 2");
		PointSetIterator iter = ps.iterator();

		// regular sequence
		assertTrue(iter.hasNext());
		assertArrayEquals(new long[] { 3, 1 }, iter.next());
		assertTrue(iter.hasNext());
		assertArrayEquals(new long[] { 4, 1 }, iter.next());
		assertTrue(iter.hasNext());
		assertArrayEquals(new long[] { 3, 2 }, iter.next());
		assertTrue(iter.hasNext());
		assertArrayEquals(new long[] { 4, 2 }, iter.next());
		assertFalse(iter.hasNext());

		// another regular sequence
		iter.reset();
		assertTrue(iter.hasNext());
		iter.fwd();
		assertArrayEquals(new long[] { 3, 1 }, iter.get());
		assertTrue(iter.hasNext());
		iter.fwd();
		assertArrayEquals(new long[] { 4, 1 }, iter.get());
		assertTrue(iter.hasNext());
		iter.fwd();
		assertArrayEquals(new long[] { 3, 2 }, iter.get());
		assertTrue(iter.hasNext());
		iter.fwd();
		assertArrayEquals(new long[] { 4, 2 }, iter.get());
		assertFalse(iter.hasNext());

		// irregular sequences
		iter.reset();
		iter.next();
		assertArrayEquals(new long[] { 3, 1 }, iter.get());
		iter.fwd();
		assertArrayEquals(new long[] { 4, 1 }, iter.get());
		iter.next();
		assertArrayEquals(new long[] { 3, 2 }, iter.get());
		iter.fwd();
		assertArrayEquals(new long[] { 4, 2 }, iter.get());
		assertFalse(iter.hasNext());
		assertArrayEquals(new long[] { 4, 2 }, iter.get());

	}

}
