package com.example.smart_alert;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {

    private Context ctx;
    public LanguageManager(Context ctx){
        this.ctx = ctx;
    }

    public void updateResource(String code){
        Locale locale = new Locale(code);
        locale.setDefault(locale);
        Resources resources = ctx.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}
