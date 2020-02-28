package com.example.arexperimentation

import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.ux.ArFragment

class Egg(
    arFragment: ArFragment,
    nodeParent: NodeParent,
    private val id: Int,
    private val inform: (Int) -> Unit
) :
    Model(arFragment, nodeParent) {

    override var color: Color = pickRandomColor()

    override var renderableType = RenderableType.EGG

    init {
        initializeNode()
        showNode()
        placeEgg()
    }

    override fun onTap() {
        pickEgg()
    }

    private fun pickEgg() {
        val cameraPosition = arFragment.arSceneView.arFrame?.camera?.pose
        val moveFromCamera = arFragment.arSceneView.scene.camera.right.scaled(.1f)

        if (cameraPosition == null) {
            hideNode()
        } else {
            val origin = node.worldPosition
            val destination = Vector3.add(convertPoseToVector(cameraPosition), moveFromCamera)
            move(origin, destination) {
                deleteNode()
                inform(id)
            }
        }
    }

    private fun placeEgg() {
        val cameraPosition = arFragment.arSceneView.arFrame?.camera?.pose

        if (cameraPosition != null) {
            val origin =
                Vector3.add(node.worldPosition, arFragment.arSceneView.scene.camera.up.scaled(1f))
            move(origin, node.worldPosition, 200) {}
        }
    }
}
