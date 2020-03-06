package com.example.arexperimentation.models

import com.example.arexperimentation.RenderableFactory
import com.example.arexperimentation.RenderableType
import com.example.arexperimentation.getBinomialCoeff
import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.coroutines.*
import kotlin.math.pow
import kotlin.random.Random

abstract class Model(
    protected val arFragment: ArFragment,
    private val nodeParent: NodeParent
) {

    protected var modelRenderable: ModelRenderable? = null

    abstract var renderableType: RenderableType

    abstract var color: Color

    val node: TransformableNode = TransformableNode(arFragment.transformationSystem)

    fun initializeNode() {
        modelRenderable =
            RenderableFactory.getRenderable(
                renderableType
            )
        node.apply {
            setOnTapListener { _, _ ->
                onTap()
            }
            renderable?.material?.setFloat3("baseColorTint", color)
        }
    }

    fun curvedMove(
        controlPoints: List<Vector3>,
        iterationNumber: Int = 50,
        duration: Long = 700,
        onFinished: () -> Unit
    ) {
        var t = 0f
        val dt = 1f / iterationNumber
        val sleep = duration / iterationNumber
        val n = controlPoints.size
        val binomialNCoeff = Array(n) { index ->
            getBinomialCoeff(n - 1, index)
        }

        GlobalScope.launch {
            while (t < 1) {
                var vector = Vector3()
                for (i in 0 until n) {
                    val coeff = binomialNCoeff[i] * (1 - t).pow(n - 1) * t.pow(i)
                    vector = Vector3.add(
                        vector,
                        controlPoints[i].scaled(coeff)
                    )
                }
                /*val pos = controlPoints.foldIndexed(Vector3()) { i, acc, vector ->
                    // Coeff computed using Bézier curve formula
                    val coeff = binomialCoeff[i] * (1 - t).pow(n - i) * t.pow(i)
                    Vector3.add(acc, vector.scaled(coeff))
                }*/
                node.worldPosition = vector
                t += dt
                delay(sleep)
            }
            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    fun linearMove(
        origin: Vector3,
        destination: Vector3,
        iterationNumber: Int = 50,
        duration: Long = 700L,
        onFinished: () -> Unit
    ) {

        val dVector = Vector3.subtract(destination, origin).scaled(1f / iterationNumber)
        node.worldPosition = origin

        GlobalScope.launch {
            var iteration = 0
            while (iterationNumber > iteration++) {
                node.apply {
                    worldPosition = Vector3.add(worldPosition, dVector)
                    delay(duration / iterationNumber)
                }
            }
            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    abstract fun onTap()

    fun setNodeColor() {
        node.renderable?.material?.setFloat3("baseColorTint", this.color)
    }

    fun pickRandomColor(): Color {
        return Color(
            Random.nextFloat(),
            Random.nextFloat(),
            Random.nextFloat()
        )
    }

    fun showNode() {
        node.renderable = modelRenderable!!.makeCopy()
        node.setParent(nodeParent)
        setNodeColor()
    }

    open fun hideNode() {
        node.renderable = null
    }

    open fun deleteNode() {
        hideNode()
        nodeParent.removeChild(node)
    }
}


