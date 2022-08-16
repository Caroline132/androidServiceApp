package com.group15.seg2105finalproject.branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;

import java.util.HashMap;

public class BusinessHours extends AppCompatActivity {
    Button updateHours;
    Switch mondaySwitch;
    Switch tuesdaySwitch;
    Switch wednesdaySwitch;
    Switch thursdaySwitch;
    Switch fridaySwitch;
    Switch saturdaySwitch;
    Switch sundaySwitch;
    Spinner mondayStart;
    Spinner tuesdayStart;
    Spinner wednesdayStart;
    Spinner thursdayStart;
    Spinner fridayStart;
    Spinner saturdayStart;
    Spinner sundayStart;
    Spinner mondayEnd;
    Spinner tuesdayEnd;
    Spinner wednesdayEnd;
    Spinner thursdayEnd;
    Spinner fridayEnd;
    Spinner saturdayEnd;
    Spinner sundayEnd;
    String[] hours;
    DatabaseReference dbRef;
    public static final String USER_INFO = EmployeeWelcomeActivity.USER_INFO;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_hours);

        updateHours = (Button) findViewById(R.id.updateHours);
        mondaySwitch = (Switch) findViewById(R.id.mondaySwitch);
        tuesdaySwitch = (Switch) findViewById(R.id.tuesdaySwitch);
        wednesdaySwitch = (Switch) findViewById(R.id.wednesdaySwitch);
        thursdaySwitch = (Switch) findViewById(R.id.thursdaySwitch);
        fridaySwitch = (Switch) findViewById(R.id.fridaySwitch);
        saturdaySwitch = (Switch) findViewById(R.id.saturdaySwitch);
        sundaySwitch = (Switch) findViewById(R.id.sundaySwitch);
        mondayStart = (Spinner) findViewById(R.id.mondayStart);
        tuesdayStart = (Spinner) findViewById(R.id.tuesdayStart);
        wednesdayStart = (Spinner) findViewById(R.id.wednesdayStart);
        thursdayStart = (Spinner) findViewById(R.id.thursdayStart);
        fridayStart = (Spinner) findViewById(R.id.fridayStart);
        saturdayStart = (Spinner) findViewById(R.id.saturdayStart);
        sundayStart = (Spinner) findViewById(R.id.sundayStart);
        mondayEnd = (Spinner) findViewById(R.id.mondayEnd);
        tuesdayEnd = (Spinner) findViewById(R.id.tuesdayEnd);
        wednesdayEnd = (Spinner) findViewById(R.id.wednesdayEnd);
        thursdayEnd = (Spinner) findViewById(R.id.thursdayEnd);
        fridayEnd = (Spinner) findViewById(R.id.fridayEnd);
        saturdayEnd = (Spinner) findViewById(R.id.saturdayEnd);
        sundayEnd = (Spinner) findViewById(R.id.sundayEnd);

        //province array
        hours = new String[]{"12:00 am","1:00 am","2:00 am","3:00 am",
                "4:00 am", "5:00 am", "6:00 am","7:00 am","8:00 am","9:00 am","10:00 am","11:00 am","12:00 pm","1:00 pm","2:00 pm","3:00 pm",
                "4:00 pm", "5:00 pm", "6:00 pm","7:00 pm","8:00 pm","9:00 pm","10:00 pm","11:00 pm"};

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                hours);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        mondayStart.setAdapter(ad);
        tuesdayStart.setAdapter(ad);
        wednesdayStart.setAdapter(ad);
        thursdayStart.setAdapter(ad);
        fridayStart.setAdapter(ad);
        saturdayStart.setAdapter(ad);
        sundayStart.setAdapter(ad);
        mondayEnd.setAdapter(ad);
        tuesdayEnd.setAdapter(ad);
        wednesdayEnd.setAdapter(ad);
        thursdayEnd.setAdapter(ad);
        fridayEnd.setAdapter(ad);
        saturdayEnd.setAdapter(ad);
        sundayEnd.setAdapter(ad);

        //get username
        Intent i = getIntent();
        username = i.getStringExtra(USER_INFO);

        //get database reference to specific user
        dbRef = FirebaseDatabase.getInstance().getReference("user").child(username);

        updateHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyHours(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(dbRef.child("hours")!=null){
                    if(snapshot.child("hours").child("Monday")!=null){
                        mondayStart.setSelection(getHoursIndex(mondayStart, snapshot.child("hours").child("Monday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Monday")!=null){
                        mondayEnd.setSelection(getHoursIndex(mondayEnd, snapshot.child("hours").child("Monday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Tuesday")!=null){
                        tuesdayStart.setSelection(getHoursIndex(tuesdayStart, snapshot.child("hours").child("Tuesday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Tuesday")!=null){
                        tuesdayEnd.setSelection(getHoursIndex(tuesdayEnd, snapshot.child("hours").child("Tuesday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Wednesday")!=null){
                        wednesdayStart.setSelection(getHoursIndex(wednesdayStart, snapshot.child("hours").child("Wednesday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Wednesday")!=null){
                        wednesdayEnd.setSelection(getHoursIndex(wednesdayEnd, snapshot.child("hours").child("Wednesday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Thursday")!=null){
                        thursdayStart.setSelection(getHoursIndex(thursdayStart, snapshot.child("hours").child("Thursday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Thursday")!=null){
                        thursdayEnd.setSelection(getHoursIndex(thursdayEnd, snapshot.child("hours").child("Thursday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Friday")!=null){
                        fridayStart.setSelection(getHoursIndex(fridayStart, snapshot.child("hours").child("Friday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Friday")!=null){
                        fridayEnd.setSelection(getHoursIndex(fridayEnd, snapshot.child("hours").child("Friday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Saturday")!=null){
                        saturdayStart.setSelection(getHoursIndex(saturdayStart, snapshot.child("hours").child("Saturday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Saturday")!=null){
                        saturdayEnd.setSelection(getHoursIndex(saturdayEnd, snapshot.child("hours").child("Saturday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Sunday")!=null){
                        sundayStart.setSelection(getHoursIndex(sundayStart, snapshot.child("hours").child("Sunday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Sunday")!=null){
                        sundayEnd.setSelection(getHoursIndex(sundayEnd, snapshot.child("hours").child("Sunday").child("endTime").getValue(String.class)));
                    }

                    //enable the enableServiceSwitch if true
                    if(snapshot.child("hours").child("Monday").child("isEnabled")!=null){
                        boolean mondayEnabled = Boolean.valueOf(snapshot.child("hours").child("Monday").child("isEnabled").getValue(String.class));
                        if(mondayEnabled){
                            mondaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Tuesday").child("isEnabled")!=null){
                        boolean tuesdayEnabled = Boolean.valueOf(snapshot.child("hours").child("Tuesday").child("isEnabled").getValue(String.class));
                        if(tuesdayEnabled){
                            tuesdaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Wednesday").child("isEnabled")!=null){
                        boolean wednesdayEnabled = Boolean.valueOf(snapshot.child("hours").child("Wednesday").child("isEnabled").getValue(String.class));
                        if(wednesdayEnabled){
                            wednesdaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Thursday").child("isEnabled")!=null){
                        boolean thursdayEnabled = Boolean.valueOf(snapshot.child("hours").child("Thursday").child("isEnabled").getValue(String.class));
                        if(thursdayEnabled){
                            thursdaySwitch.setChecked(true);

                        }
                    }
                    if(snapshot.child("hours").child("Friday").child("isEnabled")!=null){
                        boolean fridayEnabled = Boolean.valueOf(snapshot.child("hours").child("Friday").child("isEnabled").getValue(String.class));
                        if(fridayEnabled){
                            fridaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Saturday").child("isEnabled")!=null){
                        boolean saturdayEnabled = Boolean.valueOf(snapshot.child("hours").child("Saturday").child("isEnabled").getValue(String.class));
                        if(saturdayEnabled){
                            saturdaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Sunday").child("isEnabled")!=null){
                        boolean sundayEnabled = Boolean.valueOf(snapshot.child("hours").child("Sunday").child("isEnabled").getValue(String.class));
                        if(sundayEnabled){
                            sundaySwitch.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void modifyHours(View v){
        String mondayStartTime = mondayStart.getSelectedItem().toString();
        String mondayEndTime = mondayEnd.getSelectedItem().toString();
        String tuesdayStartTime = tuesdayStart.getSelectedItem().toString();
        String tuesdayEndTime = tuesdayEnd.getSelectedItem().toString();
        String wednesdayStartTime = wednesdayStart.getSelectedItem().toString();
        String wednesdayEndTime = wednesdayEnd.getSelectedItem().toString();
        String thursdayStartTime = thursdayStart.getSelectedItem().toString();
        String thursdayEndTime = thursdayEnd.getSelectedItem().toString();
        String fridayStartTime = fridayStart.getSelectedItem().toString();
        String fridayEndTime = fridayEnd.getSelectedItem().toString();
        String saturdayStartTime = saturdayStart.getSelectedItem().toString();
        String saturdayEndTime = saturdayEnd.getSelectedItem().toString();
        String sundayStartTime = sundayStart.getSelectedItem().toString();
        String sundayEndTime = sundayEnd.getSelectedItem().toString();

        boolean mondayChecked = mondaySwitch.isChecked();
        boolean tuesdayChecked = tuesdaySwitch.isChecked();
        boolean wednesdayChecked = wednesdaySwitch.isChecked();
        boolean thursdayChecked = thursdaySwitch.isChecked();
        boolean fridayChecked = fridaySwitch.isChecked();
        boolean saturdayChecked = saturdaySwitch.isChecked();
        boolean sundayChecked = sundaySwitch.isChecked();
        if(validateHours(mondayStartTime,mondayEndTime)&&validateHours(tuesdayStartTime,tuesdayEndTime)&& validateHours(wednesdayStartTime,wednesdayEndTime)&&validateHours(thursdayStartTime,thursdayEndTime)&&validateHours(fridayStartTime,fridayEndTime)&& validateHours(saturdayStartTime,saturdayEndTime)&&validateHours(sundayStartTime,sundayEndTime)) {
            HashMap<String, Object> monday_map = new HashMap<String, Object>();
            monday_map.put("startTime", mondayStartTime);
            monday_map.put("endTime", mondayEndTime);
            monday_map.put("isEnabled", String.valueOf(mondayChecked));

            dbRef.child("hours").child("Monday").setValue(monday_map);


            HashMap<String, Object> tuesday_map = new HashMap<String, Object>();
            tuesday_map.put("startTime", tuesdayStartTime);
            tuesday_map.put("endTime", tuesdayEndTime);
            tuesday_map.put("isEnabled", String.valueOf(tuesdayChecked));

            dbRef.child("hours").child("Tuesday").setValue(tuesday_map);

            HashMap<String, Object> wednesday_map = new HashMap<String, Object>();
            wednesday_map.put("startTime", wednesdayStartTime);
            wednesday_map.put("endTime", wednesdayEndTime);
            wednesday_map.put("isEnabled", String.valueOf(wednesdayChecked));

            dbRef.child("hours").child("Wednesday").setValue(wednesday_map);

            HashMap<String, Object> thursday_map = new HashMap<String, Object>();
            thursday_map.put("startTime", thursdayStartTime);
            thursday_map.put("endTime", thursdayEndTime);
            thursday_map.put("isEnabled", String.valueOf(thursdayChecked));

            dbRef.child("hours").child("Thursday").setValue(thursday_map);

            HashMap<String, Object> friday_map = new HashMap<String, Object>();
            friday_map.put("startTime", fridayStartTime);
            friday_map.put("endTime", fridayEndTime);
            friday_map.put("isEnabled", String.valueOf(fridayChecked));

            dbRef.child("hours").child("Friday").setValue(friday_map);

            HashMap<String, Object> saturday_map = new HashMap<String, Object>();
            saturday_map.put("startTime", saturdayStartTime);
            saturday_map.put("endTime", saturdayEndTime);
            saturday_map.put("isEnabled", String.valueOf(saturdayChecked));

            dbRef.child("hours").child("Saturday").setValue(saturday_map);

            HashMap<String, Object> sunday_map = new HashMap<String, Object>();
            sunday_map.put("startTime", sundayStartTime);
            sunday_map.put("endTime", sundayEndTime);
            sunday_map.put("isEnabled", String.valueOf(sundayChecked));

            dbRef.child("hours").child("Sunday").setValue(sunday_map);

            Toast.makeText(getApplicationContext(), "Hours Modified", Toast.LENGTH_LONG).show();
        }
        else{
            updateData();
            Toast.makeText(getApplicationContext(), "Can't set end hour before start hour!", Toast.LENGTH_LONG).show();
        }
    }

    //gets index of current province
    private int getHoursIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private boolean validateHours(String start, String end){
        if((start.contains("am")&&end.contains("am")) ||(start.contains("pm")&&end.contains("pm"))  ){
            int startSub = Integer.valueOf(start.substring(0,start.indexOf(':')));
            int endSub = Integer.valueOf(end.substring(0,end.indexOf(':')));
            if(startSub>endSub){
                return false;
            }
        }
        if(end.equals("12:00 am")){
            return false;
        }
        return true;
    }

    private void updateData(){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(dbRef.child("hours")!=null){
                    if(snapshot.child("hours").child("Monday")!=null){
                        mondayStart.setSelection(getHoursIndex(mondayStart, snapshot.child("hours").child("Monday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Monday")!=null){
                        mondayEnd.setSelection(getHoursIndex(mondayEnd, snapshot.child("hours").child("Monday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Tuesday")!=null){
                        tuesdayStart.setSelection(getHoursIndex(tuesdayStart, snapshot.child("hours").child("Tuesday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Tuesday")!=null){
                        tuesdayEnd.setSelection(getHoursIndex(tuesdayEnd, snapshot.child("hours").child("Tuesday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Wednesday")!=null){
                        wednesdayStart.setSelection(getHoursIndex(wednesdayStart, snapshot.child("hours").child("Wednesday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Wednesday")!=null){
                        wednesdayEnd.setSelection(getHoursIndex(wednesdayEnd, snapshot.child("hours").child("Wednesday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Thursday")!=null){
                        thursdayStart.setSelection(getHoursIndex(thursdayStart, snapshot.child("hours").child("Thursday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Thursday")!=null){
                        thursdayEnd.setSelection(getHoursIndex(thursdayEnd, snapshot.child("hours").child("Thursday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Friday")!=null){
                        fridayStart.setSelection(getHoursIndex(fridayStart, snapshot.child("hours").child("Friday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Friday")!=null){
                        fridayEnd.setSelection(getHoursIndex(fridayEnd, snapshot.child("hours").child("Friday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Saturday")!=null){
                        saturdayStart.setSelection(getHoursIndex(saturdayStart, snapshot.child("hours").child("Saturday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Saturday")!=null){
                        saturdayEnd.setSelection(getHoursIndex(saturdayEnd, snapshot.child("hours").child("Saturday").child("endTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Sunday")!=null){
                        sundayStart.setSelection(getHoursIndex(sundayStart, snapshot.child("hours").child("Sunday").child("startTime").getValue(String.class)));
                    }
                    if(snapshot.child("hours").child("Sunday")!=null){
                        sundayEnd.setSelection(getHoursIndex(sundayEnd, snapshot.child("hours").child("Sunday").child("endTime").getValue(String.class)));
                    }

                    //enable the enableServiceSwitch if true
                    if(snapshot.child("hours").child("Monday").child("isEnabled")!=null){
                        boolean mondayEnabled = Boolean.valueOf(snapshot.child("hours").child("Monday").child("isEnabled").getValue(String.class));
                        if(mondayEnabled){
                            mondaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Tuesday").child("isEnabled")!=null){
                        boolean tuesdayEnabled = Boolean.valueOf(snapshot.child("hours").child("Tuesday").child("isEnabled").getValue(String.class));
                        if(tuesdayEnabled){
                            tuesdaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Wednesday").child("isEnabled")!=null){
                        boolean wednesdayEnabled = Boolean.valueOf(snapshot.child("hours").child("Wednesday").child("isEnabled").getValue(String.class));
                        if(wednesdayEnabled){
                            wednesdaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Thursday").child("isEnabled")!=null){
                        boolean thursdayEnabled = Boolean.valueOf(snapshot.child("hours").child("Thursday").child("isEnabled").getValue(String.class));
                        if(thursdayEnabled){
                            thursdaySwitch.setChecked(true);

                        }
                    }
                    if(snapshot.child("hours").child("Friday").child("isEnabled")!=null){
                        boolean fridayEnabled = Boolean.valueOf(snapshot.child("hours").child("Friday").child("isEnabled").getValue(String.class));
                        if(fridayEnabled){
                            fridaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Saturday").child("isEnabled")!=null){
                        boolean saturdayEnabled = Boolean.valueOf(snapshot.child("hours").child("Saturday").child("isEnabled").getValue(String.class));
                        if(saturdayEnabled){
                            saturdaySwitch.setChecked(true);
                        }
                    }
                    if(snapshot.child("hours").child("Sunday").child("isEnabled")!=null){
                        boolean sundayEnabled = Boolean.valueOf(snapshot.child("hours").child("Sunday").child("isEnabled").getValue(String.class));
                        if(sundayEnabled){
                            sundaySwitch.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}