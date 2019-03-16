package com.msr.mygeo;

//import android.content.Intent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Criteria;
import java.util.Locale;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private Button b1;
    private TextView eT1;
    private TextView eT2;
    private TextView tv1;
    double lat;
    double lng;
    Geocoder gc;    //CharSequence PhoneNo="+919030833398";
    Locale locale;
    LocationManager locationManager;
    Location location;
    LocationListener locationListener;
    Intent intent;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.b1);
        eT1 = (EditText) findViewById(R.id.eT1);
        eT2 = (EditText) findViewById(R.id.eT2);
        tv1 = findViewById(R.id.tv1);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onStart() {
        super.onStart();
        gc = new Geocoder(this, Locale.getDefault());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    lat = Double.parseDouble(eT1.getText().toString());
                    lng = Double.parseDouble(eT2.getText().toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),"ERROR",Toast.LENGTH_SHORT).show();
                }
                findAddress(lat, lng);

            }
        });

        try {
            Toast.makeText(getBaseContext(), "MYGEOLOCATION", Toast.LENGTH_SHORT).show();

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            String provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    eT1.setText(String.valueOf(lat));
                    eT2.setText((String.valueOf(lng)));
                    Toast.makeText(getBaseContext(), "LOCATION CHANGED", Toast.LENGTH_SHORT).show();
                }

                public void onProviderDisabled(String provider) {

                    Toast.makeText(getBaseContext(), "CHECK GPS CONNECTION", Toast.LENGTH_SHORT).show();

                }

                public void onProviderEnabled(String provider) {
                    Toast.makeText(getBaseContext(), "GPS ENABLED", Toast.LENGTH_SHORT).show();
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Toast.makeText(getBaseContext(), "STATE CHANGED", Toast.LENGTH_SHORT).show();
                }

            };
            locationManager.requestLocationUpdates(provider, 5000L, 10f, locationListener);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
protected void onStop()
    {
        locationManager.removeUpdates(locationListener);
    }

private void findAddress(double lat,double lng)
{
    try{
    List<Address> addresses = gc.getFromLocation(lat,lng,10);
    StringBuilder sb = new StringBuilder();
    if (addresses.size() > 0)
    {
        Address address = addresses.get(0);

        for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
            sb.append(address.getAddressLine(i));

        sb.append(address.getLocality()).append("\n").append(address.getPostalCode()).append(address.getCountryName());
        //   sb.append(address.getCountryName())

        Toast.makeText(getBaseContext(),addresses.get(0).getCountryName(),Toast.LENGTH_SHORT).show();

    }
    else
    {
        Toast.makeText(getBaseContext(),"no address",Toast.LENGTH_SHORT).show();
    }
    String str=sb.toString();
    tv1.setText( "Your Current Position is: " + str);
}
                catch (Exception io){
        io.printStackTrace();
    }

}

}





