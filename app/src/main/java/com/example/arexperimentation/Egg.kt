package com.example.arexperimentation

import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.coroutines.*

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
        modelRenderable.getAnimationData(0)
        val animationData = modelRenderable.getAnimationData("fly")
        val birdAnimator = ModelAnimator(animationData, modelRenderable)
        birdAnimator.start()
        birdAnimator.repeatCount = 1000
        // setNodeColor(pickRandomColor())
    }

    override fun onTap() {
        hideNode()
    }

    override fun hideNode() {
        val cameraPosition = arFragment.arSceneView.arFrame?.camera?.pose
        val moveFromCamera = arFragment.arSceneView.scene.camera.right.scaled(.1f)

        if (cameraPosition == null) {
            nodeParent.removeChild(node)
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
                    super.hideNode()
                    inform(id)
                }
            }
        }
    }
}
