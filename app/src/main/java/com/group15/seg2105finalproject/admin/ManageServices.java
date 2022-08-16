package com.group15.seg2105finalproject.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;
import com.group15.seg2105finalproject.main.Service;

public class ManageServices extends AppCompatActivity {
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String> listKeys = new ArrayList<String>();
    ArrayAdapter<String> adapter1;

    private EditText serviceName;
    private EditText servicePrice;
    private RadioButton userEnabled;
    private ListView serviceListView;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Boolean searchMode = false;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;
    private String id_selectedService;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef;
    private DatabaseReference allDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);
        dbRef = FirebaseDatabase.getInstance().getReference("service");
        allDBRef = FirebaseDatabase.getInstance().getReference();

        serviceName = (EditText) findViewById(R.id.serviceName);
        servicePrice = (EditText) findViewById(R.id.servicePrice);
        userEnabled = (RadioButton) findViewById(R.id.isEnabled);
        serviceListView = (ListView) findViewById(R.id.serviceListView);
        addButton = (Button) findViewById(R.id.buttonAdd);
        editButton = (Button) findViewById(R.id.buttonEdit);
        deleteButton = (Button) findViewById(R.id.buttonDelete);
        deleteButton.setEnabled(false);

        adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                listItems);
        serviceListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addService(view);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService(view);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(itemSelected){

                    Intent i = new Intent(getApplicationContext(), EditService.class);
                    i.putExtra("ServiceToEdit", id_selectedService);
                    ManageServices.this.startActivity(i);
                }

            }
        });

        serviceListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        deleteButton.setEnabled(true);
                        selectedPosition = position;
                        itemSelected = true;
                        id_selectedService = listKeys.get(position);

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //create list view
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot serviceSnapshot: snapshot.getChildren()){
                    listItems.add(serviceSnapshot.child("serviceName").getValue(String.class));
                    listKeys.add(serviceSnapshot.getKey());
                }
                serviceListView.setAdapter(adapter1);
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
        adapter1.clear();
    }

    public void addService(View view) {
        String name = serviceName.getText().toString();
        String price = servicePrice.getText().toString();
        boolean checked = userEnabled.isChecked();

        //if service name and price are valid then add new service
        if(validateServiceName(name) && validateServicePrice(price)){
            double priceDouble = Double.parseDouble(price);
//            Service service = new Service(name, priceDouble, checked, new ArrayList<String>());

            ArrayList<String> requirements = new ArrayList<String>();
            requirements.add("first name");
            requirements.add("last name");
            requirements.add("date of birth");
            requirements.add("address");
            requirements.add("email");

            HashMap<String, Object> service_map = new HashMap<String, Object>();
            service_map.put("hourlyRate", priceDouble);
            service_map.put("isEnabled", checked);
            service_map.put("requirements", requirements);
            service_map.put("serviceName", name);


            String key = dbRef.push().getKey();
            dbRef.child(key).setValue(service_map);

            //update list view
            listItems.add(name);
            listKeys.add(key);
            adapter1.notifyDataSetChanged();

        }

        //Reset the EditText in the front end
        serviceName.setText("");
        servicePrice.setText("");

        //remove enable
        userEnabled.setChecked(false);

        Toast.makeText(getApplicationContext(), "Service Added", Toast.LENGTH_LONG).show();


    }

    public void deleteService(View view) {
        String serviceName = listItems.get(selectedPosition);
        String serviceKey = listKeys.get(selectedPosition);

        //prevent deleting three basic services
        if(!serviceName.equals("Car rental")&&!serviceName.equals("Moving assistance")&&!serviceName.equals("Truck rental")){
            dbRef.child(listKeys.get(selectedPosition)).removeValue();
            Toast.makeText(getApplicationContext(), "Service successfully deleted", Toast.LENGTH_SHORT).show();
            //remove in employee lists as well
            allDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //for all the users that are employees
                    for (DataSnapshot allUserSnapshot: snapshot.child("user").getChildren()){
                        if(allUserSnapshot.child("role").child("isEmployee").getValue(Boolean.class)){
                            for(DataSnapshot userSnapshot: allUserSnapshot.child("enabledServices").getChildren()){
                                //get all the enabled services from specific employee/branch
                                    //if the enabled service matches with the one we are deleting then we remove it
                                    if(userSnapshot.getValue(String.class).equals(serviceKey)){
                                        userSnapshot.getRef().removeValue();
                                    }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //update listView
            listKeys.remove(serviceKey);
            listItems.remove(serviceName);
            adapter1.notifyDataSetChanged();

            //update delete button
            deleteButton.setEnabled(false);
        }
        else{
            Toast.makeText(getApplicationContext(), "Permanent service: cannot be deleted", Toast.LENGTH_LONG).show();
        }
    }

    //method to validate service name
    public boolean validateServiceName(String name){
        if (name.isEmpty()) {
            serviceName.setError("Must enter a service name.");
            serviceName.requestFocus();
            return false;
        }
        if(listItems.contains(name)){
            serviceName.setError("Service already created.");
            serviceName.requestFocus();
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
                servicePrice.setError("Must enter a valid price.");
                servicePrice.requestFocus();
                return false;
            }
            return true;
        }
        else{
            servicePrice.setError("Must enter a valid price.");
            servicePrice.requestFocus();
            return false;
        }
    }
}