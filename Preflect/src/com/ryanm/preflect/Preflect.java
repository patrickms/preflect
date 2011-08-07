package com.ryanm.preflect;

import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.ryanm.preflect.annote.Variable;

/**
 * Handy utility for manipulating object trees that have been marked up with
 * {@link Variable} annotations
 * 
 * @author ryanm
 */
public class Preflect
{
	/**
	 * When <code>true</code>, a {@link StructuralError} will be thrown if a
	 * private field or method is encountered with a {@link Variable} annotation,
	 * when <code>false</code>, no error is raised and the variable is ignored.
	 * Default value is <code>true</code>
	 */
	public static boolean errorOnNonPublicVariables = true;

	/**
	 * The name under which applied configurations are automatically saved. Set
	 * to <code>null</code> to disable automatic saving
	 */
	public static String autoSaveName = "default";

	/**
	 * Logging tag
	 */
	public static final String LOG_TAG = "Preflect";

	/**
	 * The activity request flag used in
	 * {@link Activity#startActivityForResult(Intent, int)} when launching the
	 * configuration activity (value = 266344)
	 */
	public static final int ACTIVITY_REQUEST_FLAG = 266344;

	private static Queue<ConfigResult> deferredResults =
			new LinkedList<Preflect.ConfigResult>();

	private Preflect()
	{
	}

	/**
	 * @param returnTo
	 *           The activity to return to when we are done configuring
	 * @param showPersist
	 *           When <code>true</code>, the user will be able manually save
	 *           multiple different configurations.
	 * @param showConfirm
	 *           When <code>true</code>, the user will have to confirm or cancel
	 *           configuration changes before leaving the generated
	 *           {@link PreferenceActivity}. This might be useful if the
	 *           preference hierarchy is deep and the user risks prematurely
	 *           leaving the activity by accident
	 * @param roots
	 *           The (annotated) objects to configure.
	 */
	public static void configure( final Activity returnTo,
			final boolean showPersist, final boolean showConfirm,
			final Object... roots )
	{
		if( roots == null )
		{
			Log.e( LOG_TAG, "Attempt made to configure null roots. "
					+ "I don't think we'll be doing that" );
		}
		else
		{
			Log.i( LOG_TAG, "Launching configuration activity" );

			final Intent i = new Intent( returnTo, PreflectActivity.class );
			i.putExtra( Util.CONF_TAG, Extract.extract( roots ).toString() );
			i.putExtra( Util.RETURNTO_TAG, returnTo.getClass().getName() );
			i.putExtra( Util.SHOW_PERSIST_MENU, showPersist );
			i.putExtra( Util.SHOW_CONFIRM_MENU, showConfirm );

			returnTo.startActivityForResult( i, ACTIVITY_REQUEST_FLAG );
		}
	}

	/**
	 * Call this from your activity in {@link Activity} .onActivityResult() to
	 * apply a configuration
	 * 
	 * @param requestCode
	 *           from {@link Activity}.onActivityResult()
	 * @param resultCode
	 *           from {@link Activity}.onActivityResult()
	 * @param data
	 *           from {@link Activity}.onActivityResult()
	 * @param configTargets
	 *           roots of the object trees to apply configurations to
	 * @return <code>true</code> if the configuration was applied,
	 *         <code>false</code> otherwise
	 */
	public static boolean
			onActivityResult( final int requestCode, final int resultCode,
					final Intent data, final Object... configTargets )
	{
		if( configTargets.length == 0 )
		{
			Log.e( Preflect.LOG_TAG,
					"No application targets specified in onActivityResult(). "
							+ "You probably didn't mean to do that" );
		}

		if( requestCode == Preflect.ACTIVITY_REQUEST_FLAG
				&& resultCode == Activity.RESULT_OK )
		{
			Log.i( LOG_TAG, "Applying configuration" );
			try
			{
				Apply.apply(
						new JSONObject( data.getStringExtra( Util.CONF_TAG ) ),
						configTargets );
				return true;
			}
			catch( final JSONException e )
			{
				Log.e(
						LOG_TAG,
						"Problem parsing json data : " + data.getStringExtra( "conf" ),
						e );
			}
		}

		return false;
	}

	/**
	 * Call this from your {@link Activity}.onActivityResult() instead of
	 * {@link #onActivityResult(int, int, Intent, Object...)} to save a
	 * configuration for later application. This is useful if your configuration
	 * changes need to be done in a particular thread like, for example, OpenGL
	 * stuff
	 * 
	 * @param requestCode
	 *           from {@link Activity}.onActivityResult()
	 * @param resultCode
	 *           from {@link Activity}.onActivityResult()
	 * @param data
	 *           from {@link Activity}.onActivityResult()
	 */
	public static void deferActivityResult( final int requestCode,
			final int resultCode, final Intent data )
	{
		if( requestCode == Preflect.ACTIVITY_REQUEST_FLAG
				&& resultCode == Activity.RESULT_OK )
		{
			synchronized( deferredResults )
			{
				Log.i( LOG_TAG, "Deferring configuration" );
				try
				{
					final JSONObject json =
							new JSONObject( data.getStringExtra( Util.CONF_TAG ) );
					deferredResults.add( new ConfigResult( json ) );
				}
				catch( final JSONException e )
				{
					Log.e(
							LOG_TAG,
							"Problem parsing json data : "
									+ data.getStringExtra( Util.CONF_TAG ), e );
				}
			}
		}
	}

	/**
	 * Call this to apply configurations previously deferred in
	 * {@link #deferActivityResult(int, int, Intent)}
	 * 
	 * @param configTargets
	 *           roots of the object trees to apply configurations to
	 */
	public static void applyDeferredConfigurations(
			final Object... configTargets )
	{
		synchronized( deferredResults )
		{
			while( !deferredResults.isEmpty() )
			{
				Log.i( LOG_TAG, "Applying deferred configuration" );

				final ConfigResult cr = deferredResults.poll();
				Apply.apply( cr.config, configTargets );
			}
		}
	}

	/**
	 * For deferring configuration application
	 * 
	 * @author ryanm
	 */
	private static class ConfigResult
	{
		private final JSONObject config;

		private ConfigResult( final JSONObject config )
		{
			this.config = config;
		}
	}
}
