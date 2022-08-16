package com.group15.seg2105finalproject.branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;
import com.group15.seg2105finalproject.main.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class EditBranchProfile extends AppCompatActivity {
    public static final String USER_INFO = EmployeeWelcomeActivity.USER_INFO;
    Button addressButton;
    EditText editStreetNumber;
    EditText editStreetName;
    EditText editPostal;
    EditText editPhone;
    Button phoneButton;
    DatabaseReference dbRef;
    String username;
    String[] provinces;
    Spinner provinceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_branch_profile);
        addressButton = (Button) findViewById(R.id.addressButton);
        phoneButton = (Button) findViewById(R.id.phoneButton);
        editStreetNumber = (EditText) findViewById(R.id.editStreetNumber);
        editStreetName = (EditText) findViewById(R.id.editStreetName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPostal = (EditText) findViewById(R.id.editPostalCode);
        provinceSpinner = (Spinner) findViewById(R.id.provinceSpinner);

        //formats phone number
        editPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //province array
        provinces = new String[]{"Alberta","British Columbia","Manitoba",
                "New Brunswick","Newfoundland and Labrador", "Northwest Territories",
                "Nova Scotia","Nunavut","Ontario", "Prince Edward Island", "Quebec",
                "Saskatchewan","Yukon"};

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                provinces);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        provinceSpinner.setAdapter(ad);

        //get username
        Intent i = getIntent();
        username = i.getStringExtra(USER_INFO);

        //get database reference to specific user
        dbRef =FirebaseDatabase.getInstance().getReference("user").child(username);

        //set new address
        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyAddress(v);
            }
        });

        //set new phone number
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPhone(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //populate address fields
                if(snapshot.child("address")!=null){
                    if(snapshot.child("address").child("number").getValue(String.class)!=null){
                        editStreetNumber.setText(snapshot.child("address").child("number").getValue(String.class));
                    }
                    if(snapshot.child("address").child("name").getValue(String.class)!=null){
                        editStreetName.setText(snapshot.child("address").child("name").getValue(String.class));
                    }
                    if(snapshot.child("address").child("province").getValue(String.class)!=null){
                        provinceSpinner.setSelection(getProvinceIndex(provinceSpinner, snapshot.child("address").child("province").getValue(String.class)));
                    }
                    if(snapshot.child("address").child("postal").getValue(String.class)!=null){
                        editPostal.setText(snapshot.child("address").child("postal").getValue(String.class));
                    }
                }

                //populate phone number
                if(snapshot.child("phone").getValue(String.class)!=null){
                    editPhone.setText(snapshot.child("phone").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void modifyAddress(View v){
        String number = editStreetNumber.getText().toString();
        String name = editStreetName.getText().toString();
        String province = provinceSpinner.getSelectedItem().toString();
        String postal = editPostal.getText().toString();

        if(validateStreetNumber(number)&& validateStreetName(name)&& validatePostal(postal)){
            Address address = new Address(number,name,province,postal);

            HashMap<String, Object> address_map = new HashMap<String, Object>();
            address_map.put("number", address.number);
            address_map.put("name", address.street);
            address_map.put("province", address.province);
            address_map.put("postal", address.postal);

            try {
                dbRef.child("address").setValue(address_map);
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "Address Not Modified", Toast.LENGTH_LONG).show();
            }
        }
        Toast.makeText(getApplicationContext(), "Address Modified", Toast.LENGTH_LONG).show();
    }

    public void modifyPhone(View v){
        String phone = editPhone.getText().toString();

        if(validatePhone(phone)){
            dbRef.child("phone").setValue(phone);
        }
        Toast.makeText(getApplicationContext(), "Phone Number Modified", Toast.LENGTH_LONG).show();
    }

    public boolean validateStreetName(String n){
        if (n.isEmpty()) {
            editStreetName.setError("Must enter a street name.");
            editStreetName.requestFocus();
            return false;
        }
        if (containsNumOrSChar(n).size() != 0) {
            editStreetName.setError("Must not enter numbers or special characters.");
            editStreetName.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validateStreetNumber(String n){
        if(!n.isEmpty()){
            Integer number = 0;
            try {
                number = Integer.parseInt(n);
            } catch (NumberFormatException e) {
                editStreetNumber.setError("Must enter a valid number.");
                editStreetNumber.requestFocus();
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean validatePhone(String p){
        if(p.matches("^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")){
            return true;
        }
        else{
            editPhone.setError("Must enter a valid number.");
            editPhone.requestFocus();
        }
        return false;
    }

    public boolean validatePostal(String p){
        if(p.matches("^[ABCEGHJ-NPRSTVXY][0-9][ABCEGHJ-NPRSTV-Z] [0-9][ABCEGHJ-NPRSTV-Z][0-9]$")){
            return true;
        }
        else{
            editPostal.setError("Must enter a valid postal code.");
            editPostal.requestFocus();
        }
        return false;
    }

    /**
     * Return true if a string contains numbers or special characters.
     * @param text
     * @return true if a string contains numbers or special characters.
     */
    private ArrayList<Character> containsNumOrSChar(String text) {
        // create hash set of all numbers and special characters
        HashSet<String> vals = new HashSet<String>();
        vals.addAll(Arrays.asList("1234567890".split("")));
        // allows ', -, _, `, "
        vals.addAll(Arrays.asList("!@%^&*()=+~{}|\\;:,<>/?".split("")));

        ArrayList<Character> res = new ArrayList<Character>();

        for (int i = 0; i < text.length(); i++) {
            if (vals.contains(String.valueOf(text.charAt(i)))) {
                res.add(text.charAt(i));
            }
        }

        return res;
    }

    //gets index of current province
    private int getProvinceIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
}