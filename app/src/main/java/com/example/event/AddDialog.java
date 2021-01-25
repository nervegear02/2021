package com.example.event;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.event.Database.Events;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddDialog extends AppCompatDialogFragment {

    EditText name, details, price;

    DatePicker schedule;
    long maxid = 0;
    String phoneNum, _userName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_dialog, null);
        builder.setView(view)
                .setTitle("Add")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String event_name = name.getText().toString();
                        String event_details = details.getText().toString();
                        String event_price = price.getText().toString();
                        int scheduleday = schedule.getDayOfMonth();
                        int schedulemonth = schedule.getMonth();
                        int scheduleyear = schedule.getYear();
                        String event_schedule = scheduleday + "/" + schedulemonth + "/" + scheduleyear;

                        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext(), SessionManager.SESSION_USERSESSION);
                        if (sessionManager.checkLogin()) {
                            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
//                            String userName = userDetails.get(SessionManager.KEY_FULLNAME);
//                            _userName = userName.toString();
                            phoneNum = userDetails.get(SessionManager.KEY_SESSIONPHONENO);
                        }

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users");
                        DatabaseReference userRef = myRef.child(phoneNum);
                        DatabaseReference phoneRef = userRef.child("Event");
                        Events addEvents = new Events(event_name, event_details, event_schedule, event_price, false);
                        phoneRef.push().setValue(addEvents);
                    }
                });

        name = view.findViewById(R.id.add_event_name);
        details = view.findViewById(R.id.add_event_details);
        price = view.findViewById(R.id.add_event_price);
        schedule = view.findViewById(R.id.schedule_picker);
        return builder.create();
    }
}
