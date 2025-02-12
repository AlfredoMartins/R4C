package ao.co.r4c.activity.main.customer.fragment;

import static android.support.constraint.Constraints.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arsy.maps_library.MapRipple;
import com.github.nkzawa.emitter.Emitter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ao.co.r4c.R;
import ao.co.r4c.activity.main.ChatMesssageActivity;
import ao.co.r4c.activity.main.ThanksActivity;
import ao.co.r4c.activity.main.driver.fragment.DriverInformationsDialogFragment;
import ao.co.r4c.adapter.AutoCompleteDestinationAdapter;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.helper.CustomInfoWindow;
import ao.co.r4c.model.Local;
import ao.co.r4c.model.LocalItem;
import ao.co.r4c.model.MotoristaInfo;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.model.UsuariosActivo;
import ao.co.r4c.model.Viagem;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.ApiInterface;
import ao.co.r4c.storage.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHomeFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnInfoWindowClickListener {

    private static final float DEFAULT_ZOOM = 16f;
    private static final int REQUEST_CALL = 1;
    public static List<LocalItem> localItemList;
    public static List<Local> locaisList;
    public static ArrayList<UsuariosActivo> usuariosActivoArrayList;
    public static SupportMapFragment supportMapFragment;
    public static Button btn_call_driver;
    public static AutoCompleteTextView txt_origem_pesquisa, txt_destino_pesquisa;
    public static UsuariosActivo activo_proximo;
    public static String telefone_motorista;
    public static HashMap<String, Integer> itens_places;
    /*Verify if is there a avalible driver*/
    private final boolean encontrou = false;
    public CardView cardView_bottom_sheet_rider_driver;
    Context context;
    Location lastLocation;
    LocationRequest locationRequest;
    ImageView img_expandable;
    ImageButton imageButton_message;
    AutoCompleteTextView editText;
    MapRipple mapRipple;
    ImageButton imageButton_call;
    Marker my_marker;
    AutoCompleteDestinationAdapter adapter = null;
    int id_viagem;
    List<String> places;
    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;
    private TextView txt_nome_motorista;
    private TextView txt_telefone_motorista;

    public static int pesquisaSequecial(ArrayList<LocalItem> localItemList, String text) {

        for (int i = 0; i < localItemList.size(); i++)
            if (localItemList.get(i).getDestinationName().equalsIgnoreCase(text.trim()))
                return i;

        return -1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_customer, container, false);
        this.context = getActivity();

        cardView_bottom_sheet_rider_driver = view.findViewById(R.id.card_view_driver_informations);
        cardView_bottom_sheet_rider_driver.setVisibility(View.INVISIBLE);

        txt_nome_motorista = view.findViewById(R.id.txt_nome_motorista);
        txt_telefone_motorista = view.findViewById(R.id.txt_telefone);

        txt_origem_pesquisa = view.findViewById(R.id.input_search_from);
        txt_destino_pesquisa = view.findViewById(R.id.input_search_destination);

        listarTodosLocais();

        if (mMap == null) {
            Objects.requireNonNull(supportMapFragment).getMapAsync(this);
        }


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

                Intent intent = new Intent(context, ChatMesssageActivity.class);
                intent.putExtra("id", Constantes.id_motorista);
                intent.putExtra("username", txt_nome_motorista.getText());
                intent.putExtra("telefone", txt_telefone_motorista.getText());

                startActivity(intent);
            }
        });

        btn_call_driver = view.findViewById(R.id.btn_search_driver);

        if (!SharedPreferenceManager.getInstance(getContext()).getSettings().isMotorista_mais_proximo()) {
            btn_call_driver.setText("Procurar motorista");

        }

        txt_origem_pesquisa.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    txt_destino_pesquisa.requestFocus();

                    return true;
                }

                return false;
            }
        });

        /*Button click event*/
        btn_call_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Validating the fields*/
                String origem = txt_origem_pesquisa.getText().toString().trim();
                String destino = txt_destino_pesquisa.getText().toString().trim();

                if (origem.isEmpty()) {
                    txt_origem_pesquisa.setError("Campo obrigatório.");
                    txt_origem_pesquisa.requestFocus();
                    return;
                }

                if (destino.isEmpty()) {
                    txt_destino_pesquisa.setError("Campo obrigatório.");
                    txt_destino_pesquisa.requestFocus();
                    return;
                }

                /*Verify, if both fields are equals*/
                if (origem.equals(destino)) {
                    txt_destino_pesquisa.setError("Trajectória inválida. Especifique outro destino.");
                    txt_destino_pesquisa.requestFocus();
                    return;
                }

                /*Verify if the address exists and is avalibles*/
                if (!places.contains(origem)) {
                    txt_origem_pesquisa.setError("Local inválido.");
                    txt_origem_pesquisa.requestFocus();
                    return;
                }

                /*Verify if the address exists and is avalibles*/
                if (!places.contains(destino)) {
                    txt_destino_pesquisa.setError("Local inválido.");
                    txt_destino_pesquisa.requestFocus();
                    return;
                }

                switch (Constantes.NIVEL_VIAGEM) {
                    case 0:

                        Constantes.socketClient.getSocket().emit("drivers_avalibles").on("drivers_avalibles", new Emitter.Listener() {
                            @Override
                            public void call(final Object... args) {

                                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (!args[0].toString().equals("[]")) {

                                            try {
                                                usuariosActivoArrayList = new ArrayList<>();

                                                JSONArray jsonArray = new JSONArray(args[0].toString());
                                                for (int i = 0; i < jsonArray.length(); i++)
                                                    if (!jsonArray.get(i).toString().equals("null")) {

                                                        JSONObject object = jsonArray.optJSONObject(i);

                                                        UsuariosActivo usuariosActivo = new UsuariosActivo(
                                                                i,
                                                                object.getString("nome"),
                                                                Double.valueOf(object.getString("latitude")),
                                                                Double.valueOf(object.getString("longitude"))
                                                        );

                                                        usuariosActivoArrayList.add(usuariosActivo);

                                                        //Toast.makeText(context, "Vector de motoristas preenchido!", Toast.LENGTH_SHORT).show();
                                                    }

                                                marcarMinhaLocalizacao();

                                            } catch (Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else
                                            Toast.makeText(context, "Nenhum motorista disponível ...", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }
                        });


                        break;

                    case 1:
                        //Cancelar viagem


                        /*Instanciate the ApiInterface*/
                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                        /*Call the interface*/
                        Call<Viagem> call = apiInterface.cancelarViagem(id_viagem);

                        /*Enqueue*/
                        call.enqueue(new Callback<Viagem>() {
                            @Override
                            public void onResponse(Call<Viagem> call, @NonNull Response<Viagem> response) {
                                if (response.isSuccessful() && response.code() == 201) {

                                    btn_call_driver.setText("Chamar motorista");
                                    Constantes.NIVEL_VIAGEM = 0;

                                    Toast.makeText(context, "Viagem cancelada !", Toast.LENGTH_SHORT).show();

                                    Constantes.socketClient.getSocket().emit("cancelar_viagem", Constantes.id_motorista);


                                    Intent intent = new Intent(getContext(), CustomerMapsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    getActivity().finish();

                                } else {
                                    Toast.makeText(getActivity(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Viagem> call, Throwable t) {
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                }

            }
        });


        Constantes.socketClient.getSocket().emit("driver_answer").on("driver_answer", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject object = (JSONObject) args[0];

                        if (!args[0].toString().equals("[]")) {

                            try {

                                String resposta = object.getString("answer");

                                if (resposta.equals("Yes")) {
                                    String nome = object.getString("nome");
                                    String telefone = object.getString("telefone");

                                    txt_nome_motorista.setText(nome);
                                    txt_telefone_motorista.setText(telefone);
                                    telefone_motorista = telefone;

                                    cardView_bottom_sheet_rider_driver.setVisibility(View.VISIBLE);

                                    final Usuario usuario = SharedPreferenceManager.getInstance(context).getUsuario();

                                    int id_motorista = Constantes.id_motorista;
                                    int id_passageiro = usuario.getId();

                                    int id_origem = itens_places.get(txt_origem_pesquisa.getText().toString().trim());
                                    int id_destino = itens_places.get(txt_destino_pesquisa.getText().toString().trim());

                                    /*Instanciate the ApiInterface*/
                                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                                    /*Call the interface*/
                                    Call<Viagem> call = apiInterface.inserirViagem(id_motorista, id_passageiro, id_origem, id_destino);

                                    /*Enqueue*/
                                    call.enqueue(new Callback<Viagem>() {
                                        @Override
                                        public void onResponse(Call<Viagem> call, @NonNull Response<Viagem> response) {
                                            if (response.isSuccessful() && response.code() == 201) {

                                                btn_call_driver.setText("Cancelar");
                                                Constantes.NIVEL_VIAGEM = 1;

                                                id_viagem = Objects.requireNonNull(response.body()).getId();

                                                //Toast.makeText(context, "Viagem criada com sucesso. Id = " + id_viagem, Toast.LENGTH_SHORT).show();

                                                Constantes.socketClient.getSocket().emit("trip_created",
                                                        activo_proximo.getId(),
                                                        usuario.getId(),
                                                        id_viagem);

                                            } else {
                                                Toast.makeText(getActivity(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Viagem> call, Throwable t) {
                                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                } else {
                                    Toast.makeText(context, "Solicitação negada.", Toast.LENGTH_SHORT).show();

                                    cardView_bottom_sheet_rider_driver.setVisibility(View.INVISIBLE);
                                }

                            } catch (JSONException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });


        Constantes.socketClient.getSocket().emit("rider_completed").on("rider_completed", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!args[0].toString().equals("[]")) {
                            try {
                                Intent intent = new Intent(context, ThanksActivity.class);
                                intent.putExtra("id_viagem", id_viagem);
                                startActivity(intent);
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

    private boolean exite(ArrayList<LocalItem> localItemList, String text) {

        for (LocalItem item : localItemList)
            if (item.getDestinationName().trim().equals(text))
                return true;

        return false;
    }

    /*Fills the above array list of locations avalible to trip*/
    private void fillDestiantionList() {
        localItemList = new ArrayList<>();
        localItemList.add(new LocalItem("1º de Maio"));
        localItemList.add(new LocalItem("Rocha-Pinto"));
        localItemList.add(new LocalItem("Gamek"));
        localItemList.add(new LocalItem("Murro Bento"));
        localItemList.add(new LocalItem("Mutamba"));
        localItemList.add(new LocalItem("Viana"));
        localItemList.add(new LocalItem("Cacuaco"));
        localItemList.add(new LocalItem("Avenida"));
        localItemList.add(new LocalItem("Benfica"));
        localItemList.add(new LocalItem("Prenda"));
        localItemList.add(new LocalItem("Maianga"));
        localItemList.add(new LocalItem("Maianga"));
    }

    /*Make the pick up request*/
    private void solicitarMotorista(UsuariosActivo motoristaActivo, String distancia) {

        Usuario passageiro_activo = SharedPreferenceManager.getInstance(context).getUsuario();

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

    /*Indentify the customer current place into the map*/
    private void marcarMinhaLocalizacao() {

        /*If the radius animations is running, it's going to stop, 'cause of thread*/
        if (mapRipple != null && mapRipple.isAnimationRunning())
            mapRipple.stopRippleMapAnimation();

        /*If the customer marker already exists, it's desapire*/
        if (my_marker != null)
            my_marker.remove();

        /*Get the current location*/
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

        Usuario usuario = SharedPreferenceManager.getInstance(context).getUsuario();

        /*Sinalize on the map, the customer marker*/
        my_marker = mMap.addMarker(new MarkerOptions()
                .snippet(usuario.getTelefone())
                .position(latLng).
                        title("Eu")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        my_marker.showInfoWindow();

        /*Animate the camera*/
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        /*Make the animation when the costumer request  a car*/
        mapRipple = new MapRipple(mMap, latLng, context);
        mapRipple.withNumberOfRipples(1);
        mapRipple.withDistance(800);
        mapRipple.withRippleDuration(1000);
        mapRipple.withTransparency(0.5f);

        /*Start the radius animation*/
        //mapRipple.startRippleMapAnimation();


        if (!SharedPreferenceManager.getInstance(context).getSettings().isMotorista_mais_proximo()) {
            searchForActiveDrivers();
        } else {
            /*Enable the calling driver*/
            //btn_call_driver.setText("Chamando Motorista ...");

            /*Call a function that returns all close drivers*/
            listActiveDrivers();
        }
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
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindow(context));
        mMap.setOnInfoWindowClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
                -8.823693,
                13.245404)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    /*Setting function*/
    protected synchronized void buildApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    private void searchForActiveDrivers() {

        /*Get all the distances between driver and customer*/
        Constantes.distances_collections = new HashMap<>();

        /*This struture, put marker in map, each momment find a driver working and close*/
        for (UsuariosActivo usuario : usuariosActivoArrayList) {

            Location latLng = new Location("Motorista");

            /*Getting the latitude and the longitude*/
            latLng.setLatitude(usuario.getLatitude());
            latLng.setLongitude(usuario.getLongitude());

            /*Get the distance between the customer and driver*/
            Float distancia = lastLocation.distanceTo(latLng);

            LatLng latLng_m = new LatLng(usuario.getLatitude(), usuario.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .snippet(Math.round(distancia) + " metros")
                    .position(latLng_m).
                            title(usuario.getId() + "-" + usuario.getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car_driver)));
        }
    }

    private void listActiveDrivers() {
        /*Get all the distances between driver and customer*/
        HashMap<Integer, Float> distances_collections = new HashMap<>();

        /*Get the closest driver*/
        activo_proximo = usuariosActivoArrayList.get(0);

        Location latLng = new Location("Motorista");

        /*Getting the latitude and the longitude*/
        latLng.setLatitude(activo_proximo.getLatitude());
        latLng.setLongitude(activo_proximo.getLongitude());

        Float distance_proximo = lastLocation.distanceTo(latLng);

        /*This struture, put marker in map, each momment find a driver working and close*/
        for (UsuariosActivo usuario : usuariosActivoArrayList) {

            latLng = new Location("Motorista");

            /*Getting the latitude and the longitude*/
            latLng.setLatitude(usuario.getLatitude());
            latLng.setLongitude(usuario.getLongitude());

            /*Get the distance between the customer and driver*/
            Float distancia = lastLocation.distanceTo(latLng);
            distances_collections.put(usuario.getId(), distancia);

            /*Get the closest distance*/
            if (distancia < distance_proximo) {
                activo_proximo = usuario;
                distance_proximo = distancia;
            }
        }

        /*Sent pickup resquest to closest driver*/
        if (!distances_collections.isEmpty()) {
            try {

                LatLng latLng_m = new LatLng(activo_proximo.getLatitude(), activo_proximo.getLongitude());

                mMap.addMarker(new MarkerOptions()
                        .snippet(Math.round(distance_proximo) + " metros")
                        .position(latLng_m).
                                title(activo_proximo.getId() + "-" + activo_proximo.getNome())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car_driver)));

                solicitarMotorista(activo_proximo, distance_proximo.toString());

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*List all avalible places*/
    private void listarTodosLocais() {

        /*Instanciate the ApiInterface*/
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        /*Call the interface*/
        Call<ArrayList<Local>> listCall = apiInterface.listarLocais();

        /*Enqueue*/
        listCall.enqueue(new Callback<ArrayList<Local>>() {
            @Override
            public void onResponse(Call<ArrayList<Local>> call, Response<ArrayList<Local>> response) {
                if (response.isSuccessful() && response.code() == 201) {

                    /*Get the response body json object*/
                    locaisList = response.body();
                    localItemList = new ArrayList<>();
                    places = new ArrayList<>();
                    itens_places = new HashMap<String, Integer>();

                    for (Local item : locaisList
                    ) {
                        localItemList.add(new LocalItem(item.getNome().trim()));
                        places.add(item.getNome().trim());
                        itens_places.put(item.getNome().trim(), item.getId());
                        //Toast.makeText(context, item.getNome(), Toast.LENGTH_SHORT).show();
                    }

                    try {
                        adapter = new AutoCompleteDestinationAdapter(context, localItemList);
                        txt_origem_pesquisa.setAdapter(adapter);
                        txt_destino_pesquisa.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Erro inesperado: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Local>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*Called each momment the location change*/
    @Override
    public void onLocationChanged(Location location) {

        try {


            /*Gets the last customer location*/
            this.lastLocation = location;

            /*Convert location class to Latitude and Longitude*/
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        /*
        mMap.addMarker(new MarkerOptions()
                .snippet(SharedPreferenceManager.getInstance(context).getUsuario().getTelefone())
                .position(latLng).
                        title("Eu")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
*/

            Usuario usuario = SharedPreferenceManager.getInstance(context).getUsuario();

            UsuariosActivo usuariosActivo = new UsuariosActivo(
                    usuario.getId(),
                    usuario.getNome(),
                    location.getLatitude(),
                    location.getLongitude()
            );


            //Update only if the driver is working on
            if (Constantes.ESTADO_PASSAGEIRO == 1)
                Constantes.socketClient.updateCustomerState(usuariosActivo);


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

    public void refresh() {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(supportMapFragment).getMapAsync(this);
    }

    /*It's also a setting function, called when the google server is connected with the app*/
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

    @Override
    public void onInfoWindowClick(Marker marker) {

        if (!marker.getTitle().equals("Eu")) {

            //MotoristaInfo.setId_motorista(Constantes.getId(marker.getTitle()));
            MotoristaInfo.nome = (Constantes.getName(marker.getTitle()));
            MotoristaInfo.distancia = (marker.getSnippet());
            MotoristaInfo.id_motorista = (Constantes.getId(marker.getTitle()));

            try {

                //Calling the dialog information driver fragment
                DriverInformationsDialogFragment dialogFragment = new DriverInformationsDialogFragment();
                dialogFragment.setTargetFragment(this, 1);
                dialogFragment.show(getFragmentManager(), "Informações");

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
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
                callPhone(telefone_motorista);
            }
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.colorGray));
                    polyline.setClickable(true);

                }
            }
        });
    }
}