package com.example.arexperimentation

import com.google.ar.core.Pose
import com.google.ar.sceneform.math.Vector3

fun convertPoseToVector(pose: Pose): Vector3 {
    return Vector3(pose.tx(), pose.ty(), pose.tz())
}

fun getBinomialCoeff(n: Int, k: Int): Int {
    if (k == n || k == 0) return 1

    return getBinomialCoeff(n - 1, k - 1) + getBinomialCoeff(n - 1, k)
}