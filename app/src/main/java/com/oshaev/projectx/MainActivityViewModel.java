package com.oshaev.projectx;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {

    private DatabaseReference reference;
    private DatabaseReference equitiesReference;
    public ArrayList<Instrument> instruments;

    public ArrayList<ArrayList<Instrument>> listArrayListInstruments;
    private MutableLiveData<ArrayList<ArrayList<Instrument>>> listsData;

    public ArrayList<Equity> equities;
    private MutableLiveData<ArrayList<Instrument>> instrumentsData;
    private MutableLiveData<ArrayList<Equity>> equityData;



    public MainActivityViewModel() {
        instruments = new ArrayList<>();
        equities = new ArrayList<>();
        instrumentsData = new MutableLiveData<>();
        equityData = new MutableLiveData<>();
        listsData = new MutableLiveData<>();

        equitiesReference = FirebaseDatabase.getInstance().getReference("equities");
        reference = FirebaseDatabase.getInstance()
                .getReference();
    }


    public MutableLiveData<ArrayList<Equity>> getEquitiesData() {
        equitiesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Equity equity = snapshot.getValue(Equity.class);
                equity.setKey(snapshot.getKey());
                equities.add(equity);
                equityData.postValue(equities);
                Log.d("getEquityData", "added");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return equityData;
    }



    public MutableLiveData<ArrayList<Instrument>> getEquityData()
    {
        reference.child("equities").child("intel").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    Instrument instrument = snapshot.getValue(Instrument.class);
                    instruments.add(instrument);
                    //equities.add(equity);
                    instrumentsData.postValue(instruments);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return instrumentsData;
    }


}










