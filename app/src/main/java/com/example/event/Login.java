package com.example.event;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    //Variables
    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNo, password;
    RelativeLayout progressbar;
    CheckBox rememberMe;
    TextInputEditText phoneNoEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNo = findViewById(R.id.login_phone_number);
        password = findViewById(R.id.login_password);
        rememberMe = findViewById(R.id.rememberMe);
        progressbar = findViewById(R.id.login_progress_bar);
        phoneNoEditText = findViewById(R.id.edit_phone_number);
        passwordEditText = findViewById(R.id.edit_password);
        progressbar.setVisibility(View.GONE);

        //Check if phoneNo and password is already saved in Shared Preference
        SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            phoneNoEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENO));
            passwordEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }
    }

    public void letTheUserLoggedIn(View view) {
        //Check Internet
        if (!isConnected(this)) {
            showCustomeDialog();
        }
        //Validate PhoneNo
        if (!valitdateFields()) {
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        //Get data
        String _phoneNo = phoneNo.getEditText().getText().toString().trim();
        final String _password = password.getEditText().getText().toString().trim();
        if (_phoneNo.charAt(0) == '0') {
            _phoneNo = _phoneNo.substring(1);
        }
        final String _completePhoneNo = "+" + countryCodePicker.getFullNumber() + _phoneNo;

        if (rememberMe.isChecked()) {
            SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_REMEMBERME);
            sessionManager.createRememberMeSession(_phoneNo, _password);
        }

        //Check database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    phoneNo.setError(null);
                    phoneNo.setErrorEnabled(false);

                    String systemPassword = dataSnapshot.child(_completePhoneNo).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        //Get users data from database
                        String _fullname = dataSnapshot.child(_completePhoneNo).child("fullname").getValue(String.class);
                        String _address = dataSnapshot.child(_completePhoneNo).child("address").getValue(String.class);
                        String _email = dataSnapshot.child(_completePhoneNo).child("email").getValue(String.class);
                        String _phoneNo = dataSnapshot.child(_completePhoneNo).child("phoneNo").getValue(String.class);
                        String _dateOfBirth = dataSnapshot.child(_completePhoneNo).child("date").getValue(String.class);
                        String _password = dataSnapshot.child(_completePhoneNo).child("password").getValue(String.class);
                        String _gender = dataSnapshot.child(_completePhoneNo).child("gender").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), HomeDashboard.class);
                        intent.putExtra("fullname", _fullname);
                        intent.putExtra("email", _email);
                        intent.putExtra("address", _address);
                        intent.putExtra("date", _dateOfBirth);
                        intent.putExtra("phoneNo", _phoneNo);

                        //Create Session
                        SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_USERSESSION);
                        sessionManager.createLoginSession(_address, _fullname, _email, _phoneNo, _dateOfBirth, _password, _gender);

                        startActivity(intent);

                        progressbar.setVisibility(View.GONE);

                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "No such user exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) ||
                (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Please connect to the internet to proceed")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            }
        });
    }

    private boolean valitdateFields() {
        String _phoneNo = phoneNo.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();
        if (_phoneNo.isEmpty()) {
            phoneNo.setError("Field can not be empty");
            phoneNo.requestFocus();
            return false;
        } else if (_password.isEmpty()) {
            password.setError("Field can not be empty");
            password.requestFocus();
            return false;
        } else {
            phoneNo.setError(null);
            password.setError(null);
            phoneNo.setErrorEnabled(false);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void callForgotPassword(View view) {
        startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
    }

    public void SignUpFromLogin(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        finish();
    }
}