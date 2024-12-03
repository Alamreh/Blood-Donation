package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loginactivity extends AppCompatActivity {
    private TextView backButton;
    private TextInputEditText loginemail,loginpassword;
    private TextView forgetPassword;
    private Button  loginBtn;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    //check user is login or logout

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_loginactivity );
        mAuth=FirebaseAuth.getInstance();

        //checkuser is already loged in or not
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(Loginactivity.this,MainActivity.class);
                    startActivity( intent );
                    finish();

                }

            }
        };

        backButton=findViewById( R.id.backButton );
         backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Loginactivity.this,SelectRegistrationActivity.class);
                startActivity( intent );
            }
        } );
         loginemail=findViewById( R.id.loginemail );
         loginpassword=findViewById( R.id.loginpassword );
         forgetPassword=findViewById( R.id.forgetpassword );
          loginBtn=findViewById( R.id.signIn );
          progressDialog=new ProgressDialog(  this);
          mAuth=FirebaseAuth.getInstance();


          loginBtn.setOnClickListener( new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  final String email=loginemail.getText().toString();
                  final String password=loginpassword.getText().toString();

                  if(TextUtils.isEmpty( email )){
                      loginemail.setError( "Email is required" );
                  }
                  if(TextUtils.isEmpty( password )){
                      loginpassword.setError( "password is required" );
                  }


                  else{
                      progressDialog.setMessage( "Log is in Progress" );
                      progressDialog.setCanceledOnTouchOutside( false );
                      progressDialog.show();

                      //autheticate the user

                      mAuth.signInWithEmailAndPassword( email,password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                              if(task.isSuccessful()){
                                  Toast.makeText( Loginactivity.this,"Login Successfull",Toast.LENGTH_SHORT ).show();
                                  Intent intent=new Intent(Loginactivity.this,MainActivity.class);
                                  startActivity( intent );
                                  finish();
                              }else{
                                  Toast.makeText( Loginactivity.this,task.getException().toString(),Toast.LENGTH_SHORT ).show();
                              }

                              progressDialog.dismiss();
                          }
                      } );

                  }
              }
          } );


    }
    //user is jump to mainActivity or not

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener( mAuthStateListener );
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener( mAuthStateListener );
    }
}