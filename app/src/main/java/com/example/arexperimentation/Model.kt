package com.example.arexperimentation

import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.coroutines.*
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
        modelRenderable = RenderableFactory.getRenderable(renderableType.uri)
        node.apply {
            setOnTapListener { _, _ ->
                onTap()
            }
            renderable?.material?.setFloat3("baseColorTint", color)
        }
    }

    fun move(
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


