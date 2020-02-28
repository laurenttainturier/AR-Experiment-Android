package com.example.arexperimentation

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.ar.sceneform.rendering.ModelRenderable
import java.util.function.Consumer

enum class RenderableType(val uri: String) {
    EGG("crane.sfb"),
    ARROW("arrow.sfb")
}

class RenderableFactory {
    companion object {

        val renderables = mutableMapOf<String, ModelRenderable>()

        fun createRenderables(context: Context) {
            RenderableType.values().forEach { assetUri ->
                createRenderable(context, assetUri.uri) {
                    renderables[assetUri.uri] = it!!
                }
            }
        }

        fun getRenderable(uri: String) : ModelRenderable? {
            return renderables[uri]
        }

        private fun createRenderable(
            context: Context, assetUri: String, notify: (ModelRenderable?) -> Unit
        ) {
            ModelRenderable.builder()
                .setSource(context, Uri.parse(assetUri))
                .build()
                .thenAccept(Consumer<ModelRenderable> { renderable: ModelRenderable ->
                    notify(renderable)
                })
                .exceptionally { t: Throwable? ->
                    Toast.makeText(
                        context,
                        "Unable to load $assetUri renderable \n Error: ${t.toString()   }",
                        Toast.LENGTH_LONG
                    ).show()
                    null
                }
        }

    }
}