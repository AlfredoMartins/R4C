package ao.co.r4c.activity.main.customer.fragment;

import static ao.co.r4c.activity.main.customer.fragment.CustomerHomeFragment.supportMapFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import ao.co.r4c.R;
import ao.co.r4c.activity.login.LoginActivity;
import ao.co.r4c.activity.main.AboutActivity;
import ao.co.r4c.activity.main.SettingsActivity;
import ao.co.r4c.activity.main.TripsActivity;
import ao.co.r4c.activity.main.UserProfileActivity;
import ao.co.r4c.activity.main.driver.fragment.DriverNewsFragment;
import ao.co.r4c.helper.Constantes;
import ao.co.r4c.model.Usuario;
import ao.co.r4c.onAppKilled;
import ao.co.r4c.service.ApiClient;
import ao.co.r4c.service.SocketClient;
import ao.co.r4c.storage.SharedPreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class CustomerMapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 12345;
    SwitchCompat switchCompat;
    AppBarLayout appBarLayout;
    View view;
    /*Atributos*/
    //CustomerNotificationFragment customerNotificationFragment;
    CustomerNotificationFragment customerNotificationFragment;
    CustomerChatFragment customerChatFragment;
    CustomerRantingsFragment customerRantingsFragment;
    CustomerHomeFragment customerHomeFragment;
    CustomerEarningFragment customerEarningFragment;
    DriverNewsFragment driverNewsFragment;
    CircleImageView circleImageView;
    BottomNavigationViewEx bottomNavigationViewEx;
    FragmentTransaction fragmentTransaction;
    //Configuração do menu
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:

                            if (!supportMapFragment.isResumed())
                                supportMapFragment.onResume();

                            ShowFragment(customerHomeFragment);

                            break;

                        case R.id.nav_embolso:
                            ShowFragment(customerEarningFragment);
                            if (supportMapFragment.isResumed())
                                supportMapFragment.onPause();
                            break;

                        case R.id.nav_chat:
                            ShowFragment(customerChatFragment);

                            if (supportMapFragment.isResumed())
                                supportMapFragment.onPause();
                            break;

                        case R.id.nav_notification:
                            ShowFragment(driverNewsFragment);

                            if (supportMapFragment.isResumed())
                                supportMapFragment.onPause();
                            break;

                        case R.id.nav_favoritos:
                            ShowFragment(customerRantingsFragment);
                            if (supportMapFragment.isResumed())
                                supportMapFragment.onPause();
                            break;
                    }

                    return true;
                }
            };
    private ShareActionProvider shareActionProvider;
    private Boolean permissao = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocationPermission();

        setContentView(R.layout.activity_main_customer);

        try {
            //Instatiate the socket
            Constantes.socketClient = new SocketClient();

            Usuario usuario = SharedPreferenceManager.getInstance(this).getUsuario();
            Constantes.socketClient.connectUser(usuario.getId(), usuario.getId_categoria());

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        startService(new Intent(CustomerMapsActivity.this, onAppKilled.class));

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            appBarLayout = findViewById(R.id.app_layout);

            //Switch user state
            switchCompat = findViewById(R.id.switch_estado);

            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorYellowDark));
                        Constantes.ESTADO_PASSAGEIRO = 1;
                        switchCompat.setText("Online");
                    } else {
                        appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorBlackYellowPrototipo));
                        Constantes.ESTADO_PASSAGEIRO = 0;
                        switchCompat.setText("Offline");
                    }
                }
            });

            //Referenciando os Fragments
            customerHomeFragment = new CustomerHomeFragment();
            customerEarningFragment = new CustomerEarningFragment();
            customerNotificationFragment = new CustomerNotificationFragment();
            customerChatFragment = new CustomerChatFragment();
            customerRantingsFragment = new CustomerRantingsFragment();
            driverNewsFragment = new DriverNewsFragment();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

            Usuario usuario = SharedPreferenceManager.getInstance(this).getUsuario();
            TextView txt_usuario_activo = findViewById(R.id.txt_usuario_ativo);
            txt_usuario_activo.setText(usuario.getNome() + " " + usuario.getSobrenome());

            TextView txt_email_activo = findViewById(R.id.txt_email_activo);
            txt_email_activo.setText(usuario.getEmail());

            TextView txt_numero_activo = findViewById(R.id.txt_numero_activo);
            txt_numero_activo.setText(usuario.getTelefone());

            ImageButton imageButton_settings = findViewById(R.id.img_settings);


            circleImageView = findViewById(R.id.imageView);

            try {
                if (usuario.getFoto_url().isEmpty())
                    circleImageView.setImageResource(R.drawable.img_user_default);
                else
                    Glide.with(this).load(ApiClient.getBaseUrl() + "r4c/api/objects/usuarios/upload_images/" + SharedPreferenceManager.getInstance(this).getUsuario().getId() + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(circleImageView);
            } catch (Exception e) {
                circleImageView.setImageResource(R.drawable.img_user_default);
            }


            imageButton_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustomerMapsActivity.this, SettingsActivity.class);
                    startActivity(intent);

                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            bottomNavigationViewEx = findViewById(R.id.bottom_navigation_badge);
            bottomNavigationViewEx.setOnNavigationItemSelectedListener(navigationItemReselectedListener);
            bottomNavigationViewEx.setSelectedItemId(R.id.nav_home);

            addBadgeAt(2, 5);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private Badge addBadgeAt(int position, int number) {

        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(bottomNavigationViewEx.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                            Toast.makeText(CustomerMapsActivity.this, "Badge removed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

/*
    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPreferenceManager.getInstance(this).sessaoIniciada()) {

            Intent intent = new Intent(CustomerMapsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;

        if (id == R.id.nav_update_profile) {

            intent = new Intent(CustomerMapsActivity.this, UserProfileActivity.class);
            startActivity(intent);

            return false;

        } else if (id == R.id.nav_tripper) {

            try {
                intent = new Intent(CustomerMapsActivity.this, TripsActivity.class);
                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_about) {

            intent = new Intent(CustomerMapsActivity.this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            SharedPreferenceManager.getInstance(this).terminarSessao();
            Constantes.socketClient.disconnectUser(SharedPreferenceManager.getInstance(this).getUsuario().getId(), SharedPreferenceManager.getInstance(this).getUsuario().getId_categoria());

            intent = new Intent(CustomerMapsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            return false;

        } else if (id == R.id.nav_exit) {
            Constantes.socketClient.disconnectUser(SharedPreferenceManager.getInstance(this).getUsuario().getId(), SharedPreferenceManager.getInstance(this).getUsuario().getId_categoria());
            finish();
        } else if (id == R.id.nav_share) {
            Intent intent_share = new Intent(Intent.ACTION_SEND);
            intent_share.setType("text/plan");

            String share_body = "R4C";
            String share_sub = "Queres carona? Baixe já a App em http://r4c.co.ao/download";

            intent_share.putExtra(Intent.EXTRA_SUBJECT, share_body);
            intent_share.putExtra(Intent.EXTRA_TEXT, share_sub);

            startActivity(Intent.createChooser(intent_share, "Partilhar"));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ShowFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setCustomAnimations(Animation.ZORDER_TOP, Animation.ABSOLUTE);
        fragmentTransaction.commit();
    }

/*
    @Override
    protected void onStop() {
        super.onStop();
        Constantes.socketClient.disconnectUser(SharedPreferenceManager.getInstance(this).getUsuario().getId());

    }*/


    private void getLocationPermission() {
        String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissao = true;
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        permissao = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permissao = false;
                            break;
                        }

                        //Inicia o mapa
                        permissao = true;
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


    }
}