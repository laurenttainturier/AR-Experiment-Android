package com.example.arexperimentation

import androidx.lifecycle.MutableLiveData
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.ux.ArFragment

class ObjectManager(
    private val arFragment: ArFragment
) {

    private var arrow: Arrow? = null

    private val visibleEggs = hashMapOf<Int, Egg>()

    private val hiddenEggs = hashMapOf<Int, Egg>()

    private var eggCount = 0

    var eggFoundCount : MutableLiveData<Int> = MutableLiveData()

    init {
        eggFoundCount.value = 0
    }

    fun addEgg(hitResult: HitResult) {
        if (arrow == null)
            arrow = Arrow(arFragment, arFragment.arSceneView.scene.camera)

        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(arFragment.arSceneView.scene)

        visibleEggs[++eggCount] = Egg(arFragment, anchorNode, eggCount) { eggId ->
            hideEgg(eggId)
        }
        arrow!!.showNode()
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

    fun getNearestEgg() {
        if (arrow == null) return

        val arrowVector = arrow!!.node.worldPosition

        val arrowDistance = visibleEggs.mapValues {
            Vector3.subtract(
                arrowVector,
                it.value.node.worldPosition
            )
        }

        val closestEgg = arrowDistance.minBy { it.value.length() } ?: return

        val ( eggId, direction) = closestEgg
        val color = visibleEggs[eggId]!!.color

        arrow!!.node.localRotation = Quaternion.lookRotation(direction, Vector3.up())
        arrow!!.setNodeColor(color)
    }
}