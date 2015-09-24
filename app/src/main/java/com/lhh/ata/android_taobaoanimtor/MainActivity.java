package com.lhh.ata.android_taobaoanimtor;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.lhh.aea.emanate.view.EmanateTaobaoView;


public class MainActivity extends Activity {

    private ImageView ivIcon,ivCar;

    private Button btnAddCar;

    private EmanateTaobaoView emanateTaobaoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ivIcon = (ImageView) findViewById(R.id.ivIcon);

        ivCar = (ImageView) findViewById(R.id.ivCar);

        btnAddCar = (Button) findViewById(R.id.btnAddCar);

        emanateTaobaoView = (EmanateTaobaoView)findViewById(R.id.emanateTaobaoView);

        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGiftEmate();
            }
        });
    }

    public void sendGiftEmate(){
        if(emanateTaobaoView != null){
            if(ivIcon != null) {
                int[] location = new int[2];
                int[] endLocation = new int[2];
                ivIcon.getLocationOnScreen(location);
                ivCar.getLocationOnScreen(endLocation);
                emanateTaobaoView.setEmanateLoc(location[0],location[1],endLocation[0],endLocation[1]);
            }

            emanateTaobaoView.setDrawable(getResources().getDrawable(R.drawable.tb),
                    ivIcon.getMeasuredWidth(), ivIcon.getMeasuredHeight());

            emanateTaobaoView.onStartEmanate();
        }
    }

}
