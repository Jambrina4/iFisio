package com.ojambrina.ifisio.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;

public class AppPreferences {
    private static final String PREF_FILE = "MyPreferences";

    private static final String EMAIL = "email";

    private Context context;

    public AppPreferences() {

    }

    private SharedPreferences getSharedPreferences() {
        if (context != null) {
            return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        } else {
            return App.getInstance().getApplicationContext().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        }
    }

    /**
     * GETTER'S
     **/

    public String getEmail() {
        return getSharedPreferences().getString(EMAIL, null);
    }

    /**
     * SETTER'S
     **/

    public void setEmail(String email) {
        this.getSharedPreferences().edit().putString(EMAIL, email).apply();
    }
    /**
     * CLEAR
     **/

    public void clearPreferences() {
        this.getSharedPreferences().edit().clear().apply();
    }

}
