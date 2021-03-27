package com.oshaev.projectx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {



    MainActivityViewModel model;
    RecyclerView equitiesRecyclerView;
    LinearLayoutManager manager;
    EquityAdapter adapter;

    private ArrayList<Instrument> instrumentArrayList;
    private ArrayList<Equity> equityArrayList;

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries mSeries2;
    private double graph2LastXValue = 5d;
    Long timeDifference = 0L;
    public ArrayList<ArrayList<Instrument>> listListInstruments;

    private boolean loadFlag = true;

    ProgressBar mainProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        //FirebaseApp.initializeApp(this);


        instrumentArrayList = new ArrayList<>();
        equityArrayList = new ArrayList<>();

        mainProgressBar = findViewById(R.id.mainProgressBar);

        instrumentArrayList.add(
                new Instrument("df", 2L,2L,2L,2L,2L,2L,2L,2L));
        instrumentArrayList.add(
                new Instrument("df", 2L,2L,2L,2L,2L,2L,2L,2L));

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("equities");

        model.getEquitiesData().observe(this, new Observer<ArrayList<Equity>>() {
            @Override
            public void onChanged(ArrayList<Equity> equities) {
                equityArrayList = equities;
                adapter.notifyDataSetChanged();
                mainProgressBar.setVisibility(View.GONE);
            }
        });

        equitiesRecyclerView = findViewById(R.id.equitiesRecyclerView);

        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new EquityAdapter(model.equities);
        equitiesRecyclerView.setLayoutManager(manager);
        equitiesRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickListener(new EquityAdapter.OnEquityClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                intent.putExtra("key", equityArrayList.get(position).getKey());
                startActivity(intent);
            }
        });


        /*
        ref.child("aflt").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("mtss").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("amd").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("dsky").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("gazp").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("moex").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("rual").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("rugr").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("kmaz").child("currentInstruments").setValue(instrumentArrayList);
        ref.child("intel").child("currentInstruments").setValue(instrumentArrayList);

         */
        //ref.child("rtkm").child("currentInstruments").setValue(instrumentArrayList);


        /*
        model.getEquitiesData().observe(this,
                new Observer<ArrayList<ArrayList<Instrument>>>() {
                    @Override
                    public void onChanged(ArrayList<ArrayList<Instrument>> arrayLists) {
                        listListInstruments = arrayLists;
                    }
                });


        //Toast.makeText(this, ""+ getTimeMinutes(20210122L, 224500L ), Toast.LENGTH_SHORT).show();

        model.getEquityData()
                .observe(this, new Observer<ArrayList<Instrument>>() {
                    @Override
                    public void onChanged(ArrayList<Instrument> equities) {
                        adapter.instruments = equities;
                        adapter.notifyDataSetChanged();



                        long prevTime = 0;
                        long currentTime = 0;
                        int i = 0;
                        Long timeMinutes = 0L;


                        for(int j = 0 ; j < equities.size() ; j++){
                            if(j>2) {

                                prevTime = getTimeMinutes(equities.get(j-1).getDATE(),
                                        equities.get(j-1).getTIME());
                                currentTime = getTimeMinutes(equities.get(j).getDATE(),
                                        equities.get(j).getTIME());

                                if(currentTime-prevTime>10)
                                {
                                    timeDifference += getTimeMinutes(equities.get(j).getDATE(),
                                        equities.get(j).getTIME())-getTimeMinutes(equities.get(j-1).getDATE(),
                                            equities.get(j-1).getTIME());
                                }

                                mSeries2.appendData(new DataPoint
                                                (getTimeMinutes(equities.get(j).getDATE(),
                                                        equities.get(j).getTIME(), timeDifference), equities.get(j).getCLOSE()),
                                        true, 100000);
                            } else {
                                mSeries2.appendData(new DataPoint
                                                (getTimeMinutes(equities.get(j).getDATE(),
                                                        equities.get(j).getTIME()), equities.get(j).getCLOSE()),
                                        true, 100000);
                            }
                        }
                    }
                });

         */



    }





}
