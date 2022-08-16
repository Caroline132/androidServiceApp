package com.group15.seg2105finalproject.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class DeleteUser extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    List<String> listItems;
    int userPosition;
    String userSelected;
    ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        Button buttonOne = (Button) findViewById(R.id.delete);
        userListView = (ListView) findViewById(R.id.userListView);
        listItems = new ArrayList<>();

        buttonOne.setEnabled(false);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                listItems);
        userListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        userListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        buttonOne.setEnabled(true);
                        userPosition = position;
                        userSelected = listItems.get(userPosition);
                    }
                });

        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                deleteUser();
                buttonOne.setEnabled(false);
                if(userPosition < listItems.size()){
                    userListView.setItemChecked(userPosition,false);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        //create list view
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("user");

        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot serviceSnapshot: snapshot.getChildren()){
                    //don't want to delete hardcoded employee user
                    if(!serviceSnapshot.getKey().equals("employee")){
                        listItems.add(serviceSnapshot.getKey());
                    }
                }
                userListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean deleteUser() {
        //getting the specified product reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("user").child(userSelected);

        listItems.remove(userPosition);
        adapter.notifyDataSetChanged();


        //updating product
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

}