package com.group15.seg2105finalproject.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FillForm extends AppCompatActivity {

    List list;
    android.widget.ListView lv;
    FormListviewAdapter adapter;
    Button buttonFill;
    ArrayList<String> allServicesItems = new ArrayList<String>();
    ArrayList<String> allServicesKeys = new ArrayList<String>();

    String username;
    String branchId;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        buttonFill = findViewById(R.id.buttonFill);

        //firebase reference
        dbRef = FirebaseDatabase.getInstance().getReference();

        lv =(ListView)findViewById(R.id.formList);
        adapter = new FormListviewAdapter(this,allServicesItems);
        lv.setAdapter(adapter);

        buttonFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFillPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        username = i.getStringExtra(CustomerBranchInteraction.USER_INFO);
        branchId = i.getStringExtra(CustomerBranchInteraction.BRANCH_ID);

        String serviceId = i.getStringExtra(CustomerBranchInteraction.SELECTED_SERVICE);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //create array list of all enabled services
                for (DataSnapshot requirement: snapshot.child("service").child(serviceId).child("requirements").getChildren()){
                    allServicesItems.add(requirement.getValue(String.class));
                    allServicesKeys.add(requirement.getKey());
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buttonFillPressed(){
        //validate fields
        boolean emptyField = false;

        HashMap<String, Object> form = adapter.getForm();
        Log.d("Form", form.toString());
        for (Object field : form.values()){
            Log.d("FIELD", field.toString());
            if (field.toString().length() == 0){
                emptyField = true;
                break;
            }
        }
        if (emptyField){
            Toast.makeText(getBaseContext(), "Field is empty. Must fill all fields", Toast.LENGTH_LONG).show();
        }else{
            //post to db
            Log.d("FillForm", "Posting to DB");
            HashMap<String, Object> requestInfo = new HashMap<String, Object>();
            requestInfo.put("client", username);
            requestInfo.put("information", form);
            requestInfo.put("status", 0);

            String key = dbRef.child("user").child(branchId).child("requests").push().getKey();
            DatabaseReference newRequest = dbRef.child("user").child(branchId).child("requests").child(key);
            newRequest.setValue(requestInfo);


            //saving in client
            String serviceName = getIntent().getStringExtra(CustomerBranchInteraction.SELECTED_SERVICE_NAME);
            DatabaseReference customerRequest = dbRef.child("user").child(username).child("requests").push();
            customerRequest.child("service").setValue(serviceName);
            customerRequest.child("employeeName").setValue(branchId);
            customerRequest.child("serviceKey").setValue(key);

            Toast.makeText(getBaseContext(), "Service request created!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}