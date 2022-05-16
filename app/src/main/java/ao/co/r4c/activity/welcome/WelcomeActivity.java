package ao.co.r4c.activity.welcome;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ao.co.r4c.R;
import ao.co.r4c.activity.login.LoginActivity;
import ao.co.r4c.activity.main.customer.fragment.CustomerMapsActivity;
import ao.co.r4c.activity.main.driver.fragment.DriverMapsActivity;
import ao.co.r4c.activity.signup.RegisterActivity;
import ao.co.r4c.storage.SharedPreferenceManager;

public class WelcomeActivity extends AppCompatActivity {

    Button btn_login;
    Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);


        //Chama novo Fragment Login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //Chama novo Fragment SignUp
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPreferenceManager.getInstance(this).sessaoIniciada()) {

            if (SharedPreferenceManager.getInstance(this).getUsuario().getId_categoria() == 1) {
                Intent intent = new Intent(WelcomeActivity.this, DriverMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(WelcomeActivity.this, CustomerMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}
