package com.ponomarev.ruler.protractor

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object CoordinatesUtils {
    data class Polar(val r:Float,val phi:Float)
    val oneDegreeInRadian:Float = (PI /180f).toFloat()

    fun degreeToRadians(degree:Float):Float = (degree* oneDegreeInRadian).toFloat()

    //угол - второй парамер
    fun getPolar(cartesian:Pair<Float,Float>): Polar =
        Polar(
            sqrt(cartesian.first*cartesian.first
                +cartesian.second*cartesian.second),
            atan2(cartesian.first,cartesian.second)
        )

    fun getPolarWithDegrees(r:Float, phiInDegrees:Float): Polar =
        Polar(r,phiInDegrees* oneDegreeInRadian)

    fun getCartesian(polar: Polar):Pair<Float,Float> {
        val axisAngle=PI/2f
        return Pair(polar.r*cos(polar.phi+axisAngle).toFloat(), polar.r* sin(polar.phi+axisAngle).toFloat())
    }



}