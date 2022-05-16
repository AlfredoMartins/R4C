package ao.co.r4c.activity.password;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ao.co.r4c.R;

public class SendCodeActivity extends AppCompatActivity {

    Button btn_enviar;
    EditText txt_email_telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_code);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_enviar = findViewById(R.id.btn_send);
        txt_email_telefone = findViewById(R.id.txt_email);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendCodeActivity.this, ConfirmationCodeActivity.class);

                if (!txt_email_telefone.getText().toString().contains("@")) {
                    intent.putExtra("telefone", txt_email_telefone.getText());
                } else
                    intent.putExtra("email", txt_email_telefone.getText());

                startActivity(intent);
            }
        });


    }
}
