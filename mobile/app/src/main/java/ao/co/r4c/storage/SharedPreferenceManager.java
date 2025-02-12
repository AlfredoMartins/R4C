package ao.co.r4c.storage;

import android.content.Context;
import android.content.SharedPreferences;

import ao.co.r4c.model.Settings;
import ao.co.r4c.model.Usuario;

public class SharedPreferenceManager {

    private static final String SHARED_PREFERENCE_NAME = "ao.co.r4c.data_login_key";
    private static final String SHARED_PREFERENCE_NAME_SETTINGS = "ao.co.r4c.data_settings";

    private static SharedPreferenceManager sharedPreferenceManager;
    private final Context context;

    public SharedPreferenceManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {

        if (sharedPreferenceManager == null) {
            sharedPreferenceManager = new SharedPreferenceManager(context);
        }

        return sharedPreferenceManager;
    }

    public void salvarDadosUsuario(Usuario usuario) {
        SharedPreferences sharedPreferences =
                this.context.getSharedPreferences(
                        SHARED_PREFERENCE_NAME,
                        Context.MODE_PRIVATE
                );

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", usuario.getId());
        editor.putString("nome", usuario.getNome());
        editor.putString("sobrenome", usuario.getSobrenome());
        editor.putString("email", usuario.getEmail());
        editor.putString("senha", usuario.getSenha());
        editor.putString("telefone", usuario.getTelefone());
        editor.putInt("id_categoria", usuario.getId_categoria());
        editor.putString("foto_url", usuario.getFoto_url());

        editor.apply();
    }


    public void atualizarDefinicoes(Settings settings) {
        SharedPreferences sharedPreferences =
                this.context.getSharedPreferences(
                        SHARED_PREFERENCE_NAME_SETTINGS,
                        Context.MODE_PRIVATE
                );

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("motorista_mais_proximo", settings.isMotorista_mais_proximo());
        editor.putBoolean("receber_notificacoes", settings.isReceber_notificacoes());
        editor.putBoolean("vibrar", settings.isVibrar());
        editor.putBoolean("receber_notificacoes_actualizacoes", settings.isReceber_notificacoes_actualizacoes());
        editor.putString("url_web_service", settings.getUr_web_service());
        editor.putString("porta_web_service", settings.getPorta_web_service());

        editor.apply();
    }


    public Settings getSettings() {

        SharedPreferences sharedPreferences =
                this.context.getSharedPreferences(
                        SHARED_PREFERENCE_NAME_SETTINGS,
                        Context.MODE_PRIVATE
                );

        Settings settings = new Settings();

        settings.setMotorista_mais_proximo(sharedPreferences.getBoolean("motorista_mais_proximo", true));
        settings.setReceber_notificacoes(sharedPreferences.getBoolean("receber_notificacoes", true));
        settings.setVibrar(sharedPreferences.getBoolean("vibrar", true));
        settings.setReceber_notificacoes_actualizacoes(sharedPreferences.getBoolean("receber_notificacoes_actualizacoes", true));
        settings.setUr_web_service(sharedPreferences.getString("url_web_service", "http://192.168.43.30"));
        settings.setPorta_web_service(sharedPreferences.getString("porta_web_service", ":81/"));

        return settings;
    }

    public boolean sessaoIniciada() {

        SharedPreferences sharedPreferences =
                this.context.getSharedPreferences(
                        SHARED_PREFERENCE_NAME,
                        Context.MODE_PRIVATE
                );

        return (sharedPreferences.getInt("id", -1) != -1);
    }

    public Usuario getUsuario() {

        SharedPreferences sharedPreferences =
                this.context.getSharedPreferences(
                        SHARED_PREFERENCE_NAME,
                        Context.MODE_PRIVATE
                );

        Usuario usuario = new Usuario();
        usuario.setId(sharedPreferences.getInt("id", -1));
        usuario.setNome(sharedPreferences.getString("nome", null));
        usuario.setSobrenome(sharedPreferences.getString("sobrenome", null));
        usuario.setEmail(sharedPreferences.getString("email", null));
        usuario.setSenha(sharedPreferences.getString("senha", null));
        usuario.setTelefone(sharedPreferences.getString("telefone", null));
        usuario.setId_categoria(sharedPreferences.getInt("id_categoria", -1));
        usuario.setFoto_url(sharedPreferences.getString("foto_url", null));

        return usuario;
    }

    public void terminarSessao() {
        SharedPreferences sharedPreferences =
                this.context.getSharedPreferences(
                        SHARED_PREFERENCE_NAME,
                        Context.MODE_PRIVATE
                );

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
