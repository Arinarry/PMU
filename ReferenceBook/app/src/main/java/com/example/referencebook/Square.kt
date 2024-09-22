package com.example.referencebook

import javax.microedition.khronos.opengles.GL10
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Square {

    private val vertices = floatArrayOf(
        -1.0f, 1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
        1.0f, -1.0f, 0.0f,
        1.0f, 1.0f, 0.0f
    )

    private val vertexBuffer: FloatBuffer
    init {
        val byteBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
        byteBuffer.order(ByteOrder.nativeOrder())
        vertexBuffer = byteBuffer.asFloatBuffer()
        vertexBuffer.put(vertices)
        vertexBuffer.position(0)
    }

    fun draw(gl: GL10) {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4)
    }
}