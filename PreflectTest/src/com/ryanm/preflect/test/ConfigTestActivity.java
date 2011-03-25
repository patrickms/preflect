
package com.ryanm.preflect.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ryanm.preflect.Preflect;

/**
 * @author ryanm
 */
public class ConfigTestActivity extends Activity
{
	private ConfTest testy = new ConfTest();

	private TextView tv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		ScrollView sc = new ScrollView( this );
		tv = new TextView( this );
		sc.addView( tv );

		tv.setText( testy.toString() );

		setContentView( sc );
	}

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		Preflect.onActivityResult( requestCode, resultCode, data, testy );

		// refresh the text to show all those lovely changed values
		tv.setText( testy.toString() );
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		if( keyCode == KeyEvent.KEYCODE_MENU )
		{
			Preflect.configure( this, true, true, testy );

			return true;
		}

		return super.onKeyDown( keyCode, event );
	}
}