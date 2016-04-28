package com.chatt.demo;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

/**
 * The Class ChattApp is the Main Application class of this app. The onCreate
 * method of this class initializes the Parse.
 */
public class ChattApp extends Application
{

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();

		Parse.initialize(this, "ZVuYQgGmT79jqb4HpbbabGz8xenRTXaYI30cCTDM",
				"Zrw7TkZIy1jGGY4JEGl0MAtakRBLXCeIMnF4qjrQ");
		ParseInstallation.getCurrentInstallation().saveInBackground();
		Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
		ParseFacebookUtils.initialize(this);

	}
}
