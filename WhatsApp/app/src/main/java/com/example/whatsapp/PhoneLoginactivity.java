package com.example.whatsapp;

import android.app.ProgressDialog;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginactivity extends AppCompatActivity {

    private Button senVericationCodeButton,VerifyButton;
    private EditText inputPhoneNumber,InputVerifycationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks ;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private ProgressDialog loadingBar;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_loginactivity);

        mAuth=FirebaseAuth.getInstance();

        senVericationCodeButton=findViewById(R.id.sen_ver_code_buuton);
        inputPhoneNumber=findViewById(R.id.phone_nnumber_input);
        VerifyButton=findViewById(R.id.verify_button);
        InputVerifycationCode=findViewById(R.id.Verification_input);
        loadingBar = new ProgressDialog(this);

        senVericationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(PhoneLoginactivity.this, "Numero de equeridotelefono r", Toast.LENGTH_SHORT);
                String phoneNumber = inputPhoneNumber.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(PhoneLoginactivity.this, "Numero de telefono requerido", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(PhoneLoginactivity.this, "pase por aca", Toast.LENGTH_SHORT);
                    loadingBar.setTitle("Verificando telefono ****");
                    loadingBar.setMessage("espere...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber, //phone number to verify
                            60,         //timeout duration
                            TimeUnit.SECONDS,   //Unit of timeout
                            PhoneLoginactivity.this,
                            //Activity(for callback binding)
                            callbacks);

                }

            }

        });
        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senVericationCodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                String verificacionCode = InputVerifycationCode.getText().toString();

                if (TextUtils.isEmpty(verificacionCode)) {
                    Toast.makeText(PhoneLoginactivity.this, "por favor verifcar el codigo de firebAse  ", Toast.LENGTH_SHORT);

                } else {
                    loadingBar.setTitle("Verificando Codigo");
                    loadingBar.setMessage("espere...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificacionCode);

                    signInWithPhoneAuthCredential(credential);
                    Toast.makeText(PhoneLoginactivity.this, "numero de telefono no identificado ", Toast.LENGTH_SHORT);
                }

            }
        });
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                loadingBar.dismiss();
                Toast.makeText(PhoneLoginactivity.this, "Numero de telefono invalido  ", Toast.LENGTH_SHORT);


                senVericationCodeButton.setVisibility(View.VISIBLE);
                inputPhoneNumber.setVisibility(View.VISIBLE);

                VerifyButton.setVisibility(View.INVISIBLE);
                InputVerifycationCode.setVisibility(View.INVISIBLE);
            }
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                loadingBar.dismiss();
                Toast.makeText(PhoneLoginactivity.this, "Invalido Phone number  ", Toast.LENGTH_SHORT);


                senVericationCodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerifycationCode.setVisibility(View.VISIBLE);
            }

        };



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(PhoneLoginactivity.this, "Inicio de secion Correcto ", Toast.LENGTH_SHORT);
                            SendUserToMainActivity();

                        } else {

                            String message=task.getException().toString();
                            Toast.makeText(PhoneLoginactivity.this, "error 404"+message, Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    private void SendUserToMainActivity() {
        Intent intent=new Intent(PhoneLoginactivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
