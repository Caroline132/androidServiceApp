package com.group15.seg2105finalproject.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.group15.seg2105finalproject.R;

public class AdminWelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);
        //startActivity(new Intent(getApplicationContext(), DeleteUser.class));

        final Button deleteButton = findViewById(R.id.deleteUser);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeleteUser.class));
            }
        });

        final Button managementButton = findViewById(R.id.serviceManagement);
        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageServices.class));
            }
        });
    }
}
