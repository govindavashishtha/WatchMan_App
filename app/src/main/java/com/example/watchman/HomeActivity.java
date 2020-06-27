package com.example.watchman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.account.WorkAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Button inButton;
    Button outButton;
    Bitmap imageBitmap;
    ImageView imageView;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String phone = getIntent().getExtras().getString("phone");
        textView = findViewById(R.id.textView);

        inButton=findViewById(R.id.in_button);
        inButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        outButton=findViewById(R.id.out_button);
        outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outputOCR = "DL 5S 5252" ;
                // todo : add ocr functionality
                // outputOCR = getTextFromOCR();
                //todo : add the outTime to the Database
            }
        });

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            Log.e("info", "onActivityResult: "+imageBitmap );
           detectTxt();
        }
    }

    private void detectTxt() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                processTxt(firebaseVisionText);
                                Log.e("output text", "onSuccess: "+firebaseVisionText );
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Can't find text...TRY AGAIN",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
    }

    private void processTxt(FirebaseVisionText text) {
        String txt = "";
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(getApplicationContext(), "Can't find text...TRY AGAIN",
                    Toast.LENGTH_LONG).show();
            dispatchTakePictureIntent();
        }
        for (FirebaseVisionText.TextBlock block : text.getTextBlocks()) {
           txt = txt + block.getText();
        }
        Intent intent = new Intent(HomeActivity.this, AddVehicleDetails.class);
        intent.putExtra("VehicleID", txt);
        intent.putExtra("imageBitmap" , imageBitmap);
        startActivity(intent);
    }
}