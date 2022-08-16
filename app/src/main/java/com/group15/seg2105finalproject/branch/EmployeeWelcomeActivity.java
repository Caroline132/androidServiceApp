package com.group15.seg2105finalproject.branch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.group15.seg2105finalproject.R;
import com.group15.seg2105finalproject.main.MainActivity;

public class EmployeeWelcomeActivity extends AppCompatActivity {
    public static final String USER_INFO = MainActivity.USER_INFO;
    Button profileButton;
    Button servicesButton;
    Button hoursButton;
    Button requests2Button;
    Button ratingsButton;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_welcome);
        profileButton = (Button) findViewById(R.id.branchProfileButton);
        servicesButton = (Button) findViewById(R.id.branchServicesButton);
        hoursButton = (Button) findViewById(R.id.branchHoursButton);
        requests2Button = (Button) findViewById(R.id.alternativeSRButton);
        ratingsButton = (Button) findViewById(R.id.buttonRatings);

        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                username = i.getStringExtra(MainActivity.USER_INFO);
                Intent intent = new Intent(getApplicationContext(), EditBranchProfile.class);
                intent.putExtra(USER_INFO,username);
                startActivity(intent);
            }
        });

        hoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                username = i.getStringExtra(MainActivity.USER_INFO);
                Intent intent = new Intent(getApplicationContext(), BusinessHours.class);
                intent.putExtra(USER_INFO,username);
                startActivity(intent);
            }
        });

        servicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                username = i.getStringExtra(MainActivity.USER_INFO);
                Intent intent = new Intent(getApplicationContext(), ManageBranchServices.class);
                intent.putExtra(USER_INFO,username);
                startActivity(intent);
            }
        });


        requests2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                username = i.getStringExtra(MainActivity.USER_INFO);
                Intent intent = new Intent(getApplicationContext(), ManageServiceRequests.class);
                intent.putExtra(USER_INFO,username);
                startActivity(intent);
            }
        });

        ratingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                username = i.getStringExtra(MainActivity.USER_INFO);
                Intent intent = new Intent(getApplicationContext(), ViewRatings.class);
                intent.putExtra(USER_INFO,username);
                startActivity(intent);
            }
        });
    }
}
