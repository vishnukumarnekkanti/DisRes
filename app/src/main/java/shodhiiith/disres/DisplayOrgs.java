package shodhiiith.disres;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DisplayOrgs extends Fragment {

    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;

    private List<String> buildingTypes = Arrays.asList("Hospital", "Fire Brigade", "Rescue", "Blood Bank", "Police", "NGO & Rescue");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.display_orgs, container, false);

        MapsInitializer.initialize(getActivity());

        mMapView = (MapView) inflatedView.findViewById(R.id.mapview);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(inflatedView);

        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.mapview)).getMap();
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.setTrafficEnabled(true);
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        Map<String, List<Address>> hotSpots = new HardData().getHardCodedData();
        for (Map.Entry<String, List<Address>> entry : hotSpots.entrySet()) {
            String key = entry.getKey();
            List<Address> value = entry.getValue();
            for (Address location : value) {
                Marker place = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title(key)
                        .alpha(0.7f));
                if(key.equals("Affected")) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f);
                    mMap.moveCamera(cameraUpdate);
                }
                if(key.equals("Hospital")){
                    place.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_medical));
                }
                if(key.equals("Blood Bank")){
                    place.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_blood));
                }
                if(key.equals("Fire Brigade")){
                    place.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_fire));
                }
                if(key.equals("Police")){
                    place.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_police));
                }
                if(key.equals("NGO & Rescue")){
                    place.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_ngo));
                }
                if (!buildingTypes.contains(key)) {
                    CircleOptions circleOptions = new CircleOptions()
                            .center(new LatLng(location.getLatitude(), location.getLongitude()));
                    circleOptions.fillColor(Color.argb(80, 255, 0, 0));
                    circleOptions.strokeColor(Color.argb(80, 255, 0, 0));
                    circleOptions.strokeWidth(3);
                    circleOptions.radius(500);
                    mMap.addCircle(circleOptions);
                }
            }
        }
       /* if(mMap.isMyLocationEnabled()){    //////need to set my location, priority high
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE); //getSystemService(LOCATION_SERVICE);
            Criteria criteria   = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, false);
            Location location   = locationManager.getLastKnownLocation(bestProvider);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f);

            // LatLng posLatLon    = new LatLng(location.getLatitude(), location.getLongitude());
            //Toast.makeText(getActivity().getApplicationContext(), "position: "+ location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
           mMap.moveCamera(cameraUpdate);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
}
//removed codes *****************************************************************************//
/*start temp locations*/
        /*
        Address spot1 = new Address(null);
        spot1.setLatitude(17.4456);
        spot1.setLongitude(78.3497);
        Address spot2 = new Address(null);
        spot2.setLatitude(17.4456);
        spot2.setLongitude(78.3427);
        Address spot3 = new Address(null);
        spot3.setLatitude(17.44219);
        spot3.setLongitude(78.3587);
        Address spot4 = new Address(null);
        spot4.setLatitude(17.4502415);
        spot4.setLongitude(78.364239);
        Address spot5 = new Address(null);
        spot5.setLatitude(17.45219);
        spot5.setLongitude(78.3637);
        Address spot6 = new Address(null);
        spot6.setLatitude(17.452415);
        spot6.setLongitude(78.331239);
        List<Address> affectedAreas = new ArrayList<Address>();
        affectedAreas.add(spot1);
        affectedAreas.add(spot2);
        List<Address> healthCare = new ArrayList<Address>();
        healthCare.add(spot3);
        healthCare.add(spot4);
        List<Address> bloodBanks = new ArrayList<Address>();
        bloodBanks.add(spot5);
        List<Address> fireBrigade = new ArrayList<Address>();
        fireBrigade.add(spot6);
        Map<String, List<Address>> hotSpots = new HashMap<String, List<Address>>();
        hotSpots.put("Affected", affectedAreas);
        hotSpots.put("Hospital", healthCare);
        hotSpots.put("Blood Bank", bloodBanks);
        hotSpots.put("Fire Brigade", fireBrigade);*/
        /*end tmp locations*/
//Toast.makeText(getApplicationContext(), hotSpots.values().toString(), Toast.LENGTH_LONG).show();