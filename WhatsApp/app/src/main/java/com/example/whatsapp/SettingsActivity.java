package com.example.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button udateAccountSetings;
    private EditText userName,userStatus;
    private CircleImageView userProflieImage;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        InitializeFields();
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();

        userName.setVisibility(View.INVISIBLE);

        udateAccountSetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });

        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {

        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image")){

                            String retrieveUserName=dataSnapshot.child("name").getValue().toString();
                            String retrievesStatus=dataSnapshot.child("status").getValue().toString();
                            String retrieveprofileImage=dataSnapshot.child("image").getValue().toString();

                            userName.setText(retrieveUserName);
                            userStatus.setText(retrievesStatus);
                        }else if(dataSnapshot.exists() && dataSnapshot.hasChild("name")){

                            String retrieveUserName=dataSnapshot.child("name").getValue().toString();
                            String retrievesStatus=dataSnapshot.child("status").getValue().toString();

                            userName.setText(retrieveUserName);
                            userStatus.setText(retrievesStatus);

                        }else{
                            userName.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingsActivity.this,"Por establezca su informacion de perfil",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void updateSettings() {
        String setUserName=userName.getText().toString();
        String setStatus=userStatus.getText().toString();

        if(TextUtils.isEmpty(setUserName)){
            Toast.makeText(this,"Por favor escriba su nombre",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(setStatus)){
            Toast.makeText(this,"Por favor escriba su Estado",Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String,String> profileMap=new HashMap<>();
                profileMap.put("uid",currentUserID);
                profileMap.put("name",setUserName);
                profileMap.put("status", setStatus);

             RootRef.child("Users").child(currentUserID).setValue(profileMap)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {

                             if(task.isSuccessful()){
                                 SendUserToMainActivity();
                                 Toast.makeText(SettingsActivity.this,"Perfil Guardado Correctamente",Toast.LENGTH_SHORT).show();
                             }else{
                                 String mensaje=task.getException().toString();
                                 Toast.makeText(SettingsActivity.this,"Error: " + mensaje,Toast.LENGTH_SHORT).show();
                             }

                         }
                     });
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent=new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void InitializeFields() {

        udateAccountSetings=(Button)findViewById(R.id.update_settings_button);
        userName=(EditText)findViewById(R.id.set_user_name);
        userStatus=(EditText)findViewById(R.id.set_profile_status);
        userProflieImage=(CircleImageView)findViewById(R.id.set_profile_image);
    }
}
