package org.tensorflow.lite.examples.helper_for_visually_impaired;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.Locale;

public class text_from_img extends AppCompatActivity {

    ImageView imageview;
    Button btnProcess;
    Button button1;
    TextView txtView;
    Bitmap bitmap;
    LinearLayout layout;
    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_from_img);

        imageview = findViewById(R.id.image_view);

        txtView = findViewById(R.id.txtView);
        layout = findViewById(R.id.linearlayout);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    int lang=tts.setLanguage(Locale.US);
                }
            }
        });

        layout.setOnTouchListener(new OnSwipeTouchListener(text_from_img.this) {

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!txtRecognizer.isOperational()) {
                    txtView.setText(R.string.error_prompt);
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray items = txtRecognizer.detect(frame);
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = (TextBlock) items.valueAt(i);
                        strBuilder.append(item.getValue());
                        strBuilder.append(" ");
                        for (Text line : item.getComponents()) {
                            //extract scanned text lines here
                            Log.v("lines", line.getValue());
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                Log.v("element", element.getValue());

                            }
                        }
                    }
                    txtView.setText(strBuilder.toString().substring(0, strBuilder.toString().length() - 1));
                }

            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });

//        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//             R.drawable.hqdefault  );
//        imageview.setImageBitmap(bitmap);


        //req camera permission
        if(ContextCompat.checkSelfPermission(text_from_img.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(text_from_img.this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }
       /* button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });*/


        /*btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!txtRecognizer.isOperational()) {
                    txtView.setText(R.string.error_prompt);
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray items = txtRecognizer.detect(frame);
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = (TextBlock) items.valueAt(i);
                        strBuilder.append(item.getValue());
                        strBuilder.append(" ");
                        for (Text line : item.getComponents()) {
                            //extract scanned text lines here
                            Log.v("lines", line.getValue());
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                Log.v("element", element.getValue());

                            }
                        }
                    }
                    txtView.setText(strBuilder.toString().substring(0, strBuilder.toString().length() - 1));
                }
            }
        });
        */
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!txtView.getText().toString().equals(""))
                {

                    String s1 = txtView.getText().toString();
                    int speech = tts.speak(s1, TextToSpeech.QUEUE_FLUSH, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}

        };
        txtView.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            bitmap=(Bitmap)data.getExtras().get("data");
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageview.setImageBitmap(bitmap);
        }
    }
}