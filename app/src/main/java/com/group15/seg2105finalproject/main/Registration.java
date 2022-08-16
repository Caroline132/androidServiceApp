package com.group15.seg2105finalproject.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group15.seg2105finalproject.R;
import com.group15.seg2105finalproject.branch.Branch;
import com.group15.seg2105finalproject.customer.Customer;
import com.group15.seg2105finalproject.customer.CustomerWelcomeActivity;
import com.group15.seg2105finalproject.branch.Employee;
import com.group15.seg2105finalproject.branch.EmployeeWelcomeActivity;


import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Registration extends AppCompatActivity {
    private Button registerUserButton;
    private EditText firstName, lastName, email, username, password;
    private RadioButton checkEmployee,checkCustomer;

    private boolean isEmployee, isCustomer;

    private FirebaseAuth mAuth;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerUserButton = (Button) findViewById(R.id.registerUser);
        checkCustomer = (RadioButton) findViewById(R.id.checkEmployee);
        checkEmployee = (RadioButton) findViewById(R.id.checkEmployee);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstNameText = firstName.getText().toString().trim();
                String lastNameText = lastName.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String usernameText = username.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                if (!isNameValid(firstNameText, lastNameText) ||
                    !isUsernameValid(usernameText) ||
                    !isEmailValid(emailText) ||
                    !isPasswordValid(passwordText))
                    return;

                mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(isCustomer){
                                Customer customer = new Customer(usernameText,passwordText,firstNameText,lastNameText,emailText, new ArrayList());
                                Toast.makeText(Registration.this,"Customer Created",Toast.LENGTH_SHORT).show();

                            }else if(isEmployee){
                                Branch branch = new Branch("", "",new ArrayList(), new HashMap());
                                Employee user = new Employee(usernameText,passwordText,firstNameText,lastNameText,emailText, branch);
                                Toast.makeText(Registration.this,"Employee Created",Toast.LENGTH_SHORT).show();
                            }

                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<String, Boolean> role = new HashMap<String, Boolean>();
                            role.put("isAdmin", false);
                            role.put("isEmployee", isEmployee);
                            role.put("isCustomer", isCustomer);

                            writeNewUser(user.getUid(), emailText, usernameText, role,firstNameText,lastNameText);

                            Class welcomeActivityClass;

                            if (isEmployee) {
                                welcomeActivityClass = EmployeeWelcomeActivity.class;
                                //create map of hours
                                HashMap<String, String> inside_hours_map = new HashMap<String, String>();
                                inside_hours_map.put("endTime", "12:00 pm");
                                inside_hours_map.put("isEnabled", "false");
                                inside_hours_map.put("startTime", "12:00 am");

                                HashMap<String, Object> hours_map = new HashMap<String, Object>();
                                hours_map.put("Monday", inside_hours_map);
                                hours_map.put("Tuesday", inside_hours_map);
                                hours_map.put("Wednesday", inside_hours_map);
                                hours_map.put("Thursday", inside_hours_map);
                                hours_map.put("Friday", inside_hours_map);
                                hours_map.put("Saturday", inside_hours_map);
                                hours_map.put("Sunday", inside_hours_map);

                                db.child("user").child(usernameText).child("hours").setValue(hours_map);
                            }
                            else
                                welcomeActivityClass = CustomerWelcomeActivity.class;

                            Intent intent = new Intent(getApplicationContext(),welcomeActivityClass);
                            intent.putExtra(MainActivity.USER_INFO, usernameText);
                            startActivity(intent);

                        }else{
                            Toast.makeText(Registration.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void onRoleSelect(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (view.getId() == R.id.checkCustomer) {
            isCustomer = true;
            isEmployee = false;
        } else {
            isCustomer = false;
            isEmployee = true;
        }
    }

    /**
     * Write a new user to the firebase database.
     * @param userID
     * @param email
     * @param username
     * @param role
     */
    private void writeNewUser(String userID, String email, String username, HashMap<String, Boolean> role, String firstName, String lastName) {
        db.child("user").child(username).child("first name").setValue(firstName);
        db.child("user").child(username).child("last name").setValue(lastName);
        db.child("user").child(username).child("email").setValue(email);
        db.child("user").child(username).child("userID").setValue(userID);
        db.child("user").child(username).child("role").setValue(role);
    }

    /**
     * Return true if usernameText string is not empty.
     * @param usernameText
     * @return true if usernameText string is not empty.
     */
    private boolean isUsernameValid(String usernameText) {
        if (usernameText.isEmpty()) {
            username.setError("Must enter a username.");
            username.requestFocus();
            return false;
        }

        if (containsChars(usernameText, ".#&[]")) {
            username.setError("Cannot use symbols '.', '#', '$', '[', or ']'");
            username.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Return true if both the firstNameText and lastNameText are not empty & do not contain
     * numbers or special characters.
     * @param firstNameText
     * @param lastNameText
     * @return true if both the firstNameText and lastNameText are not empty & do not contain
     *         numbers or special characters.
     */
    private boolean isNameValid(String firstNameText, String lastNameText) {
        if (firstNameText.isEmpty()) {
            firstName.setError("Must enter a first name.");
            firstName.requestFocus();
            return false;
        }

        if (lastNameText.isEmpty()) {
            lastName.setError("Must enter a last name.");
            lastName.requestFocus();
            return false;
        }

        if (containsNumOrSChar(firstNameText).size() != 0) {
            firstName.setError("Must not enter numbers or special characters.");
            firstName.requestFocus();
            return false;
        }

        if (containsNumOrSChar(lastNameText).size() != 0) {
            lastName.setError("Must not enter numbers or special characters.");
            lastName.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Return true if emailText is not empty and is a valid email address.
     * @param emailText
     * @return true if emailText is not empty and is a valid email address.
     */
    private boolean isEmailValid(String emailText) {
        if (emailText.isEmpty()) {
            email.setError("Must enter an email.");
            email.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Must enter a valid email.");
            email.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Return true if passwordText is not empty, is at least 7 characters long, and contains
     * numbers or special characters.
     * @param passwordText
     * @return true if passwordText is not empty, is at least 7 characters long, and contains
     *         numbers or special characters.
     */
    private boolean isPasswordValid(String passwordText) {
        if (passwordText.isEmpty()) {
            password.setError("Must enter a password.");
            password.requestFocus();
            return false;
        }

        if (passwordText.length() < 7) {
            password.setError("Password must be at least 7 characters long.");
            password.requestFocus();
            return false;
        }

        // check if password contains numbers or special characters
        // (including special characters not allowed in first/last names)
        if (containsNumOrSChar(passwordText).size() == 0) {
            password.setError("Password must contain numbers and/or special characters.");
            password.requestFocus();
            return false;
        }

        return true;
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

    /**
     * Return true if string s1 contains any characters in s2.
     * @param s1
     * @param s2
     * @return true if string s1 contains any characters in s2.
     */
    public static boolean containsChars(String s1, String s2) {
        HashSet<String> s2Set = new HashSet<String>();
        s2Set.addAll(Arrays.asList(s2.split("")));

        for (int i = 0; i < s1.length(); i ++) {
            if (s2Set.contains(String.valueOf(s1.charAt(i)))) {
                return true;
            }
        }

        return false;
    }

}