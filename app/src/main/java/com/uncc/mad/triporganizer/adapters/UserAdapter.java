package com.uncc.mad.triporganizer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.activities.AddUsers;
import com.uncc.mad.triporganizer.activities.TripProfileActivity;
import com.uncc.mad.triporganizer.models.UserProfile;

import java.util.ArrayList;
import java.util.Collections;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    public static ArrayList<UserProfile> userList;
    ArrayList<String> tripMembers;
    private Context c;

    public UserAdapter(ArrayList<UserProfile> userList, Context context,ArrayList<String> tripMembers) {
        this.userList = userList;
        c = context;
        this.tripMembers = tripMembers;
    //    this.addedUsers= addedUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        final UserProfile u1 = userList.get(position);
        holder.userListFullName.setText(u1.getFirstName() + " " + u1.getLastName());
        if (u1.getImageUrl() == null || u1.getImageUrl() == "") {
            holder.userphoto.setImageDrawable(c.getDrawable(R.drawable.default_avatar_icon));
        } else {
            Picasso.get().load(u1.getImageUrl()).into(holder.userphoto);
        }

        if(tripMembers.indexOf(u1.getUserUID()) != -1)
            holder.addUser.setText("Remove");
            else
                holder.addUser.setText("Add");



        if (u1.getUserUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.addUser.setVisibility(View.INVISIBLE);
        }

        holder.addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.addUser.getText().toString().equals("Add")) {
                 //   if(AddUsers.addedUsers.indexOf(u1) == -1)
                    if(!AddUsers.addedUsers.contains(u1)){
                        AddUsers.addedUsers.add(u1);
                    }
                    holder.addUser.setText("Remove");
                    holder.canBeAdded = false;// addflag =false;
                } else {
                    int removeObjectIndex = -1;
                    for(int i=0;i<AddUsers.addedUsers.size();i++){
                        if(AddUsers.addedUsers.get(i).getUserUID().equals(u1.getUserUID())){
                            removeObjectIndex = i;
                            break;
                        }
                    }
                    AddUsers.addedUsers.remove(removeObjectIndex);
                    holder.canBeAdded = true;
                    holder.addUser.setText("Add");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView userListFullName;
        public Button addUser;
        public ImageView userphoto;
        public Boolean canBeAdded;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            canBeAdded = true;
            userListFullName = itemView.findViewById(R.id.list_user_userName);
            addUser = itemView.findViewById(R.id.userAddBtn);
            userphoto = itemView.findViewById(R.id.userImage);
        }
    }
}

