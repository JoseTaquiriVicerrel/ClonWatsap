package com.example.whatsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneLoginactivity extends AppCompatActivity {

    private Button senVericationCodeButton,VerifyButton;
    private EditText inputPhoneNumber,InputVerifycationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_loginactivity);
        senVericationCodeButton=findViewById(R.id.sen_ver_code_buuton);
        VerifyButton=findViewById(R.id.verify_button);
        inputPhoneNumber=findViewById(R.id.phone_nnumber_input);
        InputVerifycationCode=findViewById(R.id.Verification_input);
        senVericationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senVericationCodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerifycationCode.setVisibility(View.VISIBLE);
            }
        });
    }
}
