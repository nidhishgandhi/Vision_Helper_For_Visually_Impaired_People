package org.tensorflow.lite.examples.helper_for_visually_impaired;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class message extends AppCompatActivity {
    EditText mess_phone, mess;
    TextToSpeech t1;
    RelativeLayout layout;
    String phoneNo;
    String message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private final int REQ_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mess_phone = findViewById(R.id.mess_phone);
        mess = findViewById(R.id.mess);

        layout = findViewById(R.id.relativeLayout);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        layout.setOnTouchListener(new OnSwipeTouchListener(message.this) {

            public void onSwipeDown() {
                super.onSwipeDown();

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
            public void onSwipeUp() {
                super.onSwipeUp();

                Intent intent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent1.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent1, MY_PERMISSIONS_REQUEST_SEND_SMS);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                phoneNo = mess_phone.getText().toString();
                message = mess.getText().toString();
                if (phoneNo.isEmpty() || message.isEmpty()) {
                    t1.speak("phone number or message is Empty", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    t1.speak("your message is "+ message + "to", TextToSpeech.QUEUE_FLUSH, null);
                    for (int i = 0; i < phoneNo.length(); i++) {
                        t1.speak(Character.toString(phoneNo.charAt(i)), QUEUE_ADD, null);
                    }
                }
                Toast.makeText(message.this, "message confirmation", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                phoneNo = mess_phone.getText().toString();
                message = mess.getText().toString();
                if (phoneNo.isEmpty()|| message.isEmpty()) {
                    t1.speak("phone number or message is Empty", TextToSpeech.QUEUE_FLUSH, null);
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),message.class);
                    PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                    //Get the SmsManager instance and call the sendTextMessage method to send message
                    SmsManager sms=SmsManager.getDefault();
                    sms.sendTextMessage(phoneNo, null, message, pi,null);

                    Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                            Toast.LENGTH_LONG).show();                
                }
            }
        });
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mess.setText(result.get(0));
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mess_phone.setText(result.get(0));
                }
                break;
            }
        }
    }*/
}

