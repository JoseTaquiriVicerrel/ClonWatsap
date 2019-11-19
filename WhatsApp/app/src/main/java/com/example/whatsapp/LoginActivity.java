package com.example.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Button LoginButton,PhoneLoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeddNewAccountLink, ForgetPasswordLink;

    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitializeFields();

        mAuth=FirebaseAuth.getInstance();

        //currentUser= mAuth.getCurrentUser();

        NeddNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });
    }

    private void AllowUserToLogin() {
        String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Por favor escriba su Email...",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Por favor escriba su Password...",Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Sing In");
            loadingBar.setMessage("Por favor espere...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                SendUserToMainActivity();
                                Toast.makeText(LoginActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }else {
                                String mensaje = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void InitializeFields() {

        LoginButton=(Button)findViewById(R.id.Login_button);
        PhoneLoginButton=(Button)findViewById(R.id.phone_login_button);
        UserEmail=(EditText)findViewById(R.id.login_email);
        UserPassword=(EditText)findViewById(R.id.login_password);
        NeddNewAccountLink=(TextView)findViewById(R.id.Nedd_new_account_link);
        ForgetPasswordLink=(TextView)findViewById(R.id.forget_password_link);

        loadingBar=new ProgressDialog(this);
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null){
            SendUserToMainActivity();
            Toast.makeText(this,"Bienvenidos 2",Toast.LENGTH_SHORT).show();
        }
    }*/

    private void SendUserToMainActivity() {
        Intent mainIntent=new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToRegisterActivity() {
        Intent registerIntent=new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
