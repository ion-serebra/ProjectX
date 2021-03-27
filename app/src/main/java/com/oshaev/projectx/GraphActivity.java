package com.oshaev.projectx;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class GraphActivity extends AppCompatActivity {


    private GraphActivityViewModel graphModel;
    private ArrayList<Instrument> instruments;
    private Long timeDifference = 0L;
    private LineGraphSeries mSeries1;
    private LineGraphSeries mSeries2;
    private Button predictButton;
    private TextView priceTextView;
    private TextView grownTextView;
    private TextView toolbarTitle;

    Long grown = 0L;
    String grownText= "";

    Long st0= 0L;
    Long startAverageValue= 0L;
    Long sEYS = 0L; // startElementsYSum
    int sVC = 30; //startValuesCount
    Long sETS = 0L; //startElementsTimeSum;
    Long sESTS = 0L; // startElementsSquaresTimeSum
    Long sMYT = 0L; // sumMultipliesYwithTime

    float holtAlpha = 0.3f;
    float holtBetta = 0.45f; // коэффициенты сглаживания по методу Холта

    //переменные a и b в системе Холта
    double startHoltA; // прогноз, очищенный от тренда
    double startHoltB; // коэффициент тренда

    double holtA;
    double holtB;





    // уравнение тренда: y = bt + a
    //система для нахождения a и b:
    //a*sVC+b*sETS=sEYS
    //a*sETS+b*sESTS=sMYT


    float alpha = 0.6f; // для резких скачков, так как маленький период

    HashMap<Long, Long> averageValues;

     GraphView graph2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();
        instruments = new ArrayList<>();
        averageValues = new HashMap<>();

        graph2 = (GraphView) findViewById(R.id.graphView);
        predictButton = findViewById(R.id.predictButton);
        priceTextView = findViewById(R.id.priceTextView);
        grownTextView = findViewById(R.id.grownTextView);
        toolbarTitle = findViewById(R.id.toolbarTitle);

        mSeries1 = new LineGraphSeries<>();
        mSeries1.setColor(Color.TRANSPARENT);

        mSeries2 = new LineGraphSeries<>();
        graph2.addSeries(mSeries2);
        graph2.addSeries(mSeries1);
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScrollable(true);
        graph2.getViewport().setScalableY(true);
        graph2.getViewport().setScrollableY(true);
        graph2.setTitleTextSize(10);





        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instruments!=null)
                {
                    mSeries1.setColor(Color.GREEN);
                }
            }
        });

        graphModel = ViewModelProviders.of(this).get(GraphActivityViewModel.class);
        instruments = graphModel.instruments;

        graphModel.getInstrumentsData(intent.getStringExtra("key")).observe(this,
                new Observer<ArrayList<Instrument>>() {
                    @Override
                    public void onChanged(ArrayList<Instrument> instr) {
                        instruments = instr;
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
                        grownTextView.setText(grownText);







                        for(int i = 0; i< sVC; i++)
                        {
                            sEYS += instruments.get(i).getCLOSE();
                            sETS += getTimeMinutes(instruments.get(i).getDATE(),
                                    instruments.get(i).getTIME());
                            sESTS +=  getTimeMinutes(instruments.get(i).getDATE(),
                                    instruments.get(i).getTIME())  * getTimeMinutes(instruments.get(i).getDATE(),
                                    instruments.get(i).getTIME());

                            sMYT += getTimeMinutes(instruments.get(i).getDATE(),
                                    instruments.get(i).getTIME()) * instruments.get(i).getCLOSE();
                        }
                        startAverageValue = sEYS/ sVC;

                         startHoltB = findHoltFactor(sVC, sETS, sEYS, sETS, sESTS, sMYT , true);
                         startHoltA = findHoltFactor(sVC, sETS, sEYS, sETS, sESTS, sMYT , false);


/*
                        for(int i = 0;i<instruments.size();i++)
                        {
                            averageValues.put(getTimeMinutes(instruments.get(i).getDATE(),
                                    instruments.get(i).getTIME(), timeDifference)  // момент времени (ось Х)
                                    , instruments.get(i).getCLOSE()); // цена в этот момент времени

                            if(i<startValuesCount) {
                                mSeries1.appendData(new DataPoint(getTimeMinutes(instruments.get(i).getDATE(),
                                        instruments.get(i).getTIME(), timeDifference)
                                                , alpha*instruments.get(i).getCLOSE()+(1-alpha)*startAverageValue) //S_t=alpha*Y_t+(1-alpha)*S_0
                                        , true, 100000);
                            } else {
                                mSeries1.appendData(new DataPoint(getTimeMinutes(instruments.get(i).getDATE(),
                                        instruments.get(i).getTIME(), timeDifference)
                                                , alpha*instruments.get(i).getCLOSE()+(1-alpha)*instruments.get(i-1).getCLOSE()) //S_t=alpha*Y_t+(1-alpha)*S_0
                                        , true, 100000);
                            }
                        }

 */






                        long prevTime = 0;
                        long currentTime = 0;
                        int i = 0;
                        Long timeMinutes = 0L;
                        Log.d("getInstrumentsData", "added");

                        for(int j = 0 ; j < instruments.size() ; j+=4){ // идём с шагом 4 и сглаживанием, чтобы не перегружать устройство
                            Log.d("getInstrumentsData", instruments.get(j).getCLOSE().toString());

                            if(j>2 && j<instruments.size()) {


                                prevTime = getTimeMinutes(instruments.get(j-1).getDATE(),
                                        instruments.get(j-1).getTIME());
                                currentTime = getTimeMinutes(instruments.get(j).getDATE(),
                                        instruments.get(j).getTIME());

                                if(currentTime-prevTime>10)
                                {
                                    timeDifference += getTimeMinutes(instruments.get(j).getDATE(),
                                            instruments.get(j).getTIME())-getTimeMinutes(instruments.get(j-1).getDATE(),
                                            instruments.get(j-1).getTIME());
                                }

                                mSeries2.appendData(new DataPoint
                                                (getTimeMinutes(instruments.get(j).getDATE(),
                                                        instruments.get(j).getTIME(), timeDifference), instruments.get(j).getCLOSE()),
                                        true, 200000);
                            } else {
                                mSeries2.appendData(new DataPoint
                                                (getTimeMinutes(instruments.get(j).getDATE(),
                                                        instruments.get(j).getTIME()), instruments.get(j).getCLOSE()),
                                        true, 200000);
                            }


                            // реализация экспоненциального сглаживания
                            averageValues.put(getTimeMinutes(instruments.get(j).getDATE(),
                                    instruments.get(j).getTIME(), timeDifference)  // момент времени (ось Х)
                                    , instruments.get(j).getCLOSE()); // цена в этот момент времени

                            if(j< sVC) {
                                mSeries1.appendData(new DataPoint(getTimeMinutes(instruments.get(j).getDATE(),
                                        instruments.get(j).getTIME(), timeDifference)
                                                , alpha*instruments.get(j).getCLOSE()+(1-alpha)*startAverageValue) //S_t=alpha*Y_t+(1-alpha)*S_0
                                        , true, 100000);

                                //holtA = startHoltA+startHoltB;



                            } else {

                                //holtA = holtAlpha*startHoltA+(1-holtAlpha)

                                mSeries1.appendData(new DataPoint(getTimeMinutes(instruments.get(j).getDATE(),
                                        instruments.get(j).getTIME(), timeDifference)
                                                , alpha*instruments.get(j).getCLOSE()
                                                +(1-alpha)*instruments.get(j-1)
                                                .getCLOSE()-startHoltB)        //S_t=alpha*Y_t+(1-alpha)*S_0
                                        , true, 100000);

                                startHoltB = holtBetta*(instruments.get(j).getCLOSE()-instruments.get(j-1).getCLOSE())
                                        +(1-holtBetta)*startHoltB;


                            }

                            //конец реализации




                        }

                        graph2.getViewport().setMinX(1);
                        graph2.getViewport().setMaxX
                                (getTimeMinutes
                                        (instruments.get(instruments.size()-1).getDATE()
                                        ,instruments.get(instruments.size()-1).getTIME()
                                                ,timeDifference));

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
                            + hourNumber.toString() + " " + minutesNumber.toString();
                    return result;
                }
                else {

                    Integer priceInt = (int) value;
                    return priceInt.toString();
                }
            }
        });





    }

    public double findHoltFactor(double a, double b, double c, double d, double e, double f, boolean isB) {
        // решаем систему двух неизвестных
        // возвращаем коэффициент тренда
        double det = ((a) * (e) - (b) * (d));  //instead of 1/
        double x = ((c) * (e) - (b) * (f)) / det; //a
        double y = ((a) * (f) - (c) * (d)) / det; //b
        if(isB) {
            return y;
        } else {
            return x;
        }
    }


    public long getTimeMinutes(Long currentDate, Long currentTime)
    {
        String dateStr = currentDate.toString();
        String dayStr = dateStr.substring(6);
        Long dayMinutes = Long.parseLong(dayStr)*24*60;
        String hoursStr = currentTime.toString().substring(0,2);
        String minutesStr = currentTime.toString().substring(2,4);

        Long outputTime = Long.parseLong(hoursStr)*60+Long.parseLong(minutesStr)+dayMinutes;

        return outputTime;
    }

    public long getTimeMinutes(Long currentDate, Long currentTime, Long timeDifference)
    {
        String dateStr = currentDate.toString();
        String dayStr = dateStr.substring(6);
        Long dayMinutes = Long.parseLong(dayStr)*24*60;
        String hoursStr = currentTime.toString().substring(0,2);
        String minutesStr = currentTime.toString().substring(2,4);

        Long outputTime = Long.parseLong(hoursStr)*60+Long.parseLong(minutesStr)+dayMinutes-timeDifference;

        return outputTime;
    }


    public void goBack(View view) {
        onBackPressed();
    }

    public void oneDayScale(View view) {
        graph2.getViewport().setMinX(getTimeMinutes
                (instruments.get(instruments.size()-(instruments.size()/30)).getDATE()
                        ,instruments.get(instruments.size()/30).getTIME()
                        ,timeDifference));

        grown = instruments.get(instruments.size()-1).getCLOSE()
                -instruments.get(instruments.size()-(instruments.size()/30)).getCLOSE();
        if(grown>0)
        {
            grownTextView.setTextColor(Color.GREEN);
            grownText = grown+"₽ ↑";
        } else {
            grownTextView.setTextColor(Color.RED);
            grownText = grown+"₽ ↓";
        }
        grownTextView.setText(grownText);


    }

    public void oneWeekScale(View view) {

        graph2.getViewport().setMinX(getTimeMinutes
                (instruments.get(instruments.size()-(instruments.size()/4)).getDATE()
                        ,instruments.get(instruments.size()-(instruments.size()/4)).getTIME()
                        ,timeDifference));

        grown = instruments.get(instruments.size()-1).getCLOSE()
                -instruments.get(instruments.size()-(instruments.size()/4)).getCLOSE();
        if(grown>0)
        {
            grownTextView.setTextColor(Color.GREEN);
            grownText = grown+"₽ ↑";
        } else {
            grownTextView.setTextColor(Color.RED);
            grownText = grown+"₽ ↓";
        }
        grownTextView.setText(grownText);

    }

    public void oneMonthScale(View view) {
        graph2.getViewport().setMinX(getTimeMinutes
                (instruments.get(0).getDATE()
                        ,instruments.get(0).getTIME()
                        ,timeDifference));

        grown = instruments.get(instruments.size()-1).getCLOSE()-instruments.get(0).getCLOSE();
        if(grown>0)
        {
            grownTextView.setTextColor(Color.GREEN);
            grownText = grown+"₽ ↑";
        } else {
            grownTextView.setTextColor(Color.RED);
            grownText = grown+"₽ ↓";
        }
        grownTextView.setText(grownText);

    }
}
