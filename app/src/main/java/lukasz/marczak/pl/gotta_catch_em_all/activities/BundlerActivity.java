package lukasz.marczak.pl.gotta_catch_em_all.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.data.BeaconsInfo;

public class BundlerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundler);

        Button btn=(Button)
        findViewById(R.id.clicker);btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BetterTimetableIntent intent =
                        new BetterTimetableIntent(BundlerActivity.this, SecondActivity.class);
                intent.putStopID(1);
                intent.putStopName("KARYNA");
                intent.putStopCode(30);
                startActivity(intent.asIntent());
            }
        });
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               final AbstractTimetableIntent intent =
                        new AbstractTimetableIntent(BundlerActivity.this, SecondActivity.class) {
                            @Override
                            public void putStopID(int id) {
                                asIntent().putExtra("ID",id);
                            }

                            @Override
                            public void putStopCode(int code) {
                                asIntent().putExtra("ID",code);
                            }

                            @Override
                            public void putStopName(String name) {
                                asIntent().putExtra("ID",name);
                            }
                        };


                intent.putStopName("GUWNIAK");
                intent.putStopCode(666);
                startActivity(intent.asIntent());
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bundler, menu);
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
}
