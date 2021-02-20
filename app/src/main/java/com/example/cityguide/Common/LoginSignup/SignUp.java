package com.example.cityguide.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    ImageView backBtn;
    Button next, login;
    TextView titleText;

    TextInputLayout regfullName, regusername, regemail, regpassword,regphoneNo;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_sign_up);


        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);


        regfullName = findViewById(R.id.signup_fullname);
        regusername = findViewById(R.id.signup_username);
        regemail = findViewById(R.id.signup_email);
        regpassword = findViewById(R.id.signup_password);
        regphoneNo = findViewById(R.id.signup_phno);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              rootNode = FirebaseDatabase.getInstance();
              reference = rootNode.getReference("Users");

              String fullname = regfullName.getEditText().getText().toString();
              String username = regusername.getEditText().getText().toString();
              String email = regemail.getEditText().getText().toString();
              String password = regpassword.getEditText().getText().toString();
              String phoneno = regphoneNo.getEditText().getText().toString();

              UserHelperClass helperClass = new UserHelperClass(fullname,username,email,phoneno,password);

                reference.child(phoneno).setValue(helperClass);
                Toast.makeText(SignUp.this,"Registerd Successfully!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });


    }

    public void callNextSignupScreen(View view) {

        Intent intent = new Intent(getApplicationContext(), SignUp2ndClass.class);

        if(!validateFullName() | !validateUsername() | !validateEmail() | !validatePassword()| !validatePhoneNumber())
        {
            return;
        }

    }

    private boolean validateFullName() {
        String val = regfullName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            regfullName.setError("Field can not be empty");
            return false;
        } else {
            regfullName.setError(null);
            regfullName.setErrorEnabled(false);
            return true;
        }

    }


    private boolean validateUsername() {
        String val = regusername.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            regusername.setError("Field can not be empty");
            return false;
        }else if(val.length() > 20)
        {
            regusername.setError("Username is to large!");
            return false;
        }
        else if(!val.matches(checkspaces))
        {
            regusername.setError("No white spaces are allowed");
            return false;
        }
        else {
            regusername.setError(null);
            regusername.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateEmail()
    {
        String val = regemail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regemail.setError("Field can not be empty");
            return false;
        }
        else if(!val.matches(checkEmail))
        {
            regemail.setError("Invalid Email");
            return false;
        }
        else {
            regemail.setError(null);
            regemail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword()
    {
        String val = regpassword.getEditText().getText().toString().trim();


        if (val.isEmpty()) {
            regpassword.setError("Field can not be empty");
            return false;
        }

        else {
            regpassword.setError(null);
            regpassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhoneNumber()
    {
        String val = regphoneNo.getEditText().getText().toString().trim();


        if (val.isEmpty()) {
            regphoneNo.setError("Field can not be empty");
            return false;
        }

        else {
            regphoneNo.setError(null);
            regphoneNo.setErrorEnabled(false);
            return true;
        }
    }


}