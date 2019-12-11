package com.example.whatsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {

    private Toolbar mTollbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;

    /////Firebase
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        RootRef= FirebaseDatabase.getInstance().getReference();

        mTollbar=(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mTollbar);
        getSupportActionBar().setTitle("WhatsApp");

        myViewPager=(ViewPager)findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter=new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout=(TabLayout)findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null){
            SendUserToLoginActivity();

        }else{
           // updateUserStatus("online");
            VerifyUserExistance();
        }

    }
    @Override
    protected void onStop()
    {
        super.onStop();

        if (currentUser != null)
        {
            //updateUserStatus("offline");
        }
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (currentUser != null)
        {
            //updateUserStatus("offline");
        }
    }
    private void VerifyUserExistance() {
        String currentUserID= mAuth.getCurrentUser().getUid();

        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("name").exists()){
                    Toast.makeText(MainActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();
                }else{
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent=new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.main_logout_options){
            mAuth.signOut();
            SendUserToLoginActivity();
        }

        if(item.getItemId()==R.id.main_settings_options){
            SendUserToSettingsActivity();
        }

        if(item.getItemId()==R.id.main_create_group_friends){
            RequestNewGroup();
        }

        if(item.getItemId()==R.id.main_options_friends){
            SendUserToFinfriensActivity();
        }
        return true;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Nombre de Grupo: ");

        final EditText groupNameField=new EditText(MainActivity.this);
        groupNameField.setHint("e.g Coding");
        builder.setView(groupNameField);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName=groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this,"Por favor escriba el nombre del grupo...",Toast.LENGTH_SHORT).show();
                }else{
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName=groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    dialog.cancel();
                }
            }
        });

        builder.show();

    }

    private void CreateNewGroup(final String groupName) {
        RootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,groupName +" a sido creado",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SendUserToSettingsActivity() {
        Intent settingsIntent=new Intent(MainActivity.this, SettingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }


    private void SendUserToFinfriensActivity() {
        Intent Buscaamigo=new Intent(MainActivity.this, FindFriends.class);
        startActivity(Buscaamigo);
        finish();
    }
    /*
    private void updateUserStatus(String state)
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time", saveCurrentTime);
        onlineStateMap.put("date", saveCurrentDate);
        onlineStateMap.put("state", state);

        RootRef.child("Users").child(currentUserID).child("userState").updateChildren(onlineStateMap);

    }*/

}
