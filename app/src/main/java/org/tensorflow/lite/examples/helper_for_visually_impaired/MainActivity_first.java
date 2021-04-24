package org.tensorflow.lite.examples.helper_for_visually_impaired;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.Currency;
import java.util.Locale;

public class MainActivity_first extends AppCompatActivity {
    GridLayout grid;
    TextToSpeech t1,myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // replace this Locale with whatever you want
                    Locale localeToUse = new Locale("en","US");
                    myTTS.setLanguage(localeToUse);
                    myTTS.speak("Hi, Welcome to Vision Helper !  There is a four part and two column\n" +
                            "one object and currency detection\n" +
                            "second call and message app\n" +
                            "third weather and location\n" +
                            "fourh  text from image and battery checker", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        grid = findViewById(R.id.grid);

        CardView c1 = (CardView) findViewById(R.id.object_detection);
        CardView c3 = (CardView) findViewById(R.id.img_to_text);
        CardView c2 = (CardView) findViewById(R.id.call);

        CardView c6 = (CardView) findViewById(R.id.location);

        CardView c4 = (CardView) findViewById(R.id.message);
        CardView c7 = (CardView) findViewById(R.id.weather);
        CardView c8 = (CardView) findViewById(R.id.battery);
        CardView c9 = (CardView) findViewById(R.id.currency);


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                t1.speak("you have opened object detection",TextToSpeech.QUEUE_FLUSH,null);
                Intent obj_det = new Intent(MainActivity_first.this,ClassifierActivity.class);
                startActivity(obj_det);

            }
        });



        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                t1.speak("you have opened call app. swipe up to speak the number. Swipe right to dial a call and swipe left to confirm the number",TextToSpeech.QUEUE_FLUSH,null);

                Intent call = new Intent(MainActivity_first.this, call_activity.class);
                startActivity(call);
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak("you have opened text from image app. swipe right to take image. swipe left to get text",TextToSpeech.QUEUE_FLUSH,null);
                Intent textfromimg = new Intent(MainActivity_first.this, text_from_img.class);
                startActivity(textfromimg);
            }
        });

        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                Toast.makeText(MainActivity_first.this, "location", Toast.LENGTH_SHORT).show();
                t1.speak("you have opened Location",TextToSpeech.QUEUE_FLUSH,null);
                 Intent myloc = new Intent(MainActivity_first.this, current_location.class);
                startActivity(myloc);
            }
        });


        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                Toast.makeText(MainActivity_first.this, "Message", Toast.LENGTH_SHORT).show();
                t1.speak("you have opened Text Message sender. swipe up to speak the number. swipe down to speak message. Swipe right to send message. swipe left to confirm it",TextToSpeech.QUEUE_FLUSH,null);
                Intent message=new Intent(MainActivity_first.this,message.class);
                startActivity(message);
            }
        });

        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                Toast.makeText(MainActivity_first.this, "weather", Toast.LENGTH_SHORT).show();
                t1.speak("you have opened weather checker. swipe up to speak city name. Swipe right to get information",TextToSpeech.QUEUE_FLUSH,null);
                Intent weather=new Intent(MainActivity_first.this,weather.class);
                startActivity(weather);
            }
        });


        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                Toast.makeText(MainActivity_first.this, "Battery", Toast.LENGTH_SHORT).show();
                t1.speak("you have opened battery checker",TextToSpeech.QUEUE_FLUSH,null);
                Intent battery=new Intent(MainActivity_first.this,battery.class);
                startActivity(battery);
            }
        });

        c9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                t1.speak("you have opened currency detection. Don't use 10 , 20 and old 100 rupees notes",TextToSpeech.QUEUE_FLUSH,null);
                Intent curr = new Intent(MainActivity_first.this, currency.class);
                startActivity(curr);

            }
        });

    }
}


