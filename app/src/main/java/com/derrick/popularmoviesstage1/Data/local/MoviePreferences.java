package com.derrick.popularmoviesstage1.Data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.derrick.popularmoviesstage1.R;

public class MoviePreferences {

    public static String getSortingQuery(Context context, String keyForSorting) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String defaultSorting = context.getString(R.string.pref_sorting_default_value);

        return prefs.getString(keyForSorting, defaultSorting);

    }

    public static void SetSortingQuery(Context context, String SortingQuery) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        String keyForSorting = context.getString(R.string.pref_sorting_key);

        editor.putString(keyForSorting, SortingQuery);

        editor.commit();

    }
}
