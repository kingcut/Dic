package com.example.dict_en_vn.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * Preference Util
 *
 * @author NamLH
 */
@SuppressLint("NewApi")
public final class PreferenceUtil {

    ////////////////////////////////////////////////////////////////////////////
    // Constant
    ////////////////////////////////////////////////////////////////////////////
	
    /** start app */
    public static final String START_APP_NOT_FIRST = "START_APP_NOT_FIRST";
    public static final String COPY_DATA = "COPY_DATA";


    /** Default name */
    private static final String DEFAULT_NAME = "Dict";
    
    // setting 
    ////////////////////////////////////////////////////////////////////////////
    // public Constant
    ////////////////////////////////////////////////////////////////////////////

    /**
     * private constructor
     */
    private PreferenceUtil(){
    	
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public Constant
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Static functions
    ////////////////////////////////////////////////////////////////////////////

    /**
     * set string value to preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context, String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        if (TextUtils.isEmpty(value)) {
            edit.remove(key);
        } else {
            edit.putString(key, value);
        }
        edit.apply();
    }
    public static boolean checkKey (Context context, String key){
    	SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    	return pref.contains(key);
    }
    /**
     * get string value
     *
     * @param context
     * @param key
     * @return 該当なし:null
     */
    public static String getString(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }
    

    /**
     * get string value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return 該当なし:null
     */
    public static String getString(Context context, String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }

    /**
     * set boolean value to preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setBoolean(Context context, String key, boolean bool) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putBoolean(key, bool);
        edit.apply();
    }

    /**
     * get boolean value
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    /**
     * save long value to preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setLong(Context context, String key, long l) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putLong(key, l);
        edit.apply();
    }

    /**
     * get long value
     *
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return 0L;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getLong(key, 0L);
    }

    /**
     * save int value to preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setInt(Context context, String key, int integer) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putInt(key, integer);
        edit.apply();
    }

    /**
     * get int value
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return 0;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }
    /**
     * save float value to preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setFloat(Context context, String key, float f) {
    	if (TextUtils.isEmpty(key)) {
    		return;
    	}
    	
    	SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    	Editor edit = pref.edit();
    	edit.putFloat(key, f);
    	edit.apply();
    }
    
    /**
     * get float value
     *
     * @param context
     * @param key
     * @return
     */
    public static float getFloat(Context context, String key) {
    	if (TextUtils.isEmpty(key)) {
    		return 0;
    	}
    	
    	SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    	return pref.getFloat(key, 0);
    }
    /**
     * delete all SharedPreferences
     * @param context Context
     */
    public static boolean deleteAll(Context context){
    	SharedPreferences settings = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    	boolean result =  settings.edit().clear().commit();
    	Editor edit = settings.edit();
        edit.apply();
    	return result;
    }
    
    public static boolean deleteOne(Context context, String key){
    	SharedPreferences settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    	boolean result =  settings.edit().clear().commit();
    	Editor edit = settings.edit();
        edit.apply();
    	return result;
    }
}
