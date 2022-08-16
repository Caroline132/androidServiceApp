package com.group15.seg2105finalproject.branch;

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
import com.group15.seg2105finalproject.customer.CustomerRequests;

import java.util.LinkedList;
import java.util.List;

public class EmployeeViewCustomerRequestDetails extends AppCompatActivity {

    public static final String USER_INFO = CustomerRequests.USER_INFO;
    ListView formRequirement;
    TextView topMessage;

    String request;
    String username;

    ArrayAdapter<String> adapter;
    List<String> requirements;

    DatabaseReference dbRefRequirementForm;


    @Override
    protected void onCreate(Bundle saveInstanceState) {


        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_employee_view_request_details);

        topMessage = (TextView) findViewById(R.id.employeeViewRequestTextView);
        formRequirement = (ListView) findViewById(R.id.customerRequestDetailEmployee);
        requirements = new LinkedList<String>();
        request = getIntent().getStringExtra("request");
        username = getIntent().getStringExtra("username");

        dbRefRequirementForm = FirebaseDatabase.getInstance().getReference("user").child(username)
                .child("requests").child(request).child("information");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, requirements);
        formRequirement.setAdapter(adapter);


    }

    @Override
    protected void onStart(){

        super.onStart();
        dbRefRequirementForm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot r: snapshot.getChildren()){

                    requirements.add(r.getKey() + ": " + r.getValue().toString());
                }

                formRequirement.setAdapter(adapter);
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
