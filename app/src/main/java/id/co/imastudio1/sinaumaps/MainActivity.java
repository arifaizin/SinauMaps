package id.co.imastudio1.sinaumaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.imastudio1.sinaumaps.helper.DirectionMapsV2;
import id.co.imastudio1.sinaumaps.helper.GPStrack;
import id.co.imastudio1.sinaumaps.helper.HeroHelper;
import id.co.imastudio1.sinaumaps.model.PlaceModel;
import id.co.imastudio1.sinaumaps.restApi.ApiService;
import id.co.imastudio1.sinaumaps.restApi.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.edtawal)
    TextView edtawal;
    @BindView(R.id.edtakhir)
    TextView edtakhir;
    @BindView(R.id.textjarak)
    TextView textjarak;
    @BindView(R.id.textwaktu)
    TextView textwaktu;
    @BindView(R.id.textharga)
    TextView textharga;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.btnlokasiku)
    Button btnlokasiku;
    @BindView(R.id.btnpanorama)
    Button btnpanorama;
    @BindView(R.id.btnHitung)
    Button btnHitung;
    @BindView(R.id.linearbottom)
    LinearLayout linearbottom;
    @BindView(R.id.relativemap)
    RelativeLayout relativemap;
    @BindView(R.id.frame1)
    FrameLayout frame1;
    private GoogleMap mMap;


    GPStrack gps;
    DirectionMapsV2 direction;
    Double lat, lon;
    //kelas latlang
    LatLng posisiku;

    String name_location;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //inisialisai

        gps = new GPStrack(MainActivity.this);
        direction = new DirectionMapsV2(this);

        //dapat lokasi
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lon = gps.getLongitude();
            addMarker(lat, lon, "Lokasi saya", name_location);
        } else {
            gps.showSettingGps();
        }


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-6.1952505, 106.7926521);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //setzoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        ////settingan map
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //compas
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //permission
        mMap.setMyLocationEnabled(true);

        //klik map dan muncul makrker baru
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //get koordirnat berdasarkan area yang dklik
                lat = latLng.latitude;
                lon = latLng.longitude;

                posisiku= new LatLng(lat,lon);

                //hapus markers sebelumnya
                mMap.clear();

                //add mareke yang baru
                mMap.addMarker(new MarkerOptions().position(posisiku));
                //convert nama lokasi, ketika marker di klik
                name_location = convertName(lat,lon);
//                mMap.addMarker(new MarkerOptions().position(posisiku).title(name_location));
                addMarker(lat, lon, "Lokasi yang dipilih", name_location);
            }
        });
    }


    //buat method add marker
    private void addMarker(Double lat, Double lon, String judul, String alamat) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon),17));
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(judul).snippet(alamat));
    }

    private String convertName(Double lat, Double lon) {
        name_location = null;
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(lat,lon,1);
            name_location = list.get(0).getAddressLine(0)+""+list.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name_location;

    }

    @OnClick({R.id.btnHitung, R.id.btnlokasiku, R.id.btnpanorama,R.id.edtawal,R.id.edtakhir})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnHitung:
                aksesrute();
                break;
            case R.id.btnlokasiku:
                akseslokasiku();
                break;
            case R.id.btnpanorama:
                aksespanorama();
                break;
            case R.id.edtawal:
                try{
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MainActivity.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                //
                startActivityForResult(intent,1);
                break;
            case R.id.edtakhir:
                try{
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MainActivity.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                //
                startActivityForResult(intent,2);

                break;
        }
    }

    private void aksespanorama() {
        relativemap.setVisibility(View.GONE);
        frame1.setVisibility(View.VISIBLE);

        SupportStreetViewPanoramaFragment panorama = (SupportStreetViewPanoramaFragment)
                getSupportFragmentManager().findFragmentById(R.id.panorama);
        panorama.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
                streetViewPanorama.setPosition(posisiku);
            }
        });
    }

    private void akseslokasiku() {
        gps = new GPStrack(MainActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    110);

        }else {
            if (gps.canGetLocation()){
                lat= gps.getLatitude();
                lon= gps.getLongitude();
                mMap.clear();
                name_location=convertName(lat,lon);
                Toast.makeText(MainActivity.this, "lat"+lat +"\nlon "+lon, Toast.LENGTH_SHORT).show();

                //add marker
                addMarker(lat, lon, "Lokasi saya", name_location);


            }else {
                gps.showSettingGps();
            }
        }
    }


    //menanggkap in startactivity result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode != 0){
            Place place = PlaceAutocomplete.getPlace(this,data);
            //get koordinat lat long

            LatLng latLng = place.getLatLng();
            Double lat = place.getLatLng().latitude;
            Double lon = place.getLatLng().longitude;
            Log.d("Cari", "onActivityResult: "+lat+lon);
            String name = place.getName().toString();
            edtawal.setText(name);
            mMap.clear();
            addMarker(lat,lon, "Lokasi yang dipilih", name_location);

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR){
            Status status = PlaceAutocomplete.getStatus(this, data);
            // TODO: Handle the error.
            Log.i("Status", status.getStatusMessage());
        } else {
            Place place = PlaceAutocomplete.getPlace(this,data);
            //get koordinat lat long

            LatLng latLng = place.getLatLng();
            Double lat = place.getLatLng().latitude;
            Double lon = place.getLatLng().longitude;
            String name = place.getName().toString();
            edtakhir.setText(name);
            mMap.clear();
            addMarker(lat,lon, "Lokasi yang dipilih", name_location);
            aksesrute();
        }
    }

    public void aksesrute() {
        ApiService api = RetrofitConfig.getInstanceRetrofit();
        Call<PlaceModel> call = api.request_route(edtawal.getText().toString(),
                edtakhir.getText().toString());

        //call back atau respon dari json
        call.enqueue(new Callback<PlaceModel>() {
            @Override
            public void onResponse(Call<PlaceModel> call, Response<PlaceModel> response) {

                HeroHelper.pre("response route " +response.message());
                if (response.isSuccessful()){

                    //route
                    ///legs[]
                    ////distance
                    ////duration
                    ///overview
                    ArrayList<PlaceModel.RouteClass> route = response.body().getRoutes();
                    PlaceModel.RouteClass.PolylineClass overview = route.get(0).getOverview_polyline();
                    ArrayList<PlaceModel.RouteClass.LegClass> legs = route.get(0).getLegs();
                    PlaceModel.RouteClass.LegClass.DistanceClass distances = legs.get(0).getDistance();
                    PlaceModel.RouteClass.LegClass.DurationClass durations = legs.get(0).getDuration();

                    //jarak
                    String jarak = distances.getText();
                    textjarak.setText(jarak);
                    //waktu
                    String waktu = durations.getText();
                    textwaktu.setText(waktu);
                    //harga
                    Double value = distances.getValue();
                    double harga = Math.ceil(value/1000);
                    double total = harga * 1000;
                    textharga.setText("Rp."+ HeroHelper.
                            toRupiahFormat2(String.valueOf(total)));
                    //direction
                    String point = overview.getPoints();
                    direction.gambarRoute(mMap, point);
                }
            }

            @Override
            public void onFailure(Call<PlaceModel> call, Throwable t) {
                HeroHelper.pre(" error route"+t.getMessage());
            }
        });
    }
}
