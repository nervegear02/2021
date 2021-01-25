package com.example.event;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.event.Adapter.EventsAdapter;
import com.example.event.Callbacks.MyButtonClickListener;
import com.example.event.Database.Events;
import com.example.event.Helper.MySwipeHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Manage extends AppCompatActivity {

    RecyclerView recyclerView;
    EventsAdapter eventsAdapter;
    FloatingActionButton floatingActionButton;
    List<Events> eventsList;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String _userName, phoneNum, date_sales;
    DatabaseReference phoneRef;
    SwipeRefreshLayout refreshLayout;
    String _sold_to, _crop_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_list);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date_sales = simpleDateFormat.format(calendar.getTime());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsList = new ArrayList<>();
        SessionManager sessionManager = new SessionManager(Manage.this, SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkLogin()) {
            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
//            String userName = userDetails.get(SessionManager.KEY_FULLNAME);
//            _userName = userName.toString();
            phoneNum = userDetails.get(SessionManager.KEY_SESSIONPHONENO);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        DatabaseReference userRef = myRef.child(phoneNum);
        phoneRef = userRef.child("Events");
        phoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Events data = ds.getValue(Events.class);
                    eventsList.add(data);
                }
                eventsAdapter = new EventsAdapter(eventsList);
                recyclerView.setAdapter(eventsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        floatingActionButton = findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventsList.clear();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users");
                DatabaseReference userRef = myRef.child(phoneNum);
                phoneRef = userRef.child("Events");
                phoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Events ndata = ds.getValue(Events.class);
                            eventsList.add(ndata);
                        }
                        eventsAdapter = new EventsAdapter(eventsList);
                        recyclerView.setAdapter(eventsAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                refreshLayout.setRefreshing(false);
            }
        });

        MySwipeHelper swipeHelper = new MySwipeHelper(this, recyclerView, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MySwipeHelper.MyButton> buffer) {
                buffer.add(new MyButton(Manage.this,
                        "Ship",
                        30,
                        0,
                        Color.parseColor("#560027"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Common.eventsSelected = eventsList.get(pos);
//                                showShipDialog();
                            }
                        }));
            }
        };
    }

//    private void showShipDialog() {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Manage.this);
//        builder.setTitle("Ship");
//        builder.setMessage("Please fill information");
//
//        View itemView = LayoutInflater.from(Manage.this).inflate(R.layout.layout_ship_dialog, null);
//        final EditText edt_other_destination_name = (EditText) itemView.findViewById(R.id.edt_other_destination_name);
//        edt_other_destination_name.setVisibility(View.GONE);
//        final EditText edt_price_sold = (EditText) itemView.findViewById(R.id.edt_price_sold);
//        Spinner mspinner = (Spinner) itemView.findViewById(R.id.choices);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Manage.this,
//                android.R.layout.simple_spinner_item,
//                getResources().getStringArray(R.array.destinationlist));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mspinner.setAdapter(adapter);
//        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (adapterView.getItemAtPosition(i).toString().equals("Others")) {
//                    edt_other_destination_name.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());
//        builder.setPositiveButton("SHIP", (dialogInterface, i) -> {
//            String destination = mspinner.getSelectedItem().toString();
//            if (destination.equals("Others")){
//                Map<String, Object> addShip = new HashMap<>();
//                addShip.put("name", edt_other_destination_name.getText().toString());
//                addShip.put("price", edt_price_sold.getText().toString());
//                addShip(addShip);
//            } else {
//                Map<String, Object> addShip = new HashMap<>();
//                addShip.put("name", destination);
//                addShip.put("price", edt_price_sold.getText().toString());
//                addShip(addShip);
//            }
//
//        });
//
//        builder.setView(itemView);
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    private void addShip(Map<String, Object> addShip) {
//        for (Map.Entry<String, Object> e : addShip.entrySet()){
//            if (e.getKey().equals("name")){
//                _sold_to = e.toString().replace("name","")
//                        .replace("=","").trim();
//            }
//
//            if (e.getKey().equals("price")){
//                _crop_price = e.toString().replace("price","")
//                .replace("=","").trim();
//            }
//        }
//        String _crop_name = String.valueOf(Common.eventsSelected.getCrop_name());
//        String _crop_volume = String.valueOf(Common.eventsSelected.getCrop_volume());
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Users");
//        DatabaseReference userRef = myRef.child(phoneNum);
//        DatabaseReference phoneRef = userRef.child("Sales");
//        Sold addSales = new Sold(_crop_name, _crop_price, _crop_volume, date_sales, _sold_to);
//        phoneRef.push().setValue(addSales);
//
//    }

    private void openDialog() {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "Add Dialog");
    }

}