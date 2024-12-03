package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.blooddonation.Adapter.UserAdapter;
import com.example.blooddonation.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView nav_profile_image;
    private TextView nav_fullname,nav_email,nav_bloodgrop,nav_type;

private RecyclerView recyclerView;
private List<User> userList;
private UserAdapter userAdapter;
private ProgressBar progressBar;

    private DatabaseReference databaseReference;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        toolbar=findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( "Blood Donation App" );

        drawerLayout=findViewById( R.id.drawerlayout );

       progressBar=findViewById( R.id.progressbar );
       recyclerView=findViewById( R.id.recyclerView );

       LinearLayoutManager layoutManager=new LinearLayoutManager( this );
       layoutManager.setReverseLayout( true );
       layoutManager.setStackFromEnd( true );
       recyclerView.setLayoutManager( layoutManager );

       userList=new ArrayList<>();
       userAdapter=new UserAdapter( MainActivity.this,userList );

       recyclerView.setAdapter( userAdapter );
       DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child( "users" ).child( FirebaseAuth.getInstance().getUid() );

       reference.addValueEventListener( new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String type =snapshot.child( "type" ).getValue().toString();
               if(type.equals( "donor" )){
                   readReceipient();

               }else{
                   readDonors();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       } );

        navigationView=findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle( MainActivity.this ,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_closed);

        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();

        nav_profile_image=navigationView.getHeaderView( 0 ).findViewById(R.id.nav_user_image );
        nav_fullname=navigationView.getHeaderView( 0 ).findViewById( R.id.nav_userFullname );

        nav_email=navigationView.getHeaderView( 0).findViewById( R.id.nav_user_email );

        nav_bloodgrop=navigationView.getHeaderView( 0).findViewById( R.id.nav_user_bloodgroup );
        nav_type=navigationView.getHeaderView( 0).findViewById( R.id.nav_user_type );

        //fetch the data from firebase
        databaseReference= FirebaseDatabase.getInstance().getReference().child( "users" ).child(
                FirebaseAuth.getInstance().getUid()
        );
      databaseReference.addValueEventListener( new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                  String name=snapshot.child( "name" ).getValue().toString();
                  nav_fullname.setText( name );
                  String  email=snapshot.child( "email" ).getValue().toString();
                   nav_email.setText(email );

                   String bloodgroup=snapshot.child( "bloodGroup" ).getValue().toString();
                   nav_bloodgrop.setText( bloodgroup );

                   String type=snapshot.child( "type" ).getValue().toString();
                   nav_type.setText( type );

                   if(snapshot.hasChild("profilePictureurl")) {
                       String imageurl = snapshot.child( "profilePictureurl" ).getValue().toString();
                       Glide.with( getApplicationContext() ).load( imageurl ).into( nav_profile_image );
                   }
                   else {
                       nav_profile_image.setImageResource(  R.drawable.profile_image );
                   }

              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      } );


    }
    private void readReceipient(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child( "users" );

        Query query=reference.orderByChild( "type" ).equalTo( "recipient" );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    userList.add( user );

                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility( View.GONE );

                if(userList.isEmpty()){
                    Toast.makeText( MainActivity.this,"No Receipients",Toast.LENGTH_SHORT ).show();
                    progressBar.setVisibility( View.GONE );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }

    public void readDonors(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child( "users" );

        Query query=reference.orderByChild( "type" ).equalTo( "donor" );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    userList.add( user );

                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility( View.GONE );

                if(userList.isEmpty()){
                    Toast.makeText( MainActivity.this,"No Donors",Toast.LENGTH_SHORT ).show();
                    progressBar.setVisibility( View.GONE );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.apositive:
                Intent intent2=new Intent(MainActivity.this,CategorySelected.class);
                intent2.putExtra( "group","A+" );
                startActivity( intent2);
                return true;
            case R.id.anegative:
                Intent intent3=new Intent(MainActivity.this,CategorySelected.class);
                intent3.putExtra( "group","A-" );
                startActivity( intent3);
                return true;
            case R.id.bpositive:
                Intent intent4=new Intent(MainActivity.this,CategorySelected.class);
                intent4.putExtra( "group","B+" );
                startActivity( intent4);
                return true;
            case R.id.bnegative:
                Intent intent5=new Intent(MainActivity.this,CategorySelected.class);
                intent5.putExtra( "group","B-" );
                startActivity( intent5);
                return true;
            case R.id.abpositive:
                Intent intent6=new Intent(MainActivity.this,CategorySelected.class);
                intent6.putExtra( "group","AB+" );
                startActivity( intent6);
                return true;
            case R.id.abnegative:
                Intent intent7=new Intent(MainActivity.this,CategorySelected.class);
                intent7.putExtra( "group","AB-" );
                startActivity( intent7);
                return true;
            case R.id.opositive:
                Intent intent8=new Intent(MainActivity.this,CategorySelected.class);
                intent8.putExtra( "group","O+" );
                startActivity( intent8);
                return true;
            case R.id.onegative:
                Intent intent9=new Intent(MainActivity.this,CategorySelected.class);
                intent9.putExtra( "group","O-" );
                startActivity( intent9);
                return true;
            case R.id.compatible:
                Intent intent10=new Intent(MainActivity.this,CategorySelected.class);
                intent10.putExtra( "group","Compatible with me" );
                startActivity( intent10 );
                return  true;

            case R.id.profile:
                Intent intent=new Intent(MainActivity.this,profileActivity.class);
                startActivity( intent);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent( MainActivity.this,  Loginactivity.class);
                startActivity(intent1);
                finish();
                return true;


        }
        drawerLayout.closeDrawer( GravityCompat.START );
        return true;

    }
}