package com.example.arexperimentation

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.ar.sceneform.rendering.ModelRenderable

enum class RenderableType(val uri: String) {
    EGG("egg.sfb"),
    ARROW("arrow.sfb")
}

class RenderableFactory {
    companion object {

        private val renderables = mutableMapOf<String, ModelRenderable>()

        fun createRenderables(context: Context) {
            RenderableType.values().forEach { assetUri ->
                createRenderable(context, assetUri.uri)
            }
        }

        fun getRenderable(uri: String) : ModelRenderable? {
            return renderables[uri]
        }

        private fun createRenderable(context: Context, assetUri: String) {
            ModelRenderable.builder()
                .setSource(context, Uri.parse(assetUri))
            .build()
                .thenApply { this.renderables[assetUri] = it }
                .exceptionally { t: Throwable? ->
                    Toast.makeText(
                        context,
                        "Unable to load $assetUri renderable \n Error: ${t.toString()   }",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

    }
}