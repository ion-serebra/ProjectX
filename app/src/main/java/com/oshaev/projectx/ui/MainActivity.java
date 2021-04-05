package com.oshaev.projectx.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.oshaev.projectx.customobjects.Equity;
import com.oshaev.projectx.calculations.EquityAdapter;
import com.oshaev.projectx.customobjects.Instrument;
import com.oshaev.projectx.viewmodels.MainActivityViewModel;
import com.oshaev.projectx.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    MainActivityViewModel model;
    RecyclerView equitiesRecyclerView;
    LinearLayoutManager manager;
    EquityAdapter adapter;

    private ArrayList<Instrument> instrumentArrayList = new ArrayList<>();
    private ArrayList<Equity> equityArrayList = new ArrayList<>();

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



        mainProgressBar = findViewById(R.id.mainProgressBar);

        Double testD = 2D;
        /*
        instrumentArrayList.add(
                new Instrument("df", 2D, 2L, testD, testD, testD, testD, testD, testD));
        instrumentArrayList.add(
                new Instrument("df", testD, testD, testD, testD, testD, testD, testD, testD));

         */
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
    }
    public void calculateTimeSeries(View view) {
    }
}
