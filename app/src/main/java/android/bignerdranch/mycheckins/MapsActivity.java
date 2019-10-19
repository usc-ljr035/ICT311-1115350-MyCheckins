package android.bignerdranch.mycheckins;

import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_ERROR = 1;
    private GoogleMap mMap;
    private CheckIn mCheckIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void updateUI() {
        //LatLng myPoint = new LatLng(-26.7174, 153.062);
        LatLng myPoint = new LatLng(mCheckIn.getLatitude(), mCheckIn.getLongitude());
        MarkerOptions myMarker = new MarkerOptions().position(myPoint).title("Receipt location");

        mMap.clear();
        mMap.addMarker(myMarker);

        int zoomLevel = 15;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myPoint, zoomLevel);
        mMap.animateCamera(update);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateUI();
    }




}
