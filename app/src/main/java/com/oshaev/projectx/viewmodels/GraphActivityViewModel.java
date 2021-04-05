package com.oshaev.projectx.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oshaev.projectx.customobjects.Instrument;
import com.oshaev.projectx.customobjects.Point;

import java.util.ArrayList;

public class GraphActivityViewModel extends ViewModel {

    private DatabaseReference instrumentListReference;
    public ArrayList<Instrument> instruments;
    Integer instrumentsCounter = 0;
    Double timeDifference = 0D;
    private MutableLiveData<ArrayList<Instrument>> instrumentsData;

    private MutableLiveData<Double> timeDifferenceData = new MutableLiveData<>();


    public Instrument instrument;
    private MutableLiveData<Instrument> instrumentMutableLiveData = new MutableLiveData<>();


    ArrayList<Point> points;
    private MutableLiveData<ArrayList<Point>> pointsData = new MutableLiveData<>();


    public GraphActivityViewModel() {
        instrumentListReference = FirebaseDatabase.getInstance().getReference("equities");
        instruments = new ArrayList<>();
        instrumentsData = new MutableLiveData<>();
        points = new ArrayList<>();
    }

    public MutableLiveData<ArrayList<Point>> getPointsData(String key)
    {
        instrumentListReference.child(key).child("currentInstruments")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.exists())
                        {
                            Instrument instrument = snapshot.getValue(Instrument.class);
                            instruments.add(instrument);
                           // points.add(new Point(instrument.getTimeMinutes(timeDifference), instrument.getCLOSE()));
                            if(instrumentsCounter<2)
                            {
                                points.add(new Point(instrument.getTimeMinutes(timeDifference), instrument.getCLOSE()));
                            }

                            instrumentsCounter++;
                            if(instrumentsCounter>1)
                            {
                                if (instruments.get(instrumentsCounter).getTimeMinutes()
                                        - instruments.get(instrumentsCounter - 1).getTimeMinutes() > 15)
                                {
                                    timeDifference += instruments.get(instrumentsCounter).getTimeMinutes()
                                            - instruments.get(instrumentsCounter - 1).getTimeMinutes();
                                }

                                    if (instruments.get(instrumentsCounter-1).getTimeMinutes()
                                            < instruments.get(instrumentsCounter).getTimeMinutes()) {
                                        points.add(
                                                new Point(instruments.get(instrumentsCounter).getTimeMinutes(timeDifference)
                                                        , instruments.get(instrumentsCounter).getCLOSE()));
                                        pointsData.postValue(points);
                                    }


                            }


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
        return pointsData;
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


    public MutableLiveData<Double> getTimeDifferenceData()
    {
        timeDifferenceData.setValue(timeDifference);
        return timeDifferenceData;
    }

}
