package com.example.referencebook

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.opengl.GLUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class MyRender(context: Context, private val currentPlanetIndexProvider: () -> Int) : GLSurfaceView.Renderer {
    private val texturedSquare: Square = Square(context)
    private val sun = Sphere(context, 0.2f)
    private val mercury = Sphere(context, 0.05f)
    private val venus = Sphere(context, 0.1f)
    private val earth = Sphere(context, 0.1f)
    private val moon = Sphere(context, 0.05f)
    private val mars = Sphere(context, 0.1f)
    private val jupiter = Sphere(context, 0.1f)
    private val saturn = Sphere(context, 0.1f)
    private val uranus = Sphere(context, 0.1f)
    private val neptune = Sphere(context, 0.1f)
    private val pluto = Sphere(context, 0.05f)
    private val blackHole = Sphere(context, 0.1f)
    private val cube = Cube()

    private var sunRotationAngle = 0f
    private var earthAngle = 0f
    private var moonAngle = 0f
    private var mercuryAngle = 0f
    private var venusAngle = 0f
    private var marsAngle = 0f
    private var jupiterAngle = 0f
    private var saturnAngle = 0f
    private var uranusAngle = 0f
    private var neptuneAngle = 0f
    private var plutoAngle = 0f

    private var width = 0
    private var height = 0

    private var blackHoleX = 1.5f
    private var blackHoleY = 1.5f
    private val blackHoleSpeedX = -0.03f
    private val blackHoleSpeedY = -0.03f
    private var blackHoleVisible = true
    private var lastResetTime = System.currentTimeMillis()

    private var currentPlanetIndex = 0
    private var planets = listOf(sun, mercury, venus, earth, moon, mars, jupiter, saturn, uranus, neptune, pluto)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        gl.glEnable(GL10.GL_TEXTURE_2D)
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)
        /*gl.glDepthFunc(GL10.GL_LEQUAL)
        gl.glEnable(GL10.GL_CULL_FACE)
        gl.glCullFace(GL10.GL_BACK)*/

        texturedSquare.loadTexture(gl, R.drawable.galaxy)
        sun.loadTexture(gl, R.drawable.sun_texture, 0)
        mercury.loadTexture(gl, R.drawable.mercury_texture, 1)
        venus.loadTexture(gl, R.drawable.venus_texture, 2)
        earth.loadTexture(gl, R.drawable.earth_texture, 3)
        moon.loadTexture(gl, R.drawable.moon_texture, 4)
        mars.loadTexture(gl, R.drawable.mars_texture, 5)
        jupiter.loadTexture(gl, R.drawable.jupiter_texture, 6)
        saturn.loadTexture(gl, R.drawable.saturn_texture, 7)
        uranus.loadTexture(gl, R.drawable.uranus_texture, 8)
        neptune.loadTexture(gl, R.drawable.neptune_texture, 9)
        pluto.loadTexture(gl, R.drawable.pluto_texture, 10)
        blackHole.loadTexture(gl, R.drawable.blackhole, 11)
    }

    override fun onSurfaceChanged(gl: GL10, w: Int, h: Int) {
        width = w
        height = h
        gl.glViewport(0, 0, width, height)
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()

        GLU.gluPerspective(gl, 45.0f, width.toFloat() / height.toFloat(), 0.1f, 100.0f)

        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
    }

    override fun onDrawFrame(gl: GL10) {
        currentPlanetIndex = currentPlanetIndexProvider()
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl.glLoadIdentity()

        gl.glPushMatrix()
        gl.glTranslatef(0.0f, 0.0f, -6.0f)
        val aspectRatio = if (height != 0) width.toFloat() / height.toFloat() else 1f
        gl.glScalef(aspectRatio * 3.0f,3.0f, 1.0f)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturedSquare.getTextureId())
        texturedSquare.draw(gl)
        gl.glPopMatrix()

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastResetTime >= 11000) {
            blackHoleX = 1.5f
            blackHoleY = 1.5f
            blackHoleVisible = true
            lastResetTime = currentTime
        }

        if (blackHoleVisible) {
            blackHoleX += blackHoleSpeedX
            blackHoleY += blackHoleSpeedY

            if (blackHoleX <= -1.5f || blackHoleY <= -1.5f) {
                blackHoleVisible = false
            }

            gl.glPushMatrix()
            gl.glTranslatef(blackHoleX, blackHoleY, -4.7f)
            gl.glBindTexture(GL10.GL_TEXTURE_2D, blackHole.getTextureId(11))
            blackHole.draw(gl)
            gl.glPopMatrix()
        }

        gl.glPushMatrix()
        gl.glTranslatef(0.0f, 0.0f, -5.0f)
        gl.glRotatef(sunRotationAngle, 0.0f, 1.0f, 0.0f)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, sun.getTextureId(0))
        sun.draw(gl)
        gl.glPopMatrix()

        if (currentPlanetIndex == 0) {
            gl.glPushMatrix()
            gl.glTranslatef(0.0f, 0.0f, -5.0f)
            val scaleCube = planets[currentPlanetIndex].radius()
            gl.glScalef(scaleCube, scaleCube, scaleCube)
            cube.draw(gl)
            gl.glPopMatrix()
        }

        drawPlanet(gl, mercury, mercuryAngle, 0.4f, 1.7f, 1, 1) // Меркурий
        drawPlanet(gl, venus, venusAngle, 0.6f, 1.5f, 2, 2) // Венера
        drawPlanet(gl, earth, earthAngle, 0.8f, 1.3f, 3, 3) // Земля
        drawMoon(gl, moon, earthAngle, moonAngle, 0.8f, 4, 4) // Луна
        drawPlanet(gl, mars, marsAngle, 1.0f, 1.1f, 5, 5) // Марс
        drawPlanet(gl, jupiter, jupiterAngle, 1.5f, 1.0f, 6, 6) // Юпитер
        drawPlanet(gl, saturn, saturnAngle, 1.7f, 0.7f, 7, 7) // Сатурн
        drawPlanet(gl, uranus, uranusAngle, 2.0f, 0.5f, 8, 8) // Уран
        drawPlanet(gl, neptune, neptuneAngle, 2.2f, 0.3f, 9, 9) // Нептун
        drawPlanet(gl, pluto, plutoAngle, 2.5f, 0.2f, 10, 10) // Плутон


        sunRotationAngle -= 1f
        mercuryAngle += 4f
        venusAngle += 3f
        earthAngle += 2f
        moonAngle += 5f
        marsAngle += 1.5f
        jupiterAngle += 1f
        saturnAngle += 0.7f
        uranusAngle += 0.5f
        neptuneAngle += 0.3f
        plutoAngle += 0.2f

        /*if (sunRotationAngle >= 360) sunRotationAngle = 0f
        if (mercuryAngle >= 360) mercuryAngle = 0f
        if (venusAngle >= 360) venusAngle = 0f
        if (earthAngle >= 360) earthAngle = 0f
        if (moonAngle >= 360) moonAngle = 0f
        if (marsAngle >= 360) marsAngle = 0f
        if (jupiterAngle >= 360) jupiterAngle = 0f
        if (saturnAngle >= 360) saturnAngle = 0f
        if (uranusAngle >= 360) uranusAngle = 0f
        if (neptuneAngle >= 360) neptuneAngle = 0f
        if (plutoAngle >= 360) plutoAngle = 0f*/
    }

    private fun drawPlanet(gl: GL10, planet: Sphere, angle: Float, radius: Float, rotationSpeed: Float, textureIndex: Int, indexPlanet: Int) {
        gl.glPushMatrix()
        val x = cos(Math.toRadians(angle.toDouble())).toFloat() * radius
        val y = sin(Math.toRadians(angle.toDouble())).toFloat() * radius
        gl.glTranslatef(x,y,-5.2f)
        gl.glRotatef(-angle * rotationSpeed, 0.0f, 1.0f, 0.0f)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, planet.getTextureId(textureIndex))
        planet.draw(gl)
        gl.glPopMatrix()

        if (currentPlanetIndex == indexPlanet) {
            gl.glPushMatrix()
            gl.glTranslatef(x, y, -5.2f)
            val scaleCube = planets[currentPlanetIndex].radius()
            gl.glScalef(scaleCube, scaleCube, scaleCube)
            cube.draw(gl)
            gl.glPopMatrix()
        }
    }

    private fun drawMoon(gl: GL10, moon: Sphere, earthAngle: Float, moonAngle: Float, radius: Float, textureIndex: Int, indexPlanet: Int) {
        gl.glPushMatrix()

        val earthX = cos(Math.toRadians(earthAngle.toDouble())).toFloat() * radius
        val earthY = sin(Math.toRadians(earthAngle.toDouble())).toFloat() * radius

        gl.glTranslatef(earthX, earthY, -5.1f)
        val moonDistance = 0.10f
        gl.glRotatef(moonAngle, 0.0f, 0.0f, 1.0f)
        gl.glTranslatef(moonDistance, 0.0f, 0.0f)

        gl.glBindTexture(GL10.GL_TEXTURE_2D, moon.getTextureId(textureIndex))
        moon.draw(gl)

        if (currentPlanetIndex == indexPlanet) {
            gl.glPushMatrix()
            val scaleCube = moon.radius() * 1.1f
            gl.glScalef(scaleCube, scaleCube, scaleCube)
            cube.draw(gl)
            gl.glPopMatrix()
        }
        gl.glPopMatrix()
    }
}

