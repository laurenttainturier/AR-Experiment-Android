package com.example.arexperimentation

import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.coroutines.*
import kotlin.random.Random


class EasterEgg(
    val id: Int,
    modelRenderable: ModelRenderable?,
    private val arFragment: ArFragment,
    private val anchorNode: AnchorNode,
    private val inform: (Int) -> Unit
) {

    val color = Color(
        Random.nextFloat(),
        Random.nextFloat(),
        Random.nextFloat()
    )

    val node: TransformableNode = TransformableNode(arFragment.transformationSystem).apply {
        setParent(anchorNode)
        setOnTapListener { _, _ ->
            pickUp()
        }
        renderable = modelRenderable?.makeCopy()
        renderable?.material?.setFloat3("baseColorTint", color)
    }

    private fun pickUp() {
        val cameraPosition = arFragment.arSceneView.arFrame?.camera?.pose
        val moveFromCamera = arFragment.arSceneView.scene.camera.right.scaled(.1f)

        if (cameraPosition == null) {
            anchorNode.removeChild(node)
            return
        }

        node.apply {
            val iterationNb = 50
            val dx = (cameraPosition.tx() - worldPosition.x + moveFromCamera.x) / iterationNb
            val dy = (cameraPosition.ty() - worldPosition.y + moveFromCamera.y) / iterationNb
            val dz = (cameraPosition.tz() - worldPosition.z + moveFromCamera.z) / iterationNb

            GlobalScope.launch {
                var iteration = 0
                while (iterationNb > iteration++) {
                    worldPosition = Vector3(
                        worldPosition.x + dx,
                        worldPosition.y + dy,
                        worldPosition.z + dz
                    )

                    delay(700L / iterationNb)
                }
                withContext(Dispatchers.Main) {
                    anchorNode.removeChild(node)
                    inform(id)
                }
            }
        }
    }
}
