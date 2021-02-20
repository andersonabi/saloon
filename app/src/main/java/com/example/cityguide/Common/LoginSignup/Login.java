package com.example.cityguide.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cityguide.Databases.SessionManager;
import com.example.cityguide.LocationOwner.RetailerDashboard;
import com.example.cityguide.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumber, password;
    Button login_btn;
    RelativeLayout progressBar;
    CheckBox rememberMe;
    TextInputEditText phoneNumberEditText, passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_login);

        countryCodePicker = findViewById(R.id.login_country_code_picker);
        phoneNumber = findViewById(R.id.login_phone_number);
        password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress_bar);
        rememberMe = findViewById(R.id.remember_me);
        phoneNumberEditText =findViewById(R.id.login_phone_number_editText);
        passwordEditText = findViewById(R.id.login_password_editText);

        SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
        if(sessionManager.checkRememberMe())
        {
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            phoneNumberEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER));
            passwordEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }

    }


    private Boolean validateUsername()
    {
        String val = phoneNumber.getEditText().getText().toString();

        if(val.isEmpty())
        {
            phoneNumber.setError("Field cannot be empty");
            return false;
        }else
            {
                phoneNumber.setError(null);
                phoneNumber.setErrorEnabled(false);
                return true;
            }

    }

    private Boolean validatePassword()
    {
        String val = password.getEditText().getText().toString();
        if(val.isEmpty())
        {
            password.setError("Field cannot be empty");
            return false;
        }else
            {
                password.setError(null);
                password.setErrorEnabled(false);
               return true;
            }

    }


    public void loginUser(View view)
    {
     if(!validateUsername() | !validatePassword())
     {
         return;
     }else
         {
             isUser();
         }
    }


    private void isUser()
    {
        final String userEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();


        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("phoneNo").equalTo(userEnteredPhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredPhoneNumber).child("password").getValue(String.class);

                    if(passwordFromDB.equals(userEnteredPassword))
                    {

                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);

                      String usernameFromDB = dataSnapshot.child(userEnteredPassword).child("username").getValue(String.class);
                      String fullnameFromDB = dataSnapshot.child(userEnteredPassword).child("fullName").getValue(String.class);
                      String phoneNoFromDB = dataSnapshot.child(userEnteredPassword).child("phoneNo").getValue(String.class);
                      String emailFromDB = dataSnapshot.child(userEnteredPassword).child("email").getValue(String.class);

                       Intent intent = new Intent(getApplicationContext(),RetailerDashboard.class);
                       startActivity(intent);
                       progressBar.setVisibility(View.GONE);

                    }else
                        {
                            progressBar.setVisibility(View.GONE);
                            password.setError("Wrong Password");
                            password.requestFocus();
                        }

                }else {
                    progressBar.setVisibility(View.GONE);
                    phoneNumber.setError("Invalid Phonenumber");
                    phoneNumber.requestFocus();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }




    public void letTheUserLoggedIn(View view) {


        if(!isConnected(this))
        {
            showCustomDialog();
        }


        progressBar.setVisibility(View.VISIBLE);

        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
         final String _password = password.getEditText().getText().toString().trim();

        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }

        final String _completePhoneNumber = "+" + countryCodePicker.getFullNumber() + _phoneNumber;

        if(rememberMe.isChecked()){

            SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
            sessionManager.createrRememberMeSession(_phoneNumber,_password);
        }



        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    String systemPassword = dataSnapshot.child(_completePhoneNumber).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _fullname = dataSnapshot.child(_completePhoneNumber).child("fullName").getValue(String.class);
                        String _username = dataSnapshot.child(_completePhoneNumber).child("username").getValue(String.class);
                        String _email = dataSnapshot.child(_completePhoneNumber).child("email").getValue(String.class);
                        String _phoneNo = dataSnapshot.child(_completePhoneNumber).child("phoneNo").getValue(String.class);
                        String _password = dataSnapshot.child(_completePhoneNumber).child("password").getValue(String.class);

                        SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_USERSESSION);
                        sessionManager.createLoginSession(_fullname,_username,_email,_phoneNo,_password);

                        startActivity(new Intent(getApplicationContext(), RetailerDashboard.class));
                        progressBar.setVisibility(View.GONE);

                     //   Toast.makeText(Login.this, _fullname+"\n"+_email+"\n"+_phoneNo, Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                } else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Data does not exist!", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),RetailerStartUpScreen.class));
                        finish();
                    }
                });

    }


    private boolean isConnected(Login login) {

        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected()) )
        {
            return true;
        }
        else
            {
                return false;
            }


    }


    public void callForgetPassword(View view)
    {
        startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
    }


}