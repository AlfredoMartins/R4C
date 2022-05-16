package ao.co.r4c.activity.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.customer.fragment.CustomerHomeFragment;
import ao.co.r4c.model.Settings;
import ao.co.r4c.storage.SharedPreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    CheckBox checkBox_closest_driver, checkBox_notification, checkBox_vibrate, checkBox_update;
    TextView txt_closest_driver, txt_notification, txt_vibrate, txt_update;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        checkBox_closest_driver = findViewById(R.id.checkbox_closeste_driver);
        checkBox_notification = findViewById(R.id.checkbox_receive_notifications);
        checkBox_vibrate = findViewById(R.id.checkbox_vibrate);
        checkBox_update = findViewById(R.id.checkbox_state_update_notification);

        txt_closest_driver = findViewById(R.id.txt_state_closeste_driver);
        txt_notification = findViewById(R.id.txt_receive_notifications);
        txt_update = findViewById(R.id.txt_state_update_notification);
        txt_vibrate = findViewById(R.id.txt_state_vibrate);

        checkBox_closest_driver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    txt_closest_driver.setText("(Não)");
                    settings.setMotorista_mais_proximo(false);
                    actualizarDefinicoes();
                    CustomerHomeFragment.btn_call_driver.setText("Procurar motorista");
                    CustomerHomeFragment.btn_call_driver.refreshDrawableState();
                } else {
                    txt_closest_driver.setText("(Sim)");
                    settings.setMotorista_mais_proximo(true);
                    actualizarDefinicoes();
                    CustomerHomeFragment.btn_call_driver.setText("Chamar motorista");
                    CustomerHomeFragment.btn_call_driver.refreshDrawableState();
                }
            }
        });


        checkBox_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    txt_notification.setText("(Não)");
                    settings.setReceber_notificacoes(false);
                    actualizarDefinicoes();
                } else {
                    txt_notification.setText("(Sim)");
                    settings.setReceber_notificacoes(true);
                    actualizarDefinicoes();
                }
            }
        });


        checkBox_update.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    txt_update.setText("(Não)");
                    settings.setReceber_notificacoes_actualizacoes(false);
                    actualizarDefinicoes();
                } else {
                    txt_update.setText("(Sim)");
                    settings.setReceber_notificacoes_actualizacoes(true);
                    actualizarDefinicoes();
                }
            }
        });

        checkBox_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    txt_vibrate.setText("(Não)");
                    settings.setVibrar(false);
                    actualizarDefinicoes();
                } else {
                    txt_vibrate.setText("(Sim)");
                    settings.setVibrar(true);
                    actualizarDefinicoes();
                }
            }
        });


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

        TextView txt_proxy_configuration = findViewById(R.id.txt_proxy_configuration);

        txt_proxy_configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Calling the dialog information proxy settings
                ProxySettingsDialogFragment dialogFragment = new ProxySettingsDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "Informações");
            }
        });

        carregarDefinicoes();
    }

    private void carregarDefinicoes() {
        settings = SharedPreferenceManager.getInstance(this).getSettings();

        checkBox_closest_driver.setChecked(settings.isMotorista_mais_proximo());
        checkBox_vibrate.setChecked(settings.isVibrar());
        checkBox_notification.setChecked(settings.isReceber_notificacoes());
        checkBox_update.setChecked(settings.isReceber_notificacoes_actualizacoes());

        checkBox_closest_driver.refreshDrawableState();
        checkBox_vibrate.refreshDrawableState();
        checkBox_notification.refreshDrawableState();
        checkBox_update.refreshDrawableState();

    }


    private void actualizarDefinicoes() {
        SharedPreferenceManager.getInstance(this).atualizarDefinicoes(settings);
    }
}
