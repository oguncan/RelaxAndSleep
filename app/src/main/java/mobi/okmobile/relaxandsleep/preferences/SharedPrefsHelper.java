package mobi.okmobile.relaxandsleep.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;


public class SharedPrefsHelper {

    /**
     * Called to save supplied value in shared preferences against given key.
     *
     * @param context Context of caller activity
     * @param key     Key of value to save against
     * @param value   Value to save
     */
    public static void saveToPrefs(Context context, String key, Object value) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            final SharedPreferences.Editor editor = prefs.edit();
            if (value instanceof Integer) {
                editor.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof String) {
                editor.putString(key, value.toString());
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, ((Boolean) value).booleanValue());
            } else if (value instanceof Long) {
                editor.putLong(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                editor.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Double) {
                editor.putLong(key, Double.doubleToRawLongBits((double) value));
            }
            editor.commit();
        }
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     *
     * @param context      Context of caller activity
     * @param key          Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static Object getFromPrefs(Context context, String key, Object defaultValue) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            try {
                if (defaultValue instanceof String) {
                    return sharedPrefs.getString(key, defaultValue.toString());
                } else if (defaultValue instanceof Integer) {
                    return sharedPrefs.getInt(key, ((Integer) defaultValue).intValue());
                } else if (defaultValue instanceof Boolean) {
                    return sharedPrefs.getBoolean(key, ((Boolean) defaultValue).booleanValue());
                } else if (defaultValue instanceof Long) {
                    return sharedPrefs.getLong(key, ((Long) defaultValue).longValue());
                } else if (defaultValue instanceof Float) {
                    return sharedPrefs.getFloat(key, ((Float) defaultValue).floatValue());
                } else if (defaultValue instanceof Double) {
                    return Double.longBitsToDouble(sharedPrefs.getLong(key, Double.doubleToLongBits((double) defaultValue)));
                }
            } catch (Exception e) {
                Log.e("Execption", e.getMessage());
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * @param context Context of caller activity
     * @param key     Key to delete from SharedPreferences
     */
    public static void removeFromPrefs(Context context, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            final SharedPreferences.Editor editor = prefs.edit();
            editor.remove(key);
            editor.commit();
        }
    }

    public static boolean hasKey(Context context, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            return prefs.contains(key);
        }
        return false;
    }
}

