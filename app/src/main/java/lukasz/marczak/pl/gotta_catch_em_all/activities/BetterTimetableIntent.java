package lukasz.marczak.pl.gotta_catch_em_all.activities;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Lukasz Marczak on 2015-08-25.
 */
public class BetterTimetableIntent {

    private Intent intent;

    public BetterTimetableIntent(Context context, Class klass) {
        intent = new Intent(context, klass);
    }

    public Intent asIntent() {
        if (intent == null)
            throw new NullPointerException("Intent cannot be null!!!");
        return intent;
    }

    public void putStopID(int id) {
        intent.putExtra("ID", id);
    }

    public void putStopCode(int code) {
        intent.putExtra("CODE", code);
    }

    public void putStopName(String name) {
        intent.putExtra("NAME", name);
    }
}
