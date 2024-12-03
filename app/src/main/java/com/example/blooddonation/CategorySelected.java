package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.blooddonation.Adapter.UserAdapter;
import com.example.blooddonation.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CategorySelected extends AppCompatActivity {
  private Toolbar toolbar;
  private RecyclerView recyclerView;
  private List<User> userList;
  private UserAdapter adapter;
  private  String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_category_selected );
        toolbar =findViewById( R.id.toolbar );

//         getSupportActionBar().setDisplayShowHomeEnabled( true );
//         getSupportActionBar().setDisplayHomeAsUpEnabled( true );

         recyclerView =findViewById( R.id.recyclerView );
        LinearLayoutManager layoutManager=new LinearLayoutManager( this );
        layoutManager.setStackFromEnd( true );
        layoutManager.setReverseLayout( true );
        recyclerView.setLayoutManager( layoutManager );

        userList=new ArrayList<>();
        adapter=new UserAdapter( this,userList );
        recyclerView.setAdapter( adapter);


        if(getIntent().getExtras()!=null){
            title=getIntent().getStringExtra("group");

            getSupportActionBar().setTitle( "Blood Group " +title);
            if(title.equals( "Compatible with me" )){
                getCompatibleUser();
                getSupportActionBar().setTitle( "Compatible with me" );
            }
            else{
                readuser();
            }

        }


    }

    private void getCompatibleUser() {
      //  DatabaseReference re=FirebaseDatabase.getInstance().getReference().child( "users" ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() );
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child( "users" ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result;
                String type=snapshot.child( "type" ).getValue().toString();
                if(type.equals( "donor" )){
                    result="receipient";
                }else{
                    result="donor";

                }

                String bloodgroup=snapshot.child( "bloodgroup" ).getValue().toString();

                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child( "users" );
                Query query=reference.orderByChild( "search" ).equalTo( result+bloodgroup );
                reference.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            User user=dataSnapshot.getValue(User.class);
                            userList.add( user );

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }

    private void readuser() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child( "users" ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result;
                String type=snapshot.child( "type" ).getValue().toString();
                if(type.equals( "donor" )){
                    result="recipient";
                }else{
                    result="donor";
                }

                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child( "users" );
                Query query=reference.orderByChild( "search" ).equalTo( result+title );
                reference.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      userList.clear();
                      for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                          User user=dataSnapshot.getValue(User.class);
                          userList.add( user );

                      }
                      adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent=new Intent( CategorySelected.this,MainActivity.class);
                startActivity( intent);
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected( item );
        }

    }


}