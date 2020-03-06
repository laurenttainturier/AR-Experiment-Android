package com.example.arexperimentation.models

import com.example.arexperimentation.RenderableType
import com.example.arexperimentation.convertPoseToVector
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

    override var renderableType =
        RenderableType.EGG

    init {
        initializeNode()
        showNode()
//        placeEgg()
    }

    override fun onTap() {
        pickEgg()
    }

    private fun pickEgg() {
        val cameraPosition = arFragment.arSceneView.arFrame?.camera?.pose
        val moveFromCamera = arFragment.arSceneView.scene.camera.right.scaled(.1f)

        if (cameraPosition == null) {
            deleteNode()
        } else {
            val origin = node.worldPosition
            val destination = Vector3.add(
                convertPoseToVector(
                    cameraPosition
                ), moveFromCamera)

            linearMove(origin, destination) {
                deleteNode()
                inform(id)
            }
        }
    }

    private fun placeEgg() {
        val cameraPosition = arFragment.arSceneView.arFrame?.camera?.pose
        val moveFromCamera = arFragment.arSceneView.scene.camera.right.scaled(.1f)

        if (cameraPosition != null) {
            val origin = Vector3.add(
                convertPoseToVector(
                    cameraPosition
                ), moveFromCamera)

            linearMove(origin, node.worldPosition, 200) {
//                val renderable = (node.renderable as ModelRenderable)
//                val animationData = renderable.getAnimationData(0)
//                ModelAnimator(animationData, renderable).start()
            }
            /*curvedMove(
                listOf(
                    origin,
                    Vector3.add(origin, camera.back.scaled(1f)),
                    Vector3.add(destination, camera.forward.scaled(1f)),
                    destination
                )
            ) {}*/
            /*curvedMove(
                listOf(
                    origin,
                    Vector3.add(origin, arFragment.arSceneView.scene.camera.forward.scaled(.1f)),
                    *//*Vector3.add(
                        arFragment.arSceneView.scene.camera.forward.scaled(5f),
                        arFragment.arSceneView.scene.camera.down.scaled(.4f)
                    ),
                    Vector3.add(
                        arFragment.arSceneView.scene.camera.forward.scaled(4.9f),
                        arFragment.arSceneView.scene.camera.down.scaled(.4f)
                    )
                    Vector3.add(node.worldPosition, arFragment.arSceneView.scene.camera.back.scaled(.1f)),*//*
                    node.worldPosition
                )
            )*/
        }
    }
}
