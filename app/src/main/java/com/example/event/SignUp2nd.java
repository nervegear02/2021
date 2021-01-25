package com.example.event;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SignUp2nd extends AppCompatActivity {

    //Variables
    Button next, login;
    TextView titleText, slideText;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up2nd);
        //Hooks
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);
        slideText = findViewById(R.id.signup_slide_text);
        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.birthdate_picker);
    }

    public void callNextSignupScreen(View view) {

        if(!validateGender() | !validateBirthday()){
            return;
        }

        //Get all values passed from previous screens
        String _fullname = getIntent().getStringExtra("fullname");
        String _password = getIntent().getStringExtra("password");
        String _address = getIntent().getStringExtra("address");
        String _email = getIntent().getStringExtra("email");

        selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
        String _gender = selectedGender.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        String _date = day + "/" + month + "/" + year;

        Intent intent = new Intent(getApplicationContext(), SignUp3rd.class);

        //Pass all fields to the next activity
        intent.putExtra("address", _address);
        intent.putExtra("fullname", _fullname);
        intent.putExtra("password", _password);
        intent.putExtra("date", _date);
        intent.putExtra("email", _email);
        intent.putExtra("gender", _gender);

        //Add Transition and call next activity
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(next, "transition_next_btn");
        pairs[1] = new Pair<View, String>(login, "transition_login_btn");
        pairs[2] = new Pair<View, String>(titleText, "transition_title_text");
        pairs[3] = new Pair<View, String>(slideText, "transition_slide_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2nd.this, pairs);
        startActivity(intent, options.toBundle());

    }

    public void callLoginFromSignUp(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateBirthday() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 18) {
            Toast.makeText(this, "You are not eligible to apply", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}