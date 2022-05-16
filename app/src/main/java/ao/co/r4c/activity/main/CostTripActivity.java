package ao.co.r4c.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.driver.fragment.DriverMapsActivity;
import ao.co.r4c.helper.Constantes;

public class CostTripActivity extends AppCompatActivity {

    TextView txt_data, txt_total_embolso, txt_embolso, txt_preco_basico, txt_tempo, txt_distancia, txt_origem, txt_destino;

    Button btn_salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_trip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_data = findViewById(R.id.txt_data);
        txt_total_embolso = findViewById(R.id.txt_total_embolso);
        txt_preco_basico = findViewById(R.id.txt_preco_basico);
        txt_tempo = findViewById(R.id.txt_tempo);
        txt_distancia = findViewById(R.id.txt_distancia);
        txt_origem = findViewById(R.id.txt_origem);
        txt_destino = findViewById(R.id.txt_destino);

        String data = Calendar.getInstance().getTime().toString();
        String total_embolso = getIntent().getExtras().getString("preco").substring(0, getIntent().getExtras().getString("preco").lastIndexOf('.' + 2));


        String preco_basico = Constantes.preco_minimo.toString().substring(0, Constantes.preco_minimo.toString().lastIndexOf('.') + 2);
        String tempo = getIntent().getExtras().getString("tempo");
        String distancia = getIntent().getExtras().getString("distancia");
        String origem = getIntent().getExtras().getString("origem");
        String destino = getIntent().getExtras().getString("destino");

        txt_data.setText(data);
        txt_tempo.setText(tempo);
        txt_distancia.setText(distancia + "m");
        txt_origem.setText(origem);
        txt_destino.setText(destino);
        txt_preco_basico.setText(preco_basico);

        txt_total_embolso.setText(total_embolso + "KZS");

        btn_salvar = findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CostTripActivity.this, DriverMapsActivity.class);
                Constantes.Zerar();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
