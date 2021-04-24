package org.tensorflow.lite.examples.helper_for_visually_impaired;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class weather extends AppCompatActivity {
    EditText city;
    TextView result;
    TextToSpeech tts;
    LinearLayout layout;
    private final int REQ_CODE = 100;


    private final String url="http://api.openweathermap.org/data/2.5/weather";
    private final String appid="e8b9d895af4902c725f800fe84b99a9b";
    DecimalFormat df = new DecimalFormat("#.##");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        city=(findViewById(R.id.city));
        // country=(findViewById(R.id.country));
        result=(findViewById(R.id.result));
        layout = findViewById(R.id.linearlayout);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    int lang=tts.setLanguage(Locale.US);
                }
            }
        });

        layout.setOnTouchListener(new OnSwipeTouchListener(weather.this) {
            public void onSwipeUp(){
                super.onSwipeUp();

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                String tempurl="";
                String cit =city.getText().toString().trim();
                //String countr=country.getText().toString().trim();
                if(cit.equals("")){
                    result.setText("City field cannot be empty");
                }else{
//            if(!countr.equals("")){
//                tempurl=url+"?q="+cit+","+countr+"&appid="+appid;
//            }else
//         {
                    tempurl=url+"?q="+cit+"&appid="+appid;
                }

                StringRequest stringRequest=new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast. LENGTH_SHORT);
                        //Log.d("response",response);
                        String output="";
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            JSONArray jsonArray=jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather=jsonArray.getJSONObject(0);
                            String description=jsonObjectWeather.getString("description");
                            JSONObject jsonObjectMain=jsonResponse.getJSONObject("main");
                            double temp= jsonObjectMain.getDouble("temp")-273.15;
                            double feels=jsonObjectMain.getDouble("feels_like")-273.15;
                            float pressure=jsonObjectMain.getInt("pressure");
                            int humidity=jsonObjectMain.getInt("humidity");
                            JSONObject jsonObjectWind=jsonResponse.getJSONObject("wind");
                            String wind=jsonObjectWind.getString("speed");
                            JSONObject jsonobjclouds=jsonResponse.getJSONObject("clouds");
                            String clouds = jsonobjclouds.getString("all");
                            JSONObject jsonObjectSys=jsonResponse.getJSONObject("sys");
                            String countryname=jsonObjectSys.getString("country");
                            String cityname=jsonResponse.getString("name");
                            result.setTextColor(Color.rgb(68,134,199));
                            output+="Current weather of "+cityname+"("+countryname+")"
                                    +"\n Temp: "+df.format(temp)+" °C"
                                    +"\n Feels Like: "+df.format(feels)+" °C"
                                    +"\n Humidity: "+humidity+"%"
                                    +"\n Description: "+description
                                    +"\n Wind Speed: " + wind+" meteres per second"
                                    +"\n Cloudiness: "+clouds+"%"
                                    +"\n Pressre: "+pressure+"hpa";
                            result.setText(output);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });


        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!result.getText().toString().equals(""))
                {

                    String s1 = result.getText().toString();
                    int speech = tts.speak(s1, TextToSpeech.QUEUE_FLUSH, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}

        };
        result.addTextChangedListener(textWatcher);
    }
    /*public void getWeatherDetails(View view) {

    }*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    city.setText(result.get(0));
                }
                break;
            }
        }
    }


}
