package com.group15.seg2105finalproject.branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;

import java.util.LinkedList;
import java.util.List;

public class ViewRatings extends AppCompatActivity {
    public static final String USER_INFO = EmployeeWelcomeActivity.USER_INFO;
    ListView ratingsList;

    ArrayAdapter<String> adapter;
    List<String> ratings;

    DatabaseReference dbRefRatings;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);

        ratingsList = (ListView) findViewById(R.id.ratingsList);
        ratings = new LinkedList<String>();
        username = getIntent().getStringExtra(USER_INFO);

        dbRefRatings = FirebaseDatabase.getInstance().getReference("user").child(username).child("ratings");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ratings);
        ratingsList.setAdapter(adapter);
    }

    @Override
    protected void onStart(){

        super.onStart();
        dbRefRatings.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot r: snapshot.getChildren()){

                    ratings.add(r.child("customer").getValue().toString()+ ": " + r.child("value").getValue().toString()+"\n"+r.child("comment").getValue().toString());
                }

                ratingsList.setAdapter(adapter);
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