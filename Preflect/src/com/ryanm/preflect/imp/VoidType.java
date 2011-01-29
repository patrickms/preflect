
package com.ryanm.preflect.imp;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.Preference;

import com.ryanm.preflect.ParseException;
import com.ryanm.preflect.VariableType;

/**
 * @author ryanm
 */
public class VoidType extends VariableType<Void>
{
	/***/
	public VoidType()
	{
		super( void.class );
	}

	@Override
	public String encode( Void value )
	{
		return "void";
	}

	@Override
	public Void decode( String encoded, Class runtimeType ) throws ParseException
	{
		return null;
	}

	@Override
	protected Preference buildPreference( Context context, Class type, String value )
	{
		CheckBoxPreference pref = new CheckBoxPreference( context );
		pref.setChecked( Boolean.parseBoolean( value ) );
		return pref;
	}
}
