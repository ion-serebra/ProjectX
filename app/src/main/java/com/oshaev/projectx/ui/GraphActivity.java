package com.oshaev.projectx.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.oshaev.projectx.viewmodels.GraphActivityViewModel;
import com.oshaev.projectx.calculations.HoltPrediction;
import com.oshaev.projectx.customobjects.Instrument;
import com.oshaev.projectx.customobjects.Point;
import com.oshaev.projectx.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class GraphActivity extends AppCompatActivity {


    private GraphActivityViewModel graphModel;
    private Double timeDifference = 0D;
    private LineGraphSeries mSeries1;
    private LineGraphSeries mSeries2;
    private TextView priceTextView;
    private TextView grownTextView;
    private TextView toolbarTitle;
    private TextView predictValueTextView;
    private TextView predictGrowthTextView;

    Double grown = 0D;
    Double predictGrow = 0D;
    String grownText= "";


    // уравнение тренда: y = bt + a
    //система для нахождения a и b:
    //a*sVC+b*sETS=sEYS
    //a*sETS+b*sESTS=sMYT


     GraphView graph2;



    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Point> smoothPoints = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();

        graph2 = (GraphView) findViewById(R.id.graphView);
        priceTextView = findViewById(R.id.priceTextView);
        grownTextView = findViewById(R.id.grownTextView);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        predictValueTextView = findViewById(R.id.predictValueTextView);
        predictGrowthTextView = findViewById(R.id.predictGrowthTextView);

        mSeries1 = new LineGraphSeries<>();
        mSeries1.setColor(Color.GREEN);

        mSeries2 = new LineGraphSeries<>();
        graph2.addSeries(mSeries2);
        graph2.addSeries(mSeries1);
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScrollable(true);
        graph2.getViewport().setScalableY(true);
        graph2.getViewport().setScrollableY(true);
        graph2.setTitleTextSize(10);
        graph2.getViewport().setMinX(mSeries2.getLowestValueX() - 4000);
        graph2.getViewport().setMaxX(mSeries2.getHighestValueX() + (1/2.0));
        graph2.getGridLabelRenderer().setPadding(40);

        graphModel = ViewModelProviders.of(this).get(GraphActivityViewModel.class);

        graphModel.getInstrumentsData(intent.getStringExtra("key")).observe(
                this, new Observer<ArrayList<Instrument>>() {
                    @Override
                    public void onChanged(final ArrayList<Instrument> instruments) {
                        Toast.makeText(GraphActivity.this, instruments.size() + "", Toast.LENGTH_SHORT).show();
                        priceTextView.setText(instruments.get(instruments.size()-1).getCLOSE()+"₽");
                        toolbarTitle.setText(instruments.get(0).getTICKER());

                        grown = instruments.get(instruments.size()-1).getCLOSE()
                                -instruments.get(0).getCLOSE();
                        if(grown>0)
                        {
                            grownTextView.setTextColor(Color.GREEN);
                            grownText = grown+"₽ ↑";
                        } else {
                            grownTextView.setTextColor(Color.RED);
                            grownText = grown+"₽ ↓";
                        }
                        grownTextView.setText(new DecimalFormat("#0.00").format(instruments.get(instruments.size()-1).getCLOSE()
                                -instruments.get(0).getCLOSE()));
                    }
                    }); //

        graphModel.getPointsData(intent.getStringExtra("key")).observe(this, new Observer<ArrayList<Point>>() {
                    @Override
                    public void onChanged(ArrayList<Point> points) {

                        boolean flag = true;
                        if(flag) {
                            HoltPrediction holtPrediction = new HoltPrediction(points);

                            Double predictionValue = holtPrediction.calculateOneStepPrediction();
                            predictGrow = predictionValue -points.get(points.size()-1).getY();

                            String predictString =
                                    new DecimalFormat("#0.00").format(predictionValue)+"₽";

                            if(predictionValue>points.get(points.size()-1).getY()) // цена выросла
                            {
                                predictGrowthTextView.setTextColor(Color.GREEN);
                                predictGrowthTextView.setText(new DecimalFormat("#0.0000").format(predictGrow)+"₽ ↑");
                            } else {
                                predictGrowthTextView.setTextColor(Color.RED);
                                predictGrowthTextView.setText(new DecimalFormat("#0.0000").format(predictGrow)+"₽ ↓");
                            }

                            predictValueTextView.setText(predictString);
                            smoothPoints = holtPrediction.getSmoothAFactorList();
                            flag=false;
                        }


                        for(int i = 0; i<points.size()-1;i++)
                        {

                        Log.d("points", " x:"+points.get(i).getX()+"; y:"+points.get(i).getY());
                        //if(points.get(i+1).getX()>points.get(i).getX()) {
                            mSeries2.appendData(new DataPoint(
                                        points.get(i).getX(), points.get(i).getY()),
                                false, 2000000);

                            mSeries1.appendData(new DataPoint(
                                            smoothPoints.get(i).getX(), smoothPoints.get(i).getY()),
                                    false, 2000000);



                    }
                }
                });

        graphModel.getTimeDifferenceData().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                timeDifference = aDouble;
            }
        });

        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMinimumIntegerDigits(5);

        graph2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));
        graph2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {

                mNumberFormatter[0] = nf;
                mNumberFormatter[1] = nf;

                if(isValueX) {
                    Integer dayNumber = (int) (value+timeDifference) / (60 * 24);
                    String dayOfMonth = dayNumber.toString();
                    Integer hourNumber = (int) ((value+timeDifference) - dayNumber * 60 * 24) / 60;
                    Integer minutesNumber = (int) ((value+timeDifference) - dayNumber * 60 * 24 - hourNumber * 60);

                    String result = "01." + dayOfMonth + "\n"
                            + hourNumber.toString() + ":" + minutesNumber.toString();
                    return result;
                }
                else {

                    Integer priceInt = (int) value;
                    return priceInt.toString();
                }
            }
        });
    }

    public void goBack(View view) {
        finish();
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
