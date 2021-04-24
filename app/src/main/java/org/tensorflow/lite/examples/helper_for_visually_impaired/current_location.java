package org.tensorflow.lite.examples.helper_for_visually_impaired;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class current_location extends AppCompatActivity implements LocationListener {


    //Button button_location,speak;
    TextView textView_location;
    LocationManager locationManager;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        textView_location = findViewById(R.id.text_location);
        //  button_location = findViewById(R.id.button_location);
/*
        speak = findViewById(R.id.b_speak);
*/
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    int lang=tts.setLanguage(Locale.US);
                }
            }
        });
        //Runtime permissions
        if (ContextCompat.checkSelfPermission(current_location.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(current_location.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,current_location.this);

        }catch (Exception e){
            e.printStackTrace();
        }


//        button_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //create method
//                getLocation();
//                tts.speak("you have clicked location button",TextToSpeech.QUEUE_FLUSH,null);
//            }
//        });

//            speak.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //if(textView_location.!="Current Location" && textView_location.getText().toString()!="" ) {
//                        String s = textView_location.getText().toString();
//                        int speech = tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
//                   // }
//                }
//            });
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!textView_location.getText().toString().equals(""))
                {

                    String s1 = textView_location.getText().toString();
                    int speech = tts.speak(s1, TextToSpeech.QUEUE_FLUSH, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}

        };
        textView_location.addTextChangedListener(textWatcher);


    }

//    @SuppressLint("MissingPermission")
//    private void getLocation() {
//
//        try {
//            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,current_location.this);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void onLocationChanged(Location location) {
        tts.speak("you have clicked locationchanged button",TextToSpeech.QUEUE_FLUSH,null);
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(current_location.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String result = null;
            String address = addresses.get(0).getAddressLine(0);

            textView_location.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
