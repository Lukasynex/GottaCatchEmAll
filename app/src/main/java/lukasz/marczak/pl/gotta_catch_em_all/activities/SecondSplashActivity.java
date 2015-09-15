package lukasz.marczak.pl.gotta_catch_em_all.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.hannesdorfmann.swipeback.transformer.SlideSwipeBackTransformer;

import lukasz.marczak.pl.gotta_catch_em_all.R;

public class SecondSplashActivity extends FragmentActivity {
    public static int trzy = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(trzy>0) {
//            trzy--;
//            startActivity(new Intent(this, SplashActivity.class));
//        }
        SwipeBack.attach(this, Position.RIGHT)
                .setContentView(R.layout.activity_second_splash)
                .setSwipeBackTransformer(new SlideSwipeBackTransformer())
                .setSwipeBackView(R.layout.swipeback_default);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        finish();
        startActivity(new Intent(this, SplashActivity.class));
        overridePendingTransition(R.anim.swipeback_stack_right_in, R.anim.swipeback_stack_to_back);

//        super.onBackPressed();

    }
}
