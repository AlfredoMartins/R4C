package ao.co.r4c.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import ao.co.r4c.R;
import ao.co.r4c.activity.welcome.WelcomeActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        boolean handler = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.finish();
    }
}
