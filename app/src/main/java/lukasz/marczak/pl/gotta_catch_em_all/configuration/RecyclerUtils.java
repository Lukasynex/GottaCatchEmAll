package lukasz.marczak.pl.gotta_catch_em_all.configuration;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Lukasz Marczak on 2015-09-21.
 */
public class RecyclerUtils {

    public static View.OnTouchListener disableTouchEvents(final boolean disallow) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return disallow;
            }
        };
    }
}
