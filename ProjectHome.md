# preflect #

Allows you to construct a [PreferenceActivity](http://developer.android.com/reference/android/preference/PreferenceActivity.html) directly from your Activity's object graph - no xml or explicit construction required.

### Usage ###

Just apply [annotations](http://preflect.googlecode.com/svn/trunk/Preflect/doc/com/ryanm/preflect/annote/Variable.html) to the types, variables and methods in your code to form a tree of options and you're done! Runtime reflection is used to construct a PreferenceActivity, to retrieve and to apply variable values. Encapsulated variables can be accessed by applying the same annotations to get and set methods. Additional annotations are used to supply a variable [description](http://preflect.googlecode.com/svn/trunk/Preflect/doc/com/ryanm/preflect/annote/Summary.html), [grouping](http://preflect.googlecode.com/svn/trunk/Preflect/doc/com/ryanm/preflect/annote/Category.html) and [position](http://preflect.googlecode.com/svn/trunk/Preflect/doc/com/ryanm/preflect/annote/Order.html) in the PreferenceActivity.
Changes to unencapsulated variables can be detected with the [DirtyFlag](http://preflect.googlecode.com/svn/trunk/Preflect/doc/com/ryanm/preflect/annote/DirtyFlag.html) annotation.

An example of annotation usage can be found [here](http://code.google.com/p/preflect/source/browse/trunk/PreflectTest/src/com/ryanm/preflect/test/ConfTest.java). The interface generated is a standard [PreferenceActivity](http://developer.android.com/reference/android/preference/PreferenceActivity.html), so will match the platform look and feel. On my phone, the example class generates this:

![http://preflect.googlecode.com/svn/trunk/PreflectTest/images/device.png](http://preflect.googlecode.com/svn/trunk/PreflectTest/images/device.png) ![http://preflect.googlecode.com/svn/trunk/PreflectTest/images/device1.png](http://preflect.googlecode.com/svn/trunk/PreflectTest/images/device1.png)

Tapping on a variable name offers the user a type-appropriate input method, e.g.: [numbers](http://preflect.googlecode.com/svn/trunk/PreflectTest/images/number.png), [strings](http://preflect.googlecode.com/svn/trunk/PreflectTest/images/string.png), [enums](http://preflect.googlecode.com/svn/trunk/PreflectTest/images/enum.png), [colours](http://preflect.googlecode.com/svn/trunk/PreflectTest/images/colour.png).

### Launch and Return ###

Launching the PreferenceActivity is simply a matter of calling [Preflect.configure()](http://preflect.googlecode.com/svn/trunk/Preflect/doc/com/ryanm/preflect/Preflect.html#configure(Activity,_java.lang.Object...)), passing in references to the Activity to return to and the roots of the variable trees.

Once the user is finished with the preferences, control will be returned to your Activity. Preference changes are applied by overriding onActivityResult() in your activity like so:
```
@Override
protected void onActivityResult( int requestCode, int resultCode, Intent data )
{
	Preflect.onActivityResult( requestCode, resultCode, data, variableTreeRoots );
}
```

### Persistence ###

Configurations can be saved and loaded programmatically with the [Persist](http://preflect.googlecode.com/svn/trunk/Preflect/doc/com/ryanm/preflect/Persist.html) class methods.

### Extension ###

Preflect will provide appropriate widgets to manipulate booleans, floats, ints, Strings, enums, packed [colour](http://developer.android.com/reference/android/graphics/Color.html) ints and [Points](http://developer.android.com/reference/android/graphics/Point.html). New variable types can be added by extending [VariableType](http://preflect.googlecode.com/svn/trunk/Preflect/doc/com/ryanm/preflect/VariableType.html).

### Links ###

  * [Javadoc](http://preflect.googlecode.com/svn/trunk/Preflect/doc/index.html)
  * [Eclipse project](http://code.google.com/p/preflect/source/browse/#svn/trunk/Preflect)
  * [Usage example project](http://code.google.com/p/preflect/source/browse/#svn/trunk/PreflectTest)
  * [Zip archives of project directories](http://code.google.com/p/preflect/downloads/list)