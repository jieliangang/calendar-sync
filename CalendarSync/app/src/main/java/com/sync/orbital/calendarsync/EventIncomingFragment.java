package com.sync.orbital.calendarsync;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventIncomingFragment extends Fragment {


    public EventIncomingFragment() {
        // Required empty public constructor
    }

    private ArrayList<EventIncomingStruct> eventList;
    private ListAdapter adapterIncoming;
    private RecyclerView recyclerViewIncoming;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_incoming, container, false);

        recyclerViewIncoming = (RecyclerView) view.findViewById(R.id.recycler_incoming);
        recyclerViewIncoming.setHasFixedSize(true);

        //Event info
        eventList = new ArrayList<>();

        //linear layout manager
        RecyclerView.LayoutManager layoutManagerIncoming = new LinearLayoutManager(this.getActivity());
        recyclerViewIncoming.setLayoutManager(layoutManagerIncoming);

        //specify adapter
        adapterIncoming = new ListAdapter(getActivity(), eventList);
        recyclerViewIncoming.setAdapter(adapterIncoming);

        getFirebaseEventData(new EventsCallback(){
           @Override
           public void onCallBack(EventIncomingStruct event){
               eventList.add(event);
               adapterIncoming.notifyDataSetChanged();

           }
        });

        return view;
    }

    private void getFirebaseEventUid(final EventsUidCallback eventsUidCallback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventsUidRef = reference.child("Users")
                .child(user.getUid())
                .child("events");
        Log.i("EventUid", "adding listener");
        eventsUidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Result will be holded Here
                for (DataSnapshot dataSnap: dataSnapshot.getChildren()){
                    eventsUidCallback.onCallBack(dataSnap.getKey());
                    Log.i("EventUid", dataSnap.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Handle error
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void getFirebaseEventData(final EventsCallback eventsCallback) {
        getFirebaseEventUid(new EventsUidCallback(){
            @Override
            public void onCallBack(String eventUid){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference eventsRef = reference.child("Events")
                        .child(eventUid);
                eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Result will be holded Here
                        String name = String.valueOf(dataSnapshot.child("name").getValue());
                        String status = String.valueOf(dataSnapshot.child("status").getValue());
                        String attendees = String.valueOf(dataSnapshot.child("attendees").getValue());
                        String startDate =  String.valueOf(dataSnapshot.child("startDate").getValue());
                        String startTime =  String.valueOf(dataSnapshot.child("startTime").getValue());
                        String endDate =  String.valueOf(dataSnapshot.child("endDate").getValue());
                        String endTime =  String.valueOf(dataSnapshot.child("endTime").getValue());
                        EventIncomingStruct events =
                                new EventIncomingStruct(name, status, attendees,
                                        startDate, startTime, endDate, endTime);
                        eventsCallback.onCallBack(events);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //Handle error
                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                    }
                });
            }
        });



    }

}
