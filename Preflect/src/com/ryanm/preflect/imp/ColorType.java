
package com.ryanm.preflect.imp;

import java.lang.reflect.Type;

import android.content.Context;
import android.graphics.Color;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.Log;

import com.ryanm.preflect.ParseException;
import com.ryanm.preflect.Preflect;
import com.ryanm.preflect.VariableType;
import com.ryanm.preflect.annote.WidgetHint;

/**
 * {@link VariableType} for integer-packed {@link Color}s. Use a
 * {@link WidgetHint} to get this {@link VariableType} instead of the
 * standard {@link IntType}
 * 
 * @author ryanm
 */
public class ColorType extends CSVPrefType<Color>
{
	/***/
	public ColorType()
	{
		super( Color.class, false, false, "r", "g", "b", "a" );
	}

	@Override
	public Preference buildPreference( Context context, Class type, String value )
	{
		EditTextPreference pref =
				( EditTextPreference ) super.buildPreference( context, type, value );

		// the values need unpacked on the way in...
		int v = Integer.parseInt( value );
		String colourString =
				Color.red( v ) + ", " + Color.green( v ) + ", " + Color.blue( v ) + ", "
						+ Color.alpha( v );

		pref.setText( colourString );

		return pref;
	}

	@Override
	protected String formatInput( Object input )
	{
		float[] fv = parse( ( String ) input );

		// and packed on the way out
		// watch out for the rgba -> argb ordering
		return String.valueOf( Color.argb( ( int ) fv[ 3 ], ( int ) fv[ 0 ],
				( int ) fv[ 1 ], ( int ) fv[ 2 ] ) );
	}

	/**
	 * This will never be used - the {@link Type} of the field is used
	 * to determine decoding, while {@link WidgetHint} type is used for
	 * preference construction
	 */
	@Override
	public String encode( Color value )
	{
		Log.e( Preflect.LOG_TAG,
				"ColorVarType.encode() - This should never be called!" );
		return "";
	}

	/**
	 * This will never be used - the {@link Type} of the field is used
	 * to determine encoding, while {@link WidgetHint} type is used for
	 * preference construction
	 */
	@Override
	public Color decode( String encoded, Class runtimeType ) throws ParseException
	{
		Log.e( Preflect.LOG_TAG,
				"ColorVarType.decode() - This should never be called!" );
		return null;
	}

}
