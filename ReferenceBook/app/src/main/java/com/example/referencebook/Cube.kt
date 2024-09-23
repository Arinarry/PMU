package com.example.referencebook

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Cube {
    private val vertexBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    private val indexBuffer: ByteBuffer

    private val vertices = floatArrayOf(
        -1.0f, -1.0f, -1.0f,  // 0: нижний левый передний угол
        1.0f, -1.0f, -1.0f,   // 1: нижний правый передний угол
        1.0f, 1.0f, -1.0f,    // 2: верхний правый передний угол
        -1.0f, 1.0f, -1.0f,   // 3: верхний левый передний угол
        -1.0f, -1.0f, 1.0f,   // 4: нижний левый задний угол
        1.0f, -1.0f, 1.0f,    // 5: нижний правый задний угол
        1.0f, 1.0f, 1.0f,     // 6: верхний правый задний угол
        -1.0f, 1.0f, 1.0f     // 7: верхний левый задний угол
    )
    
    private val colors = floatArrayOf(
        1.0f, 0.0f, 0.0f, 1.0f,  // 0: Красный
        0.0f, 1.0f, 0.0f, 1.0f,  // 1: Зеленый
        0.0f, 0.0f, 1.0f, 1.0f,  // 2: Синий
        1.0f, 1.0f, 0.0f, 1.0f,  // 3: Желтый
        1.0f, 0.0f, 1.0f, 1.0f,  // 4: Магента
        0.0f, 1.0f, 1.0f, 1.0f,  // 5: Циан
        1.0f, 0.5f, 0.0f, 1.0f,  // 6: Оранжевый
        0.5f, 0.0f, 0.5f, 1.0f   // 7: Фиолетовый
    )

    private val indices = byteArrayOf(
        0, 1, 2, 0, 2, 3,  // Передняя грань
        4, 5, 6, 4, 6, 7,  // Задняя грань
        0, 4, 7, 0, 7, 3,  // Левая грань
        1, 5, 6, 1, 6, 2,  // Правая грань
        3, 2, 6, 3, 6, 7,  // Верхняя грань
        0, 1, 5, 0, 5, 4   // Нижняя грань
    )

    init {
        val vertexByteBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
        vertexByteBuffer.order(ByteOrder.nativeOrder())
        vertexBuffer = vertexByteBuffer.asFloatBuffer()
        vertexBuffer.put(vertices)
        vertexBuffer.position(0)

        val colorByteBuffer = ByteBuffer.allocateDirect(colors.size * 4)
        colorByteBuffer.order(ByteOrder.nativeOrder())
        colorBuffer = colorByteBuffer.asFloatBuffer()
        colorBuffer.put(colors)
        colorBuffer.position(0)

        indexBuffer = ByteBuffer.allocateDirect(indices.size)
        indexBuffer.put(indices)
        indexBuffer.position(0)
    }

    fun draw(gl: GL10) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer)

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.size, GL10.GL_UNSIGNED_BYTE, indexBuffer)

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)
    }
}