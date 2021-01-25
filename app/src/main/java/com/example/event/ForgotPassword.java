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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgotPassword extends AppCompatActivity {

    //Variables
    private ImageView screenIcon;
    private TextView title, description;
    private TextInputLayout phoneNumberTextField;
    private CountryCodePicker countryCodePicker;
    private Button nextBtn;
    private Animation animation;
    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        //Hooks
        screenIcon = findViewById(R.id.forgot_password_icon);
        title = findViewById(R.id.forgot_password_title);
        description = findViewById(R.id.forgot_password_description);
        countryCodePicker = findViewById(R.id.forgot_password_country_code_picker);
        phoneNumberTextField = findViewById(R.id.forgot_password_phone_number);
        nextBtn = findViewById(R.id.forgot_password_next_btn);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        //Animation Hooks
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);

        //Set Animation to all the elements
        screenIcon.setAnimation(animation);
        title.setAnimation(animation);
        description.setAnimation(animation);
        countryCodePicker.setAnimation(animation);
        phoneNumberTextField.setAnimation(animation);
        nextBtn.setAnimation(animation);
    }

    public void VerifyPhoneNo(View view) {
        //Check Internet Connection
        if (!isConnected(this)) {
            showCustomeDialog();
        }
        //Validation
        if (!validatePhoneNumber()){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Get data
        String _phoneNo = phoneNumberTextField.getEditText().getText().toString().trim();
        if (_phoneNo.charAt(0) == '0') {
            _phoneNo = _phoneNo.substring(1);
        }
        final String _completePhoneNo = "+" + countryCodePicker.getFullNumber() + _phoneNo;

        //Check if user exists in the database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //If phoneNo exists then call OTP to verify
                if (dataSnapshot.exists()) {
                    phoneNumberTextField.setError(null);
                    phoneNumberTextField.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneNo", _completePhoneNo);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    phoneNumberTextField.setError("No such user exists");
                    phoneNumberTextField.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showCustomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
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

    private boolean isConnected(ForgotPassword forgotPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) forgotPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) ||
                (mobileConn != null && mobileConn.isConnected())){
            return true;
        } else{
            return false;
        }
    }

    private boolean validatePhoneNumber() {
        String val = phoneNumberTextField.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            phoneNumberTextField.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneNumberTextField.setError("No White spaces are allowed!");
            return false;
        } else {
            phoneNumberTextField.setError(null);
            phoneNumberTextField.setErrorEnabled(false);
            return true;
        }
    }
}