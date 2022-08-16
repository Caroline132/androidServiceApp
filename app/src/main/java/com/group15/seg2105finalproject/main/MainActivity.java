package com.group15.seg2105finalproject.main;

import static com.group15.seg2105finalproject.main.User.isPasswordValid;
import static com.group15.seg2105finalproject.main.User.isUsernameValid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.branch.EmployeeWelcomeActivity;
import com.group15.seg2105finalproject.R;
import com.group15.seg2105finalproject.admin.AdminWelcomeActivity;
import com.group15.seg2105finalproject.customer.CustomerWelcomeActivity;

public class MainActivity extends AppCompatActivity {
    public static final String USER_INFO = "com.group15.seg2105finalproject.USER_INFO";

    private EditText usernameEditText, passwordEditText;

    private FirebaseAuth mAuth;
    private DatabaseReference db;

    String emailString;
    boolean isCustomer;
    boolean isEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText usernameEditText = (EditText) findViewById(R.id.username);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        TextView loginErrorMessage = (TextView) findViewById(R.id.loginError);

        db = FirebaseDatabase.getInstance().getReference().child("user");
        mAuth = FirebaseAuth.getInstance();

        final Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameString = String.valueOf(usernameEditText.getText());
                String passwordString = String.valueOf(passwordEditText.getText());

                // check if username or password were inputted
                if (!isUsernameValid(usernameString)) {
                    usernameEditText.setError("Invalid username.");
                    return;
                }

                if (!isPasswordValid(passwordString)) {
                    passwordEditText.setError("Invalid password.");
                    return;
                }

                // hard coding admin login
                if (usernameString.equals("admin") && passwordString.equals("admin")) {
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(view.getContext(), AdminWelcomeActivity.class));
                }

                // hard coding employee login
                 else if (usernameString.equals("employee") && passwordString.equals("employee")) {
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), EmployeeWelcomeActivity.class);
                    intent.putExtra(USER_INFO, "employee");
                    startActivity(intent);
                }

                 // hard coding customer login
                else if(usernameString.equals("customer") && passwordString.equals("customer")){

                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), CustomerWelcomeActivity.class);
                    intent.putExtra(USER_INFO, "customer");
                    startActivity(intent);
                }

                else{
                    // get email for firebase auth login
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            emailString = snapshot.child(usernameString).child("email").getValue(String.class);

                            if (emailString == null) {
                                Toast.makeText(MainActivity.this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // get role
                            if (snapshot.child(usernameString).child("role").child("isCustomer").getValue(Boolean.class)) {
                                isCustomer = true;
                            } else {
                                isCustomer = false;
                            }

                            mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                        Class welcomeActivityClass;
                                        if (isCustomer)
                                            welcomeActivityClass = CustomerWelcomeActivity.class;
                                        else
                                            welcomeActivityClass = EmployeeWelcomeActivity.class;

                                        Intent intent = new Intent(view.getContext(), welcomeActivityClass);
                                        intent.putExtra(USER_INFO, usernameString);
                                        startActivity(intent);
                                    } else {
                                        loginErrorMessage.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        final Button createUser = findViewById(R.id.createUser);
        createUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), Registration.class));
            }
        });

    }


}