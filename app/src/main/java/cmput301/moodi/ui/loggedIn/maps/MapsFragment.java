package cmput301.moodi.ui.loggedIn.maps;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

    // stuff
    TextView numUmoods;
    TextView numFMoods;

    // objects to interact with
    MapView mMapView;
    Switch userMoods;
    Switch followingMoods;

    // firebase access functions
    MoodiStorage moodiStorage;

    // managing the map markers
    private List<Marker> userMarkers;
    private List<Marker> followingMarkers;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // set objects to interact with
        mMapView = view.findViewById(R.id.mood_list_map);
        userMoods = view.findViewById(R.id.switch_user);
        followingMoods = view.findViewById(R.id.switch_following);
        numUmoods = view.findViewById(R.id.num_u_moods);
        numFMoods = view.findViewById(R.id.num_f_moods);

        numUmoods.setText("0");
        numFMoods.setText("0");

        // create the map object and
        initGoogleMap(savedInstanceState);

        // firebase access functions
        moodiStorage = new MoodiStorage();

        return view;
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        userMarkers = new ArrayList<Marker>();
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


        // toggle for user to choose whether their moods show on the map
        userMoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    // query markers and add them to list
                    moodiStorage.getUserMoods().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // user moods have been retrieved
                                int count = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    GeoPoint gp = (GeoPoint) document.getData().get("Location");
                                    if (gp != null) {


//                                    Number emotionalstate = (Number) document.getData().get("Index");

////                                    int icon;
//                                    float color;
//                                    if(emotionalstate.intValue() == 0){
////                                        icon = R.drawable.happy;
//                                        float HUE_YELLOW;
//                                    } else if (emotionalstate.intValue() == 1){
////                                        icon = R.drawable.mad;
//                                    } else if (emotionalstate.intValue() == 2){
////                                        icon = R.drawable.sad;
//                                    } else if (emotionalstate.intValue() == 3){
////                                        icon = R.drawable.love;
//                                    } else { // (emotionalstate == 4) {
////                                        icon = R.drawable.tired;
//                                    }

                                    userMarkers.add(map.addMarker(new MarkerOptions().position(new LatLng(gp.getLatitude(), gp.getLongitude()))));
                                    count = count + 1;
                                    }
//                                            .title("Big mood")));
//                                            .icon(BitmapDescriptorFactory
//                                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                                            .icon(BitmapDescriptorFactory.fromResource(icon))));
                                }

                                numUmoods.setText(String.valueOf(count));
                            } else {
                                Log.d(TAG, "Error getting user moods: ", task.getException());
                            }
                        }
                    });
                } else {
                    // delete user markers
                    if (userMarkers != null) {
                        for (Marker marker : userMarkers) {
                            marker.setVisible(false);
                        }

                        numUmoods.setText("0");
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
                    // remove following moods from map
                    Toast.makeText(getActivity(), "Removing moods of followed users.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        // add markers for all posts that the user selects
        // map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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