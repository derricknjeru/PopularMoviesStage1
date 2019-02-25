package com.derrick.popularmoviesstage1;

import android.app.Application;
import android.support.annotation.Nullable;


import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

import static android.util.Log.INFO;

public class MovieApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static final class CrashReportingTree extends Timber.Tree {
        @Override
        protected boolean isLoggable(@Nullable String tag, int priority) {
            return priority >= INFO;
        }

        @Override
        protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
            //Send this to firebase crashlytics
        }
    }
}
