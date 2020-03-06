package com.example.arexperimentation

import androidx.lifecycle.MutableLiveData
import com.example.arexperimentation.models.Crane
import com.example.arexperimentation.models.Arrow
import com.example.arexperimentation.models.Egg
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.coroutines.*

class ObjectManager(
    private val arFragment: ArFragment
) {

    private var arrow: Arrow? = null

    private val visibleEggs = hashMapOf<Int, Egg>()

    private val hiddenEggs = hashMapOf<Int, Egg>()

    private var eggCount = 0

    var eggFoundCount: MutableLiveData<Int> = MutableLiveData()

    init {
        eggFoundCount.value = 0
    }

    fun addEgg(hitResult: HitResult) {
        val scene = arFragment.arSceneView.scene
        val camera = scene.camera

        if (arrow == null)
            arrow =
                Arrow(arFragment, camera)

        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(scene)

        val andyHeight = .1f
        val overviewDuration = 1000L

        val andyPosition = Vector3.add(
            convertPoseToVector(anchor.pose),
            camera.up.scaled(andyHeight)
        )

        val crane = Crane(
            arFragment,
            scene,
            andyPosition
        )

        crane.linearMove(
            Vector3.add(andyPosition, camera.left.scaled(.5f)),
            Vector3.add(andyPosition, camera.right.scaled(.5f)),
            300, overviewDuration
        ) {
            crane.deleteNode()
        }

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                delay(overviewDuration / 2)
                visibleEggs[++eggCount] =
                    Egg(arFragment, anchorNode, eggCount) { eggId ->
                        hideEgg(eggId)
                    }
                delay(overviewDuration / 2)
                arrow!!.showNode()
            }
        }
    }

    private fun hideEgg(id: Int) {
        if (id in visibleEggs.keys) {
            eggFoundCount.value = eggFoundCount.value!!.toInt() + 1
            hiddenEggs[id] = visibleEggs.remove(id)!!
        }

        if (visibleEggs.size == 0) {
            arrow!!.hideNode()
        }
    }

    fun hideEggsNotInCameraField() {
        visibleEggs.forEach { (_, egg) ->

            val scalar = Vector3.dot(
                egg.node.worldPosition.normalized(),
                arrow!!.node.worldPosition.normalized()
            )

            if (scalar < .85f) {
                egg.hideNode()
            } else {
                egg.showNode()
            }
        }
    }

    fun getNearestEgg() {
        if (arrow == null) return

        val arrowDistance = visibleEggs.mapValues {
            Vector3.subtract(
                arrow!!.node.worldPosition,
                it.value.node.worldPosition
            )
        }

        val closestEgg = arrowDistance.minBy { it.value.length() } ?: return
        val (eggId, direction) = closestEgg

        // hides the arrow if it is too close
        if (direction.length() < 0.5f) {
            arrow!!.hideNode()
        } else {
            val color = visibleEggs[eggId]!!.color
            arrow!!.color = color
            arrow!!.showNode()
            arrow!!.node.localRotation = Quaternion.lookRotation(direction, Vector3.up())
        }
    }
}