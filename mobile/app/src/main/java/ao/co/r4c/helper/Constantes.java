package ao.co.r4c.helper;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.model.MotoristaInfo;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.model.UsuariosActivo;
import ao.co.r4c.service.SocketClient;

public class Constantes {
    public static final int TOTAL_ESTRELAS = 5;
    public static final Double preco_minimo = 0.55 * 20;
    public static final Double tempo_minimo = 0.35;
    public static final Double distancia_minima = 1.75;
    private static final String PACKAGE_NAME =
            "ao.co.r4c.locationaddress";
    public static int id_passageiro;
    public static int id_motorista;
    public static String tempo_distancia = "10 minutos";
    public static float distancia = 0;
    public static String endereco_passageiro;
    public static int NIVEL_VIAGEM = 0;
    public static String telefone_passageiro;
    public static SocketClient socketClient;
    public static Usuario usuario;
    public static int ESTADO_MOTORISTA = -1;
    public static int ESTADO_CHAMADA = 0;
    public static int ESTADO_PASSAGEIRO = -1;
    public static MotoristaInfo dados_motorista;
    public static HashMap<Integer, Float> distances_collections;
    public static int SUCCESS_RESULT = 0;
    public static int FAILURE_RESULT = 1;
    public static String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static void Zerar() {

        int id_passageiro;
        int id_motorista;
        tempo_distancia = "10 minutos";
        float distancia = 0;
        endereco_passageiro = "";
        int NIVEL_VIAGEM = 0;
        telefone_passageiro = "";
        socketClient = new SocketClient();
        usuario = new Usuario();
        ESTADO_MOTORISTA = -1;
        ESTADO_CHAMADA = 0;
        ESTADO_PASSAGEIRO = -1;
        dados_motorista = new MotoristaInfo();
        distances_collections = new HashMap<>();


        SUCCESS_RESULT = 0;

        FAILURE_RESULT = 1;

    }

    public static String imageToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    public static Double calcularPrecoViagem(Double distancia, int minutos) {
        return preco_minimo + (tempo_minimo * minutos) + (distancia_minima * distancia);
    }

    public static int calcularMinutoViagem(long base) {

        long diferenca = (SystemClock.elapsedRealtime() - base);
        long segundos = diferenca / 1000;
        long minutos = segundos / 60;

        return (int) minutos;
    }

    public static UsuariosActivo todasInformacoesMotorista(int id_motorista, ArrayList<UsuariosActivo> usuariosActivoArrayList) {

        int index_esq = 0;
        int index_dir = usuariosActivoArrayList.size() - 1;
        int media;

        while (index_esq <= index_dir) {
            media = (index_dir + index_esq) / 2;

            if (usuariosActivoArrayList.get(media).getId() == id_motorista)
                return usuariosActivoArrayList.get(media);
            else if (id_motorista < usuariosActivoArrayList.get(media).getId())
                index_dir = media - 1;
            else
                index_esq = media + 1;
        }

        return null;
    }


    public static void carregarEstrelasDeAvaliacao(Double per_aval, List<ImageView> imageViewList) {

        int avaliacao = 1;

        if (per_aval >= 0 && per_aval <= 20)
            avaliacao = 1;

        if (per_aval >= 21 && per_aval <= 40)
            avaliacao = 2;

        if (per_aval >= 41 && per_aval <= 60)
            avaliacao = 3;

        if (per_aval >= 61 && per_aval <= 80)
            avaliacao = 4;

        if (per_aval >= 81 && per_aval <= 100)
            avaliacao = 5;

        int j = avaliacao;
        int i = 0;

        for (; i < j; i++) {
            imageViewList.get(i).setImageResource(R.drawable.ic_star_yellow);
        }

        for (; j < imageViewList.size(); j++) {
            imageViewList.get(i).setImageResource(R.drawable.ic_star_border_yellow);
        }
    }


    public static String getId(String text) {
        return text.substring(0, text.indexOf('-'));
    }

    public static String getName(String text) {
        return text.substring(text.indexOf('-') + 1);
    }
}
