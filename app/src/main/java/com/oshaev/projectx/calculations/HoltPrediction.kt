package com.oshaev.projectx.calculations

import android.util.Log
import com.oshaev.projectx.customobjects.Point
import java.util.*



class HoltPrediction {

    private var points: ArrayList<Point> = ArrayList()
    private var holtPoints : ArrayList<Point> = ArrayList()

    private var sVC:Int = 3
    private var sETS:Double = 0.0
    private var sESTS:Double = 0.0
    private var sEYS:Double = 0.0
    private var sMYT:Double = 0.0

    private var alpha = 0.1 // коэффициент α
    private var beta = 0.1 // коэффициент β

    private var prevHoltA: Double = 0.0
    private var prevHoltB: Double = 0.0


    var smoothAFactorList: ArrayList<Point> = ArrayList()
    var smoothBFactorList: ArrayList<Point> = ArrayList()



    private var yPredictOneStep = 0.0;

    constructor(points: ArrayList<Point>) {
        this.points = points
    }


    fun calculateOneStepPrediction():Double {
        for (i in 0..sVC - 1) {
            sETS += points.get(i).x
            sESTS += points.get(i).x * points.get(i).x
            sEYS += points.get(i).y
            sMYT += points.get(i).x * points.get(i).y
        }

        prevHoltA = findHoltFactor(sVC, sETS, sEYS, sETS, sESTS, sMYT, false)
        prevHoltB = findHoltFactor(sVC, sETS, sEYS, sETS, sESTS, sMYT, true)

        Log.d("prevHoltA", prevHoltA.toString())
        Log.d("prevHoltB", prevHoltB.toString())

        smoothAFactorList.add(Point(points.get(0).x, sEYS / sVC))
        smoothBFactorList.add(Point(points.get(0).x, prevHoltB))

        for (i in 1..points.size - 1) {
            smoothAFactorList.add(
                    Point(
                            points.get(i).x,
                            alpha * points.get(i).y + (1 - alpha) * (smoothAFactorList.get(i - 1).y + smoothBFactorList.get(i - 1).y)
                    )
            )
            smoothBFactorList.add(
                    Point(
                            points.get(i).x,
                            beta * (smoothAFactorList.get(i).y - smoothAFactorList.get(i - 1).y) + (1 - beta) * smoothBFactorList.get(i - 1).y)
            )
        }

        yPredictOneStep = smoothAFactorList.get(smoothAFactorList.size - 1).y
        +(smoothBFactorList.get(smoothBFactorList.size - 1).y) * 2

        Log.d("oneStepPredict", yPredictOneStep.toString())

        for (a in smoothAFactorList) {
            Log.d("tag", " x:" + a.x.toString() + " y:" + a.y.toString());
        }

        for (b in smoothBFactorList) {
            Log.d("tag", " x:" + b.x.toString() + " y:" + b.y.toString())
        }

        return yPredictOneStep
    }





    fun findHoltFactor(sVC:Int, sETS : Double, sEYS : Double, sETS2 : Double, sESTS : Double, sMYT : Double, isB : Boolean):Double
    {
        var det = ((sVC) * (sESTS) - (sETS) * (sETS2))
        var a = ((sEYS) * (sESTS) - (sETS) * (sMYT)) / det
        var b = ((sVC) * (sMYT) - (sEYS) * (sETS2)) / det
        if(isB)
        {
            return b
        } else {
            return a
        }
    }


}