package com.example.arexperimentation

import com.google.ar.core.Pose
import com.google.ar.sceneform.math.Vector3

fun convertPoseToVector(pose: Pose): Vector3 {
    return Vector3(pose.tx(), pose.ty(), pose.tz())
}
