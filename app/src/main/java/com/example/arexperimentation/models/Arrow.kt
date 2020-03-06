package com.example.arexperimentation.models

import com.example.arexperimentation.RenderableType
import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.rendering.Color

class Arrow(arFragment: ArFragment, nodeParent: NodeParent) :
    Model(arFragment, nodeParent) {

    override var renderableType =
        RenderableType.ARROW

    override var color = Color(0f, 255f, 0f)

    init {
        initializeNode()
        node.scaleController.maxScale = .4f
        node.scaleController.minScale = .3f
        node.translationController.isEnabled = false
        node.localPosition = Vector3(0f, .12f, -0.3f)
        setNodeColor()
        showNode()
    }

    override fun onTap() {
    }
}