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

import java.util.ArrayList;

public class GraphActivityViewModel extends ViewModel {

    private DatabaseReference instrumentListReference;
    public ArrayList<Instrument> instruments;
    MutableLiveData<ArrayList<Instrument>> instrumentsData;

    public GraphActivityViewModel() {
        instrumentListReference = FirebaseDatabase.getInstance().getReference("equities");
        instruments = new ArrayList<>();
        instrumentsData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Instrument>> getInstrumentsData(String key)
    {
        instrumentListReference.child(key).child("currentInstruments")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.exists())
                        {
                            Instrument instrument = snapshot.getValue(Instrument.class);
                            instruments.add(instrument);
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
