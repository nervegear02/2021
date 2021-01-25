package com.example.event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomeDashboard extends AppCompatActivity implements View.OnClickListener {

    CardView profile, manage, search, settings, info, signout;

    String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_dashboard);

        //Define cardview
        profile = (CardView) findViewById(R.id.cardview_profile);
        search = (CardView) findViewById(R.id.cardview_search);
        manage = (CardView) findViewById(R.id.cardview_manage);
        settings = (CardView) findViewById(R.id.cardview_settings);
        info = (CardView) findViewById(R.id.cardview_info);
        signout = (CardView) findViewById(R.id.cardview_signout);

        //Click Listener
        profile.setOnClickListener(this);
        search.setOnClickListener(this);
        manage.setOnClickListener(this);
        settings.setOnClickListener(this);
        info.setOnClickListener(this);
        signout.setOnClickListener(this);

        SessionManager sessionManager = new SessionManager(HomeDashboard.this, SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkLogin()) {
            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
            phoneNum = userDetails.get(SessionManager.KEY_SESSIONPHONENO);
        }

    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
//            case R.id.cardview_profile:
//                //Get all values passed from previous screens
//                String _fullname = getIntent().getStringExtra("fullname");
//                String _address = getIntent().getStringExtra("address");
//                String _email = getIntent().getStringExtra("email");
//                String _dateOfBirth = getIntent().getStringExtra("date");
//                String _phoneNo = getIntent().getStringExtra("phoneNo");
//                i = new Intent(this, Profile.class);
//                i.putExtra("fullname", _fullname);
//                i.putExtra("address", _address);
//                i.putExtra("email", _email);
//                i.putExtra("date", _dateOfBirth);
//                i.putExtra("phoneNo", _phoneNo);
//                startActivity(i);
//                break;

//            case R.id.cardview_search:
//                i = new Intent(this, Search.class);
//                startActivity(i);
//                break;


            case R.id.cardview_manage:
                i = new Intent(this, Manage.class);
                startActivity(i);
                break;


//            case R.id.cardview_settings:
//                i = new Intent(this, Settings.class);
//                startActivity(i);
//                break;
//
//
//            case R.id.cardview_info:
//                i = new Intent(this, Info.class);
//                startActivity(i);
//                break;


            case R.id.cardview_signout:
                i = new Intent(HomeDashboard.this, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;

        }

    }
}