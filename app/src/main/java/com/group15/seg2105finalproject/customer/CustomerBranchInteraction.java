package com.group15.seg2105finalproject.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerBranchInteraction extends AppCompatActivity {
    Spinner ratingSpinner;
    Button ratingButton;
    ListView serviceListView;
    Button fillFormButton;
    EditText comments;
    public static final String USER_INFO = CustomerSearchBranches.USER_INFO;
    public static final String BRANCH_ID = CustomerSearchBranches.BRANCH_ID;
    public static final String SELECTED_SERVICE_NAME = "com.group15.seg2105finalproject.SELECTED_SERVICE_NAME";
    public static final String SELECTED_SERVICE = "com.group15.seg2105finalproject.SELECTED_SERVICE";

    ArrayList<String> allServicesItems = new ArrayList<String>();
    ArrayList<String> allServicesKeys = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayList<String> enabledServicesIDs = new ArrayList<String>();
    private String username;
    private String branchID;
    private int selectedIndex;

    String[] ratings;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_branch_interaction);

        ratingButton = (Button) findViewById(R.id.buttonRate);
        fillFormButton = (Button) findViewById(R.id.fillPutFormButton);
        ratingSpinner = (Spinner) findViewById(R.id.spinnerRating);
        serviceListView = (ListView) findViewById(R.id.availableServicesList);
        comments = (EditText) findViewById(R.id.editTextComments);

        //set ratings array and fill out spinner
        ratings = new String[]{"5","4","3","2","1"};

        ArrayAdapter adapter0
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,ratings);

        adapter0.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(adapter0);

        //firebase reference
        dbRef = FirebaseDatabase.getInstance().getReference();

        //get  intent info
        Intent i = getIntent();
        username = i.getStringExtra(CustomerSearchBranches.USER_INFO);
        branchID = i.getStringExtra(CustomerSearchBranches.BRANCH_ID);

        //set service list view
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                allServicesItems);
        serviceListView.setAdapter(adapter);
        serviceListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        serviceListView.setItemChecked(0, true);
        selectedIndex = 0;

        serviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndex = i;

            }
        });

        //onclick for rating button
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingButtonPressed();
            }
        });
        //onclick for filling out form
        fillFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillFormButtonPressed();
            }
        });
    }

    private void fillFormButtonPressed() {
        String selectedServiceId = allServicesKeys.get(selectedIndex);
        String serviceName = allServicesItems.get(selectedIndex);

        Intent i = getIntent();
        Intent intent = new Intent(getApplicationContext(), FillForm.class);
        intent.putExtra(USER_INFO,username);
        intent.putExtra(BRANCH_ID,branchID);
        intent.putExtra(SELECTED_SERVICE, selectedServiceId);
        intent.putExtra(SELECTED_SERVICE_NAME, serviceName);
        startActivity(intent);
    }

    private void ratingButtonPressed() {
        String rating = ratingSpinner.getSelectedItem().toString();
        String comment = comments.getText().toString();
        if(validateRating(comment)){
            HashMap<String, Object> ratings_map = new HashMap<String, Object>();
            ratings_map.put("customer", username);
            ratings_map.put("value", rating);
            ratings_map.put("comment", comment);

            String key = dbRef.child("user").child(branchID).child("ratings").push().getKey();
            DatabaseReference dbref1 = dbRef.child("user").child(branchID).child("ratings").child(key);
            dbref1.child("user").child(branchID).child("ratings").child(key).setValue(ratings_map);
            Toast.makeText(getApplicationContext(), "Rating Added", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validateRating(String comment){
        if (comment.isEmpty()) {
            comments.setError("Must enter a comment.");
            comments.requestFocus();
            return false;
        }
        return true;
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
                        if (!allServicesKeys.contains(serviceSnapshot.getKey())){
                            allServicesItems.add(serviceSnapshot.child("serviceName").getValue(String.class));
                            allServicesKeys.add(serviceSnapshot.getKey());
                        }
                    }
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}