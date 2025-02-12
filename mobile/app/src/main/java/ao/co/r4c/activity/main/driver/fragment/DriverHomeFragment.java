package ao.co.r4c.activity.main.driver.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.GeoApiContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.ChatMesssageActivity;
import ao.co.r4c.activity.main.CostTripActivity;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.model.UsuariosActivo;
import ao.co.r4c.model.Viagem;
import ao.co.r4c.model.google.Directions;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiGoogle;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverHomeFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        com.google.android.gms.location.LocationListener {

    private static final float DEFAULT_ZOOM = 16;
    private static final int REQUEST_CALL = 1;
    public static CountDownTimer countDownTimer;
    public static String telefone_passageiro;
    public static SupportMapFragment supportMapFragment;
    public static CardView cardView_bottom_sheet_rider_customer;
    public static Button btn_search_customer;
    public static TextView txt_origem;
    public static TextView txt_destino;
    public static TextView txt_nome;
    public static TextView txt_telefone;
    Context context;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    GeoApiContext geoApiContext = null;
    ImageButton btn_timer;
    ImageButton imageButton_call;
    ImageButton imageButton_message;
    ApiInterface googleAPI;
    long timeWhenStopped = 0;
    ArrayList<UsuariosActivo> usuariosActivoArrayList;
    View view_recorder;
    String id_passageiro;
    String nome;
    String telefone;
    String origem;
    String destino;
    String distancia;
    String id_origem_passageiro;
    String id_destino_passageiro;
    String tempo;
    TextView txt_speed;
    String id_trip;
    Date tempo_inicio;
    Date tempo_termino;
    Double preco;
    Chronometer txt_chronometer;
    TextView txt_tempo_restante, txt_alcance;
    boolean visivel_recorder = false;
    private boolean timerRunning = true;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_driver, container, false);
        this.context = getActivity();

        if (mMap == null) {
            supportMapFragment.getMapAsync(this);
        }

        cardView_bottom_sheet_rider_customer = view.findViewById(R.id.car_view_customer_informations);
        cardView_bottom_sheet_rider_customer.setVisibility(CardView.INVISIBLE);

        txt_nome = view.findViewById(R.id.txt_nome_passageiro);
        txt_origem = view.findViewById(R.id.txt_origem);
        txt_destino = view.findViewById(R.id.txt_destino);
        txt_telefone = view.findViewById(R.id.txt_telefone);
        btn_timer = view.findViewById(R.id.btn_timer);
        txt_speed = view.findViewById(R.id.txt_speed);
        txt_alcance = view.findViewById(R.id.txt_alcance);
        txt_tempo_restante = view.findViewById(R.id.txt_tempo_restante);
        view_recorder = view.findViewById(R.id.view_recorder);
        txt_chronometer = view.findViewById(R.id.txt_chronometer);

        txt_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (!visivel_recorder) {
                    view_recorder.setBackgroundResource(R.drawable.red_notification_border);
                    visivel_recorder = true;
                } else {
                    view_recorder.setBackgroundResource(R.drawable.white_notification_border);
                    visivel_recorder = false;
                }
            }
        });

        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constantes.NIVEL_VIAGEM > 0) {
                    if (!timerRunning) {
                        txt_chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                        txt_chronometer.start();
                        timerRunning = true;
                        btn_timer.setImageResource(R.drawable.ic_pause);

                    } else {
                        timeWhenStopped = txt_chronometer.getBase() - SystemClock.elapsedRealtime();
                        txt_chronometer.stop();
                        timerRunning = false;
                        btn_timer.setImageResource(R.drawable.ic_play);
                    }
                }
            }
        });


        imageButton_call = view.findViewById(R.id.img_phone);
        imageButton_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(telefone_passageiro);
            }
        });

        imageButton_message = view.findViewById(R.id.img_sms);
        imageButton_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatMesssageActivity.class);
                intent.putExtra("id", Integer.parseInt(id_passageiro));
                intent.putExtra("username", txt_nome.getText());
                intent.putExtra("telefone", txt_telefone.getText());

                startActivity(intent);
            }
        });


        btn_search_customer = view.findViewById(R.id.btn_search_driver);
        btn_search_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (Constantes.NIVEL_VIAGEM) {
                    case 0:

                        //Quando ainda não recebeu nenhuma solicitação
                        Toast.makeText(getActivity(), "Procurando passageiro ...", Toast.LENGTH_SHORT).show();

                        Usuario usuario = SharedPreferenceManager.getInstance(context).getUsuario();

                        UsuariosActivo usuariosActivo = new UsuariosActivo(
                                usuario.getId(),
                                usuario.getNome(),
                                -8.823693,
                                13.245404
                        );

                        //Update only if the driver is working on
                        if (Constantes.ESTADO_MOTORISTA == 1)
                            Constantes.socketClient.updateDriverState(usuariosActivo);

                        break;

                    case 1:


                        //Quando encontrou e precisa iniciar a viagem

                        btn_search_customer.setText("Viagem completa");
                        Constantes.NIVEL_VIAGEM = 2;

                        tempo_inicio = Calendar.getInstance().getTime();

                        txt_chronometer.setBase(SystemClock.elapsedRealtime());
                        txt_chronometer.stop();
                        txt_chronometer.start();


                        btn_timer.setImageResource(R.drawable.ic_pause);


                        break;

                    case 2:

                        //Viagem terminada, enviar a tela dos gastos


                        Constantes.NIVEL_VIAGEM = 0;
                        txt_chronometer.stop();

                        tempo_termino = Calendar.getInstance().getTime();

                        tempo = String.valueOf(Constantes.calcularMinutoViagem(txt_chronometer.getBase()));


                        preco = Constantes.calcularPrecoViagem(Double.parseDouble(distancia), Integer.parseInt(tempo));

                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                        Call<Viagem> viagemCall = apiInterface.terminarViagem(Integer.parseInt(id_trip), preco);

                        viagemCall.enqueue(new Callback<Viagem>() {
                            @Override
                            public void onResponse(@NonNull Call<Viagem> call, @NonNull Response<Viagem> response) {
                                //Abrir outra tela caso a conta for activada com sucesso
                                if (response.isSuccessful() && response.code() == 201) {

                                    Constantes.socketClient.getSocket().emit("rider_completed", id_passageiro);

                                    Intent intent = new Intent(context, CostTripActivity.class);
                                    intent.putExtra("tempo", tempo);
                                    intent.putExtra("origem", origem);
                                    intent.putExtra("destino", destino);
                                    intent.putExtra("distancia", distancia);
                                    intent.putExtra("preco", preco.toString());

                                    startActivity(intent);
                                } else {
                                    Toast.makeText(context, "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Viagem> call, Throwable t) {

                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                }
            }
        });


        Constantes.socketClient.getSocket().emit("call_driver").on("call_driver", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject object = (JSONObject) args[0];

                        if (!args[0].toString().equals("[]") && Constantes.NIVEL_VIAGEM == 0 && Constantes.ESTADO_CHAMADA == 0) {

                            try {

                                id_passageiro = object.getString("id_passageiro");
                                nome = object.getString("nome");
                                telefone = object.getString("telefone");

                                id_origem_passageiro = object.getString("id_origem");
                                id_destino_passageiro = object.getString("id_destino");

                                origem = object.getString("origem");
                                destino = object.getString("destino");
                                distancia = object.getString("distancia");

                                Intent intent = new Intent(context, CustomerCall.class);
                                intent.putExtra("id_passageiro", id_passageiro);
                                intent.putExtra("nome", nome);
                                intent.putExtra("telefone", telefone);
                                intent.putExtra("origem", origem);
                                intent.putExtra("destino", destino);
                                intent.putExtra("distancia", distancia);

                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });


        Constantes.socketClient.getSocket().emit("trip_created").on("trip_created", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject object = (JSONObject) args[0];

                        if (!args[0].toString().equals("[]")) {

                            try {
                                id_trip = object.getString("id_viagem");
                                //Toast.makeText(context, "Recebeu. Id = " + id_trip, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });


        Constantes.socketClient.getSocket().emit("cancelar_viagem").on("cancelar_viagem", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject object = (JSONObject) args[0];

                        if (!args[0].toString().equals("[]")) {

                            try {

                                Toast.makeText(context, "Viagem cancelada.", Toast.LENGTH_SHORT).show();
                                Constantes.NIVEL_VIAGEM = 0;

                                Intent intent = new Intent(getContext(), DriverMapsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                getActivity().finish();

                                btn_search_customer.setText("Procurar passageiro");

                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });


        return view;
    }


    private void listarUsuariosActivos() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ArrayList<UsuariosActivo>> listCall = apiInterface.listarUsuariosActivos();

        listCall.enqueue(new Callback<ArrayList<UsuariosActivo>>() {
            @Override
            public void onResponse(Call<ArrayList<UsuariosActivo>> call, Response<ArrayList<UsuariosActivo>> response) {
                //Abrir outra tela caso a conta for activada com sucesso
                if (response.isSuccessful() && response.code() == 201) {

                    usuariosActivoArrayList = response.body();

                    for (UsuariosActivo usuario : usuariosActivoArrayList) {

                        Location latLng = new Location("Motorista");
                        latLng.setLatitude(usuario.getLatitude());
                        latLng.setLongitude(usuario.getLongitude());

                        double distancia = lastLocation.distanceTo(latLng);

                        LatLng latLng_m = new LatLng(usuario.getLatitude(), usuario.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng_m).title(usuario.getNome() + " " + (Math.round(distancia))));

                    }

                } else {
                    Toast.makeText(getActivity(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UsuariosActivo>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        /*
        if(encontrou==false){
            raio+=100;
            listarUsuariosActivos();
        }*/
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Toast.makeText(context, "Funcionando", Toast.LENGTH_SHORT).show();
        buildApiClient();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void getDuration(Location location_1, Location location_2) {

        googleAPI = ApiGoogle.getApiClient().create(ApiInterface.class);

        Call<Directions> call = googleAPI.informacoesCaminhos(
                "driving",
                "less_driving",
                location_1.getLatitude() + "," + location_1.getLongitude(),
                location_2.getLatitude() + "," + location_2.getLongitude(),
                getResources().getString(R.string.google_api_key)
        );

        call.enqueue(new Callback<Directions>() {
            @Override
            public void onResponse(Call<Directions> call, Response<Directions> response) {

                String duracao = null;
                String endereco = null;
                if (response.body() != null) {
                    duracao = response.body().getRoutes().get(0).getLegs().get(0).getDuration().getText();
                    Constantes.tempo_distancia = duracao;

                    endereco = response.body().getRoutes().get(0).getLegs().get(0).getStart_address();
                    Constantes.endereco_passageiro = endereco;

                    Intent intent = new Intent(context, CustomerCall.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Directions> call, Throwable t) {
                Toast.makeText(context, "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected synchronized void buildApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            this.lastLocation = location;

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            Usuario usuario = SharedPreferenceManager.getInstance(context).getUsuario();

            UsuariosActivo usuariosActivo = new UsuariosActivo(
                    usuario.getId(),
                    usuario.getNome(),
                    location.getLatitude(),
                    location.getLongitude()
            );

            String velocidade = String.valueOf(location.getSpeed()).substring(0, String.valueOf(location.getSpeed()).lastIndexOf('.') + 2);
            txt_speed.setText(velocidade + " m/s");

            if (Constantes.NIVEL_VIAGEM == 2) {
                Float distance = Float.parseFloat(distancia);
                txt_alcance.setText(distancia);
                txt_tempo_restante.setText(distance / location.getSpeed() + " min");
            }


            //Update only if the driver is working on
            if (Constantes.ESTADO_MOTORISTA == 1)
                Constantes.socketClient.updateDriverState(usuariosActivo);

        } catch (Exception e) {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void callPhone(String telefone) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefone));


        if (ActivityCompat.checkSelfPermission(context,
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
                callPhone(telefone);
            }
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}