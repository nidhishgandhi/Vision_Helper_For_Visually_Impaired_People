package org.tensorflow.lite.examples.helper_for_visually_impaired;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class call_activity extends AppCompatActivity {

    EditText num;
    TextToSpeech t1;
    RelativeLayout layout;
    private final int REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_activity);

        num = findViewById(R.id.num);
        layout = findViewById(R.id.relativeLayout);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        layout.setOnTouchListener(new OnSwipeTouchListener(call_activity.this) {
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
            public void onSwipeLeft() {
                super.onSwipeLeft();
                String phone = num.getText().toString();
                if (phone.isEmpty())
                {
                    t1.speak("Number is Empty...Please enter phone number",TextToSpeech.QUEUE_FLUSH,null);
                }
                else
                {
                    t1.speak("You have dial a call ", TextToSpeech.QUEUE_FLUSH, null);
                    for(int i = 0 ; i < phone.length(); i++)
                    {
                        t1.speak(Character.toString(phone.charAt(i)),QUEUE_ADD,null);
                    }
                }
                Toast.makeText(call_activity.this, "Number confirmation", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                String phone = num.getText().toString();
                String s = "tel: " + phone;
                if (phone.isEmpty()) {
                    t1.speak("Number is Empty...Please enter phone number", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                    Toast.makeText(call_activity.this, "calling.....", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    num.setText(result.get(0));
                }
                break;
            }
        }
    }
}
