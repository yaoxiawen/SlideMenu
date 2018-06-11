package com.yxw.slidemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.yxw.slidemenu.view.SlideMenu;

public class MainActivity extends Activity {
    private ImageView iv;
    private SlideMenu sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        sm =findViewById(R.id.sm);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.switchMenu();
            }
        });
    }
}
