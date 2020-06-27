package com.example.watchman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    Button inButton;
    Button outButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String phone = getIntent().getExtras().getString("phone");

        inButton=findViewById(R.id.in_button);
        inButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outputOCR = "DL 5S 5252" ;
                // todo : add ocr functionality
               // outputOCR = getTextFromOCR();
                Intent intent = new Intent(HomeActivity.this, AddVehicleDetails.class);
                intent.putExtra("VehicleID", outputOCR);
                startActivity(intent);
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
}