
package com.ryanm.preflect.imp;

import android.graphics.Point;

import com.ryanm.preflect.ParseException;

/**
 * Demonstrates extending {@link CSVPrefType}
 * 
 * @author ryanm
 */
public class PointType extends CSVPrefType<Point>
{
	/***/
	public PointType()
	{
		super( Point.class, true, false, "x", "y" );
	}

	@Override
	public String encode( Point value )
	{
		return value.x + ", " + value.y;
	}

	@Override
	public Point decode( String encoded, Class runtimeType ) throws ParseException
	{
		float[] values = parse( encoded );

		if( values.length < 2 )
		{
			throw new ParseException( "Could not parse 2 values from \"" + encoded + "\"" );
		}

		return new Point( ( int ) values[ 0 ], ( int ) values[ 1 ] );
	}

}
