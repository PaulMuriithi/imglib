package net.imglib2.algorithm.region.localneighborhood;

import net.imglib2.meta.Axes;
import net.imglib2.meta.Metadata;
import net.imglib2.util.Util;

/**
 * Util class made of static methods, meant to simplify the writing of special cursors.
 * @author Jean-Yves Tinevez <jeanyves.tinevez@gmail.com> Sep 9, 2010
 */
public class Utils {
	
	
	/**
	 * Return the xyz calibration stored in an {@link Metadata} in a 3-elements
	 * double array. Calibration is ordered as X, Y, Z. If one axis is not found,
	 * then the calibration for this axis takes the value of 1.
	 */
	public static final double[] getSpatialCalibration(final Metadata img) {
		final double[] calibration = Util.getArrayFromValue(1d, 3);
		for (int d = 0; d < img.numDimensions(); d++) {
			if (img.axis(d).equals(Axes.X)) {
				calibration[0] = img.calibration(d);
			} else if (img.axis(d).equals(Axes.Y)) {
				calibration[1] = img.calibration(d);
			} else if (img.axis(d).equals(Axes.Z)) {
				calibration[2] = img.calibration(d);
			}
		}
		return calibration;
	}

	
	/** 
	 * Store the half-widths of a X line to scan to fill an ellipse of given axis lengths.
	 * The parameter <code>a</code> is the axis half-length in the X direction, and <code>b</code>
	 * is the axis half-length in the Y direction. 
	 * <p>
	 * The half-widths will be stored in the array <code>lineBounds</code>, which must be of size equal
	 * to at least <code>b+1</code>.
	 * <p>
	 * This is an implementation of the McIlroy's algorithm, adapted freely from 
	 * {@link http://enchantia.com/software/graphapp/doc/tech/ellipses.html}.
	 * 
	 * @param a  half-length of the ellipse in the X direction
	 * @param b  half-length of the ellipse in the Y direction
	 * @param lineBounds  will store the half-length of the ellipse lines in the X direction
	 */
	public static final void getXYEllipseBounds(int a, int b, int[] lineBounds) {
		/* e(x,y) = b^2*x^2 + a^2*y^2 - a^2*b^2 */
		int x = 0, y = b;
		int width = 0;
		long a2 = (long)a*a, b2 = (long)b*b;
		long crit1 = -(a2/4 + a%2 + b2);
		long crit2 = -(b2/4 + b%2 + a2);
		long crit3 = -(b2/4 + b%2);
		long t = -a2*y; /* e(x+1/2,y-1/2) - (a^2+b^2)/4 */
		long dxt = 2*b2*x, dyt = -2*a2*y;
		long d2xt = 2*b2, d2yt = 2*a2;

		while (y>=0 && x<=a) {
			if (t + b2*x <= crit1 ||     /* e(x+1,y-1/2) <= 0 */
					t + a2*y <= crit3) {     /* e(x+1/2,y) <= 0 */
				x++; dxt += d2xt; t += dxt;// incx();
				width += 1;
			}
			else if (t - a2*y > crit2) { /* e(x+1/2,y-1) > 0 */
				lineBounds[y] = width; //row(y, width);
				//					if (y!=0)
				//						row(xc-x, yc+y, width);
				y--; dyt += d2yt; t += dyt; // incy();
			}
			else {
				lineBounds[y] = width; // row(y, width);
				//					if (y!=0)
				//						row(xc-x, yc+y, width);
				x++; dxt += d2xt; t += dxt; //incx();
				y--; dyt += d2yt; t += dyt; //incy();
				width += 1;
			}
		}
		if (b == 0)
			lineBounds[0] = a; //row(0, 2*a+1);
	}
	
	/**
	 * Midpoint circle algorithm: store the bounds of a circle in the given array. From
	 * {@link http://en.wikipedia.org/wiki/Midpoint_circle_algorithm}
	 * @param radius  the radius of the circle
	 * @param lineBounds  the array to store bounds in
	 */
	public static final void getXYCircleBounds(int radius, int[] lineBounds)	{
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;
		int x = 0;
		int y = radius;

		lineBounds[0] = radius;

		while(x < y) 		  {
			// ddF_x == 2 * x + 1;
			// ddF_y == -2 * y;
			// f == x*x + y*y - radius*radius + 2*x - y + 1;
			if(f >= 0)  {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;  
			lineBounds[y] = x;
			lineBounds[x] = y;
		}
	}

	/**
	 * Return a copy of the double array where every value has been casted to float
	 */
	public static float[] copyDoubleArrayToFloat(final double[] array) {
		final float[] casted = new float[array.length];
		for (int i = 0; i < casted.length; i++) 
			casted[i] = (float) array[i];
		return casted;
	}

}
