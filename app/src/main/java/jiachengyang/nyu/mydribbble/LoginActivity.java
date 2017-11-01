package jiachengyang.nyu.mydribbble;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.auth.Auth;
import jiachengyang.nyu.mydribbble.dribbble.Dribbble;

public class LoginActivity extends AppCompatActivity{

    //code为第一步获取的temporary key
    public static final int REQ_CODE = 100;

    @BindView(R.id.activity_login_btn) TextView loginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Dribbble.init(this);

        if(Dribbble.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, AuthActivity.class);
                    startActivityForResult(intent, REQ_CODE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            final String authCode = data.getStringExtra(AuthActivity.KEY_CODE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // this is a network call and it's time consuming
                        String accessToken = Auth.fetchAccessToken(authCode);

                        //network to get user and store accessToken and user to SharedPreferences
                        Dribbble.login(LoginActivity.this, accessToken);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); //end dialogs
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
