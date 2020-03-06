package com.example.arexperimentation.models

import android.util.Log
import com.example.arexperimentation.RenderableType
import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ModelRenderable

class Crane(arFragment: ArFragment, nodeParent: NodeParent, position: Vector3) :
    Model(arFragment, nodeParent) {

    override var renderableType = RenderableType.CRANE

    override var color = Color(0f, 255f, 0f)

    var i = 0

    val quaternions = listOf(
        Quaternion(1f, 0f, 0f, 0f),
        Quaternion(.7f, 0f, .7f, 0f),
        Quaternion(0f, 0f, 1f, 0f),
        Quaternion(.7f, 0f, -.7f, 0f),
        Quaternion(.7f, 0f, 0f, .7f),
        Quaternion(.5f, .5f, .5f, .5f),
        Quaternion(0f, .7f, .7f, 0f),
        Quaternion(.5f, -.5f, -.5f, .5f),
        Quaternion(.7f, 0f, 0f, -.7f),
        Quaternion(.5f, -.5f, .5f, -.5f),
        Quaternion(0f, -.7f, .7f, 0f),
        Quaternion(.5f, .5f, -.5f, -.5f),
        Quaternion(.7f, .7f, 0f, 0f),
        Quaternion(.5f, .5f, .5f, -.5f),
        Quaternion(0f, 0f, .7f, -.7f),
        Quaternion(.5f, .5f, -.5f, .5f),
        Quaternion(0f, 1f, 0f, 0f),
        Quaternion(0f, .7f, 0f, -.7f),
        Quaternion(0f, 0f, 0f, 1f),
        Quaternion(0f, .7f, 0f, .7f),
        Quaternion(.7f, -.7f, 0f, 0f),
        Quaternion(.5f, -.5f, .5f, .5f),
        Quaternion(0f, 0f, .7f, .7f),
        Quaternion(.5f, -.5f, -.5f, -.5f)
    )

    init {
        initializeNode()
        node.scaleController.maxScale = 7f
        node.scaleController.minScale = 6f
        node.localRotation = Quaternion(0f, .7f, 0f, -.7f)
        showNode()
        node.worldPosition = position
        animate()
    }

    fun animate() {
        val modelRenderable = (node.renderable as ModelRenderable)
        val animationData = modelRenderable.getAnimationData(0)
        val modelAnimator = ModelAnimator(animationData, modelRenderable)
        modelAnimator.start()
        modelAnimator.repeatCount = 100
    }

    override fun onTap() {
//        node.localRotation = quaternions[++i] // Quaternion(.7f, 0f, .7f, 0f)
//        Log.e("Position:", i.toString() + " " + quaternions[i].toString())
    }
}