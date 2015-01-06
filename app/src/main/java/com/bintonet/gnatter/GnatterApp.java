package com.bintonet.gnatter;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

/**
 * The Class ChattApp is the Main Application class of this app. The onCreate
 * method of this class initializes the Parse.
 */

public class GnatterApp extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        Parse.initialize(this, "K4UTdVhNskSi5IyrcakAPg3RjRLF9z4XEDo8v2P1", "FZdDxfDCEFxaHITocsNHoeeXdUFN494f1GABuqKi");
        Log.d("GnatterApp", "Parse Initialized");
    }
}
