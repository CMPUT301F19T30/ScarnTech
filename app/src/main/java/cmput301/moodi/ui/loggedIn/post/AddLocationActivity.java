package cmput301.moodi.ui.loggedIn.post;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback {

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // map setup
        mapFragment.getMapAsync(this);

        cancelLocation = findViewById(R.id.cancel_location);
        setLocation = findViewById(R.id.set_location);

        // get last location data from bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.getString("Latitude").isEmpty() && !extras.getString("Longitude").isEmpty()) {
            latitude = Double.valueOf(extras.getString("Latitude"));
            longitude = Double.valueOf(extras.getString("Longitude"));
        } else {
            // if nothing is previously selected, default to a location in edmonton
            latitude = 53.518120;
            longitude = -113.512127;
        }

        // when user cancels, back out of activity with no action
        cancelLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // when user selects this as their location, the marker location will be uploaded to firebase
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add location to the users last location document in firebase
                MoodiStorage ms = new MoodiStorage();
                ms.addLastLocation(new GeoPoint(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude));
                finish();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // create map settings
        UiSettings mapUiSettings = googleMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        mapUiSettings.setAllGesturesEnabled(true);
        mapUiSettings.setCompassEnabled(true);

        // create old marker
        LatLng old_latlng = new LatLng(latitude, longitude);
        markerOptions = new MarkerOptions();
        markerOptions.position(old_latlng);
        markerOptions.title("Old Location");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(old_latlng, (float) 11));

        // when user selects a new location, update marker
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng new_latlng) {
                // create new marker
                markerOptions.position(new_latlng);
                markerOptions.title("New Location");

                // go to new marker
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(new_latlng));
                googleMap.addMarker(markerOptions);
            }
        });
    }
}
