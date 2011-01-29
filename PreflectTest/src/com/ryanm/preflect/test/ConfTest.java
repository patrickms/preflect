
package com.ryanm.preflect.test;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import com.ryanm.preflect.Preflect;
import com.ryanm.preflect.annote.Category;
import com.ryanm.preflect.annote.DirtyFlag;
import com.ryanm.preflect.annote.Order;
import com.ryanm.preflect.annote.Summary;
import com.ryanm.preflect.annote.Variable;
import com.ryanm.preflect.annote.WidgetHint;

/**
 * @author ryanm
 */
@Variable( "Testy" )
@Summary( "Icy shriveled testsicles" )
public class ConfTest
{
	// @Variable( "Don't do this" )
	// @Summary(
	// "This will cause an infinite loop, tree structure only please" )
	// public ConfTest objectGraphLoop = this;

	/***/
	@Variable( "An enumeration" )
	@Summary( "Mutually exclusive" )
	public TestEnum enumeration = TestEnum.Foo;

	/***/
	@Variable( "A boolean" )
	@Summary( "Only true or false, there is no \"meh\"" )
	public boolean aBoolean = true;

	/***/
	@Variable( "A float" )
	@Summary( "It's floaty light" )
	@Category( "Numbers" )
	public float aFloat = 1;

	/***/
	@Variable( "A colour" )
	@Summary( "Stored as an int, manipulated as an rgba quad" )
	@WidgetHint( Color.class )
	public int color = Color.argb( 128, 32, 256, 64 );

	/***/
	@Variable( "A point" )
	@Summary( "A good point, well made." )
	public Point point = new Point( 10, 15 );

	/***/
	@Variable( "A string" )
	@Summary( "\\n works in summaries\nbut you only get two lines" )
	public String aString = "hello";

	private int encap = 4;

	/***/
	private SubTest sub = new SubTest();

	/**
	 * @return the sub
	 */
	@Variable( "Encapsulated sub-configurable" )
	public SubTest getSub()
	{
		return sub;
	}

	/**
	 * @param sub
	 */
	@Variable( "Encapsulated sub-configurable" )
	public void setSub( SubTest sub )
	{
		this.sub = sub;
	}

	/***/
	@Variable( )
	@Summary( "I've overridden the summary!" )
	public SubTest namedSub = new SubTest();

	/**
	 * @return a number
	 */
	@Variable( "An encapsulated integer" )
	@Summary( "Whole numbers only" )
	@Category( "Numbers" )
	@Order( 0 )
	public int getEncap()
	{
		return encap;
	}

	/**
	 * @param i
	 */
	@Variable( "An encapsulated integer" )
	public void setEncap( int i )
	{
		encap = i;
	}

	/***/
	@Variable( "An action" )
	@Summary( "Tick the box and the method will be called on application" )
	public void action()
	{
		Log.i( Preflect.LOG_TAG, "Action!" );
	}

	/**
	 * The {@link DirtyFlag} annotation can be applied to boolean
	 * fields or void-return/no-arg methods, and will be set to
	 * true/invoked when a variable value is changed
	 */
	@DirtyFlag
	public void setDirty()
	{
		Log.i( Preflect.LOG_TAG, "Top-level variables are dirty!" );
	}

	/**
	 * You can also use it to watch for changes to variables further
	 * down in the tree
	 */
	@DirtyFlag( watchTree = true )
	public void setTreeDirty()
	{
		Log.i( Preflect.LOG_TAG, "Tree variables are dirty!" );
	}

	/**
	 * @author ryanm
	 */
	@Variable( "Subconf" )
	@Summary( "Standard description" )
	public static class SubTest
	{
		/***/
		@Variable
		public int subnum = 5;

		/***/
		@Variable
		public String subString = "boo!";

		@Override
		public String toString()
		{
			return subnum + " " + subString;
		}
	}

	/***/
	public enum TestEnum
	{
		/***/
		Foo,
		/***/
		Bar,
		/***/
		Baz,
	};

	@Override
	public String toString()
	{
		StringBuilder buff = new StringBuilder( "ConfTest" );
		buff.append( "\n\tenum = " + enumeration );
		buff.append( "\n\tbool = " + aBoolean );
		buff.append( "\n\tfloat = " + aFloat );
		buff.append( "\n\tencapInt = " + encap );
		buff.append( "\n\tString = " + aString );
		buff.append( "\n\tPoint = " + point );
		buff.append( "\n\tColour = " + color );
		buff.append( "\n\t\tsub = " + sub );
		buff.append( "\n\t\tnsub = " + namedSub );
		return buff.toString();
	}
}
