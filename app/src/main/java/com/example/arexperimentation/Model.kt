package com.example.arexperimentation

import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlin.random.Random

abstract class Model(
    protected val arFragment: ArFragment,
    protected  val nodeParent: NodeParent
) {

    protected lateinit var modelRenderable: ModelRenderable

    abstract var renderableType: RenderableType

    abstract var color: Color

    val node: TransformableNode = TransformableNode(arFragment.transformationSystem)

    fun initializeNode() {
        modelRenderable = RenderableFactory.getRenderable(renderableType.uri)!!
        node.apply {
            setOnTapListener { _, _ ->
                onTap()
            }
            renderable?.material?.setFloat3("baseColorTint", color)
        }
    }

    abstract fun onTap()

    fun setNodeColor(color: Color) {
        this.color = color
        node.renderable?.material?.setFloat3("baseColorTint", color)
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
    }

    open fun hideNode() {
        node.renderable = null
        nodeParent.removeChild(node)
    }
}


