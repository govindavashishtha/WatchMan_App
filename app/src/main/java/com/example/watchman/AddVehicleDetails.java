package com.example.watchman;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AddVehicleDetails extends AppCompatActivity {
    EditText checkIn , vehicleNum , apartmentDetails , name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_details);
        String vehicleID = getIntent().getExtras().getString("VehicleID");
        Bitmap imageBitmap = (Bitmap) getIntent().getParcelableExtra("ImageBitmap");

        final String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());
        ImageView vehicleImage = findViewById(R.id.car_plate_photo);
        vehicleImage.setImageBitmap(imageBitmap);
        checkIn = findViewById(R.id.check_in_details);
        vehicleNum = findViewById(R.id.vehicle_number);
        vehicleNum.setText(vehicleID);
        apartmentDetails = findViewById(R.id.appartment_details);
        name = findViewById(R.id.name);
        checkIn.setEnabled(false);
        checkIn.setText(currentDateTimeString);

        Button save = findViewById(R.id.save_details);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()) {
                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    HashMap<String, String> map;
                    map = new HashMap<>();
                    map.put("VehicleNumber", vehicleNum.getText().toString());
                    map.put("CheckInTime", currentDateTimeString);
                    map.put("ApartmentDetails", apartmentDetails.getText().toString());
                    map.put("Name", name.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child(timeStamp).setValue(map);
                    Toast.makeText(getApplicationContext(), "Information Added",
                            Toast.LENGTH_LONG).show();
                    onBackPressed();
                }else{
                    Toast.makeText(getApplicationContext(), "Please fill up all the details",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private boolean validateInputs(){
      if(vehicleNum.getText().toString().equals("")||apartmentDetails.getText().toString().equals("")||name.getText().toString().equals("")){
          return false;
      }
      return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}