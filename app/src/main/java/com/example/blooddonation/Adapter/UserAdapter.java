package com.example.blooddonation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blooddonation.Model.User;
import com.example.blooddonation.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.viewHolder> {
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from( context ).inflate( R.layout.user_display_layout ,parent,false);
         return new viewHolder( view );
    }



    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final User user=userList.get( position );
        holder.usertype.setText( user.getType());
        if(user.getType().equals( "donor" )){
            holder.emailNow.setVisibility( View.GONE );
        }
        holder.username.setText( user.getName() );
        holder.useremail.setText( user.getEmail() );
        holder.userphoneNumber.setText( user.getPhoneNumber() );
        holder.userbloodGroup.setText( user.getBloodGroup() );

        Glide.with( context ).load( user.getProfilePictureurl() ).into( holder.userProfileImage );


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {

        return userList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public CircleImageView userProfileImage;
        public TextView usertype,username,useremail,userphoneNumber,userbloodGroup;
        public Button emailNow;


        public viewHolder(@NonNull View itemView) {
            super( itemView );
            userProfileImage=itemView.findViewById( R.id.user_profile_image );
            usertype=itemView.findViewById( R.id.usertype );
            username=itemView.findViewById( R.id.username );
            useremail=itemView.findViewById( R.id.useremail );
           userphoneNumber=itemView.findViewById( R.id.userphoneNumber );
            userbloodGroup=itemView.findViewById( R.id.userbloodgroup );
            emailNow=itemView.findViewById( R.id.emailNow );

        }
    }
}
