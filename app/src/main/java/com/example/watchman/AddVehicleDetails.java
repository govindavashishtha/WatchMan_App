package com.example.watchman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AddVehicleDetails extends AppCompatActivity {
    EditText checkIn , vehicleNum , appartmentDetails , name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_details);
        String vehicleID = getIntent().getExtras().getString("VehicleID");

        final String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());

        checkIn = findViewById(R.id.check_in_details);
        vehicleNum = findViewById(R.id.vehicle_number);
        vehicleNum.setText(vehicleID);
        appartmentDetails = findViewById(R.id.appartment_details);
        name = findViewById(R.id.name);
        checkIn.setEnabled(false);
        checkIn.setText(currentDateTimeString);

        Button save = findViewById(R.id.save_details);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                HashMap<String , String> map;
                map = new HashMap<>();
                map.put("VehicleNumber" , vehicleNum.getText().toString());
                map.put("CheckInTime" , currentDateTimeString);
                map.put("AppartmentDetails" , appartmentDetails.getText().toString());
                map.put("Name", name.getText().toString());
                FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child(timeStamp).setValue(map);
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}