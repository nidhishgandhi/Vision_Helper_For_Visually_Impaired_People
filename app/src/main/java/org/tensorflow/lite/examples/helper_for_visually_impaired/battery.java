package org.tensorflow.lite.examples.helper_for_visually_impaired;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class battery extends Activity {

    TextView statusLabel;
    TextView percentageLabel;
    ImageView batteryImage;
    TextToSpeech tts;
    //private BatteryReceiver mBatteryReceiver = new BatteryReceiver();
    private IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        //statusLabel = findViewById(R.id.statusLabel);
        percentageLabel = findViewById(R.id.percentageLabel);
        batteryImage = findViewById(R.id.batteryImage);
        BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                String action = intent.getAction();

                if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)) {

                    // Status
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    String message = "";

                    switch (status) {
                        case BatteryManager.BATTERY_STATUS_FULL:
                            message = " and it is Full";
                            break;
                        case BatteryManager.BATTERY_STATUS_CHARGING:
                            message = "and it is Charging";
                            break;
                        case BatteryManager.BATTERY_STATUS_DISCHARGING:
                            message = "and it is Discharging";
                            break;
                        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                            message = "and it is Not charging";
                            break;
                        case BatteryManager.BATTERY_STATUS_UNKNOWN:
                            message = "and it is Unknown";
                            break;
                    }
                    // statusLabel.setText(message);


                    // Percentage
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int percentage = level * 100 / scale;
                    percentageLabel.setText("It is "+percentage + "% "+message);


                    // Image
                    Resources res = context.getResources();

                    if (percentage >= 90) {
                        batteryImage.setImageDrawable(res.getDrawable(R.drawable.b100));

                    } else if (90 > percentage && percentage >= 65) {
                        batteryImage.setImageDrawable(res.getDrawable(R.drawable.b75));

                    } else if (65 > percentage && percentage >= 40) {
                        batteryImage.setImageDrawable(res.getDrawable(R.drawable.b50));

                    } else if (40 > percentage && percentage >= 15) {
                        batteryImage.setImageDrawable(res.getDrawable(R.drawable.b25));

                    } else {
                        batteryImage.setImageDrawable(res.getDrawable(R.drawable.b0));

                    }

                }
            }
        };
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    int lang=tts.setLanguage(Locale.US);
                }

            }
        });
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!percentageLabel.getText().toString().equals(""))
                {

                    String s2 = percentageLabel.getText().toString();
                    int speech1 = tts.speak(s2, TextToSpeech.QUEUE_FLUSH, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}

        };

        percentageLabel.addTextChangedListener(textWatcher);

        registerReceiver(broadcastReceiver, mIntentFilter);

    }


}
