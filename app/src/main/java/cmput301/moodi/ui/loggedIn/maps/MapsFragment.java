package cmput301.moodi.ui.loggedIn.maps;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;

import static android.content.ContentValues.TAG;
import static cmput301.moodi.util.Constants.MAPVIEW_BUNDLE_KEY;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    // map
    MapView mMapView;

    // select to view user moods
    Switch userMoods;
    TextView numUmoods;

    // select to view following moods
    Switch followingMoods;
    TextView numFmoods;

    // interact with firebase
    MoodiStorage moodiStorage;

    // manage the map markers, this might be optimized in the future
    private List<Marker> userMarkers;
    private List<Marker> followingMarkers;
    private List<Marker> allMarkers;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = view.findViewById(R.id.mood_list_map);
        userMoods = view.findViewById(R.id.switch_user);
        followingMoods = view.findViewById(R.id.switch_following);
        numUmoods = view.findViewById(R.id.num_u_moods);
        numFmoods = view.findViewById(R.id.num_f_moods);

        numUmoods.setText("0");
        numFmoods.setText("0");

        // init the map object
        initGoogleMap(savedInstanceState);

        // firebase access functions
        moodiStorage = new MoodiStorage();

        return view;
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        userMarkers = new ArrayList<Marker>();
        followingMarkers = new ArrayList<Marker>();
        allMarkers = new ArrayList<Marker>();

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        // set some map settings
        UiSettings mapUiSettings = map.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        mapUiSettings.setAllGesturesEnabled(true);
        mapUiSettings.setCompassEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.5461, -113.4938), (float) 9.0));

        // toggle for user to choose whether their own moods show on the map
        userMoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    // if user checks to see their moods query the mood locations and add them to list of markers
                    moodiStorage.getUserMoods().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // user moods have been successfully retrieved

                                // for each mood, extract the location and make a marker with it
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    GeoPoint gp = (GeoPoint) document.getData().get("Location");
                                    Marker newMarker = map.addMarker(new MarkerOptions().position(new LatLng(gp.getLatitude(), gp.getLongitude())));
                                    userMarkers.add(newMarker);
                                    allMarkers.add(newMarker);
                                }

                                // display to user how many of their moods are now being shown
                                numUmoods.setText(String.valueOf(task.getResult().size()));

                                // if new markers were placed we can adjust the zoom settings to see them
                                if (allMarkers.size() == 1) {
                                    // if there is only one marker, zoom to that marker
                                    map.animateCamera(CameraUpdateFactory.newLatLng(allMarkers.get(0).getPosition()));

                                } else if (allMarkers.size() > 1) {
                                    // if there are more, zoom to the group bounds
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (Marker marker : allMarkers) {
                                        builder.include(marker.getPosition());
                                    }
                                    LatLngBounds bounds = builder.build();
                                    int padding = 120; // offset from edges of the map in pixels
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                    map.animateCamera(cu);
                                }
                            } else {
                                Log.d(TAG, "Error getting user moods: ", task.getException());
                            }
                        }
                    });
                } else {
                    // delete user markers and update total markers
                    if (userMarkers != null) {
                        for (Marker marker : userMarkers) {
                            marker.remove();
                        }
                        userMarkers.clear(); // double check? why not
                        numUmoods.setText("0");
                        allMarkers = followingMarkers;
                    }
                }
            }
        });


        // toggle for the user to choose whether to show their following moods on the map
        followingMoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show following moods on the map
                    Toast.makeText(getActivity(), "Showing moods of followed users.", Toast.LENGTH_SHORT).show();

                } else {
                    // delete following markers and update total markers
                    if (followingMarkers != null) {
                        for (Marker marker : followingMarkers) {
                            marker.setVisible(false);
                        }
                        followingMarkers.clear(); // double check? why not
                        numFmoods.setText("0");
                        allMarkers = userMarkers;
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}