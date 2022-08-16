package com.group15.seg2105finalproject.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
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
import com.group15.seg2105finalproject.branch.EmployeeWelcomeActivity;
import com.group15.seg2105finalproject.R;

import java.util.ArrayList;

public class CustomerSearchBranches extends AppCompatActivity {
    public static final String USER_INFO = EmployeeWelcomeActivity.USER_INFO;
    public static final String BRANCH_ID = "com.group15.seg2105finalproject.BRANCH_ID";
    ArrayList<String> allServicesItems = new ArrayList<String>();
    ArrayList<String> allServicesKeys = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    ArrayList<String> availableBranches = new ArrayList<String>();
    ArrayList<String> availableBranchesKeys = new ArrayList<String>();
    int branchPosition;
    String branchSelected;
    String postalCode;

    private DatabaseReference dbRefServices;
    private DatabaseReference dbRefEnabledServices;
    private DatabaseReference dbRef;

    private ListView serviceListView;
    private ListView branchListView;
    private Button selectButton;
    private Button findBranch;
    private Button findHours;
    private EditText postalAddress;
    private Button buttonFilterAddress;
    private Spinner selectProvince;

    private String username;
    Spinner daySpinner;

    String[] days;
    String[] provinces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search_branches);
        findHours = (Button) findViewById(R.id.buttonHourSearch);
        //spinner stuff
        daySpinner = (Spinner) findViewById(R.id.daysSpin);

        //selectedProvince
        selectProvince = (Spinner) findViewById(R.id.selectProvince);

        //postalAddress
        postalAddress = (EditText) findViewById(R.id.editTextTextPostalAddress);
        postalCode = postalAddress.getText().toString();
        buttonFilterAddress = (Button) findViewById(R.id.buttonSearchAddress);


        //Days array
        days = new String[]{"Monday","Tuesday","Wednesday","Thursday",
                "Friday", "Saturday", "Sunday"};

        ArrayAdapter adapter0
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,days);

        adapter0.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter0);

        //Province array
        provinces = new String[]{"Alberta", "British Columbia", "Manitoba",
                "New Brunswick", "Newfoundland and Labrador","Northwest Territories", "Nova Scotia","Nunavut",
                "Ontario", "Prince Edward Island" ," Quebec", "Saskatchewan"
                ,"Yukon"};

        ArrayAdapter<String> adapterP = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinces);
        selectProvince.setAdapter(adapterP);

        findBranch = (Button)findViewById(R.id.branchServicesButton);
        selectButton = (Button)findViewById(R.id.selectBranch);

        dbRefServices = FirebaseDatabase.getInstance().getReference("service");
        dbRef = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        username = i.getStringExtra(EmployeeWelcomeActivity.USER_INFO);
        //get database reference to specific user
        dbRefEnabledServices = FirebaseDatabase.getInstance().getReference("user").child(username).child("enabledServices");

        serviceListView = (ListView) findViewById(R.id.serviceListCustomer);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                allServicesItems);
        serviceListView.setAdapter(adapter);
        serviceListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        branchListView = (ListView) findViewById(R.id.branchList);

        adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                availableBranches);
        branchListView.setAdapter(adapter2);
        branchListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        branchListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        branchPosition = position;
                        branchSelected = availableBranches.get(branchPosition);
                    }
                });

        findHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoursButtonPressed();
            }
        });
        buttonFilterAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { addressButtonPressed(); }
        });
        findBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findButtonPressed();
            }
        });
        //When finish button clicked, update database with all checked items in list
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonPressed();
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
                        allServicesKeys.add(serviceSnapshot.getKey());
                    }
                }
                adapter.notifyDataSetChanged();

                //fill out the pending and approved lists
                for(DataSnapshot branchesSnapshot: snapshot.child("user").getChildren()){
                    if(branchesSnapshot.child("role").child("isEmployee").getValue(boolean.class)){
                        availableBranches.add(branchesSnapshot.getKey().toString());
                        availableBranchesKeys.add(branchesSnapshot.getKey().toString());
                    }
                }
                branchListView.setAdapter(adapter2);

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
        adapter2.clear();
    }

    public void hoursButtonPressed(){
        availableBranches.clear();
        availableBranchesKeys.clear();
        adapter2.notifyDataSetChanged();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot branchSnapshot : snapshot.child("user").getChildren()) {
                    if (branchSnapshot.child("role").child("isEmployee").getValue(boolean.class)) {
                        String selectedDay = daySpinner.getSelectedItem().toString();
                        String day = branchSnapshot.child("hours").child(selectedDay).getKey();
                        if (branchSnapshot.child("hours").child(selectedDay).child("isEnabled").getValue(String.class).equals("true")) {
                                StringBuffer toAdd = new StringBuffer();
                                toAdd.append(branchSnapshot.getKey());
                                toAdd.append(": ");
                                toAdd.append(branchSnapshot.child("hours").child(selectedDay).child("startTime").getValue().toString());
                                toAdd.append(" - ");
                                toAdd.append(branchSnapshot.child("hours").child(selectedDay).child("endTime").getValue().toString());
                                String toAddStr = toAdd.toString();
                                availableBranches.add(toAdd.toString());
                                availableBranchesKeys.add(branchSnapshot.getKey().toString());
                        }
                    }
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void addressButtonPressed(){

        availableBranches.clear();
        availableBranchesKeys.clear();
        adapter2.notifyDataSetChanged();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot branchSnapshot : snapshot.child("user").getChildren()) {
                    if (branchSnapshot.child("role").child("isEmployee").getValue(boolean.class)) {

                        if(branchSnapshot.child("address").child("province").getValue().equals(selectProvince.getSelectedItem().toString())){

                            String branchAddress = branchSnapshot.child("address").child("postal").getValue().toString();
                            StringBuffer address = new StringBuffer();
                            address.append(branchSnapshot.child("address").child("name").getValue().toString());
                            address.append(" ");
                            address.append(branchSnapshot.child("address").child("number").getValue().toString());
                            address.append(" ");
                            address.append(branchSnapshot.child("address").child("postal").getValue().toString());
                            address.append(" ");// in case it doesn't automatically adds spaces
                            address.append(branchSnapshot.child("address").child("province").getValue().toString());

                            availableBranches.add(branchSnapshot.getKey().toString() + ": " + address.toString());
                            availableBranchesKeys.add(branchSnapshot.getKey().toString());


                        }


                    }
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void findButtonPressed(){
        int len = serviceListView.getCount();
        SparseBooleanArray checked = serviceListView.getCheckedItemPositions();

        ArrayList<String> newEnabledServicesIDs = new ArrayList<String>();

        for (int i = 0; i < len; i++) {
            if (serviceListView.isItemChecked(i)) {
                String ID = allServicesKeys.get(i);
                newEnabledServicesIDs.add(ID);
            }
        }

        availableBranches.clear();
        availableBranchesKeys.clear();
        adapter2.notifyDataSetChanged();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot branchSnapshot: snapshot.child("user").getChildren()){
                    if(branchSnapshot.child("role").child("isEmployee").getValue(boolean.class)){
                        for(DataSnapshot servicesSnapshot: branchSnapshot.child("enabledServices").getChildren()){
                            String enabledService = servicesSnapshot.getValue(String.class);
                            if(newEnabledServicesIDs.contains(enabledService)){
                                if (!availableBranchesKeys.contains(branchSnapshot.getKey().toString())){
                                    availableBranches.add(branchSnapshot.getKey().toString());
                                    availableBranchesKeys.add(branchSnapshot.getKey().toString());
                                }
                            }
                        }
                    }
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void selectButtonPressed(){
        int count = branchListView.getCount();
        String branchSelected = "";
        boolean selected = false;
        for (int i = 0; i < count; i++) {
            if (branchListView.isItemChecked(i)) {
                selected = true;
                branchSelected = availableBranchesKeys.get(i);
            }
        }
        if(!selected){
            Toast.makeText(getApplicationContext(), "Must Select a Branch!", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent i = getIntent();
            Intent intent = new Intent(getApplicationContext(),CustomerBranchInteraction.class);
            intent.putExtra(USER_INFO,username);
            intent.putExtra(BRANCH_ID,branchSelected);
            startActivity(intent);
        }
    }
}