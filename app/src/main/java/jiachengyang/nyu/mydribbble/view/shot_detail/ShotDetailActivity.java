package jiachengyang.nyu.mydribbble.view.shot_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;

public class ShotDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    public static final String KEY_SHOT_TITLE = "shot_title";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getIntent().getStringExtra(KEY_SHOT_TITLE));

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotDetailFragment.newInstance(getIntent().getExtras()))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
