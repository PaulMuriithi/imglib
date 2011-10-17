/*

Copyright (c) 2011, Barry DeZonia.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
  * Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
  * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
  * Neither the name of the Fiji project developers nor the
    names of its contributors may be used to endorse or promote products
    derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package net.imglib2.ops.function.real;

import net.imglib2.ops.Function;
import net.imglib2.ops.Neighborhood;
import net.imglib2.ops.Real;
import net.imglib2.ops.RealOutput;
import net.imglib2.ops.RegionIndexIterator;


/**
 * 
 * @author Barry DeZonia
 *
 */
public class RealProductFunction extends RealOutput implements Function<long[],Real> {

	private final Function<long[],Real> otherFunc;
	private final Real variable;
	private RegionIndexIterator iter;
	
	public RealProductFunction(Function<long[],Real> otherFunc) {
		this.otherFunc = otherFunc;
		this.variable = createOutput();
		this.iter = null;
	}
	
	@Override
	public void evaluate(Neighborhood<long[]> region, long[] point, Real output) {
		if (iter == null)
			iter = new RegionIndexIterator(region);
		else
			iter.relocate(region.getKeyPoint());
		iter.reset();
		double product = 1;
		while (iter.hasNext()) {
			iter.fwd();
			otherFunc.evaluate(region, iter.getPosition(), variable);
			product *= variable.getReal();
		}
		output.setReal(product);
	}

	@Override
	public RealProductFunction duplicate() {
		return new RealProductFunction(otherFunc.duplicate());
	}
}