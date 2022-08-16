package com.group15.seg2105finalproject.branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;

import java.util.ArrayList;

public class ManageBranchServices extends AppCompatActivity {

    ArrayList<String> allServicesItems = new ArrayList<String>();
    ArrayList<String> allServicesKeys = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    private DatabaseReference dbRefServices;
    private DatabaseReference dbRefEnabledServices;
    private DatabaseReference dbRef;

    private ListView serviceListView;
    private Button finishButton;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_branch_services);
        dbRefServices = FirebaseDatabase.getInstance().getReference("service");
        dbRef = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        username = i.getStringExtra(EmployeeWelcomeActivity.USER_INFO);
        //get database reference to specific user
        dbRefEnabledServices = FirebaseDatabase.getInstance().getReference("user").child(username).child("enabledServices");

        serviceListView = (ListView) findViewById(R.id.serviceListView);
        finishButton = findViewById(R.id.finishButton);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                allServicesItems);
        serviceListView.setAdapter(adapter);
        serviceListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        //When finish button clicked, update database with all checked items in list
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishButtonPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //create array list of all enabled services
                for (DataSnapshot serviceSnapshot: snapshot.child("service").getChildren()){
                    if(serviceSnapshot.child("isEnabled").getValue(Boolean.class)){
                        allServicesItems.add(serviceSnapshot.child("serviceName").getValue(String.class));
                        allServicesKeys.add(serviceSnapshot.getKey().toString());
                    }
                }
                for (DataSnapshot serviceSnapshot: snapshot.child("user").child(username).child("enabledServices").getChildren()){
                    serviceListView.setItemChecked(allServicesItems.indexOf(snapshot.child("service").child(serviceSnapshot.getValue(String.class)).child("serviceName").getValue(String.class)),true);
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void finishButtonPressed(){
        int len = serviceListView.getCount();
        SparseBooleanArray checked = serviceListView.getCheckedItemPositions();

        ArrayList<String> newEnabledServicesIDs = new ArrayList<String>();

        for (int i = 0; i < len; i++) {
            if(serviceListView.isItemChecked(i)){
                String ID = allServicesKeys.get(i);
                newEnabledServicesIDs.add(ID);
            }
//            if (checked.get(i)) {
//                String ID = allServicesKeys.get(i);
//                newEnabledServicesIDs.add(ID);
//            }
        }
        dbRefEnabledServices.setValue(newEnabledServicesIDs);
        //Toast.makeText(getApplicationContext(), "Services modified", Toast.LENGTH_LONG).show();

        finish();
    }
}