package ao.co.r4c.activity.main.driver.fragment;

import static ao.co.r4c.activity.main.customer.fragment.CustomerHomeFragment.itens_places;
import static ao.co.r4c.activity.main.customer.fragment.CustomerHomeFragment.txt_destino_pesquisa;
import static ao.co.r4c.activity.main.customer.fragment.CustomerHomeFragment.txt_origem_pesquisa;
import static ao.co.r4c.activity.main.customer.fragment.CustomerHomeFragment.usuariosActivoArrayList;
import static ao.co.r4c.helper.Constantes.distances_collections;
import static ao.co.r4c.helper.Constantes.todasInformacoesMotorista;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.ChatMesssageActivity;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.Avaliacao;
import ao.co.r4c.model.MotoristaInfo;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.model.UsuariosActivo;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverInformationsDialogFragment extends DialogFragment {

    private static final int REQUEST_CALL = 1;
    CircleImageView img_profile;
    TextView txt_nome, txt_distancia, txt_avaliacao, txt_telefone;
    Button btn_call;
    ImageButton imageButton_message;
    ImageButton imageButton_call;

    List<ImageView> imageViewList;
    ImageView img_star_1, img_star_2, img_star_3, img_star_4, img_star_5;

    String telefone_motorista;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_driver_info_dialog, container, false);

        //Instanciating the components
        //img_profile = view.findViewById(R.id.img_profile);
        txt_nome = view.findViewById(R.id.txt_nome);
        txt_distancia = view.findViewById(R.id.txt_distancia);
        txt_telefone = view.findViewById(R.id.txt_telefone);
        txt_avaliacao = view.findViewById(R.id.txt_avaliacao);
        btn_call = view.findViewById(R.id.btn_call);

        txt_nome.setText("Nome: " + MotoristaInfo.nome);
        txt_distancia.setText("Distância: " + MotoristaInfo.distancia);

        img_star_1 = view.findViewById(R.id.star_1);
        img_star_2 = view.findViewById(R.id.star_2);
        img_star_3 = view.findViewById(R.id.star_3);
        img_star_4 = view.findViewById(R.id.star_4);
        img_star_5 = view.findViewById(R.id.star_5);

        imageViewList = new ArrayList<>();
        imageViewList.add(img_star_1);
        imageViewList.add(img_star_2);
        imageViewList.add(img_star_3);
        imageViewList.add(img_star_4);
        imageViewList.add(img_star_5);


        imageButton_call = view.findViewById(R.id.img_phone);
        imageButton_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(telefone_motorista);
            }
        });


        imageButton_message = view.findViewById(R.id.img_sms);
        imageButton_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChatMesssageActivity.class);
                intent.putExtra("id", Constantes.id_motorista);
                intent.putExtra("username", MotoristaInfo.nome);
                intent.putExtra("telefone", telefone_motorista);

                startActivity(intent);
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UsuariosActivo usuariosActivo = todasInformacoesMotorista(Integer.parseInt(MotoristaInfo.id_motorista), usuariosActivoArrayList);
                String distancia = distances_collections.get(Integer.parseInt(MotoristaInfo.id_motorista)).toString();

                solicitarMotorista(Objects.requireNonNull(usuariosActivo), distancia);

                dismiss();
            }
        });

        carregarAvaliacao();

        return view;
    }


    /*Make the pick up request*/
    private void solicitarMotorista(UsuariosActivo motoristaActivo, String distancia) {

        Usuario passageiro_activo = SharedPreferenceManager.getInstance(getContext()).getUsuario();

        Constantes.id_motorista = motoristaActivo.getId();

        String origem = txt_origem_pesquisa.getText().toString();
        String destino = txt_destino_pesquisa.getText().toString();

        int id_origem = itens_places.get(origem.trim());
        int id_destino = itens_places.get(destino.trim());

        Constantes.socketClient.getSocket().emit("call_driver",
                motoristaActivo.getId(),
                passageiro_activo.getId(),
                passageiro_activo.getNome(),
                passageiro_activo.getTelefone(),
                id_origem,
                id_destino,
                origem,
                destino,
                distancia
        );

    }


    private void carregarAvaliacao() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        Call<Avaliacao> call = apiInterface.carregarDadosAvaliacao(Integer.parseInt(MotoristaInfo.id_motorista));

        call.enqueue(new Callback<Avaliacao>() {
            @Override
            public void onResponse(@NonNull Call<Avaliacao> call, @NonNull Response<Avaliacao> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    Avaliacao avaliacao = response.body();

                    Double media = (double) (Integer.valueOf(Objects.requireNonNull(avaliacao).getSoma()) / Objects.requireNonNull(avaliacao).getQuantidade());

                    Double percentagem = (media * 100) / Constantes.TOTAL_ESTRELAS;
                    txt_avaliacao.setText("Avaliação: " + percentagem + "%");
                    Constantes.carregarEstrelasDeAvaliacao(percentagem, imageViewList);

                    txt_telefone.setText("Telefone: " + avaliacao.getTelefone());
                    telefone_motorista = avaliacao.getTelefone();


                } else if (response.code() == 503) {

                    Toast.makeText(getContext(),
                            "Erro inesperado.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Avaliacao> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),
                        "Erro! " + t.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    private void callPhone(String telefone) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefone));


        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else
            startActivity(callIntent);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(telefone_motorista);
            }
        }
    }

}
