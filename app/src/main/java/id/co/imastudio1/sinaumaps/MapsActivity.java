package id.co.imastudio1.sinaumaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import id.co.imastudio1.sinaumaps.helper.GPStrack;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private GPStrack gps;
    private double lat;
    private double lon;
    private String namaAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

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
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        LatLng imastudio = new LatLng(-6.1962729,106.7932099);
        mMap.addMarker(new MarkerOptions()
                .position(imastudio)
                .title("Marker in Imastudio")
                .snippet("Iki snippet")
                .draggable(true)
                .rotation(45)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(imastudio, 14));

        //membuat marker sesuai lokasi kita
        gps = new GPStrack(this);

        if (gps.canGetLocation()){
            lat = gps.getLatitude();
            lon = gps.getLongitude();
            ambilNamaAlamat(lat, lon);
            addMarker(lat, lon, "namaAlamat", namaAlamat);
            buatRadius(lat, lon);
            buatPolyLine();


        } else {
            gps.showSettingGps();
        }


    }

    private void buatPolyLine() {
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(lat, lon))
                .add(new LatLng(-6.1962729,106.7932099))
        );
    }

    private void buatRadius(double lat, double lon) {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lon))
                .radius(3000)
                .fillColor(0x4421b6ff)
                .strokeColor(0xff21b6ff)
                .strokeWidth(8);
        mMap.addCircle(circleOptions);

    }

    private String ambilNamaAlamat(double lat, double lon) {
        namaAlamat = null;
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try{
            List<Address> list = geocoder.getFromLocation(lat, lon, 1);
            namaAlamat = list.get(0).getAddressLine(0)+", "+list.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return namaAlamat;
    }

    private void addMarker(double lat, double lon, String judul, String snippet) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(judul).snippet(snippet));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));
    }
}
