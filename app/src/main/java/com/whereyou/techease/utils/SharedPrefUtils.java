package com.whereyou.techease.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Asus on 9/27/2017.
 */

public class SharedPrefUtils {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences SharedPreferencesDeclaration(Context context, String mode){
        sharedPreferences = context.getSharedPreferences(mode, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
