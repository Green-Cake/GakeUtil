package org.gakeutil.render

import org.gakeutil.util.RendererMarker

/**
 * Provides one function [render].
 * Functions that override [render] had better present [RendererMarker].
 *
 * @see RendererMarker
 */
interface IRenderable {

    @RendererMarker
    fun render(renderer: Renderer)

}