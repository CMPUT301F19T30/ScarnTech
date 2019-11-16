package cmput301.moodi.ui.loggedIn.post;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;

import static cmput301.moodi.util.Constants.PICK_CONTACT_REQUEST;

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MarkerOptions markerOptions;
    Double latitude;
    Double longitude;
    Button cancelLocation;
    Button setLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cancelLocation = findViewById(R.id.cancel_location);
        setLocation = findViewById(R.id.set_location);

        // get last location data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = Double.valueOf(extras.getString("Latitude"));
            longitude = Double.valueOf(extras.getString("Longitude"));
        } else {
            // if nothing is previously selected, default to a location in edmonton
            latitude = 53.518120;
            longitude = -113.512127;
        }

        cancelLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add location to the users last location document in firebase
                MoodiStorage ms = new MoodiStorage();
                ms.addLastLocation(new GeoPoint(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude));
                Toast.makeText(AddLocationActivity.this,
                        "Added location: " +
                        markerOptions.getPosition().latitude + ", " +
                        markerOptions.getPosition().longitude,
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings mapUiSettings = googleMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        mapUiSettings.setAllGesturesEnabled(true);
        mapUiSettings.setCompassEnabled(true);

        // Creating old marker
        LatLng old_latlng = new LatLng(latitude, longitude);
        markerOptions = new MarkerOptions();
        markerOptions.position(old_latlng);
        markerOptions.title("Old Location");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(old_latlng, (float) 11));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng new_latlng) {
                // Creating new marker
//                markerOptions = new MarkerOptions();
                markerOptions.position(new_latlng);
                markerOptions.title("New Location");

                // goto new marker
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(new_latlng));
                googleMap.addMarker(markerOptions);
            }
        });
    }
}
