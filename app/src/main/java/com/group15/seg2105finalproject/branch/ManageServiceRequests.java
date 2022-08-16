package com.group15.seg2105finalproject.branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group15.seg2105finalproject.R;

import java.util.LinkedList;
import java.util.List;

public class ManageServiceRequests extends AppCompatActivity {
    public static final String USER_INFO = EmployeeWelcomeActivity.USER_INFO;
    ListView pendingList;
    ListView approvedList;
    Button approveButton;
    Button rejectButton;
    Button backPendingButton;
    Button viewApprovedRequestDetails;
    Button viewPendingRequestDetails;
    ArrayAdapter<String> adapterPending;
    ArrayAdapter<String> adapterApproved;
    List<String> pending;
    List<String> approved;
    List<String> pendingListKeys;
    List<String> approvedListKeys;
    String username;
    DatabaseReference dbRef;
    String pendingSelected;
    int pendingPosition;
    int approvedPosition;
    String approvedSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_service_requests);

        pendingList = (ListView) findViewById(R.id.pendingListView);
        approvedList = (ListView) findViewById(R.id.approvedListView);
        approveButton= (Button) findViewById(R.id.approveButton);
        rejectButton= (Button) findViewById(R.id.rejectButton);
        backPendingButton= (Button) findViewById(R.id.backPendingButton);
        viewApprovedRequestDetails = (Button) findViewById(R.id.viewRequestApprovedDetailsEmployee);
        viewPendingRequestDetails = (Button) findViewById(R.id.viewRequestPendingDetailsEmployee);
        pending = new LinkedList<>();
        approved = new LinkedList<>();
        pendingListKeys = new LinkedList<String>();
        approvedListKeys = new LinkedList<String>();
        approveButton.setEnabled(false);
        rejectButton.setEnabled(false);
        backPendingButton.setEnabled(false);
        viewApprovedRequestDetails.setEnabled(false);
        viewPendingRequestDetails.setEnabled(false);

        //get branch username
        Intent i = getIntent();
        username = i.getStringExtra(USER_INFO);

        //get database reference of user
        dbRef = FirebaseDatabase.getInstance().getReference("user").child(username);
        //note: status in request is 0 for pending, 1 for approved and 2 for rejected

        //set adapter to populate ListView for pending requests
        adapterPending = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,pending);
        //select one requirement at a time
        pendingList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        pendingList.setAdapter(adapterPending);

        //on click when requirement from ListView is selected
        pendingList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        pendingPosition = position;
                        pendingSelected = pending.get(position);
                        approveButton.setEnabled(true);
                        rejectButton.setEnabled(true);
                        viewPendingRequestDetails.setEnabled(true);
                    }
        });

        //onclick for when user presses the approve button
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveRequest(v);
                if(pendingPosition < pending.size()){
                    pendingList.setItemChecked(pendingPosition,false);
                }
                approveButton.setEnabled(false);
                rejectButton.setEnabled(false);
                viewPendingRequestDetails.setEnabled(false);
            }
        });

        //onclick for when user presses the rejects button
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest(v);
                if(pendingPosition < pending.size()){
                    pendingList.setItemChecked(pendingPosition,false);
                }
                approveButton.setEnabled(false);
                rejectButton.setEnabled(false);
                viewPendingRequestDetails.setEnabled(false);

            }
        });
        //onclick for when user presses the rejects button
        backPendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putBackPending(v);
                if(approvedPosition < approved.size()){
                    approvedList.setItemChecked(approvedPosition,false);
                }
                backPendingButton.setEnabled(false);
                viewApprovedRequestDetails.setEnabled(false);
            }
        });

        //onClick for viewing full details of the customer approved request
        viewApprovedRequestDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), EmployeeViewCustomerRequestDetails.class);
                i.putExtra("request", approvedListKeys.get(approvedPosition));
                i.putExtra("username", username);

                ManageServiceRequests.this.startActivity(i);
            }
        });

        //onClick for viewing full details of the customer pending request
        viewPendingRequestDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), EmployeeViewCustomerRequestDetails.class);
                i.putExtra("request", pendingListKeys.get(pendingPosition));
                i.putExtra("username", username);

                ManageServiceRequests.this.startActivity(i);
            }
        });

        //set adapter to populate ListView for approved requests
        adapterApproved = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,approved);
        //select one requirement at a time
        approvedList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        //on click when requirement from ListView is selected
        //will add functionality for next deliverable!!
        approvedList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        approvedPosition = position;
                        approvedSelected = approved.get(position);
                        backPendingButton.setEnabled(true);
                        viewApprovedRequestDetails.setEnabled(true);
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //create 7 requests (commented out since temporary)
//                for(int i=0;i<7;i++){
//                    String key = dbRef.child("requests").push().getKey();
//                    HashMap<String, Object> service_map = new HashMap<String, Object>();
//                    service_map.put("client", "client"+String.valueOf(i+1));
//                    service_map.put("status", 0);
//                    dbRef.child("requests").child(key).setValue(service_map);
//                }
                //note: status in request is 0 for pending, 1 for approved and 2 for rejected
                //fill out the pending and approved lists
                for(DataSnapshot requestsSnapshot: snapshot.child("requests").getChildren()){
                    String status = requestsSnapshot.child("status").getValue().toString();
                    if(status!=null){
                        if(status.equals("0")){
                            pending.add(requestsSnapshot.child("client").getValue().toString());
                            pendingListKeys.add(requestsSnapshot.getKey());
                        }
                        else if(status.equals("1")){
                            approved.add(requestsSnapshot.child("client").getValue().toString()+", Status: approved");
                            approvedListKeys.add(requestsSnapshot.getKey());
                        }
                        else if(status.equals("2")){
                            approved.add(requestsSnapshot.child("client").getValue().toString()+", Status: rejected");
                            approvedListKeys.add(requestsSnapshot.getKey());
                        }
                    }
                }
                pendingList.setAdapter(adapterPending);
                approvedList.setAdapter(adapterApproved);
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
        adapterApproved.clear();
        adapterPending.clear();
        approveButton.setEnabled(false);
        rejectButton.setEnabled(false);
        backPendingButton.setEnabled(false);
        viewApprovedRequestDetails.setEnabled(false);
        viewPendingRequestDetails.setEnabled(false);
    }

    //method to add a requirement
    public void approveRequest(View view){
        String approvedName = pendingSelected;
        String approvedKey = pendingListKeys.get(pendingPosition);

        //update listView
        pendingListKeys.remove(pendingPosition);
        pending.remove(approvedName);
        adapterPending.notifyDataSetChanged();

        approved.add(approvedName+", Status: approved");
        approvedListKeys.add(approvedKey);
        adapterApproved.notifyDataSetChanged();

        //update database
        dbRef.child("requests").child(approvedKey).child("status").setValue(1);
    }

    //method to reject a requirement
    public void rejectRequest(View view){
        String approvedName = pendingSelected;
        String approvedKey = pendingListKeys.get(pendingPosition);

        //update listView
        pendingListKeys.remove(pendingPosition);
        pending.remove(approvedName);
        adapterPending.notifyDataSetChanged();

        Toast.makeText(getApplicationContext(),"Request successfully rejected!", Toast.LENGTH_SHORT).show();

        approved.add(approvedName+", Status: rejected");
        approvedListKeys.add(approvedKey);
        adapterApproved.notifyDataSetChanged();

        //update database
        dbRef.child("requests").child(approvedKey).child("status").setValue(2);
    }

    public void putBackPending(View view){
        String approvedName = approvedSelected;
        String approvedKey = approvedListKeys.get(approvedPosition);

        //update listView
        approvedListKeys.remove(approvedPosition);
        approved.remove(approvedName);
        adapterApproved.notifyDataSetChanged();


        //update database
        dbRef.child("requests").child(approvedKey).child("status").setValue(0);
        String pendingName = approvedName.substring(0,approvedName.indexOf(","));
        pending.add(pendingName);
        pendingListKeys.add(approvedKey);
        adapterPending.notifyDataSetChanged();



    }
}