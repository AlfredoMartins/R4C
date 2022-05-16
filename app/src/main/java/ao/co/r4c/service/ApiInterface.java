package ao.co.r4c.service;

import java.util.ArrayList;

import ao.co.r4c.model.Avaliacao;
import ao.co.r4c.model.ChatMessage;
import ao.co.r4c.model.Comentario;
import ao.co.r4c.model.Embolso;
import ao.co.r4c.model.Estatistica;
import ao.co.r4c.model.HistoricoViagem;
import ao.co.r4c.model.Image;
import ao.co.r4c.model.Local;
import ao.co.r4c.model.MessagesResponse;
import ao.co.r4c.model.NexmoSMS;
import ao.co.r4c.model.Noticia;
import ao.co.r4c.model.Solicitacao;
import ao.co.r4c.model.TelcoSMS;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.model.UsuariosActivo;
import ao.co.r4c.model.Viagem;
import ao.co.r4c.model.google.Directions;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/login.php")
    Call<Usuario> iniciarSessao(
            @Field("email") String email,
            @Field("senha") String senha
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/existencia.php")
    Call<Usuario> existencia(
            @Field("email") String email,
            @Field("telefone") String telefone
    );


    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/listar_historico_viagens.php")
    Call<ArrayList<Viagem>> listarHistoricoViagens(
            @Field("id_motorista") int id_motorista
    );


    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/listar_historico_viagens_passageiro.php")
    Call<ArrayList<Viagem>> listarHistoricoViagensPassageiro(
            @Field("id_passageiro") int id_passageiro
    );


    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/estatistica.php")
    Call<Estatistica> dadosEstatisticos(
            @Field("id_motorista") int id_motorista
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/estatistica_passageiro.php")
    Call<Estatistica> dadosEstatisticosPassageiro(
            @Field("id_passageiro") int id_passageiro
    );


    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/listar_historico_todas_viagens.php")
    Call<ArrayList<HistoricoViagem>> listarHistoricoTodasViagens(
            @Field("id_motorista") int id_motorista
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/listar_historico_todas_viagens_passageiro.php")
    Call<ArrayList<HistoricoViagem>> listarHistoricoTodasViagensPassageiro(
            @Field("id_passageiro") int id_passageiro
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/embolso.php")
    Call<Embolso> carregarEmbolso(
            @Field("id_motorista") int id_motorista
    );


    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/embolso_passageiro.php")
    Call<Embolso> carregarEmbolsoPassageiro(
            @Field("id_passageiro") int id_passageiro
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/dados_avaliacao.php")
    Call<Avaliacao> carregarDadosAvaliacao(
            @Field("id_motorista") int id_motorista
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/signup.php")
    Call<Usuario> cadastrarUsuario(
            @Field("nome") String nome,
            @Field("sobrenome") String sobrenome,
            @Field("email") String email,
            @Field("senha") String senha,
            @Field("telefone") String telefone,
            @Field("id_categoria") int id_categoria
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/activar_conta.php")
    Call<Usuario> activarConta(@Field("telefone") String telefone, @Field("email") String email);

    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/alterar_senha.php")
    Call<Usuario> alterarSenha(
            @Field("email") String email,
            @Field("telefone") String telefone,
            @Field("senha") String senha
    );


    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/actualizar_perfil.php")
    Call<Usuario> actualizarPerfil(
            @Field("id") int id,
            @Field("senha") String senha
    );

    @POST("send_message.php")
    Call<TelcoSMS> enviarMensagemTelco(@Body TelcoSMS telcoSMS);


    @FormUrlEncoded
    @POST("sms/json")
    Call<NexmoSMS> enviarMensagemNexmo(
            @Field("api_key") String api_key,
            @Field("api_secret") String api_secret,
            @Field("from") String from,
            @Field("to") String to,
            @Field("text") String text
    );


    @GET("r4c/api/objects/locais/listar_locais.php")
    Call<ArrayList<Local>> listarLocais();


    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/inserir_viagem.php")
    Call<Viagem> inserirViagem(
            @Field("id_motorista") int id_motorista,
            @Field("id_passageiro") int id_passageiro,
            @Field("id_local_origem") int id_local_origem,
            @Field("id_local_destino") int id_local_destino
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/terminar_viagem.php")
    Call<Viagem> terminarViagem(
            @Field("id") int id,
            @Field("preco") Double preco
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/carregar_comentarios.php")
    Call<ArrayList<Comentario>> carregar_comentarios(
            @Field("id_motorista") int id_motorista
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/carregar_comentarios_passageiro.php")
    Call<ArrayList<Comentario>> carregar_comentarios_passageiro(
            @Field("id_passageiro") int id_passageiro
    );


    @GET("r4c/api/objects/noticias/carregar_noticias.php")
    Call<ArrayList<Noticia>> carregar_noticias();

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/avaliar_viagem.php")
    Call<Viagem> avaliarViagem(
            @Field("id_viagem") int id,
            @Field("id_avaliacao") int id_avaliacao,
            @Field("descricao") String descricao
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/viagem/cancelar_viagem.php")
    Call<Viagem> cancelarViagem(
            @Field("id_viagem") int id_viagem
    );

    @GET("r4c/api/objects/usuarios/listar_usuarios_activos.php")
    Call<ArrayList<UsuariosActivo>> listarUsuariosActivos();

    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/carregar_dados_passageiro.php")
    Call<Usuario> carregarInformacaoPassageiro(
            @Field("id_passageiro") int id_passageiro
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/upload_image.php")
    Call<Image> uploadImage(
            @Field("id") int id_user,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/mensagem/inserir_mensagem.php")
    Call<ChatMessage> inserirMensagem(
            @Field("id_emissor") int id_emissor,
            @Field("id_receptor") int id_receptor,
            @Field("texto") String texto
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/mensagem/deletar_mensagem.php")
    Call<ChatMessage> deletarMensagem(
            @Field("id") int id_mensagem
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/solicitacoes/verificar_solicitacao.php")
    Call<Solicitacao> verificarSolicitacoes(@Field("id_motorista") int id_motorista);

    @FormUrlEncoded
    @POST("r4c/api/objects/usuarios/atualizar_localizacao.php")
    Call<Usuario> atualizarLocalizacao(
            @Field("id") int id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @FormUrlEncoded
    @POST("r4c/api/objects/mensagem/listar_mensagens.php")
    Call<ArrayList<MessagesResponse>> listarMensagens(
            @Field("user_id") int user_id
    );


    @FormUrlEncoded
    @POST("r4c/api/objects/mensagem/verificar_mensagem.php")
    Call<ArrayList<ChatMessage>> carregar_chat_mensagens(
            @Field("id_emissor") int id_emissor,
            @Field("id_receptor") int id_receptor
    );

    @GET("json")
    Call<Directions> informacoesCaminhos(
            @Query("mode") String mode,
            @Query("transit_routing_preference") String transit_routing_preference,
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );
}