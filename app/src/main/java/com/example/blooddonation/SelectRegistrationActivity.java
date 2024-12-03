package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegistrationActivity extends AppCompatActivity {

    private Button donorRegistration,recipientRegistration;
    private TextView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_select_registration );
        donorRegistration=findViewById( R.id.donorregistration );
        recipientRegistration=findViewById( R.id.recipientregistration );
        backButton=findViewById( R.id.backButton );
        donorRegistration.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectRegistrationActivity.this,DonorRegistrationActivity.class);
                startActivity( intent );
            }
        } );

        recipientRegistration.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SelectRegistrationActivity.this,RecipientRegistration.class);
                startActivity( intent );
            }
        } );

        backButton=findViewById( R.id.backButton );
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectRegistrationActivity.this,Loginactivity.class);
                startActivity( intent );
            }
        } );
    }
}