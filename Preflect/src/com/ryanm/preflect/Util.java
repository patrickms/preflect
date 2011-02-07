
package com.ryanm.preflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

import com.ryanm.preflect.annote.Category;
import com.ryanm.preflect.annote.Order;
import com.ryanm.preflect.annote.Summary;
import com.ryanm.preflect.annote.Variable;
import com.ryanm.preflect.annote.WidgetHint;

/**
 * Util methods
 * 
 * @author ryanm
 */
class Util
{
	/**
	 * JSON key for variable name
	 */
	static final String NAME = "name";

	/**
	 * JSON key for variable type
	 */
	static final String TYPE = "type";

	/**
	 * JSON key for variable summary
	 */
	static final String DESC = "desc";

	/**
	 * JSON key for variable category
	 */
	static final String CAT = "cat";

	/**
	 * JSON key for variable order
	 */
	static final String ORDER = "order";

	/**
	 * JSON key for variable order
	 */
	static final String VALUE = "value";

	/**
	 * JSON key for a boolean that indicates a variable is read-only
	 */
	static final String READ_ONLY = "readonly";

	/**
	 * Tag for the configuration string in the {@link Intent}
	 */
	static final String CONF_TAG = "conf";

	/**
	 * Tag for the name of the activity that started the
	 * {@link PreflectActivity} in the {@link Intent}
	 */
	static final String RETURNTO_TAG = "returnto";

	private Util()
	{
	}

	private static final Map<String, Class> primitiveClassMap =
			new HashMap<String, Class>();
	static
	{
		Class[] prims =
				new Class[] { boolean.class, byte.class, short.class, char.class, int.class,
						float.class, long.class, double.class, void.class };
		for( Class c : prims )
		{
			primitiveClassMap.put( c.getName(), c );
		}
	}

	/**
	 * Basically {@link Class#forName(String)}, but handles primitive
	 * types like int.class
	 * 
	 * @param name
	 * @return the class for that name, or <code>null</code> if not
	 *         found
	 */
	static Class getType( String name )
	{
		Class c = primitiveClassMap.get( name );
		if( c == null )
		{
			try
			{
				c = Class.forName( name );
			}
			catch( ClassNotFoundException e )
			{
				Log.e( Preflect.LOG_TAG, "CNFE for \"" + name + "\"", e );
			}
		}

		return c;
	}

	/**
	 * @param f
	 * @return {@link Category} name, or <code>null</code> if there is
	 *         not one present
	 */
	static String getCategory( AccessibleObject f )
	{
		Category c = f.getAnnotation( Category.class );
		if( c != null )
		{
			return c.value();
		}
		return null;
	}

	/**
	 * @param f
	 * @return {@link Variable} description, or <code>null</code> if
	 *         there is not one present
	 */
	static String getDescription( AccessibleObject f )
	{
		Summary d = f.getAnnotation( Summary.class );
		if( d != null )
		{
			return d.value();
		}
		return null;
	}

	/**
	 * @param o
	 * @return {@link Variable} description, or <code>null</code> if
	 *         there is not one present
	 */
	static String getDescription( Object o )
	{
		Summary d = o.getClass().getAnnotation( Summary.class );
		if( d != null )
		{
			return d.value();
		}
		return null;
	}

	/**
	 * The configuration name of a field
	 * 
	 * @param f
	 * @return The name of the field, or <code>null</code> if the field
	 *         is not {@link Variable}
	 */
	static String getName( Field f )
	{
		String name = null;

		Variable v = f.getAnnotation( Variable.class );

		if( v != null && Preflect.errorOnNonPublicVariables
				&& !Modifier.isPublic( f.getModifiers() ) )
		{
			throw new StructuralError( "Non-public variable encountered : "
					+ f.getDeclaringClass() + "." + f.getName()
					+ "\n Set Preflect.errorOnNonPublicVariables "
					+ "to false to suppress this error" );
		}

		if( v != null && Modifier.isPublic( f.getModifiers() ) )
		{
			name = v.value();

			if( name.length() == 0 )
			{
				// check if type is configurable
				Variable tc = f.getType().getAnnotation( Variable.class );
				if( tc != null && tc.value().length() != 0 )
				{
					name = tc.value();
				}
				else
				{
					name = f.getName();
				}
			}
		}

		return name;
	}

	/**
	 * The configuration name of an encapsulated variable
	 * 
	 * @param m
	 * @return The name of the variable, or <code>null</code> if the
	 *         method is not {@link Variable}
	 */
	static String getName( Method m )
	{
		Variable v = m.getAnnotation( Variable.class );
		String name = null;

		if( v != null )
		{
			name = v.value();

			if( name.length() == 0 )
			{
				name = m.getName();
			}
		}

		return name;
	}

	/**
	 * The configuration name of an object
	 * 
	 * @param o
	 * @return The name, or <code>null</code> if o is not
	 *         {@link Variable}
	 */
	static String getName( Object o )
	{
		Variable c = o.getClass().getAnnotation( Variable.class );
		String name = null;

		if( c != null )
		{
			name = c.value();

			if( name.length() == 0 )
			{
				name = o.getClass().getSimpleName();
			}
		}

		return name;
	}

	/**
	 * Adds the description and ranges to the json
	 * 
	 * @param conf
	 * @param ao
	 * @throws JSONException
	 */
	static void getOptional( JSONObject conf, AccessibleObject ao ) throws JSONException
	{
		if( conf != null )
		{
			conf.putOpt( DESC, getDescription( ao ) );
			conf.putOpt( CAT, getCategory( ao ) );

			Order o = ao.getAnnotation( Order.class );
			if( o != null )
			{
				conf.put( ORDER, o.value() );
			}

			WidgetHint th = ao.getAnnotation( WidgetHint.class );
			if( th != null )
			{
				conf.put( TYPE, th.value().getName() );
			}
		}
	}
}
