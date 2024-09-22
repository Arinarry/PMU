package com.example.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        GLSurfaceView g=new GLSurfaceView(this);
        g.setRenderer(new MyRender(this));
        g.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(g);
    }
}