package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView type,name,email,idNumber,phoneNumber,bloodGroup;
    private CircleImageView imageView;
    private Button backButton;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );


        toolbar=findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( "My Profile" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );


        type=findViewById( R.id.type );
        imageView=findViewById( R.id.profile_image );
        name=findViewById( R.id.fullname );
        email=findViewById( R.id.email );
        idNumber=findViewById( R.id.idnumber );
        bloodGroup=findViewById( R.id.bloodgroup );
        phoneNumber=findViewById( R.id.phoneNumber );
        backButton=findViewById( R.id.button_back );
        reference= FirebaseDatabase.getInstance().getReference().child( "users" ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() );

        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    type.setText( snapshot.child( "type" ).getValue().toString() );
                    name.setText( snapshot.child( "name" ).getValue().toString() );
                    email.setText( snapshot.child( "email" ).getValue().toString() );
                    idNumber.setText( snapshot.child( "id" ).getValue().toString() );
                    bloodGroup.setText( snapshot.child( "bloodGroup" ).getValue().toString() );
                    phoneNumber.setText( snapshot.child( "PhoneNumber" ).getValue().toString() );

                    Glide.with( getApplicationContext() ).load(snapshot.child( "profilePictureurl" ).getValue().toString() ).into( imageView );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(profileActivity.this,MainActivity.class);
                startActivity( intent);
                finish();
            }
        } );


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent=new Intent(profileActivity.this,MainActivity.class);
                startActivity( intent);
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected( item );
        }

    }
}