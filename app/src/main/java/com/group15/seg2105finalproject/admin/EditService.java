package com.group15.seg2105finalproject.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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

public class EditService extends AppCompatActivity {
    EditText editServiceName;
    EditText editServicePrice;
    Switch enableServiceSwitch;
    Button modifyServiceButton;
    EditText editAddRequirement;
    Button addRequirementButton;
    ListView requirementList;
    Button deleteRequirementButton;
    String selectedService;
    DatabaseReference dbRef;
    ArrayAdapter<String> adapter;
    List<String> requirements;
    List<String> requirementsKey;
    int requirementID;
    String requirementSelected;
    boolean enableSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        editServiceName = (EditText) findViewById(R.id.editServiceName);
        editServicePrice = (EditText) findViewById(R.id.editServicePrice);
        enableServiceSwitch = (Switch) findViewById(R.id.enableService);
        modifyServiceButton = (Button) findViewById(R.id.editService);
        editAddRequirement = (EditText) findViewById(R.id.editRequirement);
        addRequirementButton = (Button) findViewById(R.id.addRequirment);
        requirementList = (ListView) findViewById(R.id.requirmentList);
        deleteRequirementButton = (Button) findViewById(R.id.deleteRequirement);
        requirements = new ArrayList<>();
        requirementsKey = new ArrayList<>();
        deleteRequirementButton.setEnabled(false);
        enableSelected = false;

        //get service that was selected in ManageService
        Intent i = getIntent();
        selectedService = i.getStringExtra("ServiceToEdit");

        //get database reference associated to the selected service
        //dbRef = FirebaseDatabase.getInstance().getReference(selectedService);
        dbRef =FirebaseDatabase.getInstance().getReference("service");

        //set adapter to populate ListView
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,requirements);
        //select one requirement at a time
        requirementList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //on click when requirement from ListView is selected
        requirementList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        deleteRequirementButton.setEnabled(true);
                        requirementID = position;
                        requirementSelected = requirements.get(requirementID);
                        deleteRequirementButton.setEnabled(true);
                    }
                });

        //on click to modify service
        modifyServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyService(v);
            }
        });

        //on click for adding requirement
        addRequirementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequirement(v);
            }
        });

        //on click for deleting a requirement
        deleteRequirementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequirement(v);
                if(requirementID < requirements.size()){
                    requirementList.setItemChecked(requirementID,false);
                }
                deleteRequirementButton.setEnabled(false);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //populate editServiceName, editServicePrice and requirementList with correct values
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //populate editServiceName
                String newServiceName = snapshot.child(selectedService).child("serviceName").getValue(String.class);
                editServiceName.setText(newServiceName);
//
//                //populate editServicePrice
                Double newServicePrice = 0.0;
                try {
                    newServicePrice = snapshot.child(selectedService).child("hourlyRate").getValue(Double.class);
                } catch (NumberFormatException e) {
                    //Todo: handle exception
                }
                editServicePrice.setText(String.valueOf(newServicePrice));

//               //populate requirementList
                requirements.clear();
                for (DataSnapshot requirementSnapshot: snapshot.child(selectedService).child("requirements").getChildren()){
                    String requirement = requirementSnapshot.getValue(String.class);
                    String key = requirementSnapshot.getKey().toString();
                    requirements.add(requirement);
                    requirementsKey.add(key);
                }
                requirementList.setAdapter(adapter);

                //enable the enableServiceSwitch if true
                boolean enabled = snapshot.child(selectedService).child("isEnabled").getValue(Boolean.class);
                if(enabled){
                    enableServiceSwitch.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //No Error Handling
            }
        });
    }

    //method to modify service name, price and enable
    public void modifyService(View view){
        String name = editServiceName.getText().toString();
        String price = editServicePrice.getText().toString();
        boolean checked = enableServiceSwitch.isChecked();

        if(!selectedService.equals("-MnT4WQxMf4pkpY8vYX0")&&!name.equals("-MnTRTtPUxYmGbYBF9PF")&&!name.equals("-MoLQpC92CFQWH3y6_Hl")) {
            //set new service name
            if (validateServiceName(name)) {
                dbRef.child(selectedService).child("serviceName").setValue(name);
            }

            //set new service price
            if (validateServicePrice(price)) {
                double priceDouble = Double.parseDouble(price);
                dbRef.child(selectedService).child("hourlyRate").setValue(priceDouble);
            }
            //set enable
            dbRef.child(selectedService).child("isEnabled").setValue(enableServiceSwitch.isChecked());
            enableServiceSwitch.setChecked(enableServiceSwitch.isChecked());

            Toast.makeText(getApplicationContext(), "Service Modified", Toast.LENGTH_LONG).show();
        }
        else{
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    editServiceName.setText(snapshot.child(selectedService).child("serviceName").getValue().toString());
                    editServicePrice.setText(snapshot.child(selectedService).child("hourlyRate").getValue(Double.class).toString());
                    boolean enabled = snapshot.child(selectedService).child("isEnabled").getValue(Boolean.class);
                    if(enabled){
                        enableServiceSwitch.setChecked(true);
                    }
                    else{
                        enableServiceSwitch.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Toast.makeText(getApplicationContext(), "Permanent service: cannot be modified", Toast.LENGTH_LONG).show();
        }
    }

    //method to add a requirement
    public void addRequirement(View view){
        String requirementToAdd = editAddRequirement.getText().toString();
        String name = editServiceName.getText().toString();
        if(!name.equals("Car rental")&&!name.equals("Moving assistance")&&!name.equals("Truck rental")) {
            if (requirements.contains(requirementToAdd)) {
                editAddRequirement.setError("Requirement already created.");
                editAddRequirement.requestFocus();
            } else {
                if (validateServiceName(requirementToAdd)) {
                    dbRef.child(selectedService).child("requirements").child(String.valueOf(requirements.size())).setValue(requirementToAdd);
                    requirements.add(requirementToAdd);
                    requirementsKey.add(String.valueOf(requirements.size()));
                    adapter.notifyDataSetChanged();
                    editAddRequirement.getText().clear();
                }
            }
        }
        else{
            editAddRequirement.getText().clear();
            Toast.makeText(getApplicationContext(), "Permanent service: cannot add requirements", Toast.LENGTH_LONG).show();
        }
    }
    //method to delete a requirement
    public void deleteRequirement(View view){
        String name = editServiceName.getText().toString();
        if(!name.equals("Car rental")&&!name.equals("Moving assistance")&&!name.equals("Truck rental")) {
            requirementList.setItemChecked(requirementID, false);
            dbRef.child(selectedService).child("requirements").child(requirementsKey.get(requirementID)).removeValue();
            requirements.remove(requirementID);
            requirementsKey.remove(requirementID);
            adapter.notifyDataSetChanged();
        }
        else{
            Toast.makeText(getApplicationContext(), "Permanent service: cannot delete requirements", Toast.LENGTH_LONG).show();
        }
    }

    //method to validate service name
    public boolean validateServiceName(String name){
        if (name.isEmpty()) {
            editServiceName.setError("Must enter a service name.");
            editServiceName.requestFocus();
            return false;
        }
        return true;
    }

    //method to validate service price
    public boolean validateServicePrice(String price){
        if(!price.isEmpty()){
            double priceDouble = 0;
            try {
                priceDouble = Double.parseDouble(price);
            } catch (NumberFormatException e) {
                editServicePrice.setError("Must enter a valid price.");
                editServicePrice.requestFocus();
                return false;
            }
            return true;
        }
        else{
            editServicePrice.setError("Must enter a valid price.");
            editServicePrice.requestFocus();
            return false;
        }
    }


}