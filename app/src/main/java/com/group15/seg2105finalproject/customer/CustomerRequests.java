package com.group15.seg2105finalproject.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;

import java.util.LinkedList;
import java.util.List;

public class CustomerRequests extends AppCompatActivity {

    public static final String USER_INFO = CustomerWelcomeActivity.USER_INFO;

    TextView text;
    ListView customerRequest;
    String username;

    ArrayAdapter<String> adapter;

    List<String> keys;

    DatabaseReference dbRefCustomerRequests;
    DatabaseReference getDbRefCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_requests);

        text = (TextView) findViewById(R.id.requestsCustomerText);
        customerRequest = (ListView) findViewById(R.id.requestsCustomer);
        keys = new LinkedList<String>();

        Intent i = getIntent();
        username = i.getStringExtra(USER_INFO);


        getDbRefCustomer = FirebaseDatabase.getInstance().getReference("user").child(username);

        dbRefCustomerRequests = FirebaseDatabase.getInstance().getReference("user").child(username)
                .child("requests");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, keys);

        customerRequest.setAdapter(adapter);
    }


    @Override
    protected void onStart() {

        super.onStart();
        dbRefCustomerRequests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot r : snapshot.getChildren()) {

                        String serviceName = r.child("employeeName").getValue().toString() + ": " + r.child("service").getValue().toString();

                        keys.add(serviceName);

                    }

                    customerRequest.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //prevents list view from duplicating when coming back from editing requirements
        adapter.clear();
    }

}
