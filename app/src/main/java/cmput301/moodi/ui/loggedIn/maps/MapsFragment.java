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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cmput301.moodi.Objects.Mood;
import cmput301.moodi.Objects.MoodiStorage;
import cmput301.moodi.R;

import static android.content.ContentValues.TAG;
import static cmput301.moodi.util.Constants.MAPVIEW_BUNDLE_KEY;
import static cmput301.moodi.util.Constants.POST_PATH;

/**
 * Class:MapsFragments
 * This class allow user to view google map in the app, it also allocate each mood on the map
 * base on their location.
 * @since 11/15/2019
 */

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
    public List<Mood> moods;

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

    /*
    initialize google map on the app
     */
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
    /*
    Allows app able be show moods on the map
     */
    @Override
    public void onMapReady(final GoogleMap map) {
        // set some map settings
        UiSettings mapUiSettings = map.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        mapUiSettings.setAllGesturesEnabled(true);
        mapUiSettings.setCompassEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.5461, -113.4938), (float) 9.0));

        // set the dark theme style
        boolean success = map.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }

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
                                    if (gp != null) {
                                        Marker newMarker = map.addMarker(new MarkerOptions().position(new LatLng(gp.getLatitude(), gp.getLongitude())));
                                        userMarkers.add(newMarker);
                                        allMarkers.add(newMarker);
                                    }
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
                                Log.d(TAG, "Number of following markers: " + followingMarkers.size());
                                Log.d(TAG, "Number of user mood markers: " + userMarkers.size());
                            } else {
                                Log.d(TAG, "Error getting user moods: ", task.getException());
                            }
                        }
                    });
                } else {
                    // delete user markers and update total markers
                    if (userMarkers != null) {
                        for (Marker marker : userMarkers) {
                            marker.setVisible(false);
                        }
                        userMarkers.clear();
                        numUmoods.setText("0");
                        allMarkers.clear();
                        for (Marker marker : followingMarkers) {
                            allMarkers.add(marker);
                        }
                    }
                }
            }
        });

        // toggle for the user to choose whether to show their following moods on the map
        // this will only show the most recent mood for each person! if there is no location for
        // this mood then nothing will be shown.
        followingMoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // User wants to see the moods of the users they follow
                    moodiStorage.getFollowing().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // for each following, query the most recent mood
                                for (QueryDocumentSnapshot following_doc : task.getResult()) {
                                    // query this following users last mood
                                    moodiStorage.getUserMoods((String)following_doc.getData().get("following")).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                if (moods == null) {
                                                    moods = new ArrayList<>();
                                                } else {
                                                    moods.clear();
                                                }

                                                // for each mood of this person add to an array
                                                for (QueryDocumentSnapshot mood_doc : task.getResult()) {
                                                    Mood mood = new Mood();
                                                    mood.setFromDocument(mood_doc);
                                                    moods.add(mood);

                                                }

                                                if (!moods.isEmpty()) {
                                                    Collections.sort(moods);
                                                    GeoPoint gp = moods.get(0).getLocation();
                                                    if (gp != null) {
                                                        Marker newMarker = map.addMarker(new MarkerOptions().position(new LatLng(gp.getLatitude(), gp.getLongitude())));
                                                        followingMarkers.add(newMarker);
                                                        allMarkers.add(newMarker);
                                                    }
                                                }

                                                // display to user how many of their moods are now being shown
                                                numFmoods.setText(String.valueOf(followingMarkers.size()));

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
                                            }
                                        }
                                    });
                                }
                                Log.d(TAG, "Number of following markers: " + followingMarkers.size());
                                Log.d(TAG, "Number of user mood markers: " + userMarkers.size());
                            } else {
                                Log.d(TAG, "Error getting following moods: ", task.getException());
                            }
                        }
                    });
                } else {
                    // delete following markers and update total markers
                    if (followingMarkers != null) {
                        for (Marker marker : followingMarkers) {
                            marker.setVisible(false);
                        }
                        followingMarkers.clear();
                        numFmoods.setText("0");
                        allMarkers.clear();
                        for (Marker marker : userMarkers) {
                            allMarkers.add(marker);
                        }
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