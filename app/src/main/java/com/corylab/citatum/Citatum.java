package com.corylab.citatum;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class Citatum extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(updateLanguage(base, "en"));
    }

    private Context updateLanguage(Context context, String language) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale newLocale = new Locale(language);
        configuration.setLocale(newLocale);
        return context.createConfigurationContext(configuration);
    }
}