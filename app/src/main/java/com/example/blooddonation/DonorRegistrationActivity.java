package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorRegistrationActivity extends AppCompatActivity {
  private TextView backButton;
  private CircleImageView profile_image;
  private TextInputEditText registerfullname,registerNumber,registerIdNumber,registerEmailId,registerpassword;
  private Spinner bloodtype;
  private Button signUp;



  private Uri resultUri;

  private ProgressDialog progressDialog;
  private FirebaseAuth mAuth;
  private DatabaseReference userDatabaseref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_donor_registration );

        backButton = findViewById( R.id.backButton );
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( DonorRegistrationActivity.this, Loginactivity.class );
                startActivity( intent );
            }
        } );
        profile_image = findViewById( R.id.profile_image );
        registerfullname = findViewById( R.id.registerFullName );
        registerNumber = findViewById( R.id.registerNumber );
        registerIdNumber = findViewById( R.id.registerIdNumber );
        registerEmailId = findViewById( R.id.registerEmailId );
        registerpassword = findViewById( R.id.registerpassword );
        bloodtype = findViewById( R.id.bloodgroupSpinner );
        signUp = findViewById( R.id.registrationButton );

        mAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog( this );


        profile_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Intent.ACTION_PICK );
                intent.setType( "image/*" );
                startActivityForResult( intent, 1 );
            }
        } );

        signUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=registerEmailId.getText().toString().trim();
                final String number=registerNumber.getText().toString().trim();
                final String fullname=registerfullname.getText().toString();
                final String idNumber=registerIdNumber.getText().toString().trim();

                //bloodgroup are type spinner that why we use getSelectedItem();
                final String bloodgroup=bloodtype.getSelectedItem().toString().trim();
                final String password=registerpassword.getText().toString().trim();


                if(TextUtils.isEmpty( email )){
                    registerEmailId.setError( "Email is required" );
                    return;
                }
                if(TextUtils.isEmpty( fullname )){
                     registerfullname.setError( "Name is required" );
                    return;
                }
                if(TextUtils.isEmpty( number )){
                     registerNumber.setError( " Phone Number is required" );
                    return;
                }
                if(TextUtils.isEmpty(  idNumber )){
                     registerIdNumber.setError( "Id Number is required" );
                    return;
                }
                if(bloodgroup.equals( "Select your blood Group" )){
                    Toast.makeText(DonorRegistrationActivity.this, "Select Blood Group", Toast.LENGTH_SHORT ).show();
                    return;
                } if(TextUtils.isEmpty(  password )){
                     registerpassword.setError( "Password is required" );
                    return;
                }
                //after all the filling In the given edittext you want to store the data in
                else {
                    progressDialog.setMessage( "Registering You..." );
                    progressDialog.setCanceledOnTouchOutside( false );
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword( email,password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                String error=task.getException().toString();
                                Toast.makeText( DonorRegistrationActivity.this,"Error"+error,Toast.LENGTH_SHORT ).show();
                                        progressDialog.dismiss();
                            }else{
                                String currentuserId=mAuth.getCurrentUser().getUid();
                                userDatabaseref= FirebaseDatabase.getInstance().getReference()
                                        .child( "users" ).child( currentuserId );

                                HashMap userinfo=new HashMap();
                                userinfo.put( "id",currentuserId );
                                userinfo.put( "name",fullname );
                                userinfo.put( "email",email );
                                userinfo.put( "password",password );
                                userinfo.put( "PhoneNumber",number );
                                userinfo.put( "bloodGroup",bloodgroup );
                                userinfo.put( "type","donor" );
                                userinfo.put( "search" ,"donor"+bloodgroup );

                                userDatabaseref.updateChildren( userinfo ).addOnCompleteListener( new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                      if(task.isSuccessful()){
                                          Toast.makeText( DonorRegistrationActivity.this,"Successfull Registration..",Toast.LENGTH_SHORT ).show();

                                      }else{
                                          Toast.makeText( DonorRegistrationActivity.this,task.getException().toString(),Toast.LENGTH_SHORT ).show();

                                      }

                                      finish();;
                                     // progressDialog.dismiss();
                                    }
                                } );

                                if(resultUri!=null){
                                    final StorageReference filepath= FirebaseStorage.getInstance().getReference()
                                            .child( "profile images" ).child( currentuserId );

                                    Bitmap bitmap=null;
                                    try{
                                        bitmap= MediaStore.Images.Media.getBitmap( getApplication().getContentResolver(),resultUri );
                                    }catch(IOException e){
                                        e.printStackTrace();;
                                    }

                                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                                    bitmap.compress( Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream );
                                    byte[] data=byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask=filepath.putBytes( data );

                                    uploadTask.addOnFailureListener( new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText( DonorRegistrationActivity.this,"Image Upload Failed",Toast.LENGTH_SHORT ).show();

                                        }
                                    } );

                                   uploadTask.addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                       @Override
                                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                           if(taskSnapshot.getMetadata()!=null && taskSnapshot.getMetadata().getReference()!=null){
                                               Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                                               result.addOnSuccessListener( new OnSuccessListener<Uri>() {
                                                   @Override
                                                   public void onSuccess(Uri uri) {
                                                       String imageurl=uri.toString();
                                                       Map newImageMap=new HashMap();
                                                       newImageMap.put( "profilePictureurl",imageurl );

                                                       userDatabaseref.updateChildren( newImageMap ).addOnCompleteListener( new OnCompleteListener() {
                                                           @Override
                                                           public void onComplete(@NonNull Task task) {
                                                               if(task.isSuccessful()){
                                                                   Toast.makeText( DonorRegistrationActivity.this,"Image url is added to database successfully",Toast.LENGTH_SHORT ).show();
                                                               }else{
                                                                   Toast.makeText( DonorRegistrationActivity.this,task.getException().toString(),Toast.LENGTH_SHORT ).show();

                                                               }
                                                           }
                                                       } );
                                                       finish();
                                                   }
                                               } );
                                       }
                                   }
                                   } );
                                   Intent intent=new Intent(DonorRegistrationActivity.this,MainActivity.class);
                                   startActivity( intent );
                                   finish();
                                   progressDialog.dismiss();
                                }

                            }
                        }
                    } );




                }




            }
        } );

    }
    //use for set profile image
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            super.onActivityResult( requestCode, resultCode, data );
            if(requestCode==1 && resultCode==RESULT_OK && data!=null){
                resultUri=data.getData();
                profile_image.setImageURI( resultUri );
            }
        }




}