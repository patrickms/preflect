
package com.ryanm.preflect.annote;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.preference.PreferenceCategory;

/**
 * Annotation for grouping {@link Variable}s
 * 
 * @author ryanm
 */
@Documented
@Target( { ElementType.FIELD, ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
public @interface Category
{
	/**
	 * The name of the category. This value is passed into
	 * {@link PreferenceCategory#setTitle(CharSequence)}
	 */
	String value();
}
