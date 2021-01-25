package com.example.event;

import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.event.Database.CheckInternet;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    //Variables
    private ImageView screenIcon;
    private TextView title, description;
    private Button updateBtn;
    private Animation animation;
    RelativeLayout progressBar;
    TextInputLayout newPassword, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_new_password);

        //Hooks
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        screenIcon = findViewById(R.id.set_new_password_icon);
        title = findViewById(R.id.set_new_password_title);
        description = findViewById(R.id.set_new_password_description);
        updateBtn = findViewById(R.id.set_new_password_btn);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        //Animation Hooks
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);

        //Set Animation to all the elements
        screenIcon.setAnimation(animation);
        title.setAnimation(animation);
        description.setAnimation(animation);
        updateBtn.setAnimation(animation);
    }

    public void setNewPasswordBtn(View view) {
        //Check Internet Connection
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            showCustomDialog();
            return;
        }
        //Validate Password
        if (!validatePassword() | !validateConfirmPassword()) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        //Get new Password
        String _newPassword = newPassword.getEditText().getText().toString().trim();
        String _phoneNo = getIntent().getStringExtra("phoneNo");

        //Update Data
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNo).child("password").setValue(_newPassword);

        startActivity(new Intent(getApplicationContext(), ForgotpasswordSuccessMessage.class));
        finish();
    }

    private boolean validateConfirmPassword() {
        String val = confirmPassword.getEditText().getText().toString().trim();
        String checkPassword = newPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            confirmPassword.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            confirmPassword.setError("Password does not match");
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = newPassword.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            newPassword.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            newPassword.setError("Password should contain 8 characters!");
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPassword.this);
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

}