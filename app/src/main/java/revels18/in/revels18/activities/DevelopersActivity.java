package revels18.in.revels18.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import revels18.in.revels18.R;

public class DevelopersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
                toolbar.setElevation(0);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(R.string.drawer_developers);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
                appBarLayout.setElevation(0);
                appBarLayout.setTargetElevation(0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
