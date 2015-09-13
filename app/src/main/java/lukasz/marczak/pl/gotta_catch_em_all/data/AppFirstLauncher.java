package lukasz.marczak.pl.gotta_catch_em_all.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AppFirstLauncher {
    public static final String TAG = AppFirstLauncher.class.getSimpleName();
    public static final String IF_APP_FIRST_LAUNCHED = "IF_APP_FIRST_LAUNCHED";
    public static AppFirstLauncher INSTANCE = new AppFirstLauncher();

    private AppFirstLauncher() {
    }

    public void setup(Context context) {
        Log.d(TAG, "setup ");
        SharedPreferences.Editor editor = context.getSharedPreferences(IF_APP_FIRST_LAUNCHED, Context.MODE_PRIVATE).edit();
        editor.putBoolean(IF_APP_FIRST_LAUNCHED, Boolean.FALSE);
        editor.commit(); /**difference is here: editor.commit() was previous.
         people say apply() is faster than commit(). because apply is asynchronus */
    }

    public boolean ifAppFirstLaunched(Context context) {
        Log.d(TAG, "ifAppFirstLaunched ");
        return context.getSharedPreferences(IF_APP_FIRST_LAUNCHED, Context.MODE_PRIVATE)
                .getBoolean(IF_APP_FIRST_LAUNCHED, true);
    }
}