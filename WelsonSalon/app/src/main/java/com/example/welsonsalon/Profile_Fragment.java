package com.example.welsonsalon;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile_Fragment extends Fragment {
    TextView user_Name,user_email;
   Button EditProfile,Appoint_btn,Schedule_btn;
    ImageView user_image;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance("https://welson-salon-backend-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        String UserId = auth.getCurrentUser().getUid();
        GetImageFromStorage();
        //TextView
        user_Name = view.findViewById(R.id.user_Name);
        user_email = view.findViewById(R.id.user_email);
        user_image = view.findViewById(R.id.user_image);
        //authentication of user
        ref.child(auth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String current_username = snapshot.child(auth.getCurrentUser().getUid()).child("FirstName").getValue(String.class);
                    String current_Lastname = snapshot.child(auth.getCurrentUser().getUid()).child("LastName").getValue(String.class);
                    String current_user_email = snapshot.child(auth.getCurrentUser().getUid()).child("Email").getValue(String.class);

                    user_Name.setText(current_username+" "+current_Lastname);
                    user_email.setText(current_user_email);
                }
                if(user_Name.getText().toString().isEmpty() ||user_email.getText().toString().isEmpty() ){
                    getDialogInfo("Warning","You Will need to Update your profile to get values!",new Intent(getContext(),EditProfileActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error","Error = "+error);
            }
        });


        EditProfile = view.findViewById(R.id.linkToEditprofile);
        EditProfile.setOnClickListener(v -> startActivity(new Intent(getContext(),EditProfileActivity.class)));

        Appoint_btn = view.findViewById(R.id.appoint_button);
        Schedule_btn = view.findViewById(R.id.schedule_button);

        Appoint_btn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(),AppointmentActivity.class));

        });

        Schedule_btn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(),Schedule_activity.class));


        });



        return view;
    }
    private void GetImageFromStorage() {
        StorageReference downloadImageRef = FirebaseStorage.getInstance().getReference().child("Users_image");
        String userId = auth.getCurrentUser().getUid();
        downloadImageRef.child(userId).getDownloadUrl().addOnSuccessListener(uri -> {
            Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            Picasso.get().load(uri).into(user_image);
        }).addOnFailureListener(e ->{
            getDialogInfo("Hello","You will need to update your profile image",new Intent(getContext(),EditProfileActivity.class));
        });
    }
    public void getDialogInfo(String header,String des,Intent intent){
        Dialog dialog =new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();
        TextView dialog_header = dialog.findViewById(R.id.dialog_header);
        TextView dialog_dis = dialog.findViewById(R.id.dialog_text);
        Button dialog_btn = dialog.findViewById(R.id.dialog_button);
        ImageView dialog_close = dialog.findViewById(R.id.CloseDialog);

        dialog_header.setText(header);
        dialog_dis.setText(des);
        dialog_btn.setOnClickListener(v ->{ dialog.dismiss();
            startActivity(intent);});
        dialog_close.setOnClickListener(v -> dialog.dismiss());
    }
}

