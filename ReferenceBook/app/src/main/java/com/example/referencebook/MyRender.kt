package com.example.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.opengl.GLU
import com.example.referencebook.Square
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRender(context: Context) : GLSurfaceView.Renderer {

    private val square: Square = Square()

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()

        GLU.gluPerspective(gl, 45.0f, width.toFloat() / height.toFloat(), 0.1f, 100.0f)
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
    }

    override fun onDrawFrame(gl: GL10) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl.glLoadIdentity()
        gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f)
        gl.glTranslatef(0.0f, 0.0f, -5.0f)
        gl.glScalef(1.0f, 1.0f, 1.0f)
        square.draw(gl)
    }
}
