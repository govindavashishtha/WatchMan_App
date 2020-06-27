package com.example.watchman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    Button loginBTN;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String phone = firebaseUser.getPhoneNumber();
            Log.e("<<<<<<<<<<<<<<<<", "Activity main: >>>" + phone);
            Intent myIntent = new Intent(MainActivity.this  ,   HomeActivity.class);
            myIntent.putExtra("phone",phone);
            MainActivity.this.startActivity(myIntent);
        }else{
            setContentView(R.layout.activity_main);
            loginBTN = findViewById(R.id.login_btn);
            loginBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAuthentication();
                }
            });
        }
    }


    private void startAuthentication() {

        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("IN").build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
//                        .setLogo(R.drawable.app_logo)
                        .build(), RC_SIGN_IN
        );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Successfully signed in

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final String phone =firebaseUser.getPhoneNumber();
            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(!snapshot.child(phone).exists()){
                       HashMap<String , String> map ;
                       map = new HashMap<>();
                       map.put("Phone No" , phone);
                       rootRef.child(phone).setValue(map);
                   }
                   Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                   intent.putExtra("phone", phone);
                   startActivity(intent);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });


        } else {
            Log.e("ERRor", "onActivityResult: SignIn failed" );
        }
    }
}