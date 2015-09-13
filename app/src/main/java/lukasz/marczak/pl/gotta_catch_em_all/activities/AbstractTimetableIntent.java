package lukasz.marczak.pl.gotta_catch_em_all.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Lukasz Marczak on 2015-08-25.
 */
public abstract class AbstractTimetableIntent {

    private Intent intent;

    public AbstractTimetableIntent(Context context, Class klass) {
        intent = new Intent(context, klass);
    }

    @NonNull
    public Intent asIntent() {
//        if (intent == null)
//            throw new NullPointerException("Intent cannot be null!!!");
        return intent;
    }

    public abstract void putStopID(int id);

    public abstract void putStopCode(int code);

    public abstract void putStopName(String name);
}
