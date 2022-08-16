package com.group15.seg2105finalproject.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;
import com.group15.seg2105finalproject.main.MainActivity;

public class CustomerWelcomeActivity extends AppCompatActivity {

    public static final String USER_INFO = MainActivity.USER_INFO;

    TextView welcomeMessage;
    TextView roleTextView;
    Button customerRequests;
    Button searchButton;
    String username;
    String roleString;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_welcome);

        customerRequests = (Button) findViewById(R.id.customerViewOwnRequests);
        welcomeMessage = (TextView) findViewById(R.id.customerWelcomeMessage);
        roleTextView = findViewById(R.id.roleTextView1);
        searchButton = (Button) findViewById(R.id.customerSearchButton);

        Intent i = getIntent();
        username = i.getStringExtra(USER_INFO);

        db = FirebaseDatabase.getInstance().getReference().child("user");

        // get user info from db
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get role
                if (snapshot.child(username).child("role").child("isCustomer").getValue(Boolean.class)) {
                    roleString = "You are logged in as Customer";}

//                } else if (snapshot.child(usernameString).child("role").child("isEmployee").getValue(Boolean.class)){
//                    roleString = "Employee";
//                } else {
//                    roleString = "Admin";
//                }

                roleTextView.setText(roleString);

                welcomeMessage.setText("Welcome "+snapshot.child(username).child("first name").getValue().toString()+"!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerWelcomeActivity.this, "An error has occured.", Toast.LENGTH_SHORT).show();
            }
        });

        customerRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CustomerRequests.class);
                intent.putExtra(USER_INFO, username);
                startActivity(intent);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CustomerSearchBranches.class);
                intent.putExtra(USER_INFO,username);
                startActivity(intent);
            }
        });
    }

}
